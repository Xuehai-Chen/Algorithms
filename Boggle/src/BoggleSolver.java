import java.lang.Math;
import java.util.ArrayList;

public class BoggleSolver {
    private void test() {
        double res = Math.pow(256,7);
        System.out.println(res);
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary){}

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){return new ArrayList<String>();}

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){return 0;}

    public static void main(String argv[]) {
        BoggleSolver s = new BoggleSolver(new String[2]);
        s.test();
    }
}
