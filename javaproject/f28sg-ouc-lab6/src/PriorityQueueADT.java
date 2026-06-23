
public interface PriorityQueueADT {
	public int size();
	public boolean isEmpty();
	public int min() throws PriorityQueueException;
	public int removeMin() throws PriorityQueueException;
	public void insert(int j);
}
