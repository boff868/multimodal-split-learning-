
class PriorityQueueException extends RuntimeException {
	public PriorityQueueException(String err) {
		super(err);
	}
}

public class PriorityQueue implements PriorityQueueADT {

	private int[] heap;
	int last;

	public PriorityQueue(int max) {
		heap = new int[max + 1];
		last = 0;
	}

	/**
	 * @return a count of how many elements are in the priority queue
	 */
	public int size() {
		return last;
	}

	/**
	 * @return true if there are no elements in the priority queue
	 */
	public boolean isEmpty() {
		boolean judgement;
		if(last==0) {
			judgement=true;
		}else {
			return false;
		}
		return judgement;
	}

	/**
	 * @return the smallest value element in the priority queue
	 */
	public int min() {
		return heap[1];
	}

	/**
	 * removes the smallest value element in the queue
	 * 
	 * @return the smallest value element in the priority queue
	 */
	public int removeMin() {
		if(last==0) {
			throw new PriorityQueueException("the priority queue is empty");
		}
		int min=heap[1];
		heap[1]=heap[last];
		last--;
		downHeap();
		return min;
	}

	/**
	 * inserts a new element into the priority queue
	 * 
	 * @param j the value to insert into the priority queue
	 */
	public void insert(int j) {
		if(last==heap.length-1) {
			throw new PriorityQueueException("the priority queue is full");
		}
		last++;
		heap[last]=j;
		upHeap();
	}

	/*
	 * Returns the index of the smaller child for a given parent node
	 */
	private int findMin(int ind) {
		if (ind + 1  > last) {
			// only one child
			return ind;
		} else {
			if (heap[ind*2] <= heap[ind*2 + 1]) {
				return ind*2;
			} else {
				return ind*2 + 1;
			}
		}
	}


	/*
	 * Swaps two values in the heap array at positions 'i' and 'j'
	 */
	private void swap(int i, int j) {
		int tmp = heap[i];
		heap[i] = heap[j];
		heap[j] = tmp;
	}

	private void downHeap() {
		int index=1;
		while(index<=last/2) {
			int minChild=findMin(index);
			if(heap[index]<heap[minChild]) {
				break;
			}
			swap(index,minChild);
			index=minChild;
		}
	}

	private void upHeap() {
		int index=last;
		while(index!=1) {
			int parent=index/2;
			if(heap[index]>heap[parent]) {
			break;
			}
			swap(index,parent);
			index=parent;
		}
	}
	
	public static void main(String[] args) {
		PriorityQueue pq = new PriorityQueue(100);
		pq.insert(4);
		pq.insert(7);
		pq.insert(2);
		System.out.println(pq.removeMin());
		System.out.println(pq.min());
		System.out.println(pq.removeMin());
		System.out.println(pq.removeMin());
	}

}
