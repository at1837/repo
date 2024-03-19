package amazons;
import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Arrays;

/** Junit tests for our Board iterators.
 *  @author Ying Lung Tang
 */
public class IteratorTests {

    /** Run the JUnit tests in this package. */
    public static void main(String[] ignored) {
        textui.runClasses(IteratorTests.class);
    }

    /** Tests reachableFromIterator to make sure it returns all reachable
     *  Squares. This method may need to be changed based on
     *   your implementation. */
    @Test
    public void testReachableFrom() {
        Board b = new Board();
        buildBoard(b, RB);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(5, 4), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertTrue(RS.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(RS.size(), numSquares);
        assertEquals(RS.size(), squares.size());
    }

    /** Tests legalMovesIterator to make sure it returns all legal Moves.
     *  This method needs to be finished and may need to be changed
     *  based on your implementation. */
    @Test
    public void testLegalMoves() {
        Board b = new Board();
        buildBoard(b, LM);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(LMS.contains(m));
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(LMS.size(), numMoves);
        assertEquals(LMS.size(), moves.size());

        List<Move> L1 = new ArrayList<>();
        List<Move> L2 = new ArrayList<>();
        Board b2 = new Board();
        Iterator<Move> legalMoves2 = b2.legalMoves();
        while (legalMoves2.hasNext()) {
            L1.add(legalMoves2.next());
        }

        Iterator<Move> legalMoves3 = b2.legalMoves2();
        while (legalMoves3.hasNext()) {
            L2.add(legalMoves3.next());
        }

        for (Move m : L1) {
            if (L2.contains(m)) {
                L2.remove(m);
            }
        }
        assertEquals(0, L2.size());
    }


    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                b.put(piece, Square.sq(col, row));
            }
        }
    }

    static final Piece E = Piece.EMPTY;

    static final Piece W = Piece.WHITE;

    static final Piece B = Piece.BLACK;

    static final Piece S = Piece.SPEAR;

    static final Piece[][] RB =
    {
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, W, W },
            { E, E, E, E, E, E, E, S, E, S },
            { E, E, E, S, S, S, S, E, E, S },
            { E, E, E, S, E, E, E, E, B, E },
            { E, E, E, S, E, W, E, E, B, E },
            { E, E, E, S, S, S, B, W, B, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
    };

    static final Set<Square> RS =
            new HashSet<>(Arrays.asList(
                    Square.sq(5, 5),
                    Square.sq(4, 5),
                    Square.sq(4, 4),
                    Square.sq(6, 4),
                    Square.sq(7, 4),
                    Square.sq(6, 5),
                    Square.sq(7, 6),
                    Square.sq(8, 7)));

    static final Piece[][] LM =
    {
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, S, S, S, E },
            { E, E, E, E, S, S, S, E, S, S },
            { E, E, E, E, S, E, S, S, B, E },
            { E, E, E, E, S, W, E, S, B, E },
            { E, E, E, E, S, S, S, W, B, S },
            { E, E, E, E, E, E, S, S, S, E },
            { E, E, E, E, E, E, E, S, S, E },
            { E, E, E, E, E, E, E, E, S, E },
    };

    static final Set<Move> LMS =
        new HashSet<>(Arrays.asList(
            Move.mv("h4-g5(h4)"),
            Move.mv("f5-g5(f6)"),
            Move.mv("h4-f6(g5)"),
            Move.mv("f5-f6(f5)"),
            Move.mv("h4-f6(h4)"),
            Move.mv("h4-g5(f6)"),
            Move.mv("f5-f6(g5)"),
            Move.mv("f5-g5(f5)")));
}
