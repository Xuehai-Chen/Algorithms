import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieST;

import java.util.HashSet;

public class BoggleSolver {

    private HashSet<String> set = new HashSet<>();
    private TrieST<Integer> dic;
    private Graph graph;
    private BoggleBoard board;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.setGraph();
        this.dic = new TrieST<>();
        for (int i = 0; i < dictionary.length; i++) {
            this.dic.put(dictionary[i], i);
        }

    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        StdOut.println(solver.graph.toString());
        BoggleBoard board = new BoggleBoard(args[1]);
        StdOut.println(board.toString());
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

    private void setGraph() {
        this.graph = new Graph(16);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i != 0) {
                    this.graph.addEdge(i * 4 + j, (i - 1) * 4 + j);
                }
                if (j != 0) {
                    this.graph.addEdge(i * 4 + j, i * 4 + j - 1);
                }
                if (i != 0 && j != 0) {
                    this.graph.addEdge(i * 4 + j, (i - 1) * 4 + j - 1);
                }
                if (i != 0 && j != 3) {
                    this.graph.addEdge(i * 4 + j, (i - 1) * 4 + j + 1);
                }
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.board = board;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.set.addAll(findWords(i, j));
            }
        }
        return this.set;
    }

    private HashSet<String> findWords(int i, int j) {
        HashSet<String> res = new HashSet<>();
        boolean[] marked = new boolean[16];
        StringBuffer cur = new StringBuffer();
        cur.append(board.getLetter(i, j));
        return dfs(res, cur, 0, marked);
    }

    private HashSet<String> dfs(HashSet<String> res, StringBuffer cur, int v, boolean[] marked) {
        marked[v] = true;
        for (int w : this.graph.adj(v)) {
            if (!marked[w]) {
                cur.append(board.getLetter(w / 4, w % 4));
                if (this.dic.keysWithPrefix(cur.toString()) == null) {
                    return null;
                } else {
                    StdOut.println(cur.toString());
                    dfs(res, cur, w, marked);
                    if (this.dic.contains(cur.toString()) && cur.length() > 2) {
                        res.add(cur.toString());
                    }
                }
            }
        }
        return res;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!this.dic.contains(word)) return 0;
        int res;
        int length = word.length();
        switch (length) {
            case 0:
            case 1:
            case 2:
                res = 0;
                break;
            case 3:
            case 4:
                res = 1;
                break;
            case 5:
                res = 2;
                break;
            case 6:
                res = 3;
                break;
            case 7:
                res = 5;
                break;
            default:
                res = 11;
        }
        return res;
    }
}
