import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.StdOut;

public class BinaryPrint {

    // Do not instantiate.
    private BinaryPrint() {
    }
    public static void main(String[] args) {
        int bytesPerLine = 16;
        if (args.length == 1) {
            bytesPerLine = Integer.parseInt(args[0]);
        }

        int i;
        if(BinaryStdIn.isEmpty()){
            System.out.println("the binary input is empty!");
        }
        for (i = 0; !BinaryStdIn.isEmpty(); i++) {
            System.out.println(i);
            if (bytesPerLine == 0) {
                BinaryStdIn.readChar();
                continue;
            }
            if (i == 0) StdOut.printf("");
            else if (i % bytesPerLine == 0) StdOut.printf("\n", i);
            else StdOut.print(" this is a binary input: ");
            char c = BinaryStdIn.readChar();
            StdOut.printf("%02x", c & 0xff);
        }
        if (bytesPerLine != 0) StdOut.println();
        StdOut.println((i * 8) + " bits");
    }
}

