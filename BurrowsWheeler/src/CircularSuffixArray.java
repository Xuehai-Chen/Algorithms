import edu.princeton.cs.algs4.In;

public class CircularSuffixArray {
    private int[] index;
    private int N;
    private String[] circularSuffixArray;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        this.N = s.length();
        index = new int[this.N];
        circularSuffixArray = new String[this.N];
        setCircularSuffixArray(s);
        setSortedArray(circularSuffixArray, this.N);
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        CircularSuffixArray c = new CircularSuffixArray(in.readString());
        System.out.println("");

        for (int i = 0; i < c.index.length; i++) {
            System.out.println(c.index[i]);
        }
        //System.out.println(c.length());

    }

    private void setCircularSuffixArray(String s) {
        String temp = new String(s);
        for (int i = 0; i < circularSuffixArray.length; i++) {
            circularSuffixArray[i] = temp;
            temp = temp.substring(1) + temp.substring(0, 1);
            System.out.println(circularSuffixArray[i]);
        }
    }

    private void setSortedArray(String[] a, int W) {
        int N = a.length;
        int R = 256;
        String[] sortedArray = new String[N];
        int[] temp = new int[this.N];
        for (int d = W - 1; d >= 0; d--) { // Sort by key-indexed counting on dth char.
            int[] count = new int[R + 1]; // Compute frequency counts.
            for (int i = 0; i < N; i++)
                count[a[i].charAt(d) + 1]++;
            for (int r = 0; r < R; r++) // Transform counts to indices.
                count[r + 1] += count[r];
            for (int i = 0; i < N; i++) {
                sortedArray[count[a[i].charAt(d)]++] = a[i];
                if (d == W - 1) {
                    index[i] = i;
                }
                temp[count[a[i].charAt(d)] - 1] = index[i];
            }
            for (int i = 0; i < N; i++) {
                a[i] = sortedArray[i];
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
