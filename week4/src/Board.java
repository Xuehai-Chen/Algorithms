/**
 * Created by MuMu on 7/10/2017.
 */

import edu.princeton.cs.algs4.*;
import java.util.Iterator;

public class Board {
    private final int[][] board;
    private final int N;
    private int blanki;
    private int blankj;
    public Board(int[][] blocks){
        this.board = blocks;
        this.N = blocks.length;
    }           // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)

    public int dimension(){
        return N;
    }                 // board dimension n

    public int hamming(){
        int content;
        int count=0;
        for(int k=0;k<N*N;k++){
            content=board[k/N][k%N];
            if (content!= k+1&&content!=0) {
                count++;
            }
        }
        return count;
    }                   // number of blocks out of place

    public int manhattan(){
        int content;
        int manhattansum=0;
        for(int k=0;k<N*N;k++){
            content=board[k/N][k%N];
            if (content!= k+1&content!=0) {
                manhattansum+=Math.abs((content-1)/N-k/N)+Math.abs((content-1)%N-k%N);
            }
        }
        return manhattansum;
    }// sum of Manhattan distances between blocks and goal

    public boolean isGoal(){
        return this.hamming()==0;
    }                // is this board the goal board?

    public Board twin(){
        int[][] twin = new int[N][N];
        copy(twin);
        int k=0;
        while(k<N*N){
            if(twin[k/N][k%N]!=0&&twin[(k+1)/N][(k+1)%N]!=0&&k/N!=0){
                swap(twin,k/N,k%N,(k+1)/N,(k+1)%N);
                break;
            }
            else if(twin[k/N][k%N]!=0&&twin[k/N+1][k%N]!=0){
                swap(twin,k/N,k%N,k/N+1,k%N);
                break;
            }
            k++;
        }
        return new Board(twin);
    }                    // a board that is obtained by exchanging any pair of blocks

    public boolean equals(Object y){
        if(y == null) throw new IllegalArgumentException();
        if(y instanceof  Board){
            Board comp = (Board)y;
            if (N != comp.dimension()) return false;
            for(int i=0;i<N;i++) {
                for (int j = 0; j < N; j++) {
                    if (this.board[i][j] != comp.board[i][j])
                        return false;
                }
            }
        }
        else return false;
        return true;
    }        // does this board equal y?

    private void copy(int[][] copy){
        for (int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                copy[i][j] = board[i][j];
            }
        }
    }

    public Iterable<Board> neighbors(){
        Queue<Board> q = new Queue<>();
        int content;
        for(int k=0;k<N*N;k++) {
            content = board[k / N][k % N];
            if (content == 0) {
                blanki = k / N;
                blankj = k % N;
                break;
            }
        }
        if(blanki+1<N){
            int[][] aux = new int[N][N];
            copy(aux);
            swap(aux,blanki,blankj,blanki+1,blankj);
            q.enqueue(new Board(aux));
            //swap(aux,blanki,blankj,blanki+1,blankj);
        }
        if(blanki-1>=0){
            int[][] aux = new int[N][N];
            copy(aux);
            swap(aux,blanki,blankj,blanki-1,blankj);
            q.enqueue(new Board(aux));
            //swap(aux,blanki,blankj,blanki-1,blankj);
        }
        if(blankj+1<N){
            int[][] aux = new int[N][N];
            copy(aux);
            swap(aux,blanki,blankj,blanki,blankj+1);
            q.enqueue(new Board(aux));
            //swap(aux,blanki,blankj,blanki,blankj+1);
        }
        if(blankj-1>=0){
            int[][] aux = new int[N][N];
            copy(aux);
            swap(aux,blanki,blankj,blanki,blankj-1);
            q.enqueue(new Board(aux));
            //swap(aux,blanki,blankj,blanki,blankj-1);
        }
        return q;
    }     // all neighboring boards

    private void swap(int[][] copy, int previ,int prevj,int afti,int aftj){
        int temp = copy[previ][prevj];
        copy[previ][prevj]=copy[afti][aftj];
        copy[afti][aftj]=temp;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }               // string representation of this board (in the output format specified below)

    public static void main(String[] args){
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        //System.out.println(initial.dimension());
        System.out.println(initial.toString());
        //System.out.println("["+initial.blanki+","+initial.blankj+"]");
        System.out.println(initial.hamming());
        System.out.println(initial.manhattan());
        //System.out.println("["+initial.blanki+","+initial.blankj+"]");
        //System.out.println(initial.twin().toString());
        Iterator<Board> it=initial.neighbors().iterator();
        System.out.println("["+initial.blanki+","+initial.blankj+"]");
        while(it.hasNext()) {
            System.out.println(it.next().toString());
        }
    } // unit tests (not graded)
}
