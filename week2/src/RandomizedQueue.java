/**
 * Created by MuMu on 7/1/2017.
 */
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int N;
    private Item[] a;
    public RandomizedQueue(){
        N=0;
        a = (Item[]) new Object[2];
    }                 // construct an empty randomized queue
    public boolean isEmpty(){
        return N==0;
    }                 // is the queue empty?
    public int size(){
        return N;
    }                        // return the number of items on the queue
    private void resize(int max){
        Item[] temp = (Item[]) new Object[max];
        for (int i =0; i<N; i++){
            temp[i] = a[i];
        }
        a = temp;
    }
    public void enqueue(Item item){
        if(item == null) throw new java.lang.IllegalArgumentException();
        if (N == a.length) resize(2*a.length);
        a[N]= item;
        N++;
    }           // add the item
    public Item dequeue(){
        if(isEmpty()) throw new java.util.NoSuchElementException();
        int index = StdRandom.uniform(N);
        Item item = a[index];
        if (N==1){
            a[0]=null;
        }
        else {
            for (int i=index; i<N-1;i++){
                a[i]=a[i+1];
            }
            a[N-1]=null;
        }
        N--;
        if (N>0&&N==a.length/4)resize(a.length/2);
        return item;
    }                    // remove and return a random item
    public Item sample(){
        if(isEmpty()) throw new java.util.NoSuchElementException();
        int ref = StdRandom.uniform(N);
        Item item = a[ref];
        return item;
    }                     // return (but do not remove) a random item
    private class ListIterator implements Iterator<Item>{
        private int[] ref = StdRandom.permutation(N);
        private int index=0;
        public boolean hasNext(){
            return index != N;
        }
        public void remove(){
            throw new java.lang.UnsupportedOperationException();
        }
        public Item next(){
            if(!hasNext()) throw new java.util.NoSuchElementException();
            Item item = a[ref[index]];
            index++;
            return item;
        }
    }
    public Iterator<Item> iterator(){
        return new ListIterator();
    }         // return an independent iterator over items in random order
    public static void main(String[] args){
        RandomizedQueue<String> rq= new RandomizedQueue<String>();
        rq.enqueue("1");
        System.out.println(rq.size());
        rq.enqueue("2");
        rq.enqueue("3");
        rq.enqueue("4");
        System.out.println(rq.size());
        rq.dequeue();
        System.out.println(rq.size());
        rq.dequeue();
        System.out.println(rq.size());
        rq.dequeue();
        System.out.println(rq.size());
        rq.dequeue();
        System.out.println(rq.size());
    }   // unit testing (optional)
}
