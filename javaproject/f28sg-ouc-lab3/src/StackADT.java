
public interface StackADT {
	public boolean isEmpty();
	public int size();
	public void push(int o);
	public int pop() throws StackException;
	public int top() throws StackException;
}
