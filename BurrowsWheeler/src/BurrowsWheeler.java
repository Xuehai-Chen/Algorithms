import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;


public class BurrowsWheeler {
    private static String s;
    private static CircularSuffixArray c;

    private static void initialize() {
        StringBuilder sb = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            char newchar = BinaryStdIn.readChar(8);
            sb.append(newchar);
        }
        s = sb.toString();
        c = new CircularSuffixArray(s);
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
        for (int i = 0; i < N; i++) {
            BinaryStdOut.write((char) (s.charAt((c.index(i) + N - 1) % N) & 0xff));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {

        ArrayList<Character> list = new ArrayList<>();
        int first;
        int next[];
        int R = 256;

        if (BinaryStdIn.isEmpty()) throw new IllegalArgumentException();
        first = BinaryStdIn.readInt();

        int n = 0;
        while (!BinaryStdIn.isEmpty()) {
            char newchar = BinaryStdIn.readChar(8);
            list.add(newchar);
            n++;
        }

        next = new int[n];
        int[] count = new int[R + 1];
        for (int i = 0; i < n; i++) {
            count[list.get(i) + 1]++;
        }
        for (int i = 0; i < R; i++) {
            count[i + 1] += count[i];
        }
        for (int i = 0; i < n; i++) {
            next[count[list.get(i)]++] = i;
        }
        int offset = first;
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(list.get(next[offset]));
            offset = next[offset];
        }

        BinaryStdOut.close();
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
