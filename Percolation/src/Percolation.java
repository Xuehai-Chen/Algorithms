/**
 * Created by MuMu on 6/17/2017.
 */
import edu.princeton.cs.algs4.*;
public class Percolation {
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF uf_backwash;
    private int N;
    private int count_n=0;//record the number of open site;
    private boolean[] ifopen;//an array record if the site is open;

    public Percolation(int N) {
        if (N<=0) throw new IllegalArgumentException("N is<=0");
        this.N = N;
        int i;
        uf=new WeightedQuickUnionUF(N*N+2);
        uf_backwash=new WeightedQuickUnionUF(N*N+1);
        ifopen=new boolean[N*N+2];
        ifopen[0]=true;
        ifopen[N*N+1] = true;
        for(i=1;i<=N;i++){
            uf.union(0, i);
            uf_backwash.union(0, i);
            uf.union(N*N+1, (N-1)*N+i);
        }
    }// create N-by-N grid, with all sites blocked

    public void open(int i, int j){
        if (i<1||i>N) throw new IndexOutOfBoundsException("row index i out of bounds");
        if(j<1||j>N)  throw new IndexOutOfBoundsException("column index j out of bounds");
        if(ifopen[(i-1)*N+j]) return;
        ifopen[(i-1)*N+j]=true;
        count_n++;
        if (i==1){
            uf.union(0,j);
            uf_backwash.union(0, j);
        }
        else if (i>1 && ifopen[(i-2)*N+j]){
            uf.union((i-2)*N+j, (i-1)*N+j);
            uf_backwash.union((i-2)*N+j, (i-1)*N+j);
        }
        if (i==N){
            uf.union(N*N+1, (N-1)*N+j);
        }
        else if (i<N && ifopen[i*N+j]){
            uf.union(i*N+j, (i-1)*N+j);
            uf_backwash.union(i*N+j, (i-1)*N+j);
        }
        if (j!=1 && ifopen[(i-1)*N+j-1]){
            uf.union((i-1)*N+j, (i-1)*N+j-1);
            uf_backwash.union((i-1)*N+j, (i-1)*N+j-1);
        }
        if (j!=N && ifopen[(i-1)*N+j+1]){
            uf.union((i-1)*N+j, (i-1)*N+j+1);
            uf_backwash.union((i-1)*N+j, (i-1)*N+j+1);
        }
    }// open site (row i, column j) if it is not already

    public boolean isOpen(int i, int j){
        if (i<1||i>N) throw new IndexOutOfBoundsException("row index i out of bounds");
        if(j<1||j>N)  throw new IndexOutOfBoundsException("column index j out of bounds");
        return ifopen[(i-1)*N+j];
    }// is site (row i, column j) open?

    public boolean isFull(int i, int j){
        if (i<1||i>N) throw new IndexOutOfBoundsException("row index i out of bounds");
        if(j<1||j>N)  throw new IndexOutOfBoundsException("column index j out of bounds");
        return uf_backwash.connected((i-1)*N+j, 0) && ifopen[(i-1)*N+j];
    } // is site (row i, column j) full?

    public int numberOfOpenSites(){
        return count_n;
    }
    public boolean percolates() {
        return uf.connected(0, N*N+1);
    }// does the system percolate?

    public static void main(String[] args){
        int N = StdIn.readInt();
        Percolation pe=new Percolation(N);
        pe.open(1, 1);
        pe.open(2, 1);
        System.out.println(pe.percolates());
    }// test client, optional

}