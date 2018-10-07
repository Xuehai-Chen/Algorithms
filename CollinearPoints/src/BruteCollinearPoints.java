
/**
 * Created by MuMu on 7/6/2017.
 */
import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.Quick3way;
import edu.princeton.cs.algs4.StdDraw;
import java.lang.*;

public class BruteCollinearPoints {
    private int num;
    private LineSegment[] linesegments = new LineSegment[1000];
    private Point[] p = new Point[4];
    public BruteCollinearPoints(Point[] points){
        if (points==null) throw new java.lang.IllegalArgumentException();
        for (int i=0; i<points.length-3;i++){
            for (int j=i+1;j<points.length-2;j++){
                for(int k=j+1;k<points.length-1;k++){
                    for(int l=k+1;l<points.length;l++){
                        p[0]=points[i];
                        p[1]=points[j];
                        p[2]=points[k];
                        p[3]=points[l];
                        Quick3way.sort(p);
                        if(p[0]==p[1]||p[1]==p[2]||p[2]==p[3]) throw new java.lang.IllegalArgumentException();
                        if ((p[0].slopeTo(p[1]) == p[0].slopeTo(p[2]))&&(p[0].slopeTo(p[1]) == p[0].slopeTo(p[3]))){
                            LineSegment line = new LineSegment(p[0], p[3]);
                            if(num==0){
                                linesegments[num++]= line;
                                p[0].drawTo(p[3]);
                            }
                            else {
                                int count=0;
                                for (int m = 0; m < num; m++) {
                                    if (!line.toString().equals(linesegments[m].toString())) count++;
                                }
                                if (count == num) {
                                    p[0].drawTo(p[3]);
                                    linesegments[num++] = line;
                                }
                            }
                        }
                    }
                }
            }
        }
    }    // finds all line segments containing 4 points
    public int numberOfSegments(){
        return num;
    }        // the number of line segments
    public LineSegment[] segments(){
        LineSegment[] finallinesegments = new LineSegment[this.numberOfSegments()];
        for(int i=0; i<this.numberOfSegments();i++){
            finallinesegments[i]=linesegments[i];
        }
        return finallinesegments;
    }                // the line segments

    public static void main(String[] args) {
        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        // read in the input
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Point[] pointArray=new Point[N];
        for (int i = 0; i < N; i++) {
            if(in.readLine()==null) throw new java.lang.IllegalArgumentException();
            int x = in.readInt();
            int y = in.readInt();
            Point p = new Point(x, y);
            pointArray[i] = p;
            p.draw();
        }
        StdDraw.show();
        BruteCollinearPoints po=new BruteCollinearPoints(pointArray);
        System.out.println(po.numberOfSegments());
        for(int i=0;i<po.numberOfSegments();i++) {
            System.out.println(po.linesegments[i]);
        }
    }
}
