/**
 * Created by MuMu on 7/6/2017.
 */
import java.lang.*;
import java.util.Arrays;
import edu.princeton.cs.algs4.Quick3way;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private int num;
    private LineSegment[] linesegments = new LineSegment[200];
    private Point[] p = new Point[4];

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.IllegalArgumentException();
        int N = points.length;
        Quick3way.sort(points);
        Point[] aux = new Point[N];
        for (int i = 0; i < N-3; i++) {
            for (int j = i; j < N; j++) aux[j] = points[j];
            Arrays.sort(aux, i+1, N, aux[i].slopeOrder());
            int head = i+1;
            int tail = i+2;

            while (tail < N) {
        /* if equal slopes encountered, just move tail forward */
                while (tail < N && aux[i].slopeTo(aux[head]) == aux[i].slopeTo(aux[tail]))
                    tail++;
        /* on slopes not equal, check if we have a segment */
                if (tail - head >= 3) {
            /* we have a segment here */
                    int k;
                    for (k = 0; k < i; k++)
                        if (aux[k].slopeTo(aux[i]) == aux[i].slopeTo(aux[head]))
                            break;
                    if (k >= i) {
                        aux[i].drawTo(aux[tail-1]);
                        linesegments[num++]=new LineSegment(aux[i],aux[tail-1]);
                    }
                }
        /* move head to last not equal slope point */
                head = tail;
                tail = tail+1;
            }
        }
    }     // finds all line segments containing 4 or more points

    public int numberOfSegments() {
        return num;
    }        // the number of line segments

    public LineSegment[] segments() {
        LineSegment[] finallinesegments = new LineSegment[this.numberOfSegments()];
        for (int i = 0; i < this.numberOfSegments(); i++) {
            finallinesegments[i] = linesegments[i];
        }
        return finallinesegments;
    }                // the line segments
    public static void main(String[] args) {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        // read in the input
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Point[] pointArray = new Point[N];
        for (int i = 0; i < N; i++) {
            if (in.readLine() == null) throw new java.lang.IllegalArgumentException();
            int x = in.readInt();
            int y = in.readInt();
            Point p = new Point(x, y);
            pointArray[i] = p;
            p.draw();
        }
        StdDraw.show();
        FastCollinearPoints test = new FastCollinearPoints(pointArray);
        System.out.println(test.numberOfSegments());
        for (LineSegment segment : test.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}