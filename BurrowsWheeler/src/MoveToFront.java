import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {

    private static LinkedList<Character> initialize() {
        LinkedList<Character> list = new LinkedList<>();
        for(int i=0;i<256;i++) {
            list.add((char)i);
        }
        return list;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> list = initialize();
        while(!BinaryStdIn.isEmpty()){
            int index = BinaryStdIn.readChar(8);
            //System.out.println(index);
            list.remove(index);
            list.addFirst((char)index);
            BinaryStdOut.write((char)index);
        }
        BinaryStdOut.write(true);
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> list = initialize();
        while(!BinaryStdIn.isEmpty()){
            int index = BinaryStdIn.readChar(8);
            list.remove(index);
            list.addFirst((char)index);
        }
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }
    }
}
