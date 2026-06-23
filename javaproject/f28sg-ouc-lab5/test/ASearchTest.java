import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class ASearchTest {

	ASearch as;

	/*
	 * Part 1: complete unit tests
	 */
	@Before
	public void setup() {
		as = new ASearch();
		as.addEntry(new Entry("Andrew", 111));
		as.addEntry(new Entry("Ben", 543));
		as.addEntry(new Entry("Bob", 278));
		as.addEntry(new Entry("Brian", 419));
		;
		as.addEntry(new Entry("Ewen", 321));
		as.addEntry(new Entry("Peter", 123));
		as.addEntry(new Entry("Roger", 222));

	}

	/*
	 * this method test whether the search method works
	 */
	@Test
	public void testLinearSearchOK() {
		assertEquals(111,as.linearSearch("Andrew"));
	}

	/*
	 * this method test whether the search method can search a name that is not in
	 * the list
	 */
	@Test
	public void testLinearSearchFail() {
		assertEquals(-1,as.linearSearch("boff"));
	}

	/*
	 * this method test whether the binary search method works
	 */
	@Test
	public void testBinarySearchOK() {
		assertEquals(111,as.binarySearch("Andrew"));
	}

	/*
	 * this method test whether the binary search can search a name that is not in
	 * the list
	 */
	@Test
	public void testBinarySearchFail() {
		assertEquals(-1,as.linearSearch("boff"));
	}

	@Test
	public void testBinarySearchBen() {
		assertEquals(543, as.binarySearch("Ben"));
	}

	@Test
	public void testBinarySearchBob() {
		assertEquals(278, as.binarySearch("Bob"));
	}

	@Test
	public void testBinarySearchBrian() {
		assertEquals(419, as.binarySearch("Brian"));
	}

}
