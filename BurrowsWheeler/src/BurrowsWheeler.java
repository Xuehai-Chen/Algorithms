import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;


public class BurrowsWheeler {
    private static String s;
    private static CircularSuffixArray initialize() {
        StringBuilder sb = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            char newchar = BinaryStdIn.readChar(8);
            sb.append(newchar);
        }
        s = sb.toString();
        return new CircularSuffixArray(s);
    }

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        CircularSuffixArray c = initialize();
        int N = c.length();
        for (int i = 0; i < N; i++) {
            if (c.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < N; i++) {
            BinaryStdOut.write((char) (s.charAt((c.index(i) + N - 1) % N) & 0xff));
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
