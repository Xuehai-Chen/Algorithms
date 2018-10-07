/**
 * Created by MuMu on 6/17/2017.
 */
import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] trialres;
    private int n;
    private int trials;
    public PercolationStats(int n, int trials){
        trialres= new double[trials];
        this.n=n;
        this.trials= trials;
        if(n<=0) throw new IllegalArgumentException();
        if(trials<=0) throw new IllegalArgumentException();
        for (int i=0;i<trials;i++){
            Percolation pe=new Percolation(n);
            int count=0;
            while(!pe.percolates()){
                int j= StdRandom.uniform(n)+1;
                int k= StdRandom.uniform(n)+1;
                if (pe.isOpen(j,k)) continue;
                pe.open(j,k);
                count++;
            }
            trialres[i]=(double)count/(double)(n*n);
        }
    }// perform trials independent experiments on an n-by-n grid

    public double mean(){
        return StdStats.mean(trialres);
    }// sample mean of percolation threshold

    public double stddev(){
        return StdStats.stddev(trialres);
    }// sample standard deviation of percolation threshold

    public double confidenceLo(){
        return (this.mean()-1.96*this.stddev()/Math.sqrt(n));
    }// low  endpoint of 95% confidence interval

    public double confidenceHi(){
        return (this.mean()+1.96*this.stddev()/Math.sqrt(n));
    }// high endpoint of 95% confidence interval

    public static void main(String[] args){
        int n = StdIn.readInt();
        int trials = StdIn.readInt();
        PercolationStats pe =new PercolationStats(n, trials);
        System.out.println("mean="+pe.mean());
        System.out.println("stddev="+pe.stddev());
        System.out.println("95% confidence interva= ["+pe.confidenceLo()+","+pe.confidenceHi()+"]");
    }// test client (described below)
}