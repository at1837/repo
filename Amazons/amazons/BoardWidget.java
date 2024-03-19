package amazons;

import ucb.gui2.Pad;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

import java.io.IOException;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import static amazons.Square.sq;
import static amazons.Piece.WHITE;

/** A widget that displays an Amazons game.
 *  @author Ying Lung Tang
 */
class BoardWidget extends Pad {

    /* Parameters controlling sizes, speeds, colors, and fonts. */

    /** Colors of empty squares and grid lines. */
    static final Color
        SELECTED_FROM_COLOR = new Color(35, 250, 135),
        SELECTED_TO_COLOR = new Color(250, 35, 135),
        SPEAR_COLOR = new Color(64, 64, 64),
        LIGHT_SQUARE_COLOR = new Color(238, 207, 161),
        DARK_SQUARE_COLOR = new Color(205, 133, 63);


    /** Locations of images of white and black queens. */
    private static final String
        SPEAR_THROW_IMAGE = "spear.png",
        WHITE_QUEEN_IMAGE = "wq4.png",
        BLACK_QUEEN_IMAGE = "bq4.png";


    /** Size parameters. */
    private static final int
        FONT_SIZE = 18,
        SQUARE_SIDE = 30,
        BOARD_SIDE = SQUARE_SIDE * 10;


    /** A graphical representation of an Amazons board that sends commands
     *  derived from mouse clicks to COMMANDS.  */
    BoardWidget(ArrayBlockingQueue<String> commands) {
        _commands = commands;
        setMouseHandler("click", this::mouseClicked);
        setPreferredSize(BOARD_SIDE, BOARD_SIDE + 2 * SQUARE_SIDE);

        try {
            _whiteQueen = ImageIO.read(Utils.getResource(WHITE_QUEEN_IMAGE));
            _blackQueen = ImageIO.read(Utils.getResource(BLACK_QUEEN_IMAGE));
            _spearThrow = ImageIO.read(Utils.getResource(SPEAR_THROW_IMAGE));
        } catch (IOException excp) {
            System.err.println("Could not read queen images.");
            System.exit(1);
        }
        _acceptingMoves = false;
    }

    /** Draw the bare board G.  */
    private void drawGrid(Graphics2D g) {
        g.setColor(LIGHT_SQUARE_COLOR);
        for (int i = 0; i < Board.SIZE; i += 1) {
            int j = 0;
            if (i % 2 == 0) {
                j += 1;
            }
            for (; j < Board.SIZE; j += 2) {
                g.fillRect(j * SQUARE_SIDE, i * SQUARE_SIDE,
                        SQUARE_SIDE, SQUARE_SIDE);
            }
        }
        g.setColor(DARK_SQUARE_COLOR);
        for (int i = 0; i < Board.SIZE; i += 1) {
            int j = 0;
            if (i % 2 != 0) {
                j += 1;
            }
            for (; j < Board.SIZE; j += 2) {
                g.fillRect(j * SQUARE_SIDE, i * SQUARE_SIDE,
                        SQUARE_SIDE, SQUARE_SIDE);
            }
        }
        g.setColor(SELECTED_FROM_COLOR);
        if (_from != null) {
            g.fillRect(cx(_from.col()), cy(_from.row()),
                    SQUARE_SIDE, SQUARE_SIDE);
        }

        g.setColor(SELECTED_TO_COLOR);
        if (_to != null) {
            g.fillRect(cx(_to.col()), cy(_to.row()), SQUARE_SIDE, SQUARE_SIDE);
        }
    }

    /** Draw the display message G.  */
    private void drawMessage(Graphics2D g) {
        g.setColor(new Color(0, 0, 0));
        g.setStroke(new BasicStroke(3));
        g.drawLine(0,  10 * SQUARE_SIDE, 10 * SQUARE_SIDE, 10 * SQUARE_SIDE);
        g.drawLine(0,  11 * SQUARE_SIDE, 10 * SQUARE_SIDE, 11 * SQUARE_SIDE);
        g.drawLine(BOARD_SIDE / 2,  11 * SQUARE_SIDE,
                BOARD_SIDE / 2, 12 * SQUARE_SIDE + 3);
        Font f = new Font(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE);
        g.setFont(f);
        g.drawString("To move: " + _board.turn().toName(),
                3 * SQUARE_SIDE, BOARD_SIDE + 6 + SQUARE_SIDE / 2);
        if (_board.getManual().contains(Piece.WHITE)) {
            g.drawString("White: Manual", SQUARE_SIDE / 2,
                    BOARD_SIDE + 8 + SQUARE_SIDE + SQUARE_SIDE / 2);
        } else {
            g.drawString("White: Auto", SQUARE_SIDE / 2,
                    BOARD_SIDE + 8 + SQUARE_SIDE + SQUARE_SIDE / 2);
        }
        if (_board.getManual().contains(Piece.BLACK)) {
            g.drawString("Black: Manual", 5 * SQUARE_SIDE + SQUARE_SIDE / 2,
                    BOARD_SIDE + 8 + SQUARE_SIDE + SQUARE_SIDE / 2);
        } else {
            g.drawString("Black: Auto", 5 * SQUARE_SIDE + SQUARE_SIDE / 2,
                    BOARD_SIDE + 8 + SQUARE_SIDE + SQUARE_SIDE / 2);
        }
    }

