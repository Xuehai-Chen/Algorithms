import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.util.HashSet;

public class BoggleSolver {

    private HashSet<String> set = new HashSet<>();
    private TST<Integer> dic;
    private Graph graph;
    private BoggleBoard board;
    private int cols;
    private int rows;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dic = new TST<>();
        for (int i = 0; i < dictionary.length; i++) {
            this.dic.put(dictionary[i], i);
        }

    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

    private void setGraph() {
        this.graph = new Graph(rows * cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i != 0) {
                    this.graph.addEdge(i * rows + j, (i - 1) * rows + j);
                }
                if (j != 0) {
                    this.graph.addEdge(i * rows + j, i * rows + j - 1);
                }
                if (i != 0 && j != 0) {
                    this.graph.addEdge(i * rows + j, (i - 1) * rows + j - 1);
                }
                if (i != 0 && j != 3) {
                    this.graph.addEdge(i * rows + j, (i - 1) * rows + j + 1);
                }
            }
        }
        //StdOut.println(this.graph);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.board = board;
        this.cols = board.cols();
        this.rows = board.rows();
        setGraph();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                boolean[] marked = new boolean[rows * cols];
                StringBuffer cur = new StringBuffer();
                dfs(cur, rows * i + j, marked);
            }
        }
        return this.set;
    }

    private void dfs(StringBuffer cur, int v, boolean[] marked) {
        marked[v] = true;
        cur.append(board.getLetter(v / rows, v % rows));
        if (cur.length() > 2 && !this.dic.keysThatMatch(cur.toString()).iterator().hasNext()) {
            //StdOut.println("this is not a prefix in the dict: " + cur.toString());
            cur.deleteCharAt(cur.length() - 1);
            marked[v] = false;
            return;
        }
        for (int w : this.graph.adj(v)) {
            if (!marked[w]) {
                dfs(cur, w, marked);
            }
        }
        if (this.dic.contains(cur.toString()) && cur.length() > 2) {
            set.add(cur.toString());
        }
        cur.deleteCharAt(cur.length() - 1);
        marked[v] = false;
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
