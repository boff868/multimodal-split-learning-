
public class Sort {

	/*
	 * Part 4: complete method
	 */
	/**
	 * Sorts an array using a priority queue.
	 * 
	 * The effect of calling this method is that the input 'arr' array is updated
	 * in-place, rather than creating a new array holding the sorted value.
	 * 
	 * TODO Where N is the number of elements in the array the complexity is:
	 *
	 * O(n log n)
	 * 
	 * Because: Inserting n elements into the priority queue takes O(n log n), and
	 * extracting n elements also takes O(n log n), leading to an overall O(n log n)
	 * complexity.
	 * 
	 * @param arr the array to be sorted in-place
	 */
	public static void sort(int[] arr) {
		PriorityQueue pq=new PriorityQueue(arr.length);
		for(int i=0;i<arr.length;i++) {
			pq.insert(arr[i]);
		}
		for(int j=0;j<arr.length;j++) {
			arr[j]=pq.removeMin();
		}
	}

	public static void main(String[] args) {
		int[] arr = { 53, 3, 5, 2, 4, 67 };
		Sort.sort(arr);
		// should be printed in order
		
		System.out.println(arr[0]);
		System.out.println(arr[1]);
		System.out.println(arr[2]);
		System.out.println(arr[3]);
		System.out.println(arr[4]);
		System.out.println(arr[5]);
	}

}
