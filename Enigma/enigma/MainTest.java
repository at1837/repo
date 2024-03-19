package enigma;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** This is the test for the scanner in main and all the feature of machine.
 *  @author Ying Lung Tang
 */

public class MainTest {

    String configPath = "testing/correct/default.conf";
    String inputPath = "testing/correct/trivial2.inp";
    String outputPath = "testing/correct/trivial2.out";

    @Test
    public void readConfigTest() {
        String[] s = new String[] {configPath};
        Main n = new Main(s);
        Machine m = n.readConfig();
        assertEquals(12, m.getAllRotor().size());
        assertEquals(5, m.numRotors());
        assertEquals(3, m.numPawls());
        assertEquals(n.getAlphabet().size(), m.getAlphabet().size());
    }

    @Test
    public void setMachineTest() {
        String[] s = new String[] {configPath, inputPath};
        Main n = new Main(s);
        Machine m = n.readConfig();
        n.setMachine(m, "* B BETA III IV I AXLE (HQ) (EX) (IP) (TR) (BY)");
        assertEquals("Reflector B", m.getRotor(0).toString());
        assertEquals("FixedRotor BETA", m.getRotor(1).toString());
        assertEquals("MovingRotor III", m.getRotor(2).toString());
        assertEquals("MovingRotor IV", m.getRotor(3).toString());
        assertEquals("MovingRotor I", m.getRotor(4).toString());
        assertEquals('A',
                m.getRotor(1).alphabet().toChar(m.getRotor(1).setting()));
        assertEquals('X',
                m.getRotor(2).alphabet().toChar(m.getRotor(2).setting()));
        assertEquals('L',
                m.getRotor(3).alphabet().toChar(m.getRotor(3).setting()));
        assertEquals('E',
                m.getRotor(4).alphabet().toChar(m.getRotor(4).setting()));
        assertEquals("(HQ) (EX) (IP) (TR) (BY)", m.getPlugboard().getCycle());
        assertTrue(m.getPlugboard().derangement());
    }

    @Test
    public void printMessageLineTest() {
        String[] s = new String[] {configPath, inputPath};
        Main n = new Main(s);
        String input = "QVPQSOKOILPUBKJZPISFXDW";
        input = n.printMessageLine(input);
        String expect = "QVPQS OKOIL PUBKJ ZPISF XDW";
        assertEquals(expect, input);
        input = "QVP QSOKOIL PUBKJ ZPISFXD W";
        expect = "QVPQS OKOIL PUBKJ ZPISF XDW";
        input = n.printMessageLine(input);
        assertEquals(expect, input);
        input = " ";
        expect = " ";
        assertEquals(expect, input);
    }
    @Test
    public void processTest() {
        String[] s = new String[] {configPath, inputPath, outputPath};
        Main n = new Main(s);
        String expect = ("QVPQS OKOIL PUBKJ ZPISF XDW"
                + "BHCNS CXNUO AATZX SRCFY DGU"
                + "FLPNX GXIXT YJUJR CAUGE UNCFM KUF"
                + "WJFGK CIIRG XODJG VCGPQ OH"
                + "ALWEB UHTZM OXIIV XUEFP RPR"
                + "KCGVP FPYKI KITLB URVGT SFU"
                + "SMBNK FRIIM PDOFJ VTTUG RZM"
                + "UVCYL FDZPG IBXRE WXUEB ZQJO"
                + "YMHIP GRRE"
                + "GOHET UXDTW LCMMW AVNVJ VH"
                + "OUFAN TQACK"
                + "KTOZZ RDABQ NNVPO IEFQA FS"
                + "VVICV UDUER EYNPF FMNBJ VGQ"
                + "");
        String input = "";
        input += n.process();
        assertEquals(expect, input);
    }
}
