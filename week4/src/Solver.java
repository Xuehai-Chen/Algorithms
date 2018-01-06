/**
 * Created by MuMu on 7/12/2017.
 */

import edu.princeton.cs.algs4.*;

public class Solver {
    private Searchnode initial;
    private Stack<Board> solution = new Stack<>();
    private boolean issolvable=false;
    private int move;
    private class Searchnode implements Comparable<Searchnode>{
        public Board board;
        public int move;
        public int priority;
        Searchnode prev;
        public Searchnode(Board board,int move, Searchnode prev){
            this.board = board;
            this.prev = prev;
            this.move = move;
            priority = board.manhattan()+this.move;
        }
        public int compareTo(Searchnode p){
            if (p.priority < this.priority) return 1;
            else if (p.priority == this.priority){
                if(p.board.manhattan()<this.board.manhattan()) return 1;
                else return -1;
            }
            else return -1;
        }
    }

    public Solver(Board initial){
        this.initial = new Searchnode(initial,0,null);
        Searchnode initialt = new Searchnode(this.initial.board.twin(),0,null);
        Searchnode current = this.initial;
        Searchnode currentt = initialt;
        MinPQ<Searchnode> mq = new MinPQ<Searchnode>();
        MinPQ<Searchnode> mqt = new MinPQ<Searchnode>();
        mq.insert(this.initial);
        mqt.insert(initialt);
        while(true){
            if(current.board.isGoal()){
                issolvable = true;
                while(current.prev!=null){
                    this.solution.push(current.board);
                    current = current.prev;
                }
                this.solution.push(this.initial.board);
                break;
            }
            if (currentt.board.isGoal()) break;
            current = mq.delMin();
            currentt = mqt.delMin();
            for (Board b:current.board.neighbors()){
                if(current.prev ==null||!current.prev.board.equals(b)){
                    Searchnode neighnodes = new Searchnode(b,current.move+1,current);
                    mq.insert(neighnodes);
                }
            }
            for (Board b:currentt.board.neighbors()){
                if(currentt.prev ==null||!currentt.prev.board.equals(b)){
                    Searchnode tneighnodes = new Searchnode(b,currentt.move+1,currentt);
                    mqt.insert(tneighnodes);
                }
            }
        }
    }           // find a solution to the initial board (using the A* algorithm)

    public boolean isSolvable(){
        return issolvable;
    }            // is the initial board solvable?

    public int moves(){
        if(this.isSolvable()){
            return this.solution.size()-1;
        }
        else return -1;
    }                     // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution(){
        if (!this.isSolvable()) throw new IllegalArgumentException();
        else{
            return solution;
        }
    }      // sequence of boards in a shortest solution; null if unsolvable

    public static void main(String[] args){
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    } // solve a slider puzzle (given below)
}
