@SuppressWarnings("serial")
class StackException extends RuntimeException{    
	public StackException(String err) {
		super(err);
	}
}

/**
 * An implementation of a Stack class.
 * 
 * It uses an array {@code S} internally to store the stack values.
 * 
 * It uses Java Generics (the {@code <T>} parameter), meaning that
 * the class can be instantiated to store values of any type e.g.
 * 
 * <pre>{@code
 * Stack s1 = new Stack<String>();   // a stack of Strings.
 * Stack s1 = new Stack<Integer>();  // a stack of Integers.
 * }</pre>
 *
 * @param <T> the Class of the values that the stack will store
 */
public class Stack<T> implements StackADT<T> {
	private Object[] S;
	private int top;
	private int capacity;
	
	private static int DEFAULT_SIZE = 100;
	
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
	

	public void push(T e){
		if(size() == capacity)
			throw new StackException("stack is full");
		S[++top] = e;
	}
	
	@SuppressWarnings("unchecked")
	public T pop() throws StackException{
		if(isEmpty())
			throw new StackException("stack is empty");
		// this type cast is safe because we type checked the push method
		return (T) S[top--];
	}
	
	@SuppressWarnings("unchecked")
	public T top() throws StackException{
		if(isEmpty())
			throw new StackException("stack is empty");
		// this type cast is safe because we type checked the push method
		return (T) S[top];
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
}
