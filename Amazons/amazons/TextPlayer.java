package amazons;

import java.util.regex.Matcher;


/** A Player that takes input as text commands from the standard input.
 *  @author Ying Lung Tang
 */
class TextPlayer extends Player {

    /** A new TextPlayer with no piece or controller (intended to produce
     *  a template). */
    TextPlayer() {
        this(null, null);
    }

    /** A new TextPlayer playing PIECE under control of CONTROLLER. */
    private TextPlayer(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new TextPlayer(piece, controller);
    }

    @Override
    String myMove() {
        while (true) {
            String line = _controller.readLine();
            if (line == null) {
                return "quit";
            } else {
                Matcher moveMatcher = Move.MOVE_PATTERN.matcher("");
                moveMatcher.reset(line);
                Move m = Move.mv(line);
                if (moveMatcher.matches()
                        && !_controller.board().isUnblockedMove(m)) {
                    _controller.reportError("Invalid move. "
                            + "Please try again.");
                    continue;
                } else {
                    return line;
                }
            }
        }
    }

}
