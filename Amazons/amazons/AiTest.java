package amazons;

import org.junit.Test;
import static org.junit.Assert.*;

public class AiTest {

    @Test
    public void staticScoreTest() {
        Board b = new Board();
        Move first = Move.mv("d1-d7(g7)");
        Move second = Move.mv("d10-c9(h4)");
        b.makeMove(first);
        b.makeMove(second);
        Square from = Square.sq("a", "1");
        Square to = Square.sq("c", "6");
        Square spear = Square.sq("j", "9");
        b.makeMove(from, to, spear);
        Move four = Move.mv("a4-c2(e4)");
        b.makeMove(four);
        Move five = Move.mv("j7-h7(i8)");
        b.makeMove(five);
        Move six = Move.mv("d7-e7(d7)");
        b.makeMove(six);
        String expect =
                           "   - - - - - - B - - -\n"
                        +  "   - - B - - - - - - -\n"
                        +  "   - - - - - - - - S -\n"
                        +  "   B - - S W - S B - -\n"
                        +  "   - - - - - - - - - -\n"
                        +  "   - - - - - - - - - -\n"
                        +  "   - - - - S - - S - W\n"
                        +  "   - - - - - - - - - -\n"
                        +  "   - - W - - - - - - -\n"
                        +  "   - - - - - - W - - -\n";
        assertEquals(expect, b.toString());
        AI a = new AI(Piece.WHITE, null);
        assertEquals(7, a.staticScore(b));
        six = Move.mv("a7-b7(b8)");
        b.makeMove(six);
        six = Move.mv("g1-a1(b2)");
        b.makeMove(six);
        expect =
                   "   - - - - - - B - - -\n"
                +  "   - - B - - - - - - -\n"
                +  "   - S - - - - - - S -\n"
                +  "   - B - S W - S B - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - - - - - - -\n"
                +  "   - - - - S - - S - W\n"
                +  "   - - - - - - - - - -\n"
                +  "   - S W - - - - - - -\n"
                +  "   W - - - - - - - - -\n";
        assertEquals(expect, b.toString());
        assertEquals(8, a.staticScore(b));
        AI c = new AI(Piece.BLACK, null);
        assertEquals(-8, c.staticScore(b));

    }

}
