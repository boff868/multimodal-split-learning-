import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class StackTest {
	
	Stack st;
	@Before
	public void setup()
	{
		st = new Stack();
	}

	@Test
	public void testPush() {
		assertEquals(0,st.size());
		st.push(10);
		assertEquals(1,st.size());
		assertEquals(10,st.top());
	}
	
	@Test
	public void testPushPopEmpty() {
		st.push(10);
		st.pop();
		assertTrue(st.isEmpty());
	}

	@Test
	public void testTop() {
		st.push(10);
		st.push(20);
		assertEquals(20,st.top());
		st.pop();
		assertEquals(10,st.top());
	}
	
	@Test
	public void testPushTopNotEmpty() {
		st.push(10);
		st.top();
		assertFalse(st.isEmpty());
	}
	
	@Test
	public void testPop() {
		st.push(10);
		st.push(20);
		assertEquals(20,st.pop());
		assertEquals(1,st.size());	
		assertEquals(10,st.pop());
		assertEquals(0,st.size());
	}
	
	@Test(expected = StackException.class)  
	public void testEmptyPop() {  
	  st.pop();
	}
	
	@Test(expected = StackException.class)  
	public void testEmptyTop() {  
	  st.top();
	}
	
}
