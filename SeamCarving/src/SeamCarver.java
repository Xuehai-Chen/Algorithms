import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
        this.energy = this.setEnergy(this.picture);
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
        if (x < 0 || x > this.width() - 1 || y < 0 || y > this.height() - 1) throw new IllegalArgumentException();
        return this.energy[x][y];
    }

    private double[][] setEnergy(Picture picture) {
        int w = picture.width();
        int h = picture.height();
        double[][] en = new double[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (x == 0 || x == w - 1 || y == 0 || y == h - 1) {
                    en[x][y] = 1000;
                } else {
                    double xcds = this.cds(picture.getRGB(x - 1, y), picture.getRGB(x + 1, y));
                    double ycds = this.cds(picture.getRGB(x, y - 1), picture.getRGB(x, y + 1));
                    en[x][y] = Math.sqrt(xcds + ycds);
                }
            }
        }
        return en;
    }

    private double cds(int a, int b) {
        double rsquare = Math.pow(((a >> 16) & 0xFF) - ((b >> 16) & 0xFF), 2);
        double gsquare = Math.pow(((a >> 8) & 0xFF) - ((b >> 8) & 0xFF), 2);
        double bsquare = Math.pow(((a >> 0) & 0xFF) - ((b >> 0) & 0xFF), 2);
        return rsquare + gsquare + bsquare;
    }

    private double[][] setDistTo(double[][] energy) {
        double[][] distTo = new double[this.width()][this.height()];
        for (int i = 0; i < this.width(); i++) {
            for (int j = 0; j < this.height(); j++) {
                if (j == 0) {
                    distTo[i][j] = energy[i][0];
                } else {
                    distTo[i][j] = Double.MAX_VALUE;
                }
            }
        }
        return distTo;
    }

    private Bag<Integer>[][] setAdj() {
        Bag<Integer>[][] adj = (Bag<Integer>[][]) new Bag[this.width()][this.height()];
        for (int i = 0; i < this.width(); i++) {
            for (int j = 0; j < this.height(); j++) {
                adj[i][j] = new Bag();
                if (j > 0) {
                    if (i == 0) {
                        adj[i][j].add((j - 1) * this.width() + i);
                        adj[i][j].add((j - 1) * this.width() + i + 1);
                    } else if (i == this.width() - 1) {
                        adj[i][j].add((j - 1) * this.width() + i - 1);
                        adj[i][j].add((j - 1) * this.width() + i);
                    } else {
                        adj[i][j].add((j - 1) * this.width() + i - 1);
                        adj[i][j].add((j - 1) * this.width() + i);
                        adj[i][j].add((j - 1) * this.width() + i + 1);
                    }
                }
            }
        }
        return adj;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        SeamCarver sc = new SeamCarver(this.transpose(this.picture));
        int[] res = sc.findVerticalSeam();
        this.picture = sc.transpose(sc.picture);
        return res;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] res = new int[this.height()];
        double[][] energy = this.setEnergy(this.picture);
        Bag<Integer>[][] Adj = this.setAdj();
        double[][] distTo = setDistTo(energy);
        int[][] pathTo = new int[this.width()][this.height()];
        for (int i = this.width(); i < this.height() * this.width(); i++) {
            for (int adj : Adj[i % this.width()][i / this.width()]) {
                relax(adj, i, energy, distTo, pathTo);
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

    private void relax(int s, int t, double[][] energy, double[][] distTo, int[][] pathTo) {
        int sx = s % this.width();
        int sy = s / this.width();
        int tx = t % this.width();
        int ty = t / this.width();
        double weight = energy[tx][ty];
        if (distTo[tx][ty] > distTo[sx][sy] + weight) {
            distTo[tx][ty] = distTo[sx][sy] + weight;
            pathTo[tx][ty] = s;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        SeamCarver sc = new SeamCarver(this.transpose(this.picture));
        sc.removeVerticalSeam(seam);
        this.picture = sc.transpose(sc.picture);
        this.energy = this.setEnergy(this.picture);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        this.checkValidSeam(seam, this.width(), this.height());
        Picture p = new Picture(this.width() - 1, this.height());
        for (int j = 0; j < this.height(); j++) {
            for (int i = 0; i < this.width() - 1; i++) {
                if (i < seam[j]) {
                    p.setRGB(i, j, this.picture.getRGB(i, j));
                } else {
                    p.setRGB(i, j, this.picture.getRGB(i + 1, j));
                }
            }
        }
        this.picture = p;
        this.energy = this.setEnergy(this.picture);
    }

    private Picture transpose(Picture picture) {
        int h = picture.height();
        int w = picture.width();
        Picture res = new Picture(h, w);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                res.setRGB(j, i, picture.getRGB(i, j));
            }
        }
        return res;
    }

    private void checkValidSeam(int[] seam, int index, int count) {
        if (seam == null || seam.length != count) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > (index - 1)) throw new IllegalArgumentException();
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) throw new IllegalArgumentException();
        }
    }

}
