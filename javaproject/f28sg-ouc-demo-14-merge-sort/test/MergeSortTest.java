import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class MergeSortTest {

	@Test
	public void testMergeSortEmpty() {
		int[] arr = {};
		MergeSort.mergeSort(arr);
		assertTrue(arr.length == 0);
	}
	
	@Test
	public void testMergeSortRandom() {
		int[] arr = {5,1,4,2,8};
		MergeSort.mergeSort(arr);
		assertTrue("length incorrect",arr.length == 5);
		assertTrue("not sorted",MergeSort.isSorted(arr));
	}
	
	@Test
	public void testMergeSortOrdered() {
		int[] arr = {1,2,3,4};
		MergeSort.mergeSort(arr);
		assertTrue("length incorrect",arr.length == 4);
		assertTrue("not sorted",MergeSort.isSorted(arr));		
	}
	
}
