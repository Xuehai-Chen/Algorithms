import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.lang.Math;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;
    private Bag<Integer>[] adj;
    private double[] distTo;
    private int[] pathTo;
    private int target = Integer.MAX_VALUE;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        this.energy = new double[this.width()][this.height()];
        this.setEnergy();
        this.adj = (Bag<Integer>[]) new Bag[this.width() * this.height()];
        this.distTo = new double[this.width() * this.height()];
        for (int i = 0; i < this.height() * this.width(); i++) {
            if (i < this.width()) {
                distTo[i] = energy[i][0];
            } else {
                distTo[i] = Double.MAX_VALUE;
            }
        }
        this.pathTo = new int[this.width() * this.height()];
        setAdj();
    }

    private void setAdj() {
        for (int i = 0; i < this.height(); i++) {
            for (int j = 0; j < this.width(); j++) {
                this.adj[i * this.width() + j] = new Bag();
                if (i > 0) {
                    if (j == 0) {
                        this.adj[i * this.width() + j].add((i - 1) * this.width() + j);
                        this.adj[i * this.width() + j].add((i - 1) * this.width() + j + 1);
                    } else if (j == this.width() - 1) {
                        this.adj[i * this.width() + j].add((i - 1) * this.width() + j - 1);
                        this.adj[i * this.width() + j].add((i - 1) * this.width() + j);
                    } else {
                        this.adj[i * this.width() + j].add((i - 1) * this.width() + j - 1);
                        this.adj[i * this.width() + j].add((i - 1) * this.width() + j);
                        this.adj[i * this.width() + j].add((i - 1) * this.width() + j + 1);
                    }
                }
            }
        }
    }

    // current picture
    public Picture picture() {
        return this.picture;
    }

    // width of current picture
    public int width() {
        return this.picture.width();
    }

    // height of current picture
    public int height() {
        return this.picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        return this.energy[x][y];
    }

    private void setEnergy() {
        for (int x = 0; x < this.width(); x++) {
            for (int y = 0; y < this.height(); y++) {
                if (x == 0 || x == this.width() - 1 || y == 0 || y == this.height() - 1) {
                    this.energy[x][y] = 1000;
                } else {
                    double xcds = this.cds(this.picture.get(x - 1, y), this.picture.get(x + 1, y));
                    double ycds = this.cds(this.picture.get(x, y - 1), this.picture.get(x, y + 1));
                    this.energy[x][y] = Math.sqrt(xcds + ycds);
                }
            }
        }
    }

    private double cds(Color a, Color b) {
        double rsquare = Math.pow(a.getRed() - b.getRed(), 2);
        double gsquare = Math.pow(a.getGreen() - b.getGreen(), 2);
        double bsquare = Math.pow(a.getBlue() - b.getBlue(), 2);
        return rsquare + gsquare + bsquare;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] res = new int[this.width()];
        return res;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] res = new int[this.height()];
        for (int i = this.width(); i < this.height() * this.width(); i++) {
            for (int adj : this.adj[i]) {
                relax(adj, i);
            }
        }
        res[this.height() - 1] = this.width() - 1;
        for (int i = (this.height() - 1) * this.width(); i < this.height() * this.width(); i++) {
            if (distTo[i] < distTo[res[this.height() - 1]+this.width()*(this.height()-1)]) {
                res[this.height() - 1] = i % this.width();
            }
        }
        for (int i = this.height() - 2; i >= 0; i--) {
            res[i] = pathTo[res[i + 1] + this.width() * (i+1)] % this.width();
        }
        return res;
    }

    private void relax(int s, int t) {
        double weight = energy[t % this.width()][t / this.width()];
        if (this.distTo[t] > this.distTo[s] + weight) {
            this.distTo[t] = this.distTo[s] + weight;
            this.pathTo[t] = s;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

    }

}
