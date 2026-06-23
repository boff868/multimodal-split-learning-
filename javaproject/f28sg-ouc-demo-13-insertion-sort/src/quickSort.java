import java.util.ArrayList;

public class quickSort {
	private static void printIntArrayList(ArrayList<Integer> arr) {
		System.out.print("[ ");
		for (Integer i : arr) {
			System.out.print(i + " ");
		}
		System.out.println(" ]");
	}

	public static ArrayList<Integer> quickSort(ArrayList<Integer> arr) {
		if(arr.size()<2) {
			return arr;
		}
		int pivot=arr.get(0);
		ArrayList<Integer> L=new ArrayList<Integer>();
		ArrayList<Integer> E=new ArrayList<Integer>();
		ArrayList<Integer> G=new ArrayList<Integer>();
		while(arr.size()!=0) {
			int current=arr.remove(0);
			if(current<pivot) {
				L.add(current);
			}else if(current>pivot) {
				G.add(current);
			}else {
				E.add(current);
			}
		}
		ArrayList<Integer> sortedL=quickSort(L);
		ArrayList<Integer> sortedE=quickSort(E);
		ArrayList<Integer> sortedG=quickSort(G);
		ArrayList<Integer> finalList=new ArrayList<Integer>();
		finalList.addAll(sortedL);
		finalList.addAll(sortedE);
		finalList.addAll(sortedG);
		return finalList;
	}

	public static void main(String[] args) {
		ArrayList<Integer> arr2 = new ArrayList<Integer>();
		arr2.add(3);
		arr2.add(1);
		arr2.add(6);
		arr2.add(5);
		ArrayList<Integer> arr2_sorted = quickSort(arr2);
		printIntArrayList(arr2_sorted);
	}
}
