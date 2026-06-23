import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PriorityQueueTest {

	/*
	 * You are to add more tests for the insert and removeMin methods
	 * for Priority Queues in Q3 of lab 6. 
	 */

	PriorityQueue pq;

	@Before
	public void setup() {
		pq = new PriorityQueue(20);
	}

	@Test
	public void isEmptyTestTrue() {
		assertTrue(pq.isEmpty());
	}

	@Test
	public void isEmptyTestFalse() {
		pq.insert(1);
		assertFalse(pq.isEmpty());
	}

	@Test(expected = PriorityQueueException.class)
	public void removeMinEmptyTest() {
		pq.removeMin();
	}
}