    /** Draw the display message G.  */
    private void drawImage(Graphics2D g) {
        List<Square> white = _board.getWhiteQueen();
        for (Square s: white) {
            drawQueen(g, s, Piece.WHITE);
        }
        List<Square> black = _board.getBlackQueen();
        for (Square s: black) {
            drawQueen(g, s, Piece.BLACK);
        }
        List<Square> spear = _board.getSpear();
        for (Square s: spear) {
            g.drawImage(_spearThrow, cx(s.col()) + 2,
                    cy(s.row()) + 4, null);
        }
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        drawGrid(g);
        drawMessage(g);
        drawImage(g);
    }

    /** Draw a queen for side PIECE at square S on G.  */
    private void drawQueen(Graphics2D g, Square s, Piece piece) {
        g.drawImage(piece == WHITE ? _whiteQueen : _blackQueen,
                    cx(s.col()) + 2, cy(s.row()) + 4, null);
    }

    /** Handle a click on S. */
    private void click(Square s) {
        if (_from == null && _to == null && _spear == null
                && _board.isLegal(s)) {
            _from = Square.sq(s.index());
        } else if (_from != null && _to == null && _spear == null) {
            if (s.index() == _from.index()) {
                _from = null;
            } else if (_board.isUnblockedMove(_from, s, null)) {
                _to = Square.sq(s.index());
            }
        } else if (_from != null && _to != null && _spear == null) {
            if (s.index() == _to.index()) {
                _to = null;
            } else if (_board.isUnblockedMove(_from, _to, s)) {
                _spear = Square.sq(s.index());
            }
        }
        if (_from != null && _to != null && _spear != null) {
            _commands.add(Move.mv(_from, _to, _spear).toString());
            _from = null;
            _to = null;
            _spear = null;
        }
        repaint();
    }

    /** Handle mouse click event E. */
    private synchronized void mouseClicked(String unused, MouseEvent e) {
        int xpos = e.getX(), ypos = e.getY();
        int x = xpos / SQUARE_SIDE,
            y = (BOARD_SIDE - ypos) / SQUARE_SIDE;
        if (_acceptingMoves
            && x >= 0 && x < Board.SIZE && y >= 0 && y < Board.SIZE) {
            click(sq(x, y));
        }
    }

    /** Revise the displayed board according to BOARD. */
    synchronized void update(Board board) {
        _board.copy(board);
        repaint();
    }

    /** Turn on move collection iff COLLECTING, and clear any current
     *  partial selection.   When move collection is off, ignore clicks on
     *  the board. */
    void setMoveCollection(boolean collecting) {
        _acceptingMoves = collecting;
        repaint();
    }

    /** Return x-pixel coordinate of the left corners of column X
     *  relative to the upper-left corner of the board. */
    private int cx(int x) {
        return x * SQUARE_SIDE;
    }

    /** Return y-pixel coordinate of the upper corners of row Y
     *  relative to the upper-left corner of the board. */
    private int cy(int y) {
        return (Board.SIZE - y - 1) * SQUARE_SIDE;
    }

    /** Return x-pixel coordinate of the left corner of S
     *  relative to the upper-left corner of the board. */
    private int cx(Square s) {
        return cx(s.col());
    }

    /** Return y-pixel coordinate of the upper corner of S
     *  relative to the upper-left corner of the board. */
    private int cy(Square s) {
        return cy(s.row());
    }

    /** Queue on which to post move commands (from mouse clicks). */
    private ArrayBlockingQueue<String> _commands;
    /** Board being displayed. */
    private final Board _board = new Board();

    /** Image of white queen. */
    private BufferedImage _whiteQueen;
    /** Image of black queen. */
    private BufferedImage _blackQueen;
    /** Image of spear. */
    private BufferedImage _spearThrow;

    /** True iff accepting moves from user. */
    private boolean _acceptingMoves;

    /**. Collection of FROM. **/
    private Square _from;
    /**. Collection of TO. **/
    private Square _to;
    /**. Collection of SPEAR. **/
    private Square _spear;

}
