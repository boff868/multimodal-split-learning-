class StackException extends RuntimeException{    
	public StackException(String err) {
		super(err);
	}
}

public class Stack implements StackADT, Cloneable {
	private Object[] S;
	private int top;
	private int capacity;
	
	private static int DEFAULT_SIZE = 100000;
	
	public Stack(int size){
		capacity = size;
		S = new Object[size];
		top = -1;
	}

	public Stack(){
		this(DEFAULT_SIZE);
	}
	
	public boolean isEmpty(){
		return top < 0;
	}
	
	public int size(){
		return top+1; 
	}
	

	public void push(Object e){
		if(size() == capacity)
			throw new StackException("stack is full");
		S[++top] = e;
	}
	
	public Object pop() throws StackException{
		if(isEmpty())
			throw new StackException("stack is empty");
		return S[top--];
	}
	
	public Object top() throws StackException{
		if(isEmpty())
			throw new StackException("stack is empty");
		return S[top];
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer("[");
		if(size() > 0)
			buf.append(S[0]);
		for(int i = 1; i <= top;i++){
			buf.append(", " + S[i]);
		}
		buf.append("]");
		return buf.toString();
	}

	public Object clone(){  
	    try{  
	        return super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
	}
}
