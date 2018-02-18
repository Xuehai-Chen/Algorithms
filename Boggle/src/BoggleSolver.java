import edu.princeton.cs.algs4.*;

public class BoggleSolver {

    private final TST<Integer> dic;
    private SET<String> set;

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

    private void setGraph(Graph graph, BoggleBoard board) {
        int rows = board.rows();
        int cols = board.cols();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i != 0) {
                    graph.addEdge(i * cols + j, (i - 1) * cols + j);
                }
                if (j != 0) {
                    graph.addEdge(i * cols + j, i * cols + j - 1);
                }
                if (i != 0 && j != 0) {
                    graph.addEdge(i * cols + j, (i - 1) * cols + j - 1);
                }
                if (i != 0 && j != cols - 1) {
                    graph.addEdge(i * cols + j, (i - 1) * cols + j + 1);
                }
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.set = new SET<>();
        int cols = board.cols();
        int rows = board.rows();
        Graph graph = new Graph(rows * cols);
        setGraph(graph, board);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                boolean[] marked = new boolean[rows * cols];
                StringBuffer cur = new StringBuffer();
                dfs(graph, board, cols, cur, cols * i + j, marked);
            }
        }
        return this.set;
    }

    private void dfs(Graph graph, BoggleBoard board, int cols, StringBuffer cur, int v, boolean[] marked) {
        marked[v] = true;
        char charToAppend = board.getLetter(v / cols, v % cols);
        if (charToAppend == 'Q') {
            cur.append("QU");
        } else {
            cur.append(charToAppend);
        }
        if (cur.length() > 2 && !this.dic.keysWithPrefix(cur.toString()).iterator().hasNext()) {
            if (cur.charAt(cur.length() - 1) == 'U' && cur.charAt(cur.length() - 2) == 'Q') {
                cur.deleteCharAt(cur.length() - 1);
                cur.deleteCharAt(cur.length() - 1);
            } else {
                cur.deleteCharAt(cur.length() - 1);
            }
            marked[v] = false;
            return;
        }
        for (int w : graph.adj(v)) {
            if (!marked[w]) {
                dfs(graph, board, cols, cur, w, marked);
            }
        }
        if (this.dic.contains(cur.toString()) && cur.length() > 2) {
            set.add(cur.toString());
        }
        if (cur.length() > 1 && cur.charAt(cur.length() - 1) == 'U' && cur.charAt(cur.length() - 2) == 'Q') {
            cur.deleteCharAt(cur.length() - 1);
            cur.deleteCharAt(cur.length() - 1);
        } else {
            cur.deleteCharAt(cur.length() - 1);
        }
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
