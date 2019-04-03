import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;

import java.lang.IllegalArgumentException;

public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph G;
    private BreadthFirstDirectedPaths vbfdp;
    private int length;
    private int ancestor;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= G.V())
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (G.V() - 1));
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        for (int v : vertices) {
            if (v < 0 || v >= G.V()) {
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (G.V() - 1));
            }
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        this.length = -1;
        this.ancestor = -1;
        vbfdp = new BreadthFirstDirectedPaths(G, v);
        Queue<Integer> q = new Queue<>();
        boolean[] marked = new boolean[G.V()];
        int[] distTo = new int[G.V()];
        int[] edgeTo = new int[G.V()];
        for (int i = 0; i < G.V(); i++)
            distTo[i] = INFINITY;
        marked[w] = true;
        distTo[w] = 0;
        if (vbfdp.hasPathTo(w)) {
            this.ancestor = w;
            this.length = vbfdp.distTo(w);
        }
        q.enqueue(w);
        while (!q.isEmpty()) {
            int s = q.dequeue();
            for (int j : G.adj(s)) {
                if (!marked[j]) {
                    edgeTo[j] = s;
                    distTo[j] = distTo[s] + 1;
                    marked[j] = true;
                    q.enqueue(j);
                    if (vbfdp.hasPathTo(j)) {
                        int dis = vbfdp.distTo(j) + distTo[j];
                        if (dis < this.length || this.length == -1) {
                            this.length = dis;
                            this.ancestor = j;
                        }
                    }
                }
            }
        }
        return this.length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        this.length(v, w);
        return this.ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        this.length = -1;
        this.ancestor = -1;
        vbfdp = new BreadthFirstDirectedPaths(G, v);
        Queue<Integer> q = new Queue<>();
        boolean[] marked = new boolean[G.V()];
        int[] distTo = new int[G.V()];
        int[] edgeTo = new int[G.V()];
        for (int i = 0; i < G.V(); i++)
            distTo[i] = INFINITY;
        for (int i : w) {
            marked[i] = true;
            distTo[i] = 0;
            q.enqueue(i);
        }
        while (!q.isEmpty()) {
            int s = q.dequeue();
            for (int j : G.adj(s)) {
                if (!marked[j]) {
                    edgeTo[j] = s;
                    distTo[j] = distTo[s] + 1;
                    marked[j] = true;
                    q.enqueue(j);
                    if (vbfdp.hasPathTo(j)) {
                        int dis = vbfdp.distTo(j) + distTo[j];
                        if (dis < this.length || this.length == -1) {
                            this.length = dis;
                            this.ancestor = j;
                        }
                    }
                }
            }
        }
        return this.length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        this.length(v, w);
        return this.ancestor;
    }
}
