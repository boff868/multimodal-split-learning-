
public interface QueueADT<T> {
	public int size();
	public boolean isEmpty();
	public T front() throws QueueException;
	public T dequeue() throws QueueException;
	public void enqueue(T e) throws QueueException;
}
