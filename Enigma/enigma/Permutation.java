package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Ying Lung Tang
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        this._cycles = cycles;

        _mapping = new String[alphabet.size()];
        for (int i = 0; i < alphabet.size(); i += 1) {
            _mapping[i] = "";
        }
        map();
    }

    /** mapping the cycle. **/
    private void map() {
        int index = 0;
        boolean check = false;
        for (int i = 0; i < _cycles.length(); i += 1) {
            char c = _cycles.charAt(i);
            if (check && _alphabet.contains(c)) {
                _mapping[index] += c;
            }
            if (i < _cycles.length() - 1) {
                if (c == '(') {
                    check = true;
                }
                if (c == ')' && !_alphabet.contains(_cycles.charAt(i + 1))) {
                    check = false;
                    index += 1;
                }
            }
        }
    }
    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    public void addCycle(String cycle) {
        this._cycles += cycle;
        map();
    }

    /** Return the cycle. **/
    public String getCycle() {
        return _cycles;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char first = _alphabet.toChar(p);
        char second = permute(first);
        int third = _alphabet.toInt(second);
        return third;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char first = _alphabet.toChar(c);
        int second = invert(first);
        char third = (char) second;
        int fourth = _alphabet.toInt(third);
        return fourth;
    }

    /** Return if the CHARACTER in the LINE. **/
    private boolean contain(String line, char character) {
        for (int j = 0; j < line.length(); j += 1) {
            if (line.charAt(j) == character) {
                return true;
            }
        }
        return false;
    }

    /** Return the C's position in the S. **/
    private int indexOf(String s, char c) {
        int result = -1;
        for (int i = 0; i < s.length(); i += 1) {
            if (s.charAt(i) == c) {
                result = i;
            }
        }
        return result;
    }


    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        char result = p;
        for (int i = 0; i < _alphabet.size(); i += 1) {
            if (contain(_mapping[i], p)) {
                int find = indexOf(_mapping[i], p);
                if (find == _mapping[i].length() - 1) {
                    result =  _mapping[i].charAt(0);
                } else {
                    result = _mapping[i].charAt(find + 1);
                }
            }
        }
        return result;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    int invert(char c) {
        char result = c;
        for (int i = 0; i < _mapping.length; i += 1) {
            if (contain(_mapping[i], c)) {
                int find = _mapping[i].indexOf(c);
                if (c == _mapping[i].charAt(0)) {
                    result = _mapping[i].charAt(_mapping[i].length() - 1);
                } else {
                    result = _mapping[i].charAt(find - 1);
                }
            }
        }
        return result;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int count = 0;
        for (int i = 0; i < _alphabet.size(); i += 1) {
            if (_mapping[i].length() == 1) {
                return false;
            }
            if (_mapping[i].equals("")) {
                count += 1;
            }
        }
        if (count == _alphabet.size()) {
            return false;
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Store the cycles that pass in the class. */
    private String _cycles;

    /** Store the cycles that separate to each cycles. */
    private String[] _mapping;

}
