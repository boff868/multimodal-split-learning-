import java.util.ArrayList;
import java.util.Iterator;

public class ArraySort {

	/**
	 * Insertion sort of an array
	 * 
	 * @param arr the array to be sorted in-place
	 */
	public static void insertionSort(int[] arr) {
		for(int i=0;i<arr.length;i++) {
			for(int j=0;j<=i-1;j++) {
				if(arr[j]>arr[j+1]) {
					int tPointer=arr[j];
					arr[j]=arr[j+1];
					arr[j+1]=tPointer;
				}
			}
		}
	}

	/**
	 * Insertion sort of an array
	 * 
	 * This is Question 4
	 * 
	 * TODO Where N is the number of elements in the array 'arr' the complexity is:
	 *
	 * O(n^2)
	 * 
	 * Because: the while loop will run for n times, inside the loop there is a for
	 * loop while will also runs n times. So, the whole running time is O(n^2)
	 * 
	 * @param arr the array to be sorted in-place
	 */
	public static void bubbleSort(int[] arr) {
		for(int i=0;i<arr.length;i++) {
		for(int j=0;j<arr.length-i-1;j++) {
			int tPointer=arr[j];
			if(arr[j]>arr[j+1]) {
				arr[j]=arr[j+1];
				arr[j+1]=tPointer;
			}
		}
		}
	}

	/**
	 * Quick sort of an array. This method creates a new array with its values
	 * sorted, based on the values in the unsorted input array S.
	 * 
	 * This is Question 6
	 * 
	 * TODO Where N is the number of elements in the array 'S' the complexity is:
	 *
	 * O(n log n)
	 * 
	 * Because: On average, the pivot splits the list into two roughly equal parts,
	 * leading to O(log n) recursive levels, with each level processing O(n)
	 * elements. In the worst case, each split creates a sublist of size n-1,
	 * leading to O(n) recursive levels and O(n²) total operations.
	 *
	 * 
	 * @param S the unsorted input array
	 * @return the sorted output array
	 */
	public static ArrayList<Integer> quickSort(ArrayList<Integer> S) {
		if (S.size() <= 1) {
			return S;
		}
		//set a pivot to become the basic point to compare
		int pivot = S.get(0);
		//create three arraylists to store elements
		ArrayList<Integer> L = new ArrayList<Integer>();
		ArrayList<Integer> E = new ArrayList<Integer>();
		ArrayList<Integer> G = new ArrayList<Integer>();
		//put B elements into three arraylists
		while (S.size() != 0) {
			int current = S.remove(0); // Get and remove the first element
			if (current < pivot) {
				L.add(current);
			} else if (current > pivot) {
				G.add(current);
			} else {
				E.add(current);
			}
		}
		//recursive
		ArrayList sortedL = quickSort(L);
		ArrayList sortedG = quickSort(G);
		ArrayList finalList = new ArrayList<Integer>();
		//put everything in S
		finalList.addAll(sortedL);
		finalList.addAll(E);
		finalList.addAll(sortedG);
		return finalList;
	}

	/**
	 * predicate to check if array is sorted
	 * 
	 * @param arr the array to be checked
	 * @return true if the array is sorted, false otherwise
	 */
	public static boolean isSorted(int[] arr) {
		for (int i = 0; i < arr.length - 1; i++)
			if (arr[i] > arr[i + 1])
				return false;
		return true;
	}

	/**
	 * predicate to check if arrayList is sorted. Useful for checking
	 * ArrayList<Integer> lists returned from Quick Sort.
	 * 
	 * @param arr the array to be checked
	 * @return true is the aray is sorted, flalse otherwise
	 */
	public static boolean isSorted(ArrayList<Integer> arr) {
		Iterator i = arr.iterator();
		int val;
		if (i.hasNext())
			val = (int) i.next();
		else
			return true;
		while (i.hasNext()) {
			int nv = (int) i.next();
			if (val > nv)
				return false;
			val = nv;
		}
		return true;
	}

	/**
	 * Helper printing methods for testing
	 * 
	 * @param arr the array to print
	 */
	private static void printIntArray(int[] arr) {
		System.out.print("[ ");
		for (Integer i : arr) {
			System.out.print(i + " ");
		}
		System.out.println(" ]");
	}

	private static void printIntArrayList(ArrayList<Integer> arr) {
		System.out.print("[ ");
		for (Integer i : arr) {
			System.out.print(i + " ");
		}
		System.out.println(" ]");
	}

	public static void main(String[] args) {
		// testing part1
		int[] arr1 = { 5, 4, 3, 2, 1 };
		bubbleSort(arr1);
		printIntArray(arr1);

		// testing part2
		ArrayList<Integer> arr2 = new ArrayList<Integer>();
		arr2.add(3);
		arr2.add(1);
		arr2.add(6);
		arr2.add(5);
		ArrayList<Integer> arr2_sorted = quickSort(arr2);
		printIntArrayList(arr2_sorted);
		// {5,4,3,5,1};

	}

}
