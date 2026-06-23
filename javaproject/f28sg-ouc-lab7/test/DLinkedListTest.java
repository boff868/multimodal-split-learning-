import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DLinkedListTest {

	DLinkedList dl;

	@Before
	public void setup() {
		dl = new DLinkedList();
	}

	@Test
	public void testIsSortedEmpty() {
		assertTrue(dl.isSorted());
	}

	@Test
	public void testIsSortedTrue() {
		dl.addAtTail(1);
		dl.addAtTail(2);
		dl.addAtTail(3);
		dl.addAtTail(5);
		assertTrue(dl.isSorted());
	}

	@Test
	public void testIsSortedFalse() {
		dl.addAtTail(1);
		dl.addAtTail(2);
		dl.addAtTail(5);
		dl.addAtTail(4);
		assertFalse(dl.isSorted());
	}

	@Test
	public void testSizeEmpty() {
		assertEquals(0, dl.size());
	}

	@Test
	public void testSizeOne() {
		dl.addAtTail(1);
		assertEquals(1, dl.size());
	}

	@Test
	public void testSizeThree() {
		dl.addAtTail(1);
		dl.addAtTail(2);
		dl.addAtTail(5);
		assertEquals(3, dl.size());
	}

	/*
	 * I first create an empty linked list and then sort it. This method aims to
	 * test what happens when sorting an empty linked list
	 */
	@Test
	public void testInsertionSortEmpty() {
		// don't add any values to the linked list dl
		DLinkedList dl = new DLinkedList();
		// now call the insertionSort() method on dl
		dl.insertionSort();
		// test the size of the empty linked list
		assertEquals("the size of the linked list is 0", 0, dl.size());

		// test that the list is sorted
		assertTrue("chech whether the linked list is sorted", dl.isSorted());
	}

	/*
	 * I first create an empty linked list and then give ordered numbers to it. This
	 * method aims to test what happens when sorting an ordered linked list
	 */
	@Test
	public void testInsertionSortOrdered() {
		// add some numbers to the head and tail of
		// the list dl, such that the list is ordered
		DLinkedList dl = new DLinkedList();
		dl.addAtHead(1);
		dl.addAtTail(2);
		dl.addAtTail(3);
		// now call the insertionSort() method on dl
		dl.insertionSort();
		// test the size of the linked list
		assertEquals("the size of the linked list is 3", 3, dl.size());

		// test that the list is sorted
		assertTrue("chech whether the linked list is sorted", dl.isSorted());
	}

	/*
	 * I first create an empty linked list and then give random numbers to it. This
	 * method aims to test what happens when sorting a non-empty linked list
	 */
	@Test
	public void testInsertionSortRandom() {
		// add some numbers to the head and tail of
		// the list dl, such that the list is unordered
		DLinkedList dl = new DLinkedList();
		dl.addAtHead(3);
		dl.addAtTail(1);
		dl.addAtTail(2);
		// now call the insertionSort() method on dl
		dl.insertionSort();
		// test the size of the linked list
		assertEquals("the size of the linked list is 3", 3, dl.size());

		// test that the list is sorted
		assertTrue("chech whether the linked list is sorted", dl.isSorted());
	}

}
