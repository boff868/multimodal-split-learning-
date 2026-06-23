public class MergeSort {

	public static void mergeSort(int[] arr) {
		if(arr.length<2) {
			return;
		}
		int[] left=new int[arr.length/2];
		int[] right=new int[arr.length-arr.length/2];
		int halfSize=arr.length/2;
		int index=0;
		for(int i=0;i<arr.length;i++) {
			if(i<halfSize) {
				left[index]=arr[i];
				index++;
			}
			if(i>=halfSize) {
				right[index-halfSize]=arr[i];
				index++;
				}
		}
		mergeSort(left);
		mergeSort(right);
		merge(left,right,arr);
	}
	
	// to assist mergeSort
	public static void merge(int[] left, int[] right, int[] arr) {
		int index=0;
		int leftindex=0;
		int rightindex=0;
		while(leftindex<left.length&&rightindex<right.length) {
			if(left[leftindex]<right[rightindex]) {
				arr[index]=left[leftindex];
				index++;
				leftindex++;
			}else {
				arr[index]=right[rightindex];
				index++;
				rightindex++;
			}
		}
		while(leftindex<left.length) {
			arr[index]=left[leftindex];
			index++;
			leftindex++;
		}
		while(rightindex<right.length) {
			arr[index]=right[rightindex];
			index++;
			rightindex++;
		}
	}
	
	// checks if the array is sorted, useful for the unit tests.
	public static boolean isSorted(int[] arr){
		for(int i = 0; i < arr.length-1;i++)
			if(arr[i] > arr[i+1])
				return false;
		return true;
	}
	
}
