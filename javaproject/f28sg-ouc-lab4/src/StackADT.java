
public interface StackADT<T> {
	public boolean isEmpty();
	public int size();
	public void push(T e);
	public T pop() throws StackException;
	public T top() throws StackException;
}
