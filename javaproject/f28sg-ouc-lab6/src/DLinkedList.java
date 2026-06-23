public class DLinkedList<T> {

	private class Node {
		private T value;
		private Node nextNode;
		private Node prevNode;

		public Node(T v) {
			value = v;
			nextNode = null;
			prevNode = null;
		}

		public T getValue() {
			return value;
		}

		public void setValue(T v) {
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
	
	public T getHeadValue(){
		if (headNode == null)
			return null;
		return headNode.getValue();
	}
	
	public T getTailValue(){
		if (tailNode == null)
			return null;
		return tailNode.getValue();
	}
	
	public void addAtHead(T o) {
		Node newNode = new Node(o); 
		newNode.setNextNode(headNode); 
		if (headNode != null)
			headNode.setPrevNode(newNode);
		headNode = newNode; 
		// special case for empty list
		if (tailNode == null)
			tailNode = newNode;
	}

	public void addAtTail(T o) {
		Node newNode = new Node(o);
		// this means that headNode == null too!
		if(tailNode == null){
			tailNode = newNode;
			headNode = newNode;
		}else{
			newNode.setPrevNode(tailNode);
			tailNode.setNextNode(newNode);
			tailNode = newNode;
		}
	}
		
	public T deleteAtHead() {
		// list is empty 
		if(headNode == null){
			headNode = null;
			tailNode = null;
			return null;
		}
		// singleton: must update tailnode too
		if(headNode == tailNode){
			T res = headNode.getValue();
			headNode = null;
			tailNode = null;
			return res;
		}
		
		T res = headNode.getValue();
		headNode = headNode.getNextNode();
		headNode.setPrevNode(null);
		return res;
	}

	public T deleteAtTail() {
		// list is empty 
		if(tailNode == null){
			headNode = null;
			tailNode = null;
			return null;
		}
		// singleton: must update tailnode too
		if(headNode == tailNode){
			T res = tailNode.getValue();
			headNode = null;
			tailNode = null;
			return res;
		}
		T res = tailNode.getValue();
		tailNode = tailNode.getPrevNode();
		tailNode.setNextNode(null);
		return res;
	}
}


