/*
 * Queue implementation with a linked list.
 */
public class LQueue {

	private class Node {
		Object element;
		Node next;

		public Node(Object e, Node n) {
			element = e;
			next = n;
		}

		public Node(Object e) {
			element = e;
			next = null;
		}
	}

	private Node head;
	private Node tail;
	private int size;

	public LQueue() {
		head = null;
		tail = null;
		size = 0;
	}

	/*
	 * Part 3: complete the following methods
	 */

	// Part 3: complete
	/**
	 * Returns true if the queue is empty, false otherwise.
	 * 
	 * TODO Where N is the number of elements in the queue the complexity is:
	 *
	 * O(1)
	 * 
	 * Because: I just need to judge the size, no need to look through all the queue
	 */
	public boolean isEmpty() {
		if(size==0) {
			return true;
		}else {
			return false;
		}
	}

	// Part 3: complete
	/**
	 * Returns how many elements are in the queue.
	 * 
	 * TODO Where N is the number of elements in the queue the complexity is:
	 *
	 * O(1)
	 * 
	 * Because: I just need to return size without any calculation
	 */
	public int size() {
		return size;// dummy value
	}

	// Part 3: complete
	/**
	 * Adds a new element to the end of the queue.
	 * 
	 * TODO Where N is the number of elements in the queue the complexity is:
	 *
	 * O(1)
	 * 
	 * Because: I only need to add the node in the tail without looking through the
	 * whole queue
	 */
	public void enqueue(Object o) {
		Node newNode=new Node(o);
		if(size==0) {
			head=tail=newNode;
			size++;
		}else {
		tail.next=newNode;
		tail=newNode;
		size++;
		}
		}
		

	// Part 3: complete
	/**
	 * Removes the element at the front of the queue.
	 * 
	 * TODO Where N is the number of elements in the queue the complexity is:
	 *
	 * O(1)
	 * 
	 * Because: I just need to pop the element in the front out without doing any
	 * calculation to this queue
	 */
	public Object dequeue() throws QueueException {
		if(size==0) {
			throw new QueueException("the queue is empty");
		}
		Object returnedValue=head.element;
		head=head.next;
		size--;
		return returnedValue;
	}

	// Part 3: complete
	/**
	 * Returns the element at the front of the queue without removing it.
	 * 
	 * TODO Where N is the number of elements in the queue the complexity is:
	 *
	 * O(1)
	 * 
	 * Because: I just need to return the element in the front without doing any
	 * calculation to the queue
	 */
	public Object front() throws QueueException {
		if(size==0) {
			throw new QueueException("the queue is empty");
		}
		return head.element;
}
	}
