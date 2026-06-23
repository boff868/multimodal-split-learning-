import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LQueueTest {

	LQueue q;

	@Before
	public void setup() {
		q = new LQueue();
	}

	/*
	 * 2: complete the following test methods as specified.
	 */

	@Test
	// this method is to test the method isEmpty(), I use the assertTrue so that the
	// empty queue should return true for this test
	public void testIsEmpty() {
		assertTrue(q.isEmpty());
	}

	@Test
	// this method is to test the method isEmpty(), I use the assertTrue so that the
	// queue which is not empty should return false for this test
	public void testIsEmptyFalse() {
		q.enqueue(1);
		assertFalse(q.isEmpty());
	}

	@Test
	// this method is to test the method size(), I have not put anything in the
	// queue so the size should be 0
	public void testSizeEmpty() {
		assertEquals(0,q.size());
	}

	@Test
	// this method is to test the method size(), I have put a number into the queue
	// so the size should be 1
	public void testSizeNonEmpty() {
		q.enqueue(1);
		assertEquals(1,q.size());
	}

	@Test
	// queue is FIFO so when I enqueue 1 inside, the element should be in the front
	public void testEnqueue() {
	q.enqueue(1);
	q.enqueue(2);
	assertEquals(1,q.front());
	}

	@Test
	// queue is FIFO so 1 should be in the front. Afterwards, I dequeue which means
	// 1 need to leave first, so now 2 should be in the front. When dequeue is
	// called for the second time, 2 is popped out so 3 should be in the front
	public void testDequeue() {
		q.enqueue(1);
		q.enqueue(2);
		q.dequeue();
		assertEquals(2,q.front());
	}

	@Test(expected = QueueException.class)
	// I try to use dequeue in the empty queue which means it need to throw an
	// exception
	public void testEmptyDequeue() {
		q.dequeue();
	}

	// I try to use front in the empty queue which means it need to throw an
	// exception
	@Test(expected = QueueException.class)
	public void testEmptyFront() {
		q.front();
		// try to get the front value of an empty queue
	}
}
