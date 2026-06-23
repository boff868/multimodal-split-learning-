class StackException extends RuntimeException{    
	public StackException(String err) {
		super(err);
	}
}

public class Stack implements StackADT {
	private Object[] S;
	private int currentIdx;
	private int capacity;
	
	private static int DEFAULT_SIZE = 100;
	
	public Stack(int maxCapacity){
		capacity = maxCapacity;
		S = new Object[maxCapacity];
		currentIdx = -1;
	}

	public Stack(){
		this(DEFAULT_SIZE);
	}
	
	public boolean isEmpty(){
		return currentIdx < 0;
	}
	
	public int size(){
		return currentIdx+1; 
	}
	

	public void push(Object e){
		if(size() == capacity)
			throw new StackException("stack is full");
		S[++currentIdx] = e;
	}
	
	public Object pop() throws StackException{
		if(isEmpty())
			throw new StackException("stack is empty");
		return S[currentIdx--];
	}
	
	public Object top() throws StackException{
		if(isEmpty())
			throw new StackException("stack is empty");
		return S[currentIdx];
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer("[");
		if(size() > 0)
			buf.append(S[0]);
		for(int i = 1; i <= currentIdx;i++){
			buf.append(", " + S[i]);
		}
		buf.append("]");
		return buf.toString();
	}
}
