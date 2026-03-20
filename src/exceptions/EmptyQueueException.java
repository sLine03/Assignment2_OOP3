package exceptions;

/**
 * Exception thrown when attempting to dequeue or peek an empty queue.
 */
public class EmptyQueueException extends Exception {
    
    /**
     * Constructs an EmptyQueueException with a default message.
     */
    public EmptyQueueException() {
        super("The queue is currently empty.");
    }

    /**
     * Constructs an EmptyQueueException with a specific message.
     * @param message The specific error message.
     */
    public EmptyQueueException(String message) {
        super(message);
    }
}