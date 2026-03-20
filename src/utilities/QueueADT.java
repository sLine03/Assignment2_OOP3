package utilities;

import exceptions.EmptyQueueException;
import java.io.Serializable;

/**
 * The QueueADT interface represents a First-In-First-Out (FIFO) queue of objects.
 *
 * @param <E> The type of elements held in this queue
 */
public interface QueueADT<E> extends Serializable {
    /**
     * Adds an element to the rear of the queue.
     * @param element the element to add to the rear of the queue.
     * @throws NullPointerException if the specified element is null.
     */
    void enqueue(E element) throws NullPointerException;

    /**
     * Removes and returns the element at the front of the queue.
     * @return the element at the front of the queue.
     * @throws EmptyQueueException if the queue is empty.
     */
    E dequeue() throws EmptyQueueException;

    /**
     * Returns the element at the front of the queue without removing it.
     * @return the element at the front of the queue.
     * @throws EmptyQueueException if the queue is empty.
     */
    E peek() throws EmptyQueueException;

    /**
     * Removes all items from the queue.
     */
    void dequeueAll();

    /**
     * Returns true if this queue is empty.
     * @return true if this queue is empty.
     */
    boolean isEmpty();

    /**
     * Returns an iterator over the elements in this queue in proper sequence.
     * @return an iterator over the elements in this queue in proper FIFO sequence.
     */
    Iterator<E> iterator();

    /**
     * Compares the specified queue with this queue for equality.
     * @param that the queue to be compared for equality with this queue.
     * @return true if the specified queue is equal to this queue.
     */
    boolean equals(QueueADT<E> that);

    /**
     * Returns an array containing all of the elements in this queue in proper sequence.
     * @return an array containing all of the elements in this queue.
     */
    Object[] toArray();

    /**
     * Returns an array containing all of the elements in this queue in proper sequence;
     * the runtime type of the returned array is that of the specified array.
     * @param copyArray the array into which the elements of the queue are to be stored.
     * @return an array containing the elements of the queue.
     * @throws NullPointerException if the specified array is null.
     */
    E[] toArray(E[] copyArray) throws NullPointerException;

    /**
     * Returns true if the queue is at capacity.
     * @return false since dynamically allocated queues are never full.
     */
    boolean isFull();

    /**
     * Returns the number of elements in this queue.
     * @return the number of elements in this queue.
     */
    int size();

    /**
     * Returns true if this queue contains the specified element.
     * @param element element whose presence in this queue is to be tested.
     * @return true if this queue contains the specified element.
     * @throws NullPointerException if the specified element is null.
     */
    boolean contains(E element) throws NullPointerException;

    /**
     * Returns the 1-based position where an object is in this queue.
     * @param element the desired object.
     * @return the 1-based position from the front of the queue where the object is located, or -1 if not found.
     */
    int search(E element);
}