/**
 * the class implements three methods using the knowledge of the linked list to
 * rewrite the methods in stack
 */
class StackException extends RuntimeException {
	public StackException(String err) {
		super(err);
	}
}

public class Stack implements StackADT {

	private class Node {
		int element;
		Node next;

		public Node(int e, Node n) {
			element = e;
			next = n;
		}

		public int getValue() {
			return element;
		}

		public Node getNext() {
			return next;
		}
	}

	// this is a reference to the head node of the linked list
	private Node top;

	// keep track of the number of elements in the stack
	private int size;

	public Stack() {
		top = null;
		size = 0;
	}

	public boolean isEmpty() {
		return top == null;
	}

	public int size() {
		return size;
	}

	// part 3: complete
	/**
	 * Adds a new element to the stack
	 * 
	 * TODO Where N is the number of elements in the stack the complexity is:
	 *
	 * O(1)
	 * 
	 * Because: no matter what the stack looks like, I just need to push one element
	 * inside without going through the stack, so it is a O(1)
	 * 
	 * @param o the integer to add to the top of the stack
	 */
	public void push(int o) {
		Node newNode=new Node(o,top);
		top=newNode;
		size++;
		}

	// part 3: complete
	/**
	 * Removes an element from the top of the stack
	 * 
	 * TODO Where N is the number of elements in the stack the complexity is:
	 *
	 * O(1)
	 * 
	 * Because: no matter what the stack looks like, I just need to get the value of
	 * the top of the stack, so I don not need to look through the stack, which
	 * means it is an O(1)
	 *
	 * @return the integer that was at the top of the stack
	 * @throws StackException if the stack is empty
	 */
	public int pop() throws StackException {
		if(top==null) {
			throw new StackException("the stack is empty");
		}
		int value=top.getValue();
		top=top.getNext();
		size--;
		return value;
	}

	// part 3: complete
	/**
	 * Returns the integer at the top of the stack
	 * 
	 * TODO Where N is the number of elements in the stack the complexity is:
	 *
	 * O(1)
	 * 
	 * Because: I just need to return the value of the top element without doing
	 * anything to the stack
	 * 
	 * @return the integer at the top of the stack
	 * @throws StackException is the stack is empty
	 */
	public int top() throws StackException {
		if(top==null) {
			throw new StackException("the stack is empty");
		}
		return top.getValue();
	}

}
