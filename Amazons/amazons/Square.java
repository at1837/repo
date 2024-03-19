package amazons;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static amazons.Utils.*;

/** Represents a position on an Amazons board.  Positions are numbered
 *  from 0 (lower-left corner) to 99 (upper-right corner).  Squares
 *  are immutable and unique: there is precisely one square created for
 *  each distinct position.  Clients create squares using the factory method
 *  sq, not the constructor.  Because there is a unique Square object for each
 *  position, you can freely use the cheap == operator (rather than the
 *  .equals method) to compare Squares, and the program does not waste time
 *  creating the same square over and over again.
 *  @author Ying Lung Tang
 */
final class Square {

    /** The regular expression for a square designation (e.g.,
     *  a3). For convenience, it is in parentheses to make it a
     *  group.  This subpattern is intended to be incorporated into
     *  other pattern that contain square designations (such as
     *  patterns for moves). */
    static final String SQ = "([a-j](?:[1-9]|10))";

    /** Return my row position, where 0 is the bottom row. */
    int row() {
        return _row;
    }

    /** Return my column position, where 0 is the leftmost column. */
    int col() {
        return _col;
    }

    /** Return my index position (0-99).  0 represents square a1, and 99
     *  is square j10. */
    int index() {
        return _index;
    }

    /** Return my str in right formal. **/
    String str() {
        return _str;
    }

    /** Return true iff THIS - TO is a valid queen move. */
    boolean isQueenMove(Square to) {
        if (exists(to.col(), to.row())) {
            for (int i = 0; i < DIR.length; i += 1) {
                for (int j = 1; j < Board.SIZE; j += 1) {
                    Square checkSquare = queenMove(i, j);
                    if (checkSquare != null) {
                        if (to.equals(checkSquare)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /** Definitions of direction for queenMove.  DIR[k] = (dcol, drow)
     *  means that to going one step from (col, row) in direction k,
     *  brings us to (col + dcol, row + drow). */
    private static final int[][] DIR = {
        { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 },
        { 0, -1 }, { -1, -1 }, { -1, 0 }, { -1, 1 }
    };

    /** Return the Square that is STEPS>0 squares away from me in direction
     *  DIR, or null if there is no such square.
     *  DIR = 0 for north, 1 for northeast, 2 for east, etc., up to 7 for
     *  northwest. If DIR has another value, return null. Thus, unless the
     *  result is null the resulting square is a queen move away from me. */
    Square queenMove(int dir, int steps) {
        Square result = new Square(_index);
        int dirCol = DIR[dir][0];
        int dirRow = DIR[dir][1];
        for (int i = 0; i < steps; i += 1) {
            int nextCol = result.col() + dirCol;
            int nextRow = result.row() + dirRow;
            int index = findIndex(nextCol, nextRow);
            if (index == -1) {
                return null;
            } else {
                Square checkSquare = new Square(index);
                result = checkSquare;
            }
        }
        return result;
    }

    /** Return the index by COL and ROW. If over
     *  the boundary, return -1 **/
    private static int findIndex(int col, int row) {
        if (!exists(col, row)) {
            return -1;
        }
        return row * Board.SIZE + col;
    }

    /** Return the direction (an int as defined in the documentation
     *  for queenMove) of the queen move THIS-TO. */
    public int direction(Square to) {
        assert isQueenMove(to);
        int dirOfCol = to.col();
        int dirOfRow = to.row();
        if (dirOfCol == _col && dirOfRow > _row) {
            return 0;
        } else if (dirOfCol > _col && dirOfRow > _row) {
            return 1;
        } else if (dirOfCol > _col && dirOfRow == _row) {
            return 2;
        } else if (dirOfCol > _col && dirOfRow < _row) {
            return 3;
        } else if (dirOfCol == _col && dirOfRow < _row) {
            return 4;
        } else if (dirOfCol < _col && dirOfRow < _row) {
            return 5;
        } else if (dirOfCol < _col && dirOfRow == _row) {
            return 6;
        } else {
            return 7;
        }
    }

    /** Return the number that reduce DIR to only 1 step different. **/
    private int reduceDifferent(int dir) {
        int result = dir;
        if (result > 1) {
            result = 1;
        }
        if (result < -1) {
            result = -1;
        }
        return result;
    }

    /** Return true iff col and row in FROM and TO are same. **/
    public boolean equals(Square to) {
        return _col == to.col() && _row == to.row();
    }
    @Override
    public String toString() {
        return _str;
    }

    /** Return true iff COL ROW is a legal square. */
    static boolean exists(int col, int row) {
        return row >= 0 && col >= 0 && row < Board.SIZE && col < Board.SIZE;
    }

    /** Return the (unique) Square denoting COL ROW. */
    static Square sq(int col, int row) {
        if (!exists(row, col)) {
            throw error("row or column out of bounds");
        }
        return sq(row * Board.SIZE + col);
    }

    /** Return the (unique) Square denoting the position with index INDEX. */
    static Square sq(int index) {
        int row = calculateRow(index);
        int col = calculateCol(index);
        if (exists(col, row)) {
            return SQUARES[index];
        }
        return null;
    }

    /** Return the (unique) Square denoting the position COL ROW, where
     *  COL ROW is the standard text format for a square (e.g., a4). */
    static Square sq(String col, String row) {
        col = col.toLowerCase();
        int findCol = (int) col.charAt(0) - (int) 'a';
        int findRow = Integer.parseInt(row) - 1;
        int index = findIndex(findCol, findRow);
        if (index == -1) {
            throw error("row or column out of bounds");
        }
        return SQUARES[index];
    }

    /** Return the (unique) Square denoting the position in POSN, in the
     *  standard text format for a square (e.g. a4). POSN must be a
     *  valid square designation. */
    static Square sq(String posn) {
        assert posn.matches(SQ);
        String col = "";
        String row = posn.substring(1);
        col += posn.charAt(0);
        return sq(col, row);
    }

    /** Return an iterator over all Squares. */
    static Iterator<Square> iterator() {
        return SQUARE_LIST.iterator();
    }

    /** Return the Square with index INDEX. */
    private Square(int index) {
        _index = index;
        _row = calculateRow(index);
        _col = calculateCol(index);
        _str = calculateStr(index);
    }


        /** Return the row number with INDEX. **/
    private static int calculateRow(int index) {
        return index / Board.SIZE;
    }

    /** Return the col number with INDEX. **/
    private static int calculateCol(int index) {
        return index % Board.SIZE;
    }

    /** Return the str by INDEX. **/
    private static String calculateStr(int index) {
        String result = "";
        int letter = (int) 'a' + calculateCol(index);
        result += (char) letter;
        String number = String.valueOf(calculateRow(index) + 1);
        result += number;
        return result;
    }

    /** The cache of all created squares, by index. */
    private static final Square[] SQUARES =
        new Square[Board.SIZE * Board.SIZE];

    /** SQUARES viewed as a List. */
    private static final List<Square> SQUARE_LIST = Arrays.asList(SQUARES);

    static {
        for (int i = Board.SIZE * Board.SIZE - 1; i >= 0; i -= 1) {
            SQUARES[i] = new Square(i);
        }
    }

    /** My index position. */
    private final int _index;

    /** My row and column (redundant, since these are determined by _index). */
    private final int _row, _col;

    /** My String denotation. */
    private final String _str;

}
