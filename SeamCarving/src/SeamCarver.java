import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.lang.Math;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;
    private Bag<Integer>[] hadj, vadj;
    private int source = -1;
    private int target = Integer.MAX_VALUE;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        this.energy = new double[this.width()][this.height()];
        this.setEnergy();
        this.hadj = (Bag<Integer>[]) new Bag[(this.width() - 1) * this.height()];
        this.vadj = (Bag<Integer>[]) new Bag[this.width() * (this.height() - 1)];

        setAdj();
    }

    private void setAdj() {
        for (int i = 0; i < this.height() - 1; i++) {

            for (int j = 0; j < this.width(); j++) {
                this.vadj[i * this.width() + j] = new Bag();
                if (j == 0) {
                    this.vadj[i * this.width() + j].add((i + 1) * this.width() + j);
                    this.vadj[i * this.width() + j].add((i + 1) * this.width() + j + 1);
                } else if (j == this.width() - 1) {
                    this.vadj[i * this.width() + j].add((i + 1) * this.width() + j - 1);
                    this.vadj[i * this.width() + j].add((i + 1) * this.width() + j);
                } else {
                    this.vadj[i * this.width() + j].add((i + 1) * this.width() + j - 1);
                    this.vadj[i * this.width() + j].add((i + 1) * this.width() + j);
                    this.vadj[i * this.width() + j].add((i + 1) * this.width() + j + 1);
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
        return res;
    }

    private void relax() {

    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

    }

}
