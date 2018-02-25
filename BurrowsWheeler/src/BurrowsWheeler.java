import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.In;

public class BurrowsWheeler {
    private static CircularSuffixArray c;
    private In in;
    private static int[] source;

    private BurrowsWheeler(String s) {
        this.in = new In(s);
        String str = in.readString();
        source = new int[str.length()];
        for(int i=0;i<str.length();i++) {
            source[i] = str.charAt(i);
        }
        c = new CircularSuffixArray(str);
    }

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        int N = c.length();
        for (int i = 0; i < N; i++) {
            if (c.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        int[][] circularSuffixArray = new int[N][N];
        for (int i = 0; i < circularSuffixArray.length; i++) {
            for(int j = i;j<N+i;j++) {
                circularSuffixArray[i][j-i] = source[j % N];
            }
        }
        for (int i = 0; i < N; i++) {
            BinaryStdOut.write((char)(circularSuffixArray[c.index(i)][source.length] & 0xff));
        }
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {

    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        new BurrowsWheeler(args[1]);
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
