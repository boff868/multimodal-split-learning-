import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArraySortTest {

	@Test
	public void testIsSortedEmpty() {
		int[] arr = {};
		assertTrue(ArraySort.isSorted(arr));
	}

	@Test
	public void testIsSortedTrue() {
		int[] arr = { 1, 2, 3, 67, 78 };
		assertTrue(ArraySort.isSorted(arr));
	}

	@Test
	public void testIsSortedFalse() {
		int[] arr = { 1, 2, 7, 5, 3 };
		assertFalse(ArraySort.isSorted(arr));
	}

	@Test
	public void testIsSortedArrayListEmpty() {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		assertTrue(ArraySort.isSorted(arr));
	}

	@Test
	public void testIsSortedArrayListTrue() {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		arr.add(1);
		arr.add(2);
		arr.add(3);
		arr.add(5);
		assertTrue(ArraySort.isSorted(arr));
	}

	@Test
	public void testIsSortedArrayListFalse() {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		arr.add(1);
		arr.add(2);
		arr.add(3);
		arr.add(5);
		arr.add(4);
		assertFalse(ArraySort.isSorted(arr));
	}

	/*
	 * this method tests what happens when insertion sorting an ordered array
	 */
	@Test
	public void testInsertionSortOrdered() {
		int[] arr = { 1, 2, 3, 4 };
		// step 1: sort the array with ArraySort.insertionSort(arr)
		ArraySort.insertionSort(arr);
		// step 2: check the length of the array
		assertEquals("the length of the array is 4", 4, arr.length);

		// step 2: check if the array is sorted
		assertTrue("check whether the array is sorted", ArraySort.isSorted(arr));
	}

	/*
	 * this method tests what happens when insertion sorting a random array
	 */
	@Test
	public void testInsertionSortRandom() {
		int[] arr = { 4, 2, 1, 8 };
		// step 1: sort the array with ArraySort.insertionSort(arr)
		ArraySort.insertionSort(arr);
		// step 2: check the length of the array
		assertEquals("the length of the array is 4", 4, arr.length);
		// step 2: check if the array is sorted
		assertTrue("check whether the array is sorted", ArraySort.isSorted(arr));
	}

	@Test
	public void testBubbleSortEmpty() {
		int[] arr = {};
		ArraySort.bubbleSort(arr);
		assertTrue(arr.length == 0);
	}

	/*
	 * this method tests what happens when bubble sorting a random array
	 */
	@Test
	public void testBubbleSortRandom() {
		int[] arr = { 5, 1, 4, 2, 8 };
		// step 1: sort the array with ArraySort.bubbleSort(arr)
		ArraySort.bubbleSort(arr);
		// step 2: check the length of the array
		assertEquals("the length of the array should be 5", 5, arr.length);

		// step 2: check if the array is sorted
		assertTrue("check whether the array is sorted", ArraySort.isSorted(arr));
	}

	/*
	 * this method tests what happens when bubble sorting an ordered array
	 */
	@Test
	public void testBubbleSortOrdered() {
		int[] arr = { 1, 2, 3, 4 };
		// step 1: sort the array with ArraySort.bubbleSort(arr)
		ArraySort.bubbleSort(arr);
		// step 2: check the length of the array
		assertEquals("the length of the array should be 4", 4, arr.length);

		// step 2: check if the array is sorted
		assertTrue("check whether the array is sorted", ArraySort.isSorted(arr));
	}

	@Test
	public void testQuickSortSorted() {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		arr.add(1);
		arr.add(2);
		arr.add(3);
		arr.add(4);
		arr.add(5);
		ArrayList<Integer> arr_sorted = ArraySort.quickSort(arr);
		List<Integer> expectedList = Arrays.asList(1, 2, 3, 4, 5);
		assertEquals(expectedList, arr_sorted);
	}

	@Test
	public void testQuickSortRandom() {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		arr.add(1);
		arr.add(5);
		arr.add(2);
		arr.add(9);
		arr.add(5);
		ArrayList<Integer> arr_sorted = ArraySort.quickSort(arr);
		List<Integer> expectedList = Arrays.asList(1, 2, 5, 5, 9);
		assertEquals(expectedList, arr_sorted);
	}

	@Test
	public void testQuickSortEmpty() {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		ArrayList<Integer> arr_sorted = ArraySort.quickSort(arr);
		List<Integer> expectedList = Arrays.asList();
		assertEquals(expectedList, arr_sorted);
	}
}
