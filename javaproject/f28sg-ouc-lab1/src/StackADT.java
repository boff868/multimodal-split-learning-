
public interface StackADT {
	public boolean isEmpty();
	public int size();
	public void push(Object e);
	public Object pop() throws StackException;
	public Object top() throws StackException;
}
