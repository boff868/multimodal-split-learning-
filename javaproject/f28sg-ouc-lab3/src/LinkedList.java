class LinkedListException extends RuntimeException {
	public LinkedListException(String err) {
		super(err);
	}
}

public class LinkedList {

	private class Node {
		private int value;
		private Node nextNode;

		public Node(int i) {
			value = i;
			nextNode = null;
		}

		// returns the value stored in the node
		public int getValue() {
			return value;
		}

		// sets the value stored in the node
		public void setValue(int i) {
			value = i;
		}

		// Returns the Node that this Node links to
		// Note this may return null
		public Node getNextNode() {
			return nextNode;
		}

		// Sets the NextNode to the given Node
		public void setNextNode(Node n) {
			nextNode = n;
		}

		// adds Node n to the tail of the list
		public void addNodeAtTail(Node n) {
			if (this.nextNode == null) {
				this.nextNode = n;
			} else {
				this.nextNode.addNodeAtTail(n);
			}
		}

		public Node removeAtTail(Node valueAtTail) {
			if (this.nextNode == null) { // if this is the last node
				valueAtTail.setValue(this.getValue()); // copy out the value
				return null; // return null
			} else { // move onto the next Node
				this.nextNode = this.nextNode.removeAtTail(valueAtTail);
				return this;
			}

		}
	} // End of Node Class implementation

	private Node headNode; // Holds a reference to the head of the list

	public LinkedList() {
		headNode = null;
	}

	public void addAtHead(int i) {
		Node newNode = new Node(i);
		newNode.setNextNode(headNode);
		headNode = newNode;
	}

	public void addAtTail(int i) {
		Node newNode = new Node(i);
		if (headNode == null) {
			headNode = newNode;
		} else {
			headNode.addNodeAtTail(newNode);
		}
	}

	/*
	 * removes and returns the value at the head of the list
	 */
	public int removeAtHead() throws LinkedListException {
		if (headNode == null) {
			throw new LinkedListException("Cannot remove from the head of an empty linked list");
		} else {
			Node returnedNode = headNode;
			headNode = headNode.getNextNode();
			return returnedNode.getValue();
		}
	}

	/*
	 * See
	 * https://web.microsoftstream.com/video/5a15e714-bf98-4bf6-9124-573638723795
	 */
	public int removeAtTail() throws LinkedListException {
		if (headNode == null) {
			throw new LinkedListException("Cannot remove from the tail of an empty linked list");
		} else {
			Node returnedNode = new Node(-1);
			headNode = headNode.removeAtTail(returnedNode);
			return returnedNode.getValue();
		}
	}

	/*
	 * PART 2: complete the following methods
	 */

	/**
	 * The number of nodes in the linked list, and 0 for an empty linked list.
	 * 
	 * TODO Where N is the length of linked list the complexity is:
	 *
	 * O(N)
	 * 
	 * Because: We need to traverse each node in the linked list exactly once (from
	 * head to tail) to count them. The number of operations (incrementing count and
	 * moving to next node) is directly proportional to the number of nodes N.
	 * 
	 * @return the number of nodes in the linked list
	 */
	public int size() {
		Node currentNode=headNode;
		int count=0;
		while(currentNode!=null) {
			currentNode=currentNode.getNextNode();
			count++;
		}
		return count;
	}

	/**
	 * The sum of all integer nodes in the linked list, and 0 for an empty linked
	 * list.
	 *
	 * TODO Where N is the length of linked list the complexity is:
	 *
	 * O(N)
	 * 
	 * Because: We must traverse each node in the linked list exactly once (from
	 * head to tail) to accumulate their values. The number of operations (adding
	 * the node's value and moving to the next node) is directly proportional to the
	 * number of nodes N.
	 * 
	 * @return the sum of all nodes in the linked list
	 */
	public int total() {
		Node currentNode=headNode;
		int count=0;
		while(currentNode!=null) {
			count+=currentNode.getValue();
			currentNode=currentNode.getNextNode();
		}
		return count;
	}

	/*
	 * Optional: reverse the linked lists so that the first element becomes the
	 * last, the second becomes the second last, and so on. the complexity is
	 * O(N),because I need to use each node of the linked list.
	 */

	public void reverse() {
	Stack st=new Stack();
	Node currentNode=headNode;
	while(currentNode!=null) {
		st.push(currentNode.getValue());
		currentNode=currentNode.getNextNode();
	}
	Node originalNode=headNode;
	while(!st.isEmpty()) {
		originalNode.value=st.pop();
		originalNode=originalNode.getNextNode();
	}
	}

}
