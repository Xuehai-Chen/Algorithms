import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.lang.Math;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;
    private Bag<Integer>[][] adj;
    private double[][] distTo;
    private int[][] pathTo;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if(picture==null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
        this.energy = new double[this.width()][this.height()];
        this.setEnergy();
        this.adj = (Bag<Integer>[][]) new Bag[this.width()][this.height()];
        this.distTo = new double[this.width()][this.height()];
        for (int i = 0; i < this.width(); i++) {
            for (int j = 0; j < this.height(); j++) {
                if (j == 0) {
                    distTo[i][j] = energy[i][0];
                } else {
                    distTo[i][j] = Double.MAX_VALUE;
                }
            }
        }
        this.pathTo = new int[this.width()][this.height()];
        setAdj();
    }

    private void setAdj() {
        for (int i = 0; i < this.width(); i++) {
            for (int j = 0; j < this.height(); j++) {
                this.adj[i][j] = new Bag();
                if (j > 0) {
                    if (i == 0) {
                        this.adj[i][j].add((j - 1) * this.width() + i);
                        this.adj[i][j].add((j - 1) * this.width() + i + 1);
                    } else if (i == this.width() - 1) {
                        this.adj[i][j].add((j - 1) * this.width() + i - 1);
                        this.adj[i][j].add((j - 1) * this.width() + i);
                    } else {
                        this.adj[i][j].add((j - 1) * this.width() + i - 1);
                        this.adj[i][j].add((j - 1) * this.width() + i);
                        this.adj[i][j].add((j - 1) * this.width() + i + 1);
                    }
                }
            }
        }
    }

    // current picture
    public Picture picture() {
        return new Picture(this.picture);
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
        if(x<0||x>this.width()-1||y<0||y>this.height()-1) throw new IllegalArgumentException();
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
        this.picture = this.transpose(this.picture);
        SeamCarver sc = new SeamCarver(this.picture);
        int[] res = sc.findVerticalSeam();
        this.picture = this.transpose(this.picture);
        return res;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] res = new int[this.height()];
        for (int i = this.width(); i < this.height() * this.width(); i++) {
            for (int adj : this.adj[i % this.width()][i / this.width()]) {
                relax(adj, i);
            }
        }
        res[this.height() - 1] = this.width() - 1;
        for (int i = 0; i < this.width(); i++) {
            if (distTo[i][this.height() - 1] < distTo[res[this.height() - 1]][this.height() - 1]) {
                res[this.height() - 1] = i;
            }
        }
        for (int i = this.height() - 2; i >= 0; i--) {
            res[i] = pathTo[res[i + 1]][i + 1] % this.width();
        }
        return res;
    }

    private void relax(int s, int t) {
        int sx = s % this.width();
        int sy = s / this.width();
        int tx = t % this.width();
        int ty = t / this.width();
        double weight = energy[tx][ty];
        if (this.distTo[tx][ty] > this.distTo[sx][sy] + weight) {
            this.distTo[tx][ty] = this.distTo[sx][sy] + weight;
            this.pathTo[tx][ty] = s;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        this.picture = this.transpose(this.picture);
        SeamCarver sc = new SeamCarver(this.picture);
        sc.removeVerticalSeam(seam);
        this.picture = this.transpose(sc.picture);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        this.checkValidSeam(seam,this.width(),this.height());
        Picture p = new Picture(this.width() - 1, this.height());
        for (int j = 0; j < this.height(); j++) {
            for (int i = 0; i < this.width() - 1; i++) {
                if (i < seam[j]) {
                    p.set(i, j, this.picture.get(i, j));
                } else {
                    p.set(i, j, this.picture.get(i + 1, j));
                }
            }
        }
        this.picture = p;
    }

    private Picture transpose(Picture picture) {
        int h = picture.height();
        int w = picture.width();
        Picture res = new Picture(h, w);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                res.set(j, i, picture.get(i, j));
            }
        }
        return res;
    }

    private void checkValidSeam(int[] seam, int index,int count){
        if(seam==null||seam.length!=count) throw new IllegalArgumentException();
        for(int i=0;i<seam.length;i++){
            if(seam[i]<0||seam[i]>(index-1)) throw new IllegalArgumentException();
            if(i>0&&Math.abs(seam[i]-seam[i-1])>1) throw new IllegalArgumentException();
        }
    }

}
