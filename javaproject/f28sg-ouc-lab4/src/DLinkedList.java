class LinkedListException extends RuntimeException {
	public LinkedListException(String err) {
		super(err);
	}
}

public class DLinkedList {

	private class Node {
		private Object value;
		private Node nextNode;
		private Node prevNode;

		public Node(Object v) {
			value = v;
			nextNode = null;
			prevNode = null;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object v) {
			value = v;
		}

		public Node getNextNode() {
			return nextNode;
		}

		public void setNextNode(Node n) {
			nextNode = n;
		}

		public Node getPrevNode() {
			return prevNode;
		}

		public void setPrevNode(Node n) {
			prevNode = n;
		}

	}

	// Holds a reference to the head and tail of the list
	private Node headNode;
	private Node tailNode;

	public DLinkedList() {
		headNode = null;
		tailNode = null;
	}

	public Object getHeadValue() {
		if (headNode == null)
			return null;
		return headNode.value;
	}

	public Object getTailValue() {
		if (tailNode == null)
			return null;
		return tailNode.value;
	}

	public void addAtHead(Object o) {
		Node newNode = new Node(o);
		newNode.setNextNode(headNode);
		if (headNode != null)
			headNode.setPrevNode(newNode);
		headNode = newNode;
		// special case for empty list
		if (tailNode == null)
			tailNode = newNode;
	}

	public void addAtTail(Object o) {
		Node newNode = new Node(o);
		// this means that headNode == null too!
		if (tailNode == null) {
			tailNode = newNode;
			headNode = newNode;
		} else {
			newNode.setPrevNode(tailNode);
			tailNode.setNextNode(newNode);
			tailNode = newNode;
		}
	}

	public Object removeAtHead() throws LinkedListException {
		// list is empty
		if (headNode == null) {
			throw new LinkedListException("List is empty");
		}
		// singleton: must update tailnode too
		if (headNode == tailNode) {
			Object res = headNode.getValue();
			headNode = null;
			tailNode = null;
			return res;
		}

		Object res = headNode.getValue();
		headNode = headNode.getNextNode();
		headNode.setPrevNode(null);
		return res;
	}

	public Object removeAtTail() throws LinkedListException {
		// list is empty
		if (tailNode == null) {
			throw new LinkedListException("List is empty");
		}
		// singleton: must update tailnode too
		if (headNode == tailNode) {
			Object res = tailNode.getValue();
			headNode = null;
			tailNode = null;
			return res;
		}
		Object res = tailNode.getValue();
		tailNode = tailNode.getPrevNode();
		tailNode.setNextNode(null);
		return res;
	}

	/**
	 * @param idx the index position of the value
	 * @return the value in the list at a given index
	 */
	public Object get(int idx) {
		Object value = null;
		int i = 0;
		Node n = headNode;
		while (i <= idx) {
			if (n == null) {
				return -1;
			} else {
				value = n.getValue();
				n = n.getNextNode();
				i++;
			}
		}
		return value;
	}

	// Part 4: complete
	// this method is used to reverse the linked list. First, I set the
	// currentNode=headNode and use it to go through all the list
	// in each iteration, the currentNode exchange the prevNode and the nextNode.
	// Notably, I create the currentPointer to prevent the code from forgetting the
	// original nextNode
	// After the currentNode has traveled through the whole list. All the nextNode
	// and prevNode reference are exchanged. So, I jump out of the while loop.
	// The only remained staff is to exchange the name of headNode and tailNode.
	// Also, To prevent the code from forgetting the real tailNode, I set the
	// pointer temporaryTailNode to memorize the tailNode and then exchange them.
	// the complexity is O(n)
	// because I need to go through all the linked list to change the reference one
	// by one which costs a lot of time
	public void reverse() {
		Node currentNode=headNode;
		while(currentNode!=null) {
			Node temporaryPointer=currentNode.getNextNode();
			currentNode.nextNode=currentNode.getPrevNode();
			currentNode.prevNode=temporaryPointer;
			currentNode=temporaryPointer;
		}
		Node temporaryNode=headNode;
		headNode=tailNode;
		tailNode=temporaryNode;
	}

}
