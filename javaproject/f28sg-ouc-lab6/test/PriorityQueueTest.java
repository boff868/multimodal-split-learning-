import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PriorityQueueTest {

	/*
	 * Part 3: complete
	 */

	PriorityQueue pq;

	@Before
	public void setup() {
		pq = new PriorityQueue(20);
	}

	/*
	 * the method is used to test the smallest the number of the priority queue
	 */
	@Test
	public void insertTestMin() {
		pq.insert(1);
		pq.insert(2);
		pq.insert(3);
		assertEquals(1,pq.min());
	}

	/*
	 * the method is used to test the size of the priority queue
	 */
	@Test
	public void insertTestSize() {
		pq.insert(1);
		pq.insert(2);
		pq.insert(3);
		assertEquals(3,pq.size());
		}

	/*
	 * the method is used to test the smallest number of the priority queue
	 */
	@Test
	public void removeMinTest() {
		pq.insert(1);
		pq.insert(2);
		pq.insert(3);
		assertEquals(1,pq.removeMin());
	}

	@Test(expected = PriorityQueueException.class)
	public void removeMinEmptyTest() {
		pq.removeMin();
	}
}
