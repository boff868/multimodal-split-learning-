import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class InsertionSortTest {
	
	@Test
	public void testInsertionSortEmpty() {
		int[] arr = {};
		InsertionSort.insertionSort(arr);
		assertTrue(arr.length == 0);
	}
	
	@Test
	public void testInsertionSortOrdered() {
		int[] arr = {1,2,3,4};
		InsertionSort.insertionSort(arr);
		assertTrue(arr.length == 4);
		int[] expected = {1,2,3,4};
		assertArrayEquals(arr,expected);		
	}
	
	@Test
	public void testInsertionSortRandom() {
		int[] arr = {5,1,4,2,8};
		InsertionSort.insertionSort(arr);
		assertTrue(arr.length == 5);
		int[] expected = {1,2,4,5,8};
		assertArrayEquals(arr,expected);
	}
}
