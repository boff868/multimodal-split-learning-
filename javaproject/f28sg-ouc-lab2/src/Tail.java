
/**
 * This class contains a series of mathematical operation methods implemented
 * using Tail Recursion. Tail recursion refers to a situation where the
 * recursive call is the last operation in the function. Many
 * compilers/interpreters optimize tail recursion into loop-like structures,
 * thus avoiding stack overflow issues.
 */
public class Tail {

	public static int factorial(int n) {
		return tailFactorial(n, 1);
	}

	// use res to store the result of each iteration, so in recursion, I just need
	// to n*res to update each process. When n==1, I end the recursion.
	private static int tailFactorial(int n, int res) {
		if (n == 1) {
			return res;
		} else {
			return tailFactorial(n - 1, res * n);
		}
	}

	public static int sum(int n) {
		return tailSum(n, 0);
	}

	// optional part
	// use sum to store the result of each iteration, so in recursion, I just need
	// to n+sum to update the process. When n==0, I end the recursion
	private static int tailSum(int n, int sum) {
		if (n == 0) {
			return sum;
		} else {
			return tailSum(n - 1, sum + n);
		}
	}

	public static int multiply(int m, int n) {
		return tailMultiply(m, n, 0);
	}

	// optional part
	// use sum to store the result of each iteration. When n>0, use plus; when n<0,
	// use minus. and when n==0, I end the recursion.
	private static int tailMultiply(int m, int n, int sum) {
		if (n == 0) {
			return sum;
		} else if (n < 0) {
			return tailMultiply(-m, n + 1, sum - m);
		} else {
			return tailMultiply(m, n - 1, sum + m);
		}
	}
}
