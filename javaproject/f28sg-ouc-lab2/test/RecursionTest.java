import static org.junit.Assert.*;

import org.junit.Test;

public class RecursionTest {

	@Test
	public void testSum() {
		assertEquals(1,Recursion.sum(1));
		assertEquals(3,Recursion.sum(2));
		assertEquals(6,Recursion.sum(3));
		assertEquals(10,Recursion.sum(4));
	}
	
	@Test
	public void testMultiplyPositive() {
		assertEquals(1,Recursion.multiply(1,1));
		assertEquals(21,Recursion.multiply(7,3));
	}
	
	@Test
	public void testMultiplyOneNegative() {
		assertEquals(-1,Recursion.multiply(-1,1));
		assertEquals(-1,Recursion.multiply(1,-1));
		assertEquals(-21,Recursion.multiply(7,-3));
		assertEquals(-21,Recursion.multiply(-7,3));
	}
	
	@Test
	public void testMultiplyBothNegative() {
		assertEquals(21,Recursion.multiply(-7,-3));
	}
	
	@Test
	public void fibonacciTest() {
		assertEquals(21, Recursion.Fibonacci(8));
	}

}
