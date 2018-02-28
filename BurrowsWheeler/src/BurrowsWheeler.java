import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;

public class BurrowsWheeler {
    private static CircularSuffixArray c;
    private static StringBuffer sb = new StringBuffer();
    private static ArrayList<Integer> source = new ArrayList<>();

    private static void initialize() {
        while (!BinaryStdIn.isEmpty()) {
            char newchar = BinaryStdIn.readChar(8);
            source.add((int) newchar);
            sb.append(newchar);
        }
        c = new CircularSuffixArray(sb.toString());
    }

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        initialize();
        int N = c.length();
        for (int i = 0; i < N; i++) {
            if (c.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        int[][] circularSuffixArray = new int[N][N];
        for (int i = 0; i < circularSuffixArray.length; i++) {
            for (int j = i; j < N + i; j++) {
                circularSuffixArray[i][j - i] = source.get(j % N);
            }
        }
        for (int i = 0; i < N; i++) {
            BinaryStdOut.write((char) (circularSuffixArray[c.index(i)][source.size() - 1] & 0xff));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        initialize();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
