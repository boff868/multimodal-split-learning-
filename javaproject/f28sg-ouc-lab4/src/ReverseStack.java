public class ReverseStack {

	/*
	 * 1: complete implementation
	 */
	/**
	 * Reverses the order of elements in the given stack
	 * 
	 * TODO Where N is the number of elements in the stack the complexity is:
	 *
	 * O(N)
	 * 
	 * Because: I need to go through the stack to get all the elements and store
	 * them in the queue. After the stack is empty, I still need to go through the
	 * queue to put all the items into the stack
	 * 
	 * @param st the stack to be reversed
	 */
	public static <T> void reverseStack(Stack<T> st) {
		Queue<T> qe=new Queue<T>(st.size()+1);
		while(!st.isEmpty()) {
		qe.enqueue(st.pop());
		}
		while(!qe.isEmpty()) {
			st.push(qe.dequeue());
		}
		}

}
