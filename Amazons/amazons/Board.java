package amazons;

import static amazons.Piece.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collections;


/** The state of an Amazons Game.
 *  @author Ying Lung Tang
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 10;

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        if (model._turn != null) {
            _turn = setPiece(model._turn);
        }

        if (model._winner != null) {
            _winner = setPiece(model._winner);
        }
        _numMoves = model.numMoves();

        if (model._myLastMove != null) {
            _myLastMove = Move.mv(model._myLastMove.toString());
        }

        if (model._myLastPiece != null) {
            _myLastPiece = setPiece(model._myLastPiece);
        }
        if (model._oppLastMove != null) {
            _oppLastMove = Move.mv(model._oppLastMove.toString());
        }
        if (model._oppLastPiece != null) {
            _oppLastPiece = setPiece(model._oppLastPiece);
        }

        _piece = new ArrayList<>(model._piece);

        _countMove = new HashSet<>(model._countMove);
        _whiteQueen = new ArrayList<>(model._whiteQueen);
        _blackQueen = new ArrayList<>(model._blackQueen);
        _throwSpear = new ArrayList<>(model._throwSpear);
        canMoveQueen = new ArrayList<>(model.canMoveQueen);
        _auto = new ArrayList<>(model._auto);
        _manual = new ArrayList<>(model._manual);

        _switch = model._switch;
        _numMoves = model._numMoves;
        _undid = model._undid;
        canMove = model.canMove;

    }

    /** Change the _turn to P. **/
    void setTurn(Piece p) {
        _turn = p;
    }

    /** Return a new Piece that same as P. **/
    private Piece setPiece(Piece p) {
        if (p.toSymbol().equals("-")) {
            return Piece.EMPTY;
        } else if (p.toSymbol().equals("W")) {
            return Piece.WHITE;
        } else if (p.toSymbol().equals("B")) {
            return Piece.BLACK;
        } else {
            return Piece.SPEAR;
        }
    }


    /** Clears the board to the initial position. */
    void init() {
        _turn = WHITE;
        _winner = EMPTY;
        _piece = new ArrayList<>();
        _countMove = new HashSet<>();
        _whiteQueen = new ArrayList<>();
        _blackQueen = new ArrayList<>();
        _throwSpear = new ArrayList<>();
        canMoveQueen = new ArrayList<>();
        _numMoves = 0;
        _myLastMove = null;
        _myLastPiece = null;
        _switch = 0;
        canMove = 8;
        _oppLastMove = null;
        _oppLastPiece = null;
        _undid = true;

        for (int i = 0; i < Board.SIZE * Board.SIZE; i += 1) {
            _piece.add(EMPTY);
        }
        put(Piece.WHITE, 'd', "1");
        put(Piece.WHITE, 'g', "1");
        put(Piece.WHITE, 'a', "4");
        put(Piece.WHITE, 'j', "4");

        put(Piece.BLACK, 'd', "10");
        put(Piece.BLACK, 'g', "10");
        put(Piece.BLACK, 'a', "7");
        put(Piece.BLACK, 'j', "7");

        _auto = new ArrayList<>();
        _manual = new ArrayList<>();
    }

    /** Return the collection of Auto player. **/
    List<Piece> getAuto() {
        return _auto;
    }

    /**. Return the number of CanMove. **/
    int getCanMove() {
        return canMove;
    }

    /** Return the collection of Manual player. **/
    List<Piece> getManual() {
        return _manual;
    }

    /** Return the collection of piece. **/
    List getPiece() {
        return _piece;
    }

    /** Return the collection of spear. **/
    List<Square> getSpear() {
        return _throwSpear;
    }

    /** Return the Piece whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the number of moves (that have not been undone) for this
     *  board. */
    int numMoves() {
        return Board.SIZE * Board.SIZE - 8 - _numMoves;
    }


    /** Return the winner in the current position, or null if the game is
     *  not yet finished. */
    Piece winner() {
        if (_turn.toSymbol().equals("B")) {
            if (countMoveQueen("B") == 0) {
                _winner = Piece.WHITE;
                return _winner;
            }
        } else if (_turn.toSymbol().equals("W")) {
            if (countMoveQueen("W") == 0) {
                _winner = Piece.BLACK;
                return _winner;
            }
        }
        return null;
    }

    /** Return is any winner of the turn S. **/
    int countMoveQueen(String s) {
        if (!_turn.toSymbol().equals(s)) {
            _turn = _turn.opponent();
        }
        int[] position = findQueen(s);
        int count = 0;
        for (int i = 0; i < position.length; i += 1) {
            if (canMove(position[i])) {
                count += 1;
            }
        }
        if (!_turn.toSymbol().equals(s)) {
            _turn = _turn.opponent();
        }
        return count;
    }

    /** Return White queen. **/
    List<Square> getWhiteQueen() {
        return _whiteQueen;
    }

    /** Return Black queen. **/
    List<Square> getBlackQueen() {
        return _blackQueen;
    }

    /** Return number of move-able neighbor that all S queen can move. **/
    int countQueenNeighbor(String s) {
        _countMove = new HashSet<>();
        int[] position = findQueen(s);
        int count = 0;
        for (int i = 0; i < position.length; i += 1) {
            count += maxReachable(Square.sq(position[i]));
        }
        return count;
    }

    /** Return number of move-able neighbor of the turn S. **/
    int fastCountQueenNeighbor(String s) {
        _countMove = new HashSet<>();
        int[] position = findQueen(s);
        int count = 0;
        for (int i = 0; i < 4; i += 1) {
            count += numMove(position[i], true);
        }
        return count;
    }

    /** Return number of move-able queen S. **/
    int canMoveQueen(String s) {
        _countMove = new HashSet<>();
        int[] position = findQueen(s);
        int result = 0;
        for (int i = 0; i < position.length; i += 1) {
            int count = numMove(position[i], true);
            if (count != 0) {
                result += 1;
            }
        }
        return result;
    }

    /** Return iff the queen of INDEX is move-able. **/
    boolean canMove(int index) {
        return numMove(index, false) > 0;
    }

    /** Return number of neighbor that can move from INDEX and
     * if there is NEIGHBOR. **/
    int numMove(int index, boolean neighbor) {
        Square from = Square.sq(index);
        Square to;
        int moveCount = 0;
        for (int i = 0; i < 8; i += 1) {
            to = null;
            if (i == 0 && index % 10 != 0) {
                to = Square.sq(index - 1 + 10);
            } else if (i == 1) {
                to = Square.sq(index + 10);
            } else if (i == 2 && index % 10 != 9) {
                to = Square.sq(index + 1 + 10);
            } else if (i == 3 && index % 10 != 9) {
                to = Square.sq(index + 1);
            } else if (i == 4 && index % 10 != 9) {
                to = Square.sq(index + 1 - 10);
            } else if (i == 5) {
                to = Square.sq(index - 10);
            } else if (i == 6) {
                to = Square.sq(index - 1 - 10);
            } else if (i == 7 && index % 10 != 0) {
                to = Square.sq(index - 1);
            }
            if (to != null) {
                if (neighbor) {
                    if (!_countMove.contains(to.index())) {
                        if (isUnblockedMove(from, to, null)) {
                            moveCount += 1;
                            _countMove.add(to.index());
                        }
                    }
                } else {
                    if (isUnblockedMove(from, to, null)) {
                        moveCount += 1;
                    }
                }
            }
        }
        return moveCount;
    }

    /** Return the location of S Queen. **/
    int[] findQueen(String s) {
        int[] queens;
        if (s.equals("W")) {
            queens = new int[_whiteQueen.size()];
            for (int i = 0; i < _whiteQueen.size(); i += 1) {
                queens[i] = _whiteQueen.get(i).index();
            }
        } else {
            queens = new int[_blackQueen.size()];
            for (int i = 0; i < _blackQueen.size(); i += 1) {
                queens[i] = _blackQueen.get(i).index();
            }
        }
        return queens;
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        if (s.index() <= Board.SIZE * Board.SIZE - 1) {
            return _piece.get(s.index());
        }
        return null;
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        Square checkSquare = Square.sq(col, row);
        return get(checkSquare);
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, String row) {
        return get(col - 'a', Integer.parseInt(row) - 1);
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        Piece check = _piece.get(s.index());
        if (check.toSymbol().equals("W")) {
            _whiteQueen.remove(Square.sq(s.toString()));
        }
        if (check.toSymbol().equals("B")) {
            _blackQueen.remove(Square.sq(s.toString()));
        }
        if (check.toSymbol().equals("S")) {
            _throwSpear.remove(Square.sq(s.toString()));
        }
        if (p.toSymbol().equals("W")) {
            _whiteQueen.add(Square.sq(s.toString()));
        }
        if (p.toSymbol().equals("B")) {
            _blackQueen.add(Square.sq(s.toString()));
        }
        if (p.toSymbol().equals("S")) {
            _throwSpear.add(Square.sq(s.toString()));
        }
        _piece.set(s.index(), p);
    }

    /** Set square (COL, ROW) to P. */
    final void put(Piece p, int col, int row) {
        Square checkSquare = Square.sq(col, row);
        put(p, checkSquare);
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, String row) {
        put(p, col - 'a', Integer.parseInt(row) - 1);
    }

    /** Return true iff FROM - TO is an unblocked queen move on the current
     *  board, ignoring the contents of ASEMPTY, if it is encountered.
     *  For this to be true, FROM-TO must be a queen move and the
     *  squares along it, other than FROM and ASEMPTY, must be
     *  empty. ASEMPTY may be null, in which case it has no effect. */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        if (asEmpty != null) {
            if (!isLegal(from, to, asEmpty)) {
                return false;
            }
        }
        return isLegal(from, to);
    }

    /** Return true if M is reachable. **/
    boolean isUnblockedMove(Move m) {
        if (m != null) {
            Square from = m.from();
            Square to = m.to();
            Square spear = m.spear();
            return isUnblockedMove(from, to, spear);
        }
        return false;
    }

    /** Return true if TO is not reachable from FROM and spear ASEMPTY. **/
    boolean isReachable(Square from, Square to, Square asEmpty) {
        if (from.isQueenMove(to)) {
            int direction = from.direction(to);
            for (int i = 0; i < Board.SIZE; i += 1) {
                if (!from.equals(to)) {
                    Square moved = from.queenMove(direction, 1);
                    if (asEmpty != null && moved.index() == asEmpty.index()) {
                        from = moved;
                    } else if (isEmptyPiece(moved)) {
                        from = moved;
                    } else {
                        break;
                    }
                }
                if (from.equals(to)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Return max spot that CHECK can move. **/
    int maxReachable(Square check) {
        int count = 0;
        for (int i = 0; i < 8; i += 1) {
            int x = 0;
            Square from = Square.sq(check.index());
            for (int j = 0; j < Board.SIZE; j += 1) {
                Square moved = from.queenMove(i, 1);
                if (moved != null && isEmptyPiece(moved)) {
                    from = moved;
                    x += 1;
                } else {
                    break;
                }
            }
            count += x;
        }
        return count;
    }

    /** Return true iff MOVED is a empty piece. **/
    boolean isEmptyPiece(Square moved) {
        return _piece.get(moved.index()).toSymbol().equals("-");
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return _turn.toSymbol().equals(_piece.get(from.index()).toSymbol())
             && _piece.get(from.index()).toName() != null;
    }

    /** Return true iff FROM-TO is a valid first part of move, ignoring
     *  spear throwing. */
    boolean isLegal(Square from, Square to) {
        return isLegal(from) && isEmptyPiece(to) && isReachable(from, to, null);
    }

    /** Return true iff FROM-TO(SPEAR) is a legal move in the current
     *  position. */
    boolean isLegal(Square from, Square to, Square spear) {
        if (spear.index() == from.index()) {
            return isLegal(from, to);
        } else {
            return isLegal(from, to) && isEmptyPiece(spear)
                    && isReachable(to, spear, from);
        }
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        if (move != null) {
            String checkInput = move.toString();
            if (Move.isGrammaticalMove(checkInput)) {
                Move checkMove = Move.mv(checkInput);
                if (checkMove != null) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Move FROM-TO(SPEAR), assuming this is a legal move. */
    void makeMove(Square from, Square to, Square spear) {
        Move m = Move.mv(from, to, spear);
        if (m != null && isUnblockedMove((m))) {
            Move lastMove = Move.mv(from, to, spear);
            Piece lastPiece = get(from);
            put(Piece.EMPTY, from);
            put(lastPiece, to);
            put(Piece.SPEAR, spear);
            _numMoves += 1;
            if (_switch == 0) {
                _myLastMove = lastMove;
                _myLastPiece = lastPiece;
                _switch = 1;
            } else {
                _oppLastMove = lastMove;
                _oppLastPiece = lastPiece;
                _switch = 0;
            }
            _undid = false;
            if (_turn.toSymbol().equals("W")) {
                for (Square s : _whiteQueen) {
                    if (!canMoveQueen.contains(s) && !canMove(s.index())) {
                        canMove -= 1;
                        canMoveQueen.add(Square.sq(s.index()));
                    }
                }
                _turn = _turn.opponent();
                for (Square s : _blackQueen) {
                    if (!canMoveQueen.contains(s) && !canMove(s.index())) {
                        canMove -= 1;
                        canMoveQueen.add(Square.sq(s.index()));
                    }
                }
                _turn = _turn.opponent();
            } else {
                for (Square s : _blackQueen) {
                    if (!canMoveQueen.contains(s) && !canMove(s.index())) {
                        canMove -= 1;
                        canMoveQueen.add(Square.sq(s.index()));
                    }
                }
                _turn = _turn.opponent();
                for (Square s : _whiteQueen) {
                    if (!canMoveQueen.contains(s) && !canMove(s.index())) {
                        canMove -= 1;
                        canMoveQueen.add(Square.sq(s.index()));
                    }
                }
                _turn = _turn.opponent();
            }
            if (_turn.toSymbol().equals("W")) {
                _turn = Piece.BLACK;
            } else {
                _turn = Piece.WHITE;
            }
        }
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        if (move != null) {
            makeMove(move.from(), move.to(), move.spear());
        }
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (_numMoves >= 2 && !_undid) {
            put(Piece.EMPTY, _myLastMove.spear());
            put(Piece.EMPTY, _myLastMove.to());
            put(_myLastPiece, _myLastMove.from());

            put(Piece.EMPTY, _oppLastMove.spear());
            put(Piece.EMPTY, _oppLastMove.to());
            put(_oppLastPiece, _oppLastMove.from());

            _numMoves -= 2;
            if (_switch == 0) {
                _switch = 1;
            } else {
                _switch = 0;
            }
            _undid = true;
        }
    }

    /** Return an Iterator over the Squares that are reachable by an
     *  unblocked queen move from FROM. Does not pay attention to what
     *  piece (if any) is on FROM, nor to whether the game is finished.
     *  Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     *  feature is useful when looking for Moves, because after moving a
     *  piece, one wants to treat the Square it came from as empty for
     *  purposes of spear throwing.) */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /** Return an Iterator over all legal moves on the current board. */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /** Return an Iterator over all legal moves on the current board for
     *  SIDE (regardless of whose turn it is). */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /** Return an Iterator over all legal moves on the current board. */
    Iterator<Move> legalMoves2() {
        return new LegalMoveIterator2(_turn);
    }

    /** Return an Iterator over all legal moves on the current board for
     *  SIDE (regardless of whose turn it is). */
    Iterator<Move> legalMoves2(Piece side) {
        return new LegalMoveIterator2(side);
    }

    /** An iterator used by reachableFrom. */
    private class ReachableFromIterator implements Iterator<Square> {

        /** Iterator of all squares reachable by queen move from FROM,
         *  treating ASEMPTY as empty. */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = Square.sq(from.index());
            _move = Square.sq(from.index());
            _dir = -1;
            _steps = 1;
            _asEmpty = asEmpty;
            toNext();
        }

        @Override
        public boolean hasNext() {
            if (_dir < 8) {
                Square moved = _move.queenMove(_dir, _steps);
                if (moved != null && _asEmpty != null
                        && moved.index() == _asEmpty.index()) {
                    _move = moved;
                    _moved = moved;
                    return true;
                } else if (moved == null || !_from.isQueenMove(moved)
                        || !isEmptyPiece(moved)) {
                    toNext();
                    return hasNext();
                } else {
                    _move = moved;
                    _moved = moved;
                    return true;
                }
            }
            return false;
        }

        @Override
        public Square next() {
            return Square.sq(_moved.index());
        }

        /** Advance _dir and _steps, so that the next valid Square is
         *  _steps steps in direction _dir from _from. */
        private void toNext() {
            _dir += 1;
            _move = Square.sq(_from.index());
        }

        /** Starting square. */
        private Square _from;
        /** Current direction. */
        private int _dir;
        /** Current distance. */
        private int _steps;
        /** Square treated as empty. */
        private Square _asEmpty;
        /** Square treated as return for next(). **/
        private Square _move;
        /** Square treated new position. **/
        private Square _moved;

    }

    /** An iterator used by legalMoves. */
    private class LegalMoveIterator implements Iterator<Move> {

        /** All legal moves for SIDE (WHITE or BLACK). */
        LegalMoveIterator(Piece side) {
            if (side.toSymbol().equals("W")) {
                _queens = new ArrayList<>(_whiteQueen);
            } else {
                _queens = new ArrayList<>(_blackQueen);
            }
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;
            result = null;
            counter = 0;
            _queenCount = 0;
            toNext();

        }

        @Override
        public boolean hasNext() {
            counter += 1;
            if (_spearThrows.hasNext()) {
                Square s = _spearThrows.next();
                result = Move.mv(_start, _nextSquare, s);
                return true;
            } else {
                return totoNext();
            }
        }

        @Override
        public Move next() {
            return result;
        }

        /** Return iff there have more queen. **/
        private boolean totoNext() {
            if (_pieceMoves.hasNext()) {
                _nextSquare = _pieceMoves.next();
                _spearThrows = reachableFrom(_nextSquare, _start);
                if (counter != 0) {
                    return hasNext();
                } else {
                    return true;
                }
            } else {
                _queenCount += 1;
                return toNext();
            }
        }

        /** Return iff it can advance so that the next valid Move is
         *  _start-_nextSquare(sp), where sp is the next value of
         *  _spearThrows. */
        private boolean toNext() {
            if (_queenCount < _queens.size()) {
                _start = Square.sq(_queens.get(_queenCount).index());
                if (_piece.get(_start.index()).toSymbol()
                        .equals(_fromPiece.toSymbol())) {
                    _pieceMoves = reachableFrom(_start, null);
                    return totoNext();
                }
            }
            return false;
        }

        /** Color of side whose moves we are iterating. */
        private Piece _fromPiece;
        /** Current starting square. */
        private Square _start;
        /** Remaining starting squares to consider. */
        private Iterator<Square> _startingSquares;
        /** Current piece's new position. */
        private Square _nextSquare;
        /** Remaining moves from _start to consider. */
        private Iterator<Square> _pieceMoves;
        /** Remaining spear throws from _piece to consider. */
        private Iterator<Square> _spearThrows;
        /** Save for the next() return. */
        private Move result;
        /** The control of hasNext(). */
        private int counter;
        /** Collection of queens. */
        private List<Square> _queens;
        /** Index of queens. */
        private int _queenCount;
    }

    /** An iterator used by legalMoves. */
    private class LegalMoveIterator2 implements Iterator<Move> {

        /** All legal moves for SIDE (WHITE or BLACK). */
        LegalMoveIterator2(Piece side) {
            _startingSquares = Square.iterator();
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;
            result = null;
            counter = 0;
            toNext();

        }

        @Override
        public boolean hasNext() {
            counter += 1;
            if (_spearThrows.hasNext()) {
                Square s = _spearThrows.next();
                result = Move.mv(_start, _nextSquare, s);
                return true;
            } else {
                return totoNext();
            }
        }

        @Override
        public Move next() {
            return result;
        }

        /** Return iff there have more TO spot. **/
        private boolean totoNext() {
            if (_pieceMoves.hasNext()) {
                _nextSquare = _pieceMoves.next();
                _spearThrows = reachableFrom(_nextSquare, _start);
                if (counter != 0) {
                    return hasNext();
                } else {
                    return true;
                }
            } else {
                return toNext();
            }
        }

        /** Return iff is can be advance so that the next valid Move is
         *  _start-_nextSquare(sp), where sp is the next value of
         *  _spearThrows. */
        private boolean toNext() {
            if (_startingSquares.hasNext()) {
                _start = _startingSquares.next();
                if (_piece.get(_start.index()).toSymbol()
                        .equals(_fromPiece.toSymbol())) {
                    _pieceMoves = reachableFrom(_start, null);
                    return totoNext();
                } else {
                    return toNext();
                }
            }
            return false;
        }

        /** Color of side whose moves we are iterating. */
        private Piece _fromPiece;
        /** Current starting square. */
        private Square _start;
        /** Remaining starting squares to consider. */
        private Iterator<Square> _startingSquares;
        /** Current piece's new position. */
        private Square _nextSquare;
        /** Remaining moves from _start to consider. */
        private Iterator<Square> _pieceMoves;
        /** Remaining spear throws from _piece to consider. */
        private Iterator<Square> _spearThrows;
        /** Save for the next() return. */
        private Move result;
        /** The control of hasNext(). */
        private int counter;
    }

    @Override
    public String toString() {
        String result = "";
        for (int row = Board.SIZE - 1; row >= 0; row -= 1) {
            result += "  ";
            for (int col = 0; col < Board.SIZE; col += 1) {
                result += " ";
                result += get(col, row).toSymbol();
            }
            result += "\n";
        }
        return result;
    }

    /** An empty iterator for initialization. */
    private static final Iterator<Square> NO_SQUARES =
        Collections.emptyIterator();

    /** Piece whose turn it is (BLACK or WHITE). */
    private Piece _turn;
    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;

    /** Collection of piece. **/
    private List<Piece> _piece;

    /** Collection of queen's neighbor. **/
    private Set<Integer> _countMove;

    /** Collection of queen's neighbor. **/
    private List<Square> _whiteQueen;

    /** Collection of queen's neighbor. **/
    private List<Square> _blackQueen;

    /** Collection of throw spear. **/
    private List<Square> _throwSpear;

    /** Switch for saving last move, 0 = me, 1 = opponent. **/
    private int _switch;

    /** Counter for moves. **/
    private int _numMoves;

    /** Save for my last move. **/
    private Move _myLastMove;

    /** Save for my last piece. **/
    private Piece _myLastPiece;

    /** Save for last move. **/
    private Move _oppLastMove;

    /** Save for last piece. **/
    private Piece _oppLastPiece;

    /** Save for undid. **/
    private boolean _undid;

    /**. Save for Auto player. **/
    private List<Piece> _auto;

    /**. Save for Manual player. **/
    private List<Piece> _manual;

    /**. Save for can move queen. **/
    private int canMove;

    /**. Save for can move queen. **/
    private List<Square> canMoveQueen;

}
