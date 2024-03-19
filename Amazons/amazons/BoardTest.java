package amazons;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void initTest() {
        Board b = new Board();
        assertEquals(100, b.getPiece().size());
        String expect =
                 "   - - - B - - B - - -\n"
              +  "   - - - - - - - - - -\n"
              +  "   - - - - - - - - - -\n"
              +  "   B - - - - - - - - B\n"
              +  "   - - - - - - - - - -\n"
              +  "   - - - - - - - - - -\n"
              +  "   W - - - - - - - - W\n"
              +  "   - - - - - - - - - -\n"
              +  "   - - - - - - - - - -\n"
              +  "   - - - W - - W - - -\n";
        assertEquals(expect, b.toString());
    }

    @Test
    public void putTest() {
        Board b = new Board();
        Square to = Square.sq("d", "6");
        b.put(Piece.WHITE, to);
        b.put(Piece.BLACK, 6, 7);
        b.put(Piece.SPEAR, 'b', "8");
        String expect =
                           "   - - - B - - B - - -\n"
                        +  "   - - - - - - - - - -\n"
                        +  "   - S - - - - B - - -\n"
                        +  "   B - - - - - - - - B\n"
                        +  "   - - - W - - - - - -\n"
                        +  "   - - - - - - - - - -\n"
                        +  "   W - - - - - - - - W\n"
                        +  "   - - - - - - - - - -\n"
                        +  "   - - - - - - - - - -\n"
                        +  "   - - - W - - W - - -\n";
        assertEquals(expect, b.toString());
    }

    @Test
    public void getTest() {
        Board b = new Board();
        b.put(Piece.SPEAR, 'b', "8");
        Square s = Square.sq("b", "8");
        assertEquals(Piece.SPEAR, b.get(s));
        assertEquals(Piece.SPEAR, b.get(1, 7));
        assertEquals(Piece.SPEAR, b.get('b', "8"));
    }

    @Test
    public void isUnblockedMoveTest() {
        Board b = new Board();
        Square from = Square.sq("d", "1");
        Square to = Square.sq("d", "10");
        assertFalse(b.isUnblockedMove(from, to, null));
        to = Square.sq("d", "1");
        assertFalse(b.isUnblockedMove(from, to, null));
        to = Square.sq("d", "2");
        assertTrue(b.isUnblockedMove(from, to, null));
        to = Square.sq("c", "2");
        assertTrue(b.isUnblockedMove(from, to, null));
        to = Square.sq("b", "3");
        assertTrue(b.isUnblockedMove(from, to, null));
        to = Square.sq("a", "4");
        assertFalse(b.isUnblockedMove(from, to, null));
        to = Square.sq("b", "2");
        assertFalse(b.isUnblockedMove(from, to, null));
        to = Square.sq("e", "2");
        assertTrue(b.isUnblockedMove(from, to, null));
        to = Square.sq("g", "4");
        assertTrue(b.isUnblockedMove(from, to, null));
        to = Square.sq("d", "3");
        assertTrue(b.isUnblockedMove(from, to, null));
        to = Square.sq("d", "9");
        assertTrue(b.isUnblockedMove(from, to, null));
        b.put(Piece.SPEAR, 'd', "5");
        assertFalse(b.isUnblockedMove(from, to, null));
        b.put(Piece.BLACK, 'e', "1");
        to = Square.sq("f", "1");
        assertFalse(b.isUnblockedMove(from, to, null));
        to = Square.sq("c", "3");
        assertFalse(b.isUnblockedMove(from, to, null));
        b.put(Piece.WHITE, 'e', "7");
        b.put(Piece.SPEAR, 'd', "7");
        b.put(Piece.SPEAR, 'f', "7");
        b.put(Piece.SPEAR, 'e', "6");
        b.put(Piece.SPEAR, 'e', "8");
        b.put(Piece.SPEAR, 'f', "8");
        from = Square.sq("e", "7");
        to = Square.sq("e", "9");
        assertFalse(b.isUnblockedMove(from, to, null));
        to = Square.sq("e", "1");
        assertFalse(b.isUnblockedMove(from, to, null));
        to = Square.sq("g", "7");
        assertFalse(b.isUnblockedMove(from, to, null));
        to = Square.sq("g", "9");
        assertFalse(b.isUnblockedMove(from, to, null));
        to = Square.sq("g", "5");
        assertTrue(b.isUnblockedMove(from, to, null));
    }

    @Test
    public void isLegalTest() {
        Board b = new Board();
        b.put(Piece.WHITE, Square.sq("d", "6"));
        b.put(Piece.BLACK, 6, 7);
        b.put(Piece.SPEAR, 'b', "8");
        Square s = Square.sq("d", "10");
        assertFalse(b.isLegal(s));
        s = Square.sq("d", "6");
        assertTrue(b.isLegal(s));
        s = Square.sq("b", "8");
        assertFalse(b.isLegal(s));
        s = Square.sq("b", "9");
        assertFalse(b.isLegal(s));
        s = Square.sq("d1");
        assertTrue(b.isLegal(s));
        b.setTurn(Piece.BLACK);
        s = Square.sq("g10");
        assertTrue(b.isLegal(s));
        s = Square.sq("d1");
        assertFalse(b.isLegal(s));

        b.setTurn(Piece.WHITE);
        Square from = Square.sq("d", "6");
        Square to = Square.sq("g", "3");
        assertTrue(b.isLegal(from, to));
        to = Square.sq("b", "8");
        assertFalse(b.isLegal(from, to));
        to = Square.sq("d", "6");
        assertFalse(b.isLegal(from, to));

        to = Square.sq("g", "3");
        Square spear = Square.sq("g", "1");
        assertFalse(b.isLegal(from, to, spear));
        spear = Square.sq("g", "5");
        assertTrue(b.isLegal(from, to, spear));
        spear = Square.sq("g", "3");
        assertFalse(b.isLegal(from, to, spear));
        spear = Square.sq("d", "6");
        assertTrue(b.isLegal(from, to, spear));
        spear = Square.sq("e", "5");
        assertTrue(b.isLegal(from, to, spear));

        Move m = Move.mv("d1-d7(g7)");
        assertTrue(b.isLegal(m));
        m = Move.mv("d10-c9(h4)");
        assertTrue(b.isLegal(m));
        m = Move.mv("d10-c9");
        assertFalse(b.isLegal(m));
        m = Move.mv("d10-c9(h5)");
        assertFalse(b.isLegal(m));
        m = Move.mv("d10(h5)");
        assertFalse(b.isLegal(m));
    }

    @Test
    public void makeMoveTest() {
        Board b = new Board();
        b.makeMove(Move.mv("d1-d7(g7)"));
        b.makeMove(Move.mv("d10-c9(h4)"));
        String expect =
                   "   - - - - - - B - - -\n"
                +  "   - - B - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   B - - W - - S - - B\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   W - - - - - - S - W\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - W - - -\n";
        assertEquals(expect, b.toString());
        assertEquals(90, b.numMoves());
        b.makeMove(Move.mv("d4-e4(d4)"));
        assertEquals(expect, b.toString());
        assertEquals(90, b.numMoves());
        b.makeMove(Move.mv("a4-c5(a4)"));
        assertEquals(expect, b.toString());
        assertEquals(90, b.numMoves());
        b.makeMove(Move.mv("a4-c2(e4)"));
        b.makeMove(Move.mv("c9-c7(d6)"));
        expect =
                   "   - - - - - - B - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   B - B W - - S - - B\n"
                +  "   - - - S - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - S - - S - W\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - W - - - - - - -\n"
                +  "   - - - - - - W - - -\n";
        assertEquals(expect, b.toString());
        b.makeMove(Move.mv("c2-c1(c6)"));
        expect =
                   "   - - - - - - B - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   B - B W - - S - - B\n"
                +  "   - - S S - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - S - - S - W\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - W - - - W - - -\n";
        assertEquals(expect, b.toString());
    }

    @Test
    public void undoTest() {
        Board b = new Board();
        b.undo();
        assertEquals(92, b.numMoves());
        String init =
                   "   - - - B - - B - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   B - - - - - - - - B\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   W - - - - - - - - W\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - W - - W - - -\n";
        assertEquals(init, b.toString());
        b.makeMove(Move.mv("d1-d7(g7)"));
        String first =
                   "   - - - B - - B - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   B - - W - - S - - B\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   W - - - - - - - - W\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - W - - -\n";
        b.undo();
        assertEquals(first, b.toString());
        b.makeMove(Move.mv("d10-c9(h4)"));
        b.undo();
        assertEquals(init, b.toString());
        b.makeMove(Move.mv("d1-d7(g7)"));
        b.makeMove(Move.mv("d10-c9(h4)"));
        b.makeMove(Move.mv("a4-c2(e4)"));
        b.undo();
        assertEquals(first, b.toString());
        b.makeMove(Move.mv("d10-c9(h4)"));
        b.makeMove(Move.mv("a4-c2(e4)"));
        b.makeMove(Move.mv("j7-h5(j7)"));
        b.undo();
        b.makeMove(Move.mv("a4-c2(e4)"));
        b.makeMove(Move.mv("j7-h5(j7)"));
        b.makeMove(Move.mv("c2-c8(b9)"));
        b.undo();
        String expect =
                   "   - - - - - - B - - -\n"
                +  "   - - B - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   B - - W - - S - - B\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - S - - S - W\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - W - - - - - - -\n"
                +  "   - - - - - - W - - -\n";
        assertEquals(expect, b.toString());
    }


    @Test
    public void copyTest() {
        Board b = new Board();
        Move m = Move.mv("d1-d7(g7)");
        b.makeMove(m);

        Board c = new Board();
        c.copy(b);
        String expect =
                   "   - - - B - - B - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   B - - W - - S - - B\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   W - - - - - - - - W\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - W - - -\n";
        assertEquals(expect, b.toString());
        assertEquals(expect, c.toString());

        m = Move.mv("g10-e8(e9)");
        c.makeMove(m);
        String expect2 =
                   "   - - - B - - - - - -\n"
                +  "   - - - - S - - - - -\n"
                +  "   - - - - B - - - - -\n"
                +  "   B - - W - - S - - B\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   W - - - - - - - - W\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - W - - -\n";
        assertEquals(expect, b.toString());
        assertEquals(expect2, c.toString());
    }

    @Test
    public void canMoveTest() {
        Board b = new Board();
        assertTrue(b.canMove(3));
        assertFalse(b.canMove(93));
        b.put(Piece.WHITE, 'a', "1");
        b.put(Piece.WHITE, 'j', "1");
        b.put(Piece.WHITE, 'a', "10");
        b.put(Piece.WHITE, 'j', "10");
        assertTrue(b.canMove(99));
        b.put(Piece.WHITE, 'f', "5");
        assertTrue(b.canMove(45));
        b.put(Piece.SPEAR, 'a', "2");
        assertTrue(b.canMove(0));
        b.put(Piece.SPEAR, 'b', "2");
        b.put(Piece.SPEAR, 'b', "1");
        assertFalse(b.canMove(0));
        b.put(Piece.SPEAR, 'f', "4");
        b.put(Piece.SPEAR, 'f', "6");
        b.put(Piece.SPEAR, 'e', "5");
        b.put(Piece.SPEAR, 'g', "5");
        assertTrue(b.canMove(45));
        b.put(Piece.SPEAR, 'e', "4");
        b.put(Piece.SPEAR, 'e', "6");
        b.put(Piece.SPEAR, 'g', "4");
        assertTrue(b.canMove(45));
        b.put(Piece.SPEAR, 'g', "6");
        assertFalse(b.canMove(45));
        b.put(Piece.SPEAR, 'a', "9");
        b.put(Piece.SPEAR, 'b', "10");
        assertTrue(b.canMove(90));
        b.put(Piece.SPEAR, 'b', "9");
        assertFalse(b.canMove(90));
        b.put(Piece.SPEAR, 'i', "9");
        b.put(Piece.SPEAR, 'i', "10");
        b.put(Piece.SPEAR, 'j', "9");
        assertFalse(b.canMove(99));
        b.put(Piece.SPEAR, 'i', "1");
        b.put(Piece.SPEAR, 'i', "2");
        b.put(Piece.SPEAR, 'j', "2");
        assertFalse(b.canMove(9));
        b.put(Piece.SPEAR, 'a', "8");
        b.put(Piece.SPEAR, 'a', "6");
        b.put(Piece.SPEAR, 'b', "7");
        b.put(Piece.SPEAR, 'b', "6");
        b.put(Piece.SPEAR, 'b', "8");
        assertFalse(b.canMove(60));
        String expect =
                           "   W S - B - - B - S W\n"
                        +  "   S S - - - - - - S S\n"
                        +  "   S S - - - - - - - -\n"
                        +  "   B S - - - - - - - B\n"
                        +  "   S S - - S S S - - -\n"
                        +  "   - - - - S W S - - -\n"
                        +  "   W - - - S S S - - W\n"
                        +  "   - - - - - - - - - -\n"
                        +  "   S S - - - - - - S S\n"
                        +  "   W S - W - - W - S W\n";
        assertEquals(expect, b.toString());

    }
    @Test
    public void winnerTest() {
        Board b = new Board();
        assertNull(b.winner());
        b.put(Piece.SPEAR, 'c', "1");
        b.put(Piece.SPEAR, 'e', "1");
        b.put(Piece.SPEAR, 'd', "2");
        b.put(Piece.SPEAR, 'c', "2");
        b.put(Piece.SPEAR, 'e', "2");
        assertEquals(3, b.countMoveQueen("W"));
        b.put(Piece.SPEAR, 'f', "1");
        b.put(Piece.SPEAR, 'h', "1");
        b.put(Piece.SPEAR, 'g', "2");
        b.put(Piece.SPEAR, 'f', "2");
        b.put(Piece.SPEAR, 'h', "2");
        assertEquals(2, b.countMoveQueen("W"));
        b.put(Piece.SPEAR, 'a', "3");
        b.put(Piece.SPEAR, 'a', "5");
        b.put(Piece.SPEAR, 'b', "4");
        b.put(Piece.SPEAR, 'b', "3");
        b.put(Piece.SPEAR, 'b', "5");
        assertEquals(1, b.countMoveQueen("W"));
        b.put(Piece.SPEAR, 'j', "3");
        b.put(Piece.SPEAR, 'j', "5");
        b.put(Piece.SPEAR, 'i', "4");
        b.put(Piece.SPEAR, 'i', "3");
        Move m = Move.mv("j4-i5(i10)");
        b.makeMove(m);
        m = Move.mv("j7-i7(j7)");
        b.makeMove(m);
        m = Move.mv("i5-j4(h6)");
        b.makeMove(m);
        m = Move.mv("i7-i5(i7)");
        b.makeMove(m);
        assertEquals(0, b.countMoveQueen("W"));
        assertEquals(Piece.BLACK, b.winner());


    }

    @Test
    public void makeMoveAsEmptyTest() {
        Board b = new Board();
        Move white = Move.mv("a4-f4(a4)");
        b.makeMove(white);
        Move black = Move.mv("d10-d5(d2)");
        b.makeMove(black);
        String expect =
                   "   - - - - - - B - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   B - - - - - - - - B\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - B - - - - - -\n"
                +  "   S - - - - W - - - W\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - S - - - - - -\n"
                +  "   - - - W - - W - - -\n";
        assertEquals(expect, b.toString());
        white = Move.mv("f4-i4(b4)");
        b.makeMove(white);
        expect =
                   "   - - - - - - B - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   B - - - - - - - - B\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - B - - - - - -\n"
                +  "   S S - - - - - - W W\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - S - - - - - -\n"
                +  "   - - - W - - W - - -\n";
        assertEquals(expect, b.toString());
        black = Move.mv("d5-a2(i10)");
        b.makeMove(black);
        expect =
                   "   - - - - - - B - S -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   B - - - - - - - - B\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   S S - - - - - - W W\n"
                +  "   - - - - - - - - - -\n"
                +  "   B - - S - - - - - -\n"
                +  "   - - - W - - W - - -\n";
        assertEquals(expect, b.toString());
    }

}



