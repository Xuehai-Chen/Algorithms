/**
 * Created by MuMu on 7/14/2017.
 */
import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.Queue;

public class PointSET {
    private SET<Point2D> set = new SET<>();
    public PointSET(){}// construct an empty set of points
    public boolean isEmpty(){
        return this.set.size() ==0;
    }                      // is the set empty?
    public int size(){
        return this.set.size();
    }                         // number of points in the set
    public void insert(Point2D p){
        if(p==null) throw new IllegalArgumentException();
        else {
            this.set.add(p);
        }
    }// add the point to the set (if it is not already in the set)
    public boolean contains(Point2D p){
        return this.set.contains(p);
    }            // does the set contain point p?
    public void draw(){
        for(Point2D p:set){
            p.draw();
        }
    }                         // draw all points to standard draw
    public Iterable<Point2D> range(RectHV rect){
        Queue<Point2D> ps = new Queue<>();
        for(Point2D p:set){
            if(p.x()>=rect.xmin()&&p.x()<=rect.xmax()&&p.y()>=rect.ymin()&&p.y()<=rect.ymax())
                ps.enqueue(p);
        }
        return ps;
    }             // all points that are inside the rectangle
    public Point2D nearest(Point2D p){
        Point2D temp = null;
        double tempdis=Double.MAX_VALUE;
        double nextdis;
        for(Point2D pi:set){
            nextdis = p.distanceSquaredTo(pi);
            if (nextdis< tempdis){
                tempdis = nextdis;
                temp = pi;
            }
        }
        return temp;
    }             // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args){}                  // unit testing of the methods (optional)
}
