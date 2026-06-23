import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BinarySearchTreeTest {

	BinarySearchTree bst;
	
	@Before
	public void setup(){
		bst = new BinarySearchTree();
	}

	@Test
	public void isEmptyZeroNodes() {
		assertTrue(bst.isEmpty());
	}

	@Test
	public void isEmptyOneNode() {
		bst.insert(4);
		assertFalse(bst.isEmpty());
	}
	
	@Test
	public void isEmptyTwoNodes() {
		bst.insert(4);
		bst.insert(5);
		assertFalse(bst.isEmpty());
	}
	
	@Test
	public void isEmptyThreeNodes() {
		bst.insert(4);
		bst.insert(5);
		bst.insert(6);
		assertFalse(bst.isEmpty());
	}
	
	@Test
	public void isSizeZero() {
		assertEquals(0,bst.size());
	}
	
	@Test
	public void isSizeOne() {
		bst.insert(1);
		assertEquals(1,bst.size());
	}
	
	@Test
	public void isSizeTwo() {
		bst.insert(1);
		bst.insert(2);
		assertEquals(2,bst.size());
	}
	
	@Test
	public void isSizeThreeBalanced() {
		bst.insert(2);
		bst.insert(1);
		bst.insert(3);
		assertEquals(3,bst.size());
	}
	
	@Test
	public void isSizeThreeSkewedRight() {
		bst.insert(1);
		bst.insert(2);
		bst.insert(3);
		assertEquals(3,bst.size());
	}
	
	@Test
	public void isSizeThreeSkewedLeft() {
		bst.insert(3);
		bst.insert(2);
		bst.insert(1);
		assertEquals(3,bst.size());
	}
	
	@Test
	public void searchEmptyTest() {
		assertFalse(bst.search(0));
	}
	
	@Test
	public void searchOneTest() {
		bst.insert(5);
		assertTrue(bst.search(5));
	}
	
	@Test
	public void deleteOneTest() {
		bst.insert(5);
		assertTrue(bst.search(5));
		bst.delete(5);
		assertFalse(bst.search(5));
	}
	
	@Test
	public void searchBalancedTest() {
		bst.insert(2);
		bst.insert(3);
		bst.insert(1);
		assertTrue(bst.search(1));
		assertTrue(bst.search(2));
		assertTrue(bst.search(3));
	}
	
	@Test
	public void searchSkewedTest() {
		bst.insert(1);
		bst.insert(2);
		bst.insert(3);
		assertTrue(bst.search(1));
		assertTrue(bst.search(2));
		assertTrue(bst.search(3));
	}
	
	@Test
	public void deleteEmptyTest() {
		bst.delete(0);
		assertEquals(0,bst.size());
		assertTrue(bst.isEmpty());
	}
	
	@Test
	public void deleteLeafTest() {
		bst.insert(3);
		bst.insert(5);
		bst.insert(2);
		bst.insert(1);
		bst.insert(4);
		bst.insert(6);
		bst.delete(6);
		assertEquals(5,bst.size());
	}
	
	@Test
	public void deleteOneChildNodeTest() {
		bst.insert(3);
		bst.insert(5);
		bst.insert(2);
		bst.insert(1);
		bst.insert(4);
		bst.insert(6);
		bst.delete(2);
		assertEquals(5,bst.size());
	}
	
	@Test
	public void deleteTwoChildNodeTest() {
		bst.insert(3);
		bst.insert(5);
		bst.insert(2);
		bst.insert(1);
		bst.insert(4);
		bst.insert(6);
		bst.delete(5);
		assertEquals(5,bst.size());
		assertTrue(bst.search(6));
		assertTrue(bst.search(4));
	}
	
	@Test
	public void deleteRootNodeTest() {
		bst.insert(3);
		bst.insert(5);
		bst.insert(2);
		bst.insert(1);
		bst.insert(4);
		bst.insert(6);
		bst.delete(3);
		assertEquals(5,bst.size());
	}
	

}
