package enigma;

import java.util.LinkedList;

import static enigma.EnigmaException.*;

/** A class that accept the Alphabet in a list order.
 *  @author Ying Lung Tang
 */

class CharacterList extends Alphabet {

    /** An alphabet consisting of all characters in the L. */
    CharacterList(LinkedList<Character> L) {
        _range = new LinkedList<>();
        for (char c : L) {
            _range.add(Character.toUpperCase(c));
        }
    }

    @Override
    int size() {
        return _range.size();
    }

    @Override
    boolean contains(char ch) {
        return _range.contains(ch);
    }

    @Override
    char toChar(int index) {
        if (!contains(_range.get(index))) {
            throw error("character index out of range");
        }
        return _range.get(index);
    }

    @Override
    int toInt(char ch) {
        if (!contains(ch)) {
            throw error("character out of range");
        }
        return _range.indexOf(ch);
    }

    /** Range of characters in this Alphabet. */
    private LinkedList<Character> _range;

}
