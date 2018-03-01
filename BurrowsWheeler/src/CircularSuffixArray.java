import edu.princeton.cs.algs4.In;

public class CircularSuffixArray {
    private int[] index;
    private int N;
    private String s;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        this.s = s;
        this.N = s.length();
        index = new int[this.N];
        setSortedArray(this.N);
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        CircularSuffixArray c = new CircularSuffixArray(in.readString());
        System.out.println("");
        for (int i = 0; i < c.index.length; i++) {
            System.out.println(c.index[i]);
        }
        System.out.println(c.length());

    }

    private int[][] setCircularSuffixArray(String s) {
        int[] source = new int[N];
        for (int i = 0; i < N; i++) {
            source[i] = s.charAt(i);
        }
        int[][] circularSuffixArray = new int[N][N];
        for (int i = 0; i < circularSuffixArray.length; i++) {
            for (int j = i; j < N + i; j++) {
                circularSuffixArray[i][j - i] = source[j % N];
            }
        }
        return circularSuffixArray;
    }

    private char getChar(int i, int j) {
        return s.charAt((i + j) % N);
    }

    private void setSortedArray(int N) {
        int R = 256;
        int[] temp = new int[N];

        for (int d = N - 1; d >= 0; d--) { // Sort by key-indexed counting on dth char.
            int[] count = new int[R + 1]; // Compute frequency counts.
            for (int i = 0; i < N; i++)
                count[getChar(d, i) + 1]++;
            for (int r = 0; r < R; r++) // Transform counts to indices.
                count[r + 1] += count[r];
            for (int i = 0; i < N; i++) {
                if (d == N - 1) {
                    temp[count[getChar(d, i)]++] = i;
                }else{
                    temp[count[getChar(d, index[i])]++] = index[i];
                }
            }
            for (int i = 0; i < N; i++) {
                this.index[i] = temp[i];
            }
        }
    }

    // length of s
    public int length() {
        return this.N;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= this.length()) throw new IllegalArgumentException();
        return index[i];
    }

}
