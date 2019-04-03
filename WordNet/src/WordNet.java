import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private HashMap<String, ArrayList<Integer>> synset;
    private HashMap<Integer, String> synsetindex;
    private Digraph digraph;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        synset = new HashMap<>();
        synsetindex = new HashMap<>();
        readinSynsets(synsets);
        readinHypernyms(hypernyms);
        checkHypernymsValid();
        sap = new SAP(this.digraph);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet net = new WordNet(args[0], args[1]);
        System.out.println(net.isNoun("1820s"));
        System.out.println(net.isNoun("1821s"));
        while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            int dist = net.distance(v, w);
            String ancestor = net.sap(v, w);
            StdOut.printf("length = %d, ancestor = %s\n", dist, ancestor);
        }
    }

    private void readinSynsets(String synsets) {
        In sset = new In(synsets);
        int count = 0;
        while (sset.hasNextLine()) {
            String temp;
            String[] tempsplit;
            temp = sset.readLine();
            tempsplit = temp.split("\\,");
            String[] tempblank = tempsplit[1].split("\\s");
            for (int i = 0; i < tempblank.length; i++) {
                if (synset.containsKey(tempblank[i])) synset.get(tempblank[i]).add(Integer.parseInt(tempsplit[0]));
                else {
                    ArrayList<Integer> indextoadd = new ArrayList<>();
                    indextoadd.add(Integer.parseInt(tempsplit[0]));
                    synset.put(tempblank[i], indextoadd);
                }
            }
            synsetindex.put(count, tempsplit[1]);
            count++;
        }
    }

    private void readinHypernyms(String hypernyms) {
        this.digraph = new Digraph(this.synsetindex.size());
        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] tempsplit;
            tempsplit = in.readLine().split("\\,");
            for (int i = 1; i < tempsplit.length; i++) {
                this.digraph.addEdge(Integer.parseInt(tempsplit[0]), Integer.parseInt(tempsplit[i]));
            }
        }
    }

    private void checkHypernymsValid() {
        DirectedCycle cycle = new DirectedCycle(this.digraph);
        if (cycle.hasCycle()) throw new IllegalArgumentException();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synset.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return synset.keySet().contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!this.isNoun(nounA) && !this.isNoun(nounB)) throw new IllegalArgumentException();
        ArrayList<Integer> arrayA = synset.get(nounA);
        ArrayList<Integer> arrayB = synset.get(nounB);
        return sap.length(arrayA, arrayB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!this.isNoun(nounA) && !this.isNoun(nounB)) throw new IllegalArgumentException();
        ArrayList<Integer> arrayA = synset.get(nounA);
        ArrayList<Integer> arrayB = synset.get(nounB);
        int index = sap.ancestor(arrayA, arrayB);
        return synsetindex.get(index);
    }
}
