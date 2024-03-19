package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Ying Lung Tang
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
    }

    /** It is reflector. */
    @Override
    boolean reflecting() {
        return true;
    }

    /** The Limitation of reflector. */
    @Override
    int convertBackward(int e) {
        throw error("Reflector is not allow to convertBackward");
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }

    @Override
    public String toString() {
        return "Reflector " + super.name();
    }


}
