import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class LinkedListTest {

	LinkedList l;
	
	@Before
	public void setup(){
		l = new LinkedList();
	}
	/*
	 * Part 1: implement these methods
	 */
	@Test
	public void testSizeEmpty() {
		assertEquals(0,l.size());
	}

	@Test
	public void testSizeMany() {
		l.addAtHead(1);
		assertEquals(1,l.size());
	}
	
	@Test
	public void testSizeTwice() {
		l.addAtHead(1);
		assertEquals(1,l.size());
		l.addAtTail(2);
		assertEquals(2,l.size());
	}

	@Test
	public void testTotalEmpty() {
		assertEquals(0,l.total());
	}

	@Test
	public void testTotalMany() {
		l.addAtHead(1);
		l.addAtTail(2);
		assertEquals(3,l.total());
	}
	
	@Test
	public void testTotalTwice() {
		l.addAtHead(1);
		assertEquals(1,l.total());
		l.addAtTail(2);
		assertEquals(3,l.total());
	}
	
	@Test(expected=LinkedListException.class)
	public void testRemoveAtHeadEmpty() {
		l.removeAtHead();
	}
	
	@Test(expected=LinkedListException.class)
	public void testRemoveAtTailEmpty() {
		l.removeAtTail();
	}
	
	/*
	 * Optional part
	 */
	
	@Test
	public void testReverse() {
		l.addAtHead(5);
		l.addAtHead(2);
		l.addAtHead(10);
		l.reverse();
		assertEquals(5, l.removeAtHead());
		assertEquals(2, l.removeAtHead());	
		assertEquals(10, l.removeAtHead());	
	}

}
