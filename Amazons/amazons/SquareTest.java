package amazons;

import org.junit.Test;
import static org.junit.Assert.*;

public class SquareTest {
    @Test
    public void constructorTest() {
        Square s = Square.sq(0);
        assertEquals("a1", s.str());
        s = Square.sq(99);
        assertEquals("j10", s.str());
        s = Square.sq(10);
        assertEquals("a2", s.str());
        s = Square.sq(44);
        assertEquals("e5", s.str());
        s = Square.sq(1);
        assertEquals("b1", s.str());
    }
    @Test
    public void isQueenMoveTest() {
        Square from = Square.sq(88);
        Square to = Square.sq(8);
        assertTrue(from.isQueenMove(to));
        to = Square.sq(80);
        assertTrue(from.isQueenMove(to));
        from = Square.sq("b", "5");
        to = Square.sq("a", "5");
        assertTrue(from.isQueenMove(to));
        to = Square.sq("a", "4");
        assertTrue(from.isQueenMove(to));
        to = Square.sq("a", "6");
        assertTrue(from.isQueenMove(to));
        to = Square.sq("b", "6");
        assertTrue(from.isQueenMove(to));
        to = Square.sq("b", "8");
        assertTrue(from.isQueenMove(to));
        to = Square.sq("b", "1");
        assertTrue(from.isQueenMove(to));
        to = Square.sq("d", "7");
        assertTrue(from.isQueenMove(to));
        to = Square.sq("j", "1");
        assertFalse(from.isQueenMove(to));
        to = Square.sq("c", "3");
        assertFalse(from.isQueenMove(to));

    }

    @Test
    public void queenMoveTest() {
        Square s = Square.sq(0);
        assertEquals("a1", s.queenMove(0, 0).str());
        assertEquals("a1", s.queenMove(2, 0).str());
        assertEquals("a1", s.queenMove(7, 0).str());
        assertEquals("a2", s.queenMove(0, 1).str());
        assertEquals("a10", s.queenMove(0, 9).str());
        assertEquals("b2", s.queenMove(1, 1).str());
        assertEquals("b1", s.queenMove(2, 1).str());
        assertEquals("c3", s.queenMove(1, 2).str());

        assertNull(s.queenMove(3, 1));
        assertNull(s.queenMove(4, 1));
        assertNull(s.queenMove(5, 1));
        assertNull(s.queenMove(6, 1));
        assertNull(s.queenMove(7, 1));

        assertEquals("a3", s.queenMove(0, 2).str());
        assertNull(s.queenMove(0, 10));
        assertEquals("f6", s.queenMove(1, 5).str());
        assertNull(s.queenMove(1, 10));
        assertEquals("h1", s.queenMove(2, 7).str());
        assertNull(s.queenMove(2, 10));
    }

    @Test
    public void directionTest() {
        Square from = Square.sq("e5");
        Square to = Square.sq("e8");
        assertEquals(0, from.direction(to));
        to = Square.sq("j10");
        assertEquals(1, from.direction(to));
        to = Square.sq("g5");
        assertEquals(2, from.direction(to));
        to = Square.sq("h2");
        assertEquals(3, from.direction(to));
        to = Square.sq("e1");
        assertEquals(4, from.direction(to));
        to = Square.sq("d4");
        assertEquals(5, from.direction(to));
        to = Square.sq("c5");
        assertEquals(6, from.direction(to));
        to = Square.sq("a9");
        assertEquals(7, from.direction(to));
    }

    @Test
    public void sqTest() {
        assertEquals("a10", Square.sq(90).str());
        assertEquals("a1", Square.sq(0).str());
        assertEquals("j10", Square.sq(99).str());
        assertEquals("j1", Square.sq(9).str());
        assertEquals("c5", Square.sq(42).str());

        assertEquals("a10", Square.sq("a", "10").str());
        assertEquals("a1", Square.sq("a", "1").str());
        assertEquals("c5", Square.sq("c", "5").str());

        assertEquals("a1", Square.sq("a1").str());
        assertEquals("a10", Square.sq("a10").str());
        assertEquals("j8", Square.sq("j8").str());
    }
}

