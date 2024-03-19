package enigma;

import java.util.Collection;

import static enigma.EnigmaException.*;
import java.util.Iterator;

/** Class that represents a complete enigma machine.
 *  @author Ying Lung Tang
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _myRoter = new Rotor[numRotors];
        _plugboard = null;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Return the Rotor by the INDEX. **/
    public Rotor getRotor(int index) {
        return _myRoter[index];
    }

    /** Return the references of allRotor. **/
    public Collection getAllRotor() {
        return _allRotors;
    }

    /** Return the alphabet. **/
    public Alphabet getAlphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (rotors.length > _numRotors) {
            throw error("Wrong number of rotors");
        }
        for (int i = 0; i < rotors.length; i += 1) {
            Iterator itr = _allRotors.iterator();
            while (itr.hasNext()) {
                Rotor checkRotor = (Rotor) itr.next();
                if (rotors[i].equals(checkRotor.name())) {
                    _myRoter[i] = checkRotor;
                    _myRoter[i].set(0);
                    break;
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != _numRotors - 1) {
            throw error("Wrong number of setting");
        }
        for (int i = 0; i < setting.length(); i += 1) {
            if (Character.isLowerCase(setting.charAt(i))) {
                throw error("Wrong number of setting");
            }
        }
        for (int i = 1; i < _numRotors; i += 1) {
            _myRoter[i].set(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Return the reference of plugboard. **/
    public Permutation getPlugboard() {
        return _plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        doubleStepping();
        _myRoter[_numRotors - 1].advance();
        int result = 0;
        if (_plugboard != null) {
            result = _plugboard.permute(c);
        }
        for (int i = _numRotors - 1; i >= 0; i -= 1) {
            result = _myRoter[i].convertForward(result);
        }
        for (int i = 1; i < _myRoter.length; i += 1) {
            result = _myRoter[i].convertBackward(result);
        }
        if (_plugboard != null) {
            result = _plugboard.permute(result);
        }
        return result;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        msg = msg.toUpperCase();
        for (int i = 0; i < msg.length(); i += 1) {
            if (_alphabet.contains(msg.charAt(i))) {
                int x = _alphabet.toInt(msg.charAt(i));
                int y = convert(x);
                char z = _alphabet.toChar(y);
                result += z;
            }
        }
        return result;
    }

    /** return the left most Rotor that need to advance. **/
    private int leftMostRotor() {
        int leftMostIndex = -1;
        for (int i = _numRotors - 2; i > 0; i -= 1) {
            if (_myRoter[i].rotates() && _myRoter[i + 1].rotates()) {
                String checkNotches = _myRoter[i + 1].getNotches();
                for (int j = 0; j < checkNotches.length(); j += 1) {
                    if (checkNotches.charAt(j)
                            == _alphabet.toChar(_myRoter[i + 1].setting())) {
                        leftMostIndex = i;
                    }
                }
            }
        }
        return leftMostIndex;
    }

    /** Advance all the Rotor that need to advance except the last Rotor. **/
    private void doubleStepping() {
        int leftMostIndex = leftMostRotor();
        if (leftMostIndex > 0) {
            _myRoter[leftMostIndex].advance();
            for (int i = leftMostIndex + 1; i < _numRotors - 1; i += 1) {
                String checkBackNotches = _myRoter[i].getNotches();
                for (int j = 0; j < checkBackNotches.length(); j += 1) {
                    if (checkBackNotches.charAt(j)
                           == _alphabet.toChar(_myRoter[i].setting())) {
                        _myRoter[i].advance();
                    }
                }
            }
        }
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of Rotors. **/
    private int _numRotors;

    /** Number of pawls. **/
    private int _pawls;

    /** All the Rotor that is able to use. **/
    private Collection<Rotor> _allRotors;

    /** The Rotor that use for machine. **/
    private Rotor[] _myRoter;

    /** The plugboard of machine. **/
    private Permutation _plugboard;
}
