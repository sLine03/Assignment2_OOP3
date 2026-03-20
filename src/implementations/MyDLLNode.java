package implementations;

public class MyDLLNode<E> {
    private E element;
    private MyDLLNode<E> prev;
    private MyDLLNode<E> next;

    public MyDLLNode(E element) {
        this.element = element;
        this.prev = null;
        this.next = null;
    }

    public E getElement() { return element; }
    public void setElement(E element) { this.element = element; }

    public MyDLLNode<E> getPrev() { return prev; }
    public void setPrev(MyDLLNode<E> prev) { this.prev = prev; }

    public MyDLLNode<E> getNext() { return next; }
    public void setNext(MyDLLNode<E> next) { this.next = next; }
}