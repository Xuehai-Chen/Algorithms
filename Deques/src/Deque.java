/**
 * Created by MuMu on 6/29/2017.
 */
import edu.princeton.cs.algs4.*;
import java.util.Iterator;
import java.lang.*;
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;

public class Deque<Item> implements Iterable<Item> {
    private int N;
    private Node first;
    private Node last;
    private class Node{
        Item item;
        Node next;
        Node prev;
    }
    public Deque(){
        first = null;
        last = null;
        N=0;
    }                                                                   // construct an empty deque
    public boolean isEmpty(){
        return N==0;
    }                                                                   // is the deque empty?
    public int size(){
        return N;
    }                                                                   // return the number of items on the deque
    public void addFirst(Item item){
        if(item == null) throw new java.lang.IllegalArgumentException();
        Node oldfirst = first;
        first = new Node();
        first.prev = null;
        first.item = item;
        first.next = oldfirst;
        if (isEmpty()) last = first;
        else oldfirst.prev = first;
        N++;
    }                                                                   // add the item to the front
    public void addLast(Item item){
        if(item == null) throw new java.lang.IllegalArgumentException();
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next =null;
        last.prev = oldlast;
        if (isEmpty()) first = last;
        else{
            oldlast.next = last;
        }
        N++;
    }                                                               // add the item to the end
    public Item removeFirst(){
        if(isEmpty()) throw new NoSuchElementException("the deque is empty!");
        Item item = first.item;
        first = first.next;
        if(isEmpty()) last = null;
        N--;
        return item;
    }                                                               // remove and return the item from the front
    public Item removeLast(){
        if(isEmpty()) throw new NoSuchElementException("the deque is empty!");
        Item item = last.item;
        if (N==1){
            first = null;
            last = null;
        }
        else{
            last = last.prev;
            last.next = null;
        }
        N--;
        return item;
    }                                                               // remove and return the item from the end
    public Iterator<Item> iterator(){
        return new ListIterator();
    }                                                               // return an iterator over items in order from front to end
    private class ListIterator implements Iterator<Item>{
        private Node current = first;
        public boolean hasNext(){
            return current != null;
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
        public Item next(){
            if (current==null) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    public static void main(String[] args){
        Deque<String> deque = new Deque<String>();
        System.out.println(deque.isEmpty());
        deque.addFirst("this the first item added");
        System.out.println(deque.size());
        deque.addFirst("this the second item added");
        System.out.println(deque.size());
        deque.addFirst("this the third item added");
        System.out.println(deque.size());
        deque.addLast("this the last item added");
        System.out.println(deque.size());
        deque.removeFirst();
        System.out.println(deque.size());
        deque.removeLast();
        System.out.println(deque.size());
    }
}                                                                               // unit testing (optional)
