/**
 * Created by MuMu on 7/15/2017.
 */

import edu.princeton.cs.algs4.*;

public class KdTree {
    private Node root;
    private int N;
    private int nodeisnull = 1;
    private class Node{
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int depth;
        public Node(Point2D p){
            this.p = p;
        }
    }
    public KdTree(){
        this.root = null;
        this.N = 0;
    }                               // construct an empty set of points
    public boolean isEmpty(){
        return root==null;
    }                      // is the set empty?
    public int size(){
        return this.N;
    }                         // number of points in the set
    public void insert(Point2D p){
        if (p==null) throw new IllegalArgumentException();
        Node np = new Node(p);
        if (this.N==0){
            this.root = np;
            this.root.depth=1;
            this.N++;
        }
        Node x= get(p);
        if (this.nodeisnull != 0){
            double cmpx = p.x()- x.p.x();
            double cmpy = p.y()- x.p.y();
            if(x.depth%2==1){
                if(cmpx<0) x.lb = np;
                else x.rt = np;
                np.depth = x.depth+1;
                }
            else{
                if(cmpy<0) x.lb = np;
                else x.rt = np;
                np.depth = x.depth+1;
            }
            this.N++;
        }
    }              // add the point to the set (if it is not already in the set)
    private Node get(Point2D p){
        return get(root, p);
    }
    private Node get(Node x, Point2D p){
        if (p==null) return null;
        double cmpx = p.x()- x.p.x();
        double cmpy = p.y()- x.p.y();
        if(x.p.x()==p.x()&&x.p.y()==p.y()){
            this.nodeisnull = 0;
            return x;
        }
        if(x.depth%2==1){
            if(cmpx<0) {
                if(x.lb ==null){
                    this.nodeisnull = 1;
                    return x;
                }
                else{
                return get(x.lb,p);
                }
            }
            else{
                if(x.rt ==null){
                    this.nodeisnull = 1;
                    return x;
                }
                else{
                return get(x.rt,p);
                }
            }
        }
        else{
            if(cmpy<0) {
                if(x.lb ==null){
                    this.nodeisnull = 1;
                    return x;
                }
                else {
                    return get(x.lb, p);
                }
            }
            else{
                if(x.rt ==null){
                    this.nodeisnull = 1;
                    return x;
                }
                else {
                    return get(x.rt, p);
                }
            }
        }
    }

    public boolean contains(Point2D p){
        boolean sig = false;
        if(p==null) throw new IllegalArgumentException();
        Node x = get(p);
        if(this.nodeisnull == 0) sig =true;
        return sig;
    }            // does the set contain point p?
    public void draw(){
        draw(this.root);
    }                         // draw all points to standard draw
    private void draw(Node np){
        np.p.draw();
        if(np.rt!=null) draw(np.rt);
        if(np.lb!=null) draw(np.lb);
    }
    private Queue<Point2D> range(Queue queue,RectHV rect,Node x){
        if(rect.xmin()<=x.p.x()&&x.p.x()<=rect.xmax()&&rect.ymin()<=x.p.y()&&x.p.y()<=rect.ymax()){
            queue.enqueue(x.p);
        }
        if(x.depth%2==1){
            if(rect.xmax()<x.p.x()&&x.lb!=null) queue=range(queue,rect,x.lb);
            else if(rect.xmin()>x.p.x()&&x.rt!=null) queue=range(queue,rect,x.rt);
            else {
                if(x.lb!=null) queue=range(queue,rect,x.lb);
                if(x.rt!=null) queue=range(queue,rect,x.rt);
            }
        }
        else{
            if(rect.ymax()<x.p.y()&&x.lb!=null) queue=range(queue,rect,x.lb);
            else if(rect.ymin()>x.p.y()&&x.rt!=null) queue=range(queue,rect,x.rt);
            else {
                if(x.lb!=null) queue=range(queue,rect,x.lb);
                if(x.rt!=null) queue=range(queue,rect,x.rt);
            }
        }
        return queue;
    }
    public Iterable<Point2D> range(RectHV rect){
        Queue<Point2D> queue = new Queue<>();
        return range(queue,rect,root);
    }             // all points that are inside the rectangle

    private RectHV leftRect( RectHV rect,Node node) {
        if (node.depth%2==1) {
            return new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax());
        } else {
            return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y());
        }
    }

    private RectHV rightRect( RectHV rect,Node node) {
        if (node.depth%2==1) {
            return new RectHV(node.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
        } else {
            return new RectHV(rect.xmin(), node.p.y(), rect.xmax(), rect.ymax());
        }
    }
    private Node nearest(Node x,Point2D p,Node temp,RectHV rect){
        if(x==null) return temp;
        double xdis = p.distanceSquaredTo(x.p);
        double tempdis = p.distanceSquaredTo(temp.p);
        RectHV left = null;
        RectHV right = null;
        if(tempdis >= xdis||tempdis>= rect.distanceSquaredTo(p)){
            if(tempdis > xdis) temp = x;
            if(x.depth%2==1){
                left = new RectHV(rect.xmin(), rect.ymin(), x.p.x(), rect.ymax());
                right = new RectHV(x.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                if(p.x() - x.p.x()<0){
                    if(x.lb!=null) temp = nearest(x.lb,p,temp,left);
                    if(x.rt!=null) temp = nearest(x.rt,p,temp,right);
                }
                else{
                    if(x.rt!=null) temp = nearest(x.rt,p,temp,right);
                    if(x.lb!=null) temp = nearest(x.lb,p,temp,left);
                }
            }
            else{
                left = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.p.y());
                right = new RectHV(rect.xmin(), x.p.y(), rect.xmax(), rect.ymax());
                if(p.y() - x.p.y()<0){
                    if(x.rt!=null) temp = nearest(x.rt,p,temp,right);
                    if(x.lb!=null) temp = nearest(x.lb,p,temp,left);
                }
                else{
                    if(x.lb!=null) temp = nearest(x.lb,p,temp,left);
                    if(x.rt!=null) temp = nearest(x.rt,p,temp,right);
                }
            }
        }
        return temp;
    }
    public Point2D nearest(Point2D p){
        return this.nearest(this.root,p,this.root,new RectHV(0.0,0.0,1.0,1.0)).p;
    }             // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args){

        edu.princeton.cs.algs4.In in = new In(args[0]);
        StdDraw.enableDoubleBuffering();
        // initialize the two data structures with point from standard input
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        //StdOut.println(kdtree.contains(new Point2D(0.785370,0.652338)));

        while (true) {
            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);
            double x0 = 0.4, y0 = 0.4;      // initial endpoint of rectangle
            double x1 = 0.5, y1 = 0.5;
            RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
                    Math.max(x0, x1), Math.max(y0, y1));

            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            kdtree.draw();

            // draw in blue the nearest neighbor (using kd-tree algorithm)
            StdDraw.setPenColor(StdDraw.BLUE);
            kdtree.nearest(query).draw();
            StdDraw.show();
            StdDraw.pause(40);
        }
    }                  // unit testing of the methods (optional)
}
