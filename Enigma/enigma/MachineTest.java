package enigma;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;

import static enigma.TestUtils.UPPER;
import static org.junit.Assert.assertEquals;

/** Tests all the feature of the Machine class.
 *  @author Ying Lung Tang
 */
public class MachineTest {

    /** Initial the setting of sample input. **/
    private List<Rotor> setting() {
        List<Rotor> L = new LinkedList<>();

        Permutation p = new Permutation("(AELTPHQXRU) (BKNW) "
                + "(CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        MovingRotor r = new MovingRotor("I", p, "Q");
        L.add(r);
        Permutation p2 = new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) "
               + "(GR) (NT) (A) (Q)", UPPER);
        MovingRotor r2 = new MovingRotor("II", p2, "E");
        L.add(r2);
        Permutation p3 = new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG) "
                +  "(N)", UPPER);
        MovingRotor r3 = new MovingRotor("III", p3, "V");
        L.add(r3);
        Permutation p4 = new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) (DV) "
                + "(KU)", UPPER);
        MovingRotor r4 = new MovingRotor("IV", p4, "J");
        L.add(r4);

        Permutation p5 = new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) "
                + "(HIX)", UPPER);
        FixedRotor r5 = new FixedRotor("BETA", p5);
        L.add(r5);
        Permutation p6 = new Permutation("(AE) (BN) (CK) (DQ) (FU) (GY) "
               + "(HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", UPPER);
        Reflector r6 = new Reflector("B", p6);
        L.add(r6);
        Permutation p7 = new Permutation("(AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)", UPPER);
        MovingRotor r7 = new MovingRotor("V", p7, "Z");
        L.add(r7);

        return L;
    }


    @Test
    public void insertRotorsTest() {
        Machine m = new Machine(UPPER, 5, 3, setting());
        m.insertRotors(new String[] {"B", "BETA", "III", "IV", "I"});
        assertEquals("Reflector B", m.getRotor(0).toString());
        assertEquals("FixedRotor BETA", m.getRotor(1).toString());
        assertEquals("MovingRotor III", m.getRotor(2).toString());
        assertEquals("MovingRotor IV", m.getRotor(3).toString());
        assertEquals("MovingRotor I", m.getRotor(4).toString());
    }

    @Test
    public void setRotorsTest() {
        Machine m = new Machine(UPPER, 5, 3, setting());
        m.insertRotors(new String[] {"B", "BETA", "III", "IV", "I"});
        m.setRotors("AXLE");
        assertEquals(0, m.getRotor(0).setting());
        assertEquals('A',
                m.getRotor(1).alphabet().toChar(m.getRotor(1).setting()));
        assertEquals('X',
                m.getRotor(2).alphabet().toChar(m.getRotor(2).setting()));
        assertEquals('L',
                m.getRotor(3).alphabet().toChar(m.getRotor(3).setting()));
        assertEquals('E',
                m.getRotor(4).alphabet().toChar(m.getRotor(4).setting()));
    }

    /** Return the setting in M at position Index. **/
    private char getCharOfMachine(Machine m, int index) {
        return m.getRotor(index).alphabet().toChar(m.getRotor(index).setting());
    }

    @Test
    public void convertIntegerTest() {
        Machine m = new Machine(UPPER, 5, 3, setting());
        m.insertRotors(new String[]{"B", "BETA", "III", "IV", "I"});
        m.setRotors("AXLE");
        Permutation plugboard = new Permutation("(YF) (ZH)", UPPER);
        m.setPlugboard(plugboard);
        for (int i = 0; i < 12; i += 1) {
            m.convert(24);
        }
        assertEquals(0, m.getRotor(0).setting());
        assertEquals('A', getCharOfMachine(m, 1));
        assertEquals('X', getCharOfMachine(m, 2));
        assertEquals('L', getCharOfMachine(m, 3));
        assertEquals('Q', getCharOfMachine(m, 4));

        m.convert(24);
        assertEquals(0, m.getRotor(0).setting());
        assertEquals('A', getCharOfMachine(m, 1));
        assertEquals('X', getCharOfMachine(m, 2));
        assertEquals('M', getCharOfMachine(m, 3));
        assertEquals('R', getCharOfMachine(m, 4));

        for (int i = 0; i < 597; i += 1) {
            m.convert(24);
        }
        assertEquals(0, m.getRotor(0).setting());
        assertEquals('A', getCharOfMachine(m, 1));
        assertEquals('X', getCharOfMachine(m, 2));
        assertEquals('I', getCharOfMachine(m, 3));
        assertEquals('Q', getCharOfMachine(m, 4));

        m.convert(24);
        assertEquals(0, m.getRotor(0).setting());
        assertEquals('A', getCharOfMachine(m, 1));
        assertEquals('X', getCharOfMachine(m, 2));
        assertEquals('J', getCharOfMachine(m, 3));
        assertEquals('R', getCharOfMachine(m, 4));

        m.convert(24);
        assertEquals(0, m.getRotor(0).setting());
        assertEquals('A', getCharOfMachine(m, 1));
        assertEquals('Y', getCharOfMachine(m, 2));
        assertEquals('K', getCharOfMachine(m, 3));
        assertEquals('S', getCharOfMachine(m, 4));
    }

    @Test
    public void convertEncodeStringTest() {
        Machine m = new Machine(UPPER, 5, 3, setting());
        m.insertRotors(new String[]{"B", "BETA", "III", "IV", "I"});
        m.setRotors("AXLE");
        Permutation plugboard = new Permutation("(HQ) (EX) (IP) (TR) "
                + "(BY)", UPPER);
        m.setPlugboard(plugboard);

        String msg = "FROM his shoulder Hiawatha";
        String result = m.convert(msg);
        assertEquals("QVPQSOKOILPUBKJZPISFXDW", result);
        msg = "Took the camera of rosewood";
        result = m.convert(msg);
        assertEquals("BHCNSCXNUOAATZXSRCFYDGU", result);
        msg = "Made of sliding folding rosewood";
        result = m.convert(msg);
        assertEquals("FLPNXGXIXTYJUJRCAUGEUNCFMKUF", result);
    }

    @Test
    public void convertDecodeStringTest() {
        Machine m = new Machine(UPPER, 5, 3, setting());
        m.insertRotors(new String[]{"B", "BETA", "III", "IV", "I"});
        m.setRotors("AXLE");
        Permutation plugboard = new Permutation("(HQ) (EX) (IP) (TR) "
                + "(BY)", UPPER);
        m.setPlugboard(plugboard);
        String msg = "QVPQS OKOIL PUBKJ ZPISF XDW";
        String result = m.convert(msg);
        assertEquals("FROMHISSHOULDERHIAWATHA", result);
        msg = "BHCNS CXNUO AATZX SRCFY DGU";
        result = m.convert(msg);
        assertEquals("TOOKTHECAMERAOFROSEWOOD", result);
        msg = "FLPNX GXIXT YJUJR CAUGE UNCFM KUF";
        result = m.convert(msg);
        assertEquals("MADEOFSLIDINGFOLDINGROSEWOOD", result);
    }

    @Test
    public void testDoubleStep() {
        Alphabet ac = new CharacterRange('A', 'D');
        Rotor one = new Reflector("R1", new Permutation("(AC) "
                + "(BD)", ac));
        Rotor two = new MovingRotor("R2",
                new Permutation("(ABCD)", ac), "C");
        Rotor three = new MovingRotor("R3",
                new Permutation("(ABCD)", ac), "C");
        Rotor four = new MovingRotor("R4",
                new Permutation("(ABCD)", ac), "C");
        String setting = "AAA";
        Rotor[] machineRotors = {one, two, three, four};
        String[] rotors = {"R1", "R2", "R3", "R4"};
        Machine mach = new Machine(ac, 4, 3,
                new ArrayList<>(Arrays.asList(machineRotors)));
        mach.insertRotors(rotors);
        mach.setRotors(setting);

        assertEquals("AAAA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AAAB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AAAC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABD", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AACD", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABDA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABDB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABDC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABAD", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABAA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABAB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABAC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABBD", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABBA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABBB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABBC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABCD", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ACDA", getSetting(ac, machineRotors));
    }

    /** Helper method to get the String representation
     * of the current Rotor settings */
    private String getSetting(Alphabet alph, Rotor[] machineRotors) {
        String currSetting = "";
        for (Rotor r : machineRotors) {
            currSetting += alph.toChar(r.setting());
        }
        return currSetting;
    }

    @Test
    public void testExtraAlphabet() {
        LinkedList<Character> L = new LinkedList<>();
        L.add('8');
        L.add('*');
        L.add('(');
        L.add(')');
        Alphabet ac = new CharacterList(L);
        Rotor one = new Reflector("<",
                new Permutation("(8*) (())", ac));
        Rotor two = new MovingRotor("=",
                new Permutation("(8*())", ac), "8");
        Rotor three = new MovingRotor("%",
                new Permutation("(8*())", ac), "8");
        Rotor four = new MovingRotor("^",
                new Permutation("(8*())", ac), "8");
        String setting = "***";
        Rotor[] machineRotors = {one, two, three, four};
        String[] rotors = {"<", "=", "%", "^"};
        Machine mach = new Machine(ac, 4, 3,
                new ArrayList<>(Arrays.asList(machineRotors)));
        mach.insertRotors(rotors);
        mach.setRotors(setting);

        assertEquals("8***", getSetting(ac, machineRotors));
        assertEquals("(8*) (())",
                mach.getRotor(0).permutation().getCycle());
        assertEquals("(8*())",
                mach.getRotor(1).permutation().getCycle());
        assertEquals("(8*())",
                mach.getRotor(2).permutation().getCycle());
        assertEquals("(8*())",
                mach.getRotor(3).permutation().getCycle());
        mach.convert('8');
        assertEquals("8**(", getSetting(ac, machineRotors));
        mach.convert('8');
        assertEquals("8**)", getSetting(ac, machineRotors));
        mach.convert('8');
        assertEquals("8**8", getSetting(ac, machineRotors));
        mach.convert('8');
        assertEquals("8*(*", getSetting(ac, machineRotors));

    }

}
