package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Ying Lung Tang
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {


        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {

        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }

        _numRotor = -1;
        _numPawls = -1;
        _fromRange = ' ';
        _toRange = ' ';
        _addList = false;
        _range = new LinkedList<>();
        _rotors = new ArrayList<String>();
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return ALPHABET. **/
    public Alphabet getAlphabet() {
        return _alphabet;
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    public String process() {
        String result = "";
        Machine m = readConfig();
        int line = 1;
        while (_input.hasNextLine()) {
            String checkLine = _input.nextLine().toUpperCase();
            if (findFirstChar(checkLine) == -1) {
                if (_output != null) {
                    _output.println();
                } else {
                    System.out.println();
                }
                continue;
            }
            int firstChar = findFirstChar(checkLine);
            if (line == 1) {
                if (checkLine.charAt(0) != '*') {
                    throw error("Input line " + line + " wrong formal");
                }
                if (!isArguments(m, checkLine)) {
                    throw error("Input wrong arguments");
                }
            }
            if (checkLine.charAt(firstChar) == '*') {
                setMachine(m, checkLine);
                if (!m.getRotor(0).reflecting()) {
                    throw error("Input The first Rotor have to be reflector");
                }
            } else if (_alphabet.contains(checkLine.charAt(firstChar))) {
                String temp = m.convert(checkLine.toUpperCase());
                result += printMessageLine(temp);
                if (_output != null) {
                    _output.println(printMessageLine(temp));
                } else {
                    System.out.println(printMessageLine(temp));
                }
            }
            line += 1;
        }

        return result;
    }

    /** Return the first index non-space character of the LINE. **/
    private int findFirstChar(String line) {
        int index = -1;
        for (int i = 0; i < line.length(); i += 1) {
            if (line.charAt(i) != ' ') {
                index = i;
                break;
            }
        }
        return index;
    }

    /** Return the last index non-space character of the LINE. **/
    private int findLastChar(String line) {
        int index = -1;
        for (int i = line.length() - 1; i >= 0; i -= 1) {
            if (line.charAt(i) != ' ') {
                index = i;
                break;
            }
        }
        return index;
    }


    /** Return an Enigma machine configured from the contents of configuration
     *  file _CONFIG. */
    public Machine readConfig() {
        try {
            int line = 1;
            List<Rotor> allRotors = new LinkedList<>();
            while (_config.hasNextLine()) {
                String checkLine = _config.nextLine();
                String extraLine = "";
                int emptySpace = 0;
                for (int i = 0; i < checkLine.length(); i += 1) {
                    char checkChar = checkLine.charAt(i);
                    if (i <= 11 && checkChar == ' ') {
                        emptySpace += 1;
                    }
                    if (checkChar != ' ') {
                        defineAlphabet(line, i, checkChar, checkLine);
                    }
                    if (emptySpace == 11) {
                        extraLine += checkChar;
                    }
                }
                if (line >= 3) {
                    if (findFirstChar(checkLine) != -1) {
                        int lastIndex = findLastChar(checkLine);
                        char lastChar = checkLine.charAt(lastIndex);
                        if (lastChar != ')') {
                            throw error("config Line "
                                    + line + " wrong formal");
                        }
                    }
                    if (extraLine.equals("")) {
                        allRotors.add(readRotor(checkLine, line));
                    } else {
                        int checkSize = allRotors.size() - 1;
                        Rotor r = allRotors.get(checkSize);
                        r.permutation().addCycle(extraLine);
                    }
                }
                line += 1;
            }
            return new Machine(_alphabet, _numRotor, _numPawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Read _ALPHABET, check the line CHECKLINE and char CHECKCHAR
     * at index I to find number of rotor and number of pawls
     * in first 2 LINE of input. **/
    private void defineAlphabet(int line, int i,
                                char checkChar, String checkLine) {
        if (line == 1) {
            _range.add(checkChar);
            if (findFirstChar(checkLine) != 0) {
                throw error("config Line " + line + " wrong formal");
            }
            if (i == 0) {
                _fromRange = checkChar;
            }
            if (i == 1 && checkChar != '-') {
                _addList = true;
            }
            if (i == 2) {
                _toRange = checkChar;
            }
        }
        if (line == 2) {
            if (_addList) {
                _alphabet = new CharacterList(_range);
            }
            if (!_addList) {
                _alphabet = new CharacterRange(_fromRange, _toRange);
            }
            int firstCharIndex = findFirstChar(checkLine);
            char firstChar = checkLine.charAt(firstCharIndex);
            char secondChar = checkLine.charAt(firstCharIndex + 2);
            _numRotor = Character.getNumericValue(firstChar);
            _numPawls = Character.getNumericValue(secondChar);
        }
    }

    /** Return a rotor, reading its description from CHECKLINE by
     *  number of LINE. */
    private Rotor readRotor(String checkLine, int line) {
        try {
            String name = "";
            char type = ' ';
            String cycles = "";
            String notches = "";
            int wordCount = 0;
            boolean startAdd = false;
            for (int i = 0; i < checkLine.length(); i += 1) {
                char checkWord = checkLine.charAt(i);
                if (i == 0 && checkWord != ' ') {
                    wordCount += 1;
                }
                if (wordCount == 1 && checkWord != ' ') {
                    name += checkWord;
                }
                if (wordCount == 3 && checkWord != ' ') {
                    notches += checkWord;
                }
                if (wordCount == 2) {
                    if (checkWord != 'M' && checkWord != 'N'
                            && checkWord != 'R') {
                        throw error("config Line "
                                + line + " word " + wordCount
                                + " has wrong type " + checkWord);
                    }
                    type = checkWord;
                    wordCount += 1;
                }
                if (checkWord == '(') {
                    startAdd = true;
                }
                if (startAdd) {
                    cycles += checkWord;
                }
                if (checkWord == ' ') {
                    wordCount += 1;
                }
            }
            name = name.toUpperCase();
            _rotors.add(name);
            Permutation p = new Permutation(cycles, _alphabet);
            return createRotor(name, type, notches, p);
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Return the correct Type of Rotor which initialize by NAME, TYPE
     *  NOTCHES and P   . **/
    private Rotor createRotor(String name, char type,
                              String notches, Permutation p) {
        Rotor r = null;
        if (type == 'M') {
            r = new MovingRotor(name, p, notches);
        }
        if (type == 'N') {
            r = new FixedRotor(name, p);
        }
        if (type == 'R') {
            r = new Reflector(name, p);
        }
        return r;
    }

    /** Set the machine M by the LINE. **/
    public void setMachine(Machine m, String line) {
        String[] myRotor = new String[m.numRotors()];
        for (int i = 0; i < m.numRotors(); i += 1) {
            myRotor[i] = "";
        }
        String setting = "";
        String plugBoard = "";
        int countRotor = 0;
        for (int i = 2; i < line.length(); i += 1) {
            char checkChar = line.charAt(i);
            if (checkChar != ' ') {
                if (countRotor < m.numRotors()) {
                    myRotor[countRotor] += checkChar;
                } else if (countRotor == m.numRotors()) {
                    setting += checkChar;
                } else if (countRotor >= m.numRotors() + 1) {
                    plugBoard += checkChar;
                }
            }
            if (checkChar == ' ') {
                if (plugBoard.length() != 0) {
                    plugBoard += checkChar;
                }
                countRotor += 1;
            }
        }
        if (isRepeatRotor(myRotor)) {
            throw error("Input Rotor can't use twice");
        }

        if (isMisnamed(m, myRotor)) {
            throw error("Input misnamed Rotors ");
        }

        if (!isSetting(m, setting)) {
            throw error("Input wrong setting ");
        }
        setUp(m, setting, myRotor);
        if (!isMatchRotor(m)) {
            throw error("number of moving rotor not match ");
        }
        if (!plugBoard.equals(" ")) {
            Permutation p = new Permutation(plugBoard, _alphabet);
            m.setPlugboard(p);
        }

    }

    /** Set M according to the specification given on SETTINGS and
     *  MYROTOR which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings, String[] myRotor) {
        M.insertRotors(myRotor);
        M.setRotors(settings);
    }

    /** Return if the number of movingRotor match the config pawls
     *  in the machine M. **/
    private boolean isMatchRotor(Machine m) {
        int movingRotorCount = 0;
        for (int i = 0; i < m.numRotors(); i += 1) {
            if (m.getRotor(i).rotates()) {
                movingRotorCount += 1;
            }
        }
        if (movingRotorCount == m.numPawls()) {
            return true;
        }
        return false;
    }

    /** Return if ROTORS have repeat rotors. **/
    private boolean isRepeatRotor(String[] rotors) {
        for (int i = 0; i < rotors.length; i += 1) {
            String check = rotors[i];
            int count = 0;
            for (int j = 0; j < rotors.length; j += 1) {
                if (check.equals(rotors[j])) {
                    count += 1;
                }
            }
            if (count > 1) {
                return true;
            }
        }
        return false;
    }

    /** Return if ROTOR misnamed in the list of ROTORS in the
     *  machine M. **/
    private boolean isMisnamed(Machine m, String[] rotors) {
        if (rotors.length != m.numRotors()) {
            throw error("input have different number or rotor");
        }
        int count = 0;
        for (int i = 0; i < rotors.length; i += 1) {
            if (_rotors.contains(rotors[i])) {
                count += 1;
            }
        }
        if (count != m.numRotors()) {
            return true;
        }
        return false;
    }

    /** Return the SET is correct formal of M. **/
    private boolean isSetting(Machine m, String set) {
        if (set.length() != m.numRotors() - 1) {
            return false;
        }
        for (int i = 0; i < set.length(); i += 1) {
            char checkWord = set.charAt(i);
            if (!m.getAlphabet().contains(checkWord)) {

                return false;
            }
        }
        return true;
    }

    /** Return if all the character in LINE is in the M's alphabet. **/
    private boolean isArguments(Machine m, String line) {
        int wordCount = 0;
        int bucketCount = 0;
        for (int i = 1; i < line.length() - 1; i += 1) {
            char checkWord = line.charAt(i);
            if (checkWord == '('
                    && m.getAlphabet().contains(line.charAt(i + 1))) {
                bucketCount += 1;
            }
            if (checkWord == ' ') {
                wordCount += 1;
            }
        }
        wordCount -= bucketCount;
        if (wordCount != m.numRotors() + 1) {
            return false;
        }
        return true;
    }

    /** Return MSG in groups of five (except that the last group may
     *  have fewer letters). */
    public String printMessageLine(String msg) {
        int spaceCheck = 0;
        String result = "";
        for (int i = 0; i < msg.length(); i += 1) {
            if (msg.charAt(i) != ' ') {
                result += msg.charAt(i);
                spaceCheck += 1;
            }
            if (spaceCheck == 5 && i != msg.length() - 1) {
                result += " ";
                spaceCheck = 0;
            }
        }
        return result;
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Store all the rotor name. **/
    private List<String> _rotors;

    /** Save for number of Rotor. **/
    private int _numRotor;

    /** Save for number of Pawls. **/
    private int _numPawls;

    /** The starting point of _alphabet. **/
    private char _fromRange;

    /** The ending point of _alphabet. **/
    private char _toRange;

    /** Used to separate the range of list of alphabet. **/
    private boolean _addList;

    /** The list of alphabet. **/
    private LinkedList<Character> _range;
}
