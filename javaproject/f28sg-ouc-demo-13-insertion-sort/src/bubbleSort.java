
public class bubbleSort {
	private static void printIntArray(int[] arr) {
		System.out.print("[ ");
		for (Integer i : arr) {
			System.out.print(i + " ");
		}
		System.out.println(" ]");
	}

	public static void bubbleSort(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length - 1 - i; j++) {
				if (arr[j] > arr[j + 1]) {
					int currentPointer = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = currentPointer;
				}
			}
		}
	}

	public static void main(String[] args) {
		int[] arr1 = { 5, 4, 3, 2, 1 };
		bubbleSort(arr1);
		printIntArray(arr1);
	}
}
