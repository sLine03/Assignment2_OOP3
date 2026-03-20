package utilities;

import java.io.Serializable;
import java.util.EmptyStackException;

/**
 * The StackADT interface represents a Last-In-First-Out (LIFO) stack of objects.
 *
 * @param <E> The type of elements held in this stack
 */
public interface StackADT<E> extends Serializable {
    /**
     * Pushes an item onto the top of this stack.
     * @param element the element to be pushed onto this stack.
     * @throws NullPointerException if the element is null.
     */
    void push(E element) throws NullPointerException;

    /**
     * Removes the object at the top of this stack and returns that object as the value of this function.
     * @return The object at the top of this stack.
     * @throws EmptyStackException if this stack is empty.
     */
    E pop() throws EmptyStackException;

    /**
     * Looks at the object at the top of this stack without removing it from the stack.
     * @return the object at the top of this stack.
     * @throws EmptyStackException if this stack is empty.
     */
    E peek() throws EmptyStackException;

    /**
     * Removes all of the elements from this stack.
     */
    void clear();

    /**
     * Tests if this stack is empty.
     * @return true if and only if this stack contains no items; false otherwise.
     */
    boolean isEmpty();

    /**
     * Returns an array containing all of the elements in this stack in proper LIFO sequence.
     * @return an array containing all of the elements in this stack.
     */
    Object[] toArray();

    /**
     * Returns an array containing all of the elements in this stack in proper LIFO sequence;
     * the runtime type of the returned array is that of the specified array.
     * @param copyArray the array into which the elements of the stack are to be stored.
     * @return an array containing the elements of the stack.
     * @throws NullPointerException if the specified array is null.
     */
    E[] toArray(E[] copyArray) throws NullPointerException;

    /**
     * Returns true if this stack contains the specified element.
     * @param element element whose presence in this stack is to be tested.
     * @return true if this stack contains the specified element.
     * @throws NullPointerException if the specified element is null.
     */
    boolean contains(E element) throws NullPointerException;

    /**
     * Returns the 1-based position where an object is on this stack.
     * @param element the desired object.
     * @return the 1-based position from the top of the stack where the object is located; the return value -1 indicates that the object is not on the stack.
     */
    int search(E element);

    /**
     * Returns an iterator over the elements in this stack in proper sequence.
     * @return an iterator over the elements in this stack in proper LIFO sequence.
     */
    Iterator<E> iterator();

    /**
     * Compares the specified stack with this stack for equality.
     * @param that the stack to be compared for equality with this stack.
     * @return true if the specified stack is equal to this stack.
     */
    boolean equals(StackADT<E> that);

    /**
     * Returns the number of elements in this stack.
     * @return the number of elements in this stack.
     */
    int size();

    /**
     * Returns true if the stack is constrained to a max capacity and is full.
     * @return false since dynamic stacks do not overflow.
     */
    boolean stackOverflow();
}