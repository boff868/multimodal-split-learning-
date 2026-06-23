
/**
 * this class includes a series of basic methods for the stack which not only finishes this task but also provides a lot of tools for the later work
 * 1. testSizeEmpty():
   Verify that the size of an empty stack is 0. Compare `st.size()` with
0; the test passes if they are equal, otherwise fails. This case
confirms the correct initial state of an empty stack after
initialization.

2. testSizeNonEmpty():
   First, add "A" and "B" to the stack using `st.push(String)`, then
verify if the stack size is 2. If `st.size()` equals 2, the test passes,
indicating the correct size calculation for a non-empty stack.

3. testPopTwo():
   The initialization step is the same as testSizeNonEmpty() (push "A"
and "B"). The first call to `st.pop()` should return the top element
"B", leaving "A" in the stack after popping. The second call to
`st.pop()` returns "A". The test passes if both popped results match
expectations.

4. testTopTwo():
   First, push "A" and call `st.top()` — it should return the top
element "A" (without removing it). Then push "B"; the top element
updates to "B", and `st.top()` returns "B". The test passes if both
retrieved top elements match expectations.

5. testIsEmptyTrue():
   Call `st.isEmpty()` on an empty stack. If it returns `true`, the
`assertTrue` assertion passes, verifying the correct judgment of an
empty stack state.

6. testIsEmptyFalse():
   First, push "A" into the stack (making it non-empty). `st.isEmpty()`
should return `false`, and the `assertFalse` assertion passes, verifying
the correct judgment of a non-empty stack state.

7. testEmptyPop():
   Perform `st.pop()` on an empty stack, which should throw a
`StackException`. The test passes if the actual exception thrown matches
the expected exception annotated in the test method, verifying the
exception handling logic for popping from an empty stack.

8. testFullPush():
   Push "A", "B", and "C" into a stack with a capacity of 2. Since the
first two pushes fill the stack, the third push of "C" should throw a
`StackException`. The test passes if the actual exception thrown matches
the expectation.
 */
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StackTest {

	Stack st;

	@Before
	public void setup() {
		st = new Stack(2);
	}

	/*
	 * Part1: complete the following test methods as specified.
	 */

	@Test
	public void testSizeEmpty() {
		assertEquals("the size is 0",0,st.size());
	}

	@Test
	public void testSizeNonEmpty() {
		st.push(1);
		assertEquals("the size is not empty",1,st.size());
	}

	@Test
	public void testPopTwo() {
		st.push(1);
		st.push(2);
		assertEquals(2,st.pop());
		assertEquals(1,st.pop());
	}

	@Test
	public void testTopTwo() {
		st.push(1);
		assertEquals(1,st.top());
		st.push(2);
		assertEquals(2,st.top());
	}

	@Test
	public void testIsEmptyTrue() {
		assertTrue(st.isEmpty());
	}

	@Test
	public void testIsEmptyFalse() {
		st.push(1);
		assertFalse(st.isEmpty());
	}

	@Test(expected = StackException.class)
	public void testEmptyPop() {
		// try popping from an empty stack
		st.pop();
	}

	@Test(expected = StackException.class)
	public void testFullPush() {
		// try pushing too many elements to the stack
		// (which has a capacity of 2 elements)
		st.push("A");
		st.push("B");
		st.push("C");
	}

}
