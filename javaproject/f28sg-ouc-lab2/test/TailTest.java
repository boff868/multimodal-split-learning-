import static org.junit.Assert.*;

import org.junit.Test;

public class TailTest {

	// uncomment if you've attempted the optional part on tail recursive implementation.
	@Test
	public void testFactorial() {
		assertEquals(1, Tail.factorial(1));
		assertEquals(2, Tail.factorial(2));
		assertEquals(6, Tail.factorial(3));
	}

	// uncomment if you've attempted the optional part on tail recursive implementation.
	@Test
	public void testSum() {
		assertEquals(1,Tail.sum(1));
		assertEquals(3,Tail.sum(2));
		assertEquals(6,Tail.sum(3));
		assertEquals(10,Tail.sum(4));
	}

	// uncomment if you've attempted the optional part on tail recursive implementation.
	@Test
	public void testMultiply() {
		assertEquals(1,Tail.multiply(1,1));
		assertEquals(12,Tail.multiply(3,4));
		assertEquals(21,Tail.multiply(7,3));
	}
}