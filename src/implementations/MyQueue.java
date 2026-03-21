package implementations;

import utilities.Iterator;
import utilities.QueueADT;
import exceptions.EmptyQueueException;

public class MyQueue<E> implements QueueADT<E> {
    private MyDLL<E> list;

    public MyQueue() {
        list = new MyDLL<>();
    }

    @Override
    public void enqueue(E element) throws NullPointerException {
        if (element == null) throw new NullPointerException("Element cannot be null");
        list.add(element);
    }

    @Override
    public E dequeue() throws EmptyQueueException {
        if (isEmpty()) throw new EmptyQueueException("Queue is empty");
        return list.remove(0);
    }

    @Override
    public E peek() throws EmptyQueueException {
        if (isEmpty()) throw new EmptyQueueException("Queue is empty");
        return list.get(0);
    }

    @Override
    public void dequeueAll() {
        list.clear();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public boolean equals(QueueADT<E> that) {
        if (that == null) return false;
        if (this.size() != that.size()) return false;
        Iterator<E> it1 = this.iterator();
        Iterator<E> it2 = that.iterator();
        while (it1.hasNext()) {
            if (!it1.next().equals(it2.next())) return false;
        }
        return true;
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public E[] toArray(E[] copyArray) throws NullPointerException {
        return list.toArray(copyArray);
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean contains(E element) throws NullPointerException {
        if (element == null) throw new NullPointerException("Element cannot be null");
        return list.contains(element);
    }

    @Override
    public int search(E element) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(element)) {
                return i + 1;
            }
        }
        return -1;
    }
}

