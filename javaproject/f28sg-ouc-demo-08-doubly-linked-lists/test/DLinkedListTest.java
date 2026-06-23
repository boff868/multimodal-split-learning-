import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DLinkedListTest {

	DLinkedList dll;
	
	@Before
	public void setup() {
		dll = new DLinkedList();
	}
	
	@Test
	public void testAddAtHead() {
		dll.addAtHead("A");
		assertEquals("A",(String) dll.getHeadValue());
		assertEquals("A",(String) dll.getTailValue());		
		dll.addAtHead("B");
		assertEquals("B",(String) dll.getHeadValue());
		assertEquals("A",(String) dll.getTailValue());
	}

	@Test
	public void testAddAtTail() {
		dll.addAtTail("A");
		assertEquals("A",(String) dll.getHeadValue());
		assertEquals("A",(String) dll.getTailValue());		
		dll.addAtTail("B");
		assertEquals("A",(String) dll.getHeadValue());
		assertEquals("B",(String) dll.getTailValue());
	}

	@Test
	public void testDeleteAtHeadOne() {
		dll.addAtTail("A");
		dll.removeAtHead();
		assertNull(dll.getHeadValue());
		assertNull(dll.getTailValue());
	}

	@Test
	public void testDeleteAtHeadTwo() {	
		dll.addAtHead("A");	
		dll.addAtHead("B");
		
		assertEquals("B",(String) dll.removeAtHead());
		assertEquals("A",(String) dll.removeAtHead());		
	}

	@Test
	public void testDeleteAtTailTwo() {
		dll.addAtHead("B");	
		dll.addAtHead("A");
		assertEquals("A",(String) dll.getHeadValue());
		assertEquals("B",(String) dll.getTailValue());
		assertEquals("B",(String) dll.removeAtTail());
		assertEquals("A",(String) dll.removeAtTail());
		assertNull(dll.getHeadValue());
		assertNull(dll.getTailValue());
		//assertEquals("A",(String) dll.deleteAtTail());	
	}
	
	@Test(expected=LinkedListException.class)
	public void testDeleteAtHeadEmpty() {
		dll.removeAtHead();
	}
	
	@Test(expected=LinkedListException.class)
	public void testDeleteAtTailEmpty() {
		dll.removeAtTail();
	}

}
