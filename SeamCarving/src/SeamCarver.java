import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private boolean transposeFlag = false;
    private boolean setEnergyFlag = false;
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
        Picture p;
        if (this.transposeFlag) {
            p = new Picture(this.transpose(this.picture));
        } else {
            p = new Picture(this.picture);
        }
        return p;
    }

    // width of current picture
    public int width() {
        int res;
        if (this.transposeFlag) {
            res = this.picture.height();
        } else {
            res = this.picture.width();
        }
        return res;
    }

    // height of current picture
    public int height() {
        int res;
        if (this.transposeFlag) {
            res = this.picture.width();
        } else {
            res = this.picture.height();
        }
        return res;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (setEnergyFlag) {
            if (transposeFlag) {
                this.picture = this.transpose(this.picture);
                this.transposeFlag = false;
            }
            this.energy = this.setEnergy(this.picture);
            this.setEnergyFlag = false;
        }
        if (this.transposeFlag) {
            checkEnergyInputValid(y, x);
        } else {
            checkEnergyInputValid(x, y);
        }
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
        double[][] distTo = new double[this.picture.width()][this.picture.height()];
        for (int i = 0; i < this.picture.width(); i++) {
            for (int j = 0; j < this.picture.height(); j++) {
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
        int w = this.picture.width();
        int h = this.picture.height();
        Bag<Integer>[][] adj = (Bag<Integer>[][]) new Bag[w][h];
        for (int i = 0; i < this.picture.width(); i++) {
            for (int j = 0; j < this.picture.height(); j++) {
                adj[i][j] = new Bag<>();
                if (j > 0) {
                    if (i == 0) {
                        adj[i][j].add((j - 1) * this.picture.width() + i);
                        adj[i][j].add((j - 1) * this.picture.width() + i + 1);
                    } else if (i == this.picture.width() - 1) {
                        adj[i][j].add((j - 1) * this.picture.width() + i - 1);
                        adj[i][j].add((j - 1) * this.picture.width() + i);
                    } else {
                        adj[i][j].add((j - 1) * this.picture.width() + i - 1);
                        adj[i][j].add((j - 1) * this.picture.width() + i);
                        adj[i][j].add((j - 1) * this.picture.width() + i + 1);
                    }
                }
            }
        }
        return adj;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (!this.transposeFlag) {
            this.picture = this.transpose(this.picture);
            this.transposeFlag = true;
        }
        return findSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (this.transposeFlag) {
            this.picture = this.transpose(this.picture);
            this.transposeFlag = false;
        }
        return findSeam();
    }

    private int[] findSeam() {
        int[] res = new int[this.picture.height()];
        double[][] energy = this.setEnergy(this.picture);
        Bag<Integer>[][] Adj = this.setAdj();
        double[][] distTo = setDistTo(energy);
        int[][] pathTo = new int[this.picture.width()][this.picture.height()];
        for (int i = this.picture.width(); i < this.picture.height() * this.picture.width(); i++) {
            for (int adj : Adj[i % this.picture.width()][i / this.picture.width()]) {
                relax(adj, i, energy, distTo, pathTo);
            }
        }
        res[this.picture.height() - 1] = this.picture.width() - 1;
        for (int i = 0; i < this.picture.width(); i++) {
            if (distTo[i][this.picture.height() - 1] < distTo[res[this.picture.height() - 1]][this.picture.height() - 1]) {
                res[this.picture.height() - 1] = i;
            }
        }
        for (int i = this.picture.height() - 2; i >= 0; i--) {
            res[i] = pathTo[res[i + 1]][i + 1] % this.picture.width();
        }
        return res;
    }

    private void relax(int s, int t, double[][] energy, double[][] distTo, int[][] pathTo) {
        int sx = s % this.picture.width();
        int sy = s / this.picture.width();
        int tx = t % this.picture.width();
        int ty = t / this.picture.width();
        double weight = energy[tx][ty];
        if (distTo[tx][ty] > distTo[sx][sy] + weight) {
            distTo[tx][ty] = distTo[sx][sy] + weight;
            pathTo[tx][ty] = s;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (!this.transposeFlag) {
            this.picture = this.transpose(this.picture);
            this.transposeFlag = true;
        }
        this.picture = removeSeam(seam);
        this.setEnergyFlag = true;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (this.transposeFlag) {
            this.picture = this.transpose(this.picture);
            this.transposeFlag = false;
        }
        this.picture = removeSeam(seam);
        this.setEnergyFlag = true;
    }

    private Picture removeSeam(int[] seam) {
        checkValidSeam(seam, this.picture.width(), this.picture.height());
        Picture p = new Picture(this.picture.width() - 1, this.picture.height());
        for (int j = 0; j < this.picture.height(); j++) {
            for (int i = 0; i < this.picture.width() - 1; i++) {
                if (i < seam[j]) {
                    p.setRGB(i, j, this.picture.getRGB(i, j));
                } else {
                    p.setRGB(i, j, this.picture.getRGB(i + 1, j));
                }
            }
        }
        return p;
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

    private void checkEnergyInputValid(int x, int y) {
        if (x < 0 || x > this.picture.width() - 1 || y < 0 || y > this.picture.height() - 1)
            throw new IllegalArgumentException();
    }

}
