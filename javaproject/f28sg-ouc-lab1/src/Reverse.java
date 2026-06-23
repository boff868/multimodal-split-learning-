
public class Reverse {

	// Part 2
	//
	// TODO Where N is the length of array 'arr' the complexity is:O(N)
	//
	// O(N)
	//
	// Because: the first if judgment is O(1),then the second process is to push
	// all the elements to the stack, which will be an O(n), the third process is to
	// pop all the elements in the stack into the array, which is also an O(N)
	public static void reverse(String[] arr) {
	Stack st=new Stack();
	for(int i=0;i<arr.length;i++) {
		st.push(arr[i]);
	}
	for(int j=0;j<arr.length;j++) {
		arr[j]=(String) st.pop();
	}
	}
	

}
