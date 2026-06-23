public class InsertionSort {
	
	// From the lecture about sorting
	public static void insertionSort(int[] arr) {
		for(int i=0;i<arr.length;i++) {
			int current=arr[i];
			int j=i-1;
			while(j>=0 && arr[j]>current) {
				int currentPointer =arr[j];
				arr[j]=arr[j+1];
				arr[j]=currentPointer;
				j--;
			}
		}
		
	}

	// checks if array is sorted, useful for the unit tests.
	public static boolean isSorted(int[] arr){
		for(int i = 0; i < arr.length-1;i++)
			if(arr[i] > arr[i+1])
				return false;
		return true;
	}
	
	
	/*
	 * Helper printing methods for testing
	 */
	private static void printIntArray(int[] arr){
		System.out.print("[ ");
		for(Integer i : arr){
			System.out.print(i + " ");
		}
		System.out.println(" ]");
	}

	public static void main(String[] args){
		int[] arr1 = {5,4,3,2,1};
		insertionSort(arr1);
		printIntArray(arr1);
	}
	
}
