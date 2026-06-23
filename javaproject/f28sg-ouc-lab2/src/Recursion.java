/**
 * this class finishes task 1, all the methods below are using recursion to
 * solve the problem
 */
public class Recursion {

	// Part 1: complete
	// this method sum up 1 to n using recursion. First judge if n==1, this is the
	// stop point during the recursion.
	// it returns sum(n-1) which means it can go again and again until n finally
	// reaches 1
	public static int sum(int n) {
		if(n==1) {
			return 1;
		}
		return n+sum(n-1);
	}

	// Part 1 complete
	// this is a multiply method. basically we add m again and again until the count
	// n finally reaches 1.
	// Notably, if n<0, we should use minus instead of plus
	public static int multiply(int m, int n) {
		if(n==0) {
			return 0;
		}
		if(n<0) {
			return -m+multiply(m,n+1);
		}else {
			return m+multiply(m,n-1);
		}
		
	}

	// Part 1: complete
	// this queue follows the rule that the third item is the result of adding the
	// first two items together
	// I initialize the first two items because they don't follow any rule. Then,
	// use recursion to get the following result.
	public static int Fibonacci(int n) {
	if(n==0) {
		return 0;
	}else
	if(n==1) {
		return 1;
	}else {
		return Fibonacci(n-1)+Fibonacci(n-2);
	}
	}

}