package implementations;

import utilities.Iterator;
import utilities.ListADT;
import java.util.NoSuchElementException;

public class MyDLL<E> implements ListADT<E> {
    private MyDLLNode<E> head;
    private MyDLLNode<E> tail;
    private int size;

    public MyDLL() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean add(int index, E toAdd) throws NullPointerException, IndexOutOfBoundsException {
        if (toAdd == null) throw new NullPointerException("Element cannot be null");
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Invalid index");

        MyDLLNode<E> newNode = new MyDLLNode<>(toAdd);
        if (size == 0) {
            head = tail = newNode;
        } else if (index == 0) {
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        } else if (index == size) {
            newNode.setPrev(tail);
            tail.setNext(newNode);
            tail = newNode;
        } else {
            MyDLLNode<E> current = head;
            for (int i = 0; i < index; i++) current = current.getNext();
            newNode.setPrev(current.getPrev());
            newNode.setNext(current);
            current.getPrev().setNext(newNode);
            current.setPrev(newNode);
        }
        size++;
        return true;
    }

    @Override
    public boolean add(E toAdd) throws NullPointerException {
        return add(size, toAdd);
    }

    @Override
    public boolean addAll(ListADT<? extends E> toAdd) throws NullPointerException {
        if (toAdd == null) throw new NullPointerException("List cannot be null");
        Iterator<? extends E> it = toAdd.iterator();
        while (it.hasNext()) {
            add(it.next());
        }
        return true;
    }

    @Override
    public E get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Invalid index");
        MyDLLNode<E> curr = head;
        for (int i = 0; i < index; i++) curr = curr.getNext();
        return curr.getElement();
    }

    @Override
    public E remove(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Invalid index");
        E removed;
        if (size == 1) {
            removed = head.getElement();
            head = tail = null;
        } else if (index == 0) {
            removed = head.getElement();
            head = head.getNext();
            head.setPrev(null);
        } else if (index == size - 1) {
            removed = tail.getElement();
            tail = tail.getPrev();
            tail.setNext(null);
        } else {
            MyDLLNode<E> curr = head;
            for (int i = 0; i < index; i++) curr = curr.getNext();
            removed = curr.getElement();
            curr.getPrev().setNext(curr.getNext());
            curr.getNext().setPrev(curr.getPrev());
        }
        size--;
        return removed;
    }

    @Override
    public E remove(E toRemove) throws NullPointerException {
        if (toRemove == null) throw new NullPointerException("Element cannot be null");
        MyDLLNode<E> curr = head;
        int index = 0;
        while (curr != null) {
            if (curr.getElement().equals(toRemove)) {
                return remove(index);
            }
            curr = curr.getNext();
            index++;
        }
        return null;
    }

    @Override
    public E set(int index, E toChange) throws NullPointerException, IndexOutOfBoundsException {
        if (toChange == null) throw new NullPointerException("Element cannot be null");
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Invalid index");
        MyDLLNode<E> curr = head;
        for (int i = 0; i < index; i++) curr = curr.getNext();
        E old = curr.getElement();
        curr.setElement(toChange);
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(E toFind) throws NullPointerException {
        if (toFind == null) throw new NullPointerException("Element cannot be null");
        MyDLLNode<E> curr = head;
        while (curr != null) {
            if (curr.getElement().equals(toFind)) return true;
            curr = curr.getNext();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E[] toArray(E[] toHold) throws NullPointerException {
        if (toHold == null) throw new NullPointerException("Array cannot be null");
        if (toHold.length < size) {
            toHold = (E[]) java.lang.reflect.Array.newInstance(toHold.getClass().getComponentType(), size);
        }
        int i = 0;
        for (MyDLLNode<E> curr = head; curr != null; curr = curr.getNext()) {
            toHold[i++] = curr.getElement();
        }
        if (toHold.length > size) {
            toHold[size] = null;
        }
        return toHold;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (MyDLLNode<E> curr = head; curr != null; curr = curr.getNext()) {
            arr[i++] = curr.getElement();
        }
        return arr;
    }

    @Override
    public Iterator<E> iterator() {
        return new DLLIterator();
    }

    private class DLLIterator implements Iterator<E> {
        private Object[] copy;
        private int pos = 0;

        public DLLIterator() {
            copy = toArray();
        }

        @Override
        public boolean hasNext() {
            return pos < copy.length;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) throw new NoSuchElementException("No more elements in iteration");
            return (E) copy[pos++];
        }
    }
}