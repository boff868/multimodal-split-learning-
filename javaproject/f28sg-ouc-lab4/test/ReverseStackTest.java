import static org.junit.Assert.*;

import org.junit.Test;


public class ReverseStackTest {

	@Test
	public void reverseStackStringsTest() {
		// The <String> syntax uses generics to say:
		//   "This is a stack of String values"
		Stack<String> st = new Stack<String>(5);
		st.push("A");
		st.push("B");
		st.push("C");
		st.push("D");
		st.push("E");
		ReverseStack.reverseStack(st);
		assertEquals("A", st.pop());
		assertEquals("B", st.pop());
		assertEquals("C", st.pop());
		assertEquals("D", st.pop());
		assertEquals("E", st.pop());
		
	}
	
	@Test
	public void reverseStackIntegersTest() {
		// All we changed what the type parameter to <Integer>
		//
		// This generics version of the Stack class
		// is good because it allows code to be reused, there
		// is only a single implementation of Stack for all classes.
		// There is less code to write, test and maintain.
		Stack<Integer> st = new Stack<Integer>(5);
		st.push(1);
		st.push(2);
		st.push(3);
		st.push(4);
		st.push(5);
		ReverseStack.reverseStack(st);
		assertEquals(Integer.valueOf(1), st.pop());
		assertEquals(Integer.valueOf(2), st.pop());
		assertEquals(Integer.valueOf(3), st.pop());
		assertEquals(Integer.valueOf(4), st.pop());
		assertEquals(Integer.valueOf(5), st.pop());
	}

	@Test
	public void reverseStackBooleansTest() {
		Stack<Boolean> st = new Stack<Boolean>(5);
		st.push(true);
		st.push(true);
		st.push(false);
		st.push(false);
		st.push(false);
		ReverseStack.reverseStack(st);
		assertEquals(true, st.pop());
		assertEquals(true, st.pop());
		assertEquals(false, st.pop());
		assertEquals(false, st.pop());
		assertEquals(false, st.pop());
	}

}
