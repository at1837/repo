package amazons;

import static amazons.Piece.*;
import java.util.Iterator;

/** A Player that automatically generates moves.
 *  @author Ying Lung Tang
 */
class AI extends Player {

    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;
    /**. Save for the depth edge. **/
    private static final int EDGE = 60;
    /**. Save for the depth stage one. **/
    private static final int STAGE_ONE = 30;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    Move findMove() {
        Board b = new Board(board());
        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove = Move.mv("a1-a2(a3)");

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    int findMove(Board board, int depth, boolean saveMove, int sense,
                 int alpha, int beta) {
        if (depth == 0 || board.winner() != null) {
            return staticScore(board);
        }
        Iterator<Move> legalMoves = board.legalMoves();
        int bestValue = 0;
        Move bestMove = null;
        if (sense == 1) {
            bestValue = -Integer.MAX_VALUE;
            while (legalMoves.hasNext()) {
                Move m = legalMoves.next();
                Board b = new Board(board);
                b.makeMove(m);

                int compareValue =
                        findMove(b, depth - 1, false, -1, alpha, beta);
                if (bestValue < compareValue) {
                    bestValue = compareValue;
                    bestMove = Move.mv(m.toString());
                }
                alpha = Math.max(alpha, bestValue);
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            bestValue = Integer.MAX_VALUE;
            while (legalMoves.hasNext()) {
                Move m = legalMoves.next();
                Board b = new Board(board);
                b.makeMove(m);

                int compareValue =
                        findMove(b, depth - 1, false, 1, alpha, beta);
                if (bestValue > compareValue) {
                    bestValue = compareValue;
                    bestMove = Move.mv(m.toString());
                }
                alpha = Math.min(alpha, compareValue);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        if (saveMove) {
            _lastFoundMove = Move.mv(bestMove.toString());
        }
        return bestValue;
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private int maxDepth(Board board) {
        int N = board.numMoves();
        if (N >= 40) {
            return 1;
        } else if (N >= 30) {
            return 2;
        } else if (N >= 20) {
            return 3;
        } else if (N >= 10) {
            return 5;
        }
      //  else if (board.getCanMove() <= 2) {
       //     return 3;
       // }
        else {
            return 5;
        }
    }

    /** Return a heuristic value for BOARD. */
    int staticScore(Board board) {
        Piece winner = board.winner();
        if (winner == BLACK) {
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            return WINNING_VALUE;
        } else {
            int myMove = board.countQueenNeighbor(Piece.WHITE.toSymbol());

            int oppMove =
                    board.countQueenNeighbor(Piece.BLACK.toSymbol());
            return myMove - oppMove;
        }
    }

    /** Return a heuristic value for BOARD. */
    int fastStaticScore(Board board) {
        int myMove =
                board.fastCountQueenNeighbor(_myPiece.toSymbol());
        int oppMove =
                board.fastCountQueenNeighbor(_myPiece.opponent().toSymbol());
        return myMove - oppMove;
    }

}
