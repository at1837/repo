package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Ying Lung Tang
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        this._notches = notches;

    }

    /** It is moving rotates. */
    @Override
    boolean rotates() {
        return true;
    }

    /** Moving Rotor have different advance. */
    @Override
    void advance() {
        this.set(permutation().wrap(setting() + 1));
    }

    /** Return the notches at NOTCHES. */
    @Override
    public String getNotches() {
        return _notches;
    }

    @Override
    public String toString() {
        return "MovingRotor " + super.name();
    }

    /** Store the notches. */
    private String _notches;
}
