import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final int MINVALUE = Integer.MIN_VALUE;
    private WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

    public String outcast(String[] nouns) {
        int mindist = MINVALUE;
        String result = "";
        int[] tempdist = new int[nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++) {
                tempdist[i] += wordnet.distance(nouns[i], nouns[j]);
            }
            if (tempdist[i] > mindist) {
                mindist = tempdist[i];
                result = nouns[i];
            }
        }
        return result;
    }
}
