import static org.junit.Assert.*;

import org.junit.Test;


public class ReverseTest {
	
	/*
	 *  Tests for part 2
	 */
	
	@Test
	public void testReverse() {
		String[] empty = {};
		String[] one = {"A"};
		String[] three = {"A","B","C"};
		
		String[] rev_empty = {};
		String[] rev_one = {"A"};
		String[] rev_three = {"C","B","A"};
		
		Reverse.reverse(rev_empty);
		Reverse.reverse(rev_one);
		Reverse.reverse(rev_three);
		
		assertArrayEquals(empty,rev_empty);
		assertArrayEquals(one,rev_one);
		assertArrayEquals(three,rev_three);
	}	
}