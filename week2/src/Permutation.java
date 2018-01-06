/**
 * Created by MuMu on 7/1/2017.
 */
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.*;

public class Permutation {
    public static void main(String[] args){
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> input = new RandomizedQueue<String>();
        int i = 1;
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            if (i<=k)
                input.enqueue(str);
            else if (StdRandom.uniform(i)<k){
                String s = input.dequeue();
                input.enqueue(str);
            }
            i++;
        }
        for (String s:input)
            StdOut.println(s);
    }
}
