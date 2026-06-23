import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LSearchTest {

	LSearch ls;
	/*
	 * Part 1: complete unit tests
	 */

	@Before
	public void setup() {
		ls = new LSearch();
		// doesn't have to be ordered!
		ls.addAtHead(new Entry("Andrew", 111));
		ls.addAtHead(new Entry("Ewen", 321));
		ls.addAtHead(new Entry("Peter", 123));
		ls.addAtHead(new Entry("Roger", 222));

	}

	/*
	 * the method test whether linearSearch works
	 */
	@Test
	public void testLinearSearchOK() {
		assertEquals(111,ls.linearSearch("Andrew"));
	}

	/*
	 * the method test whether linearSearch can find a name that is not in the list
	 */
	@Test
	public void testLinearSearchFail() {
		assertEquals(-1,ls.linearSearch("boff"));
	}

}
