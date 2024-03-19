package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Ying Lung Tang
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _set = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return this._set;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        this._set = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        this._set = _permutation.alphabet().toInt(cposn);
    }

    /** Return the notches at NOTCHES. */
    public String getNotches() {
        return "";
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int in = _permutation.wrap(p + _set);
        int mid = _permutation.wrap(_permutation.permute(in));
        int out = _permutation.wrap(mid - _set);
        return out;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int in = _permutation.wrap(e + _set);
        int mid = _permutation.wrap(_permutation.invert(in));
        int out = _permutation.wrap(mid - _set);
        return out;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** Store the offset in setting. */
    private int _set;

}
