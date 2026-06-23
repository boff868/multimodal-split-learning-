import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TrieTest {

	/*
	 * Part 1: complete
	 */

	Trie trie;

	@Before
	public void setup() {
		trie = new Trie();
	}
	
	/**
	 * Tests for countAllWords() in the Trie class
	 * 
	 * Implement these tests for the first part of Q1
	 */
	/*
	 * this method implements countAllWordsEmptyTest to test an empty trie
	 */
	@Test
	public void countAllWordsEmptyTest() {
		// test countAllWords() for an empty trie
		assertEquals("there are nothing in the trie",0,trie.countAllWords());
	}
	/*
	 * this method implements scountAllWordsTest to test a trie that is not empty 
	 */
	@Test
	public void countAllWordsTest() {
		// step 1: add some words to the trie
		trie.insert("ball");
		// step 2: test countAllWords() for the trie
		assertEquals("the number of words in the trie is 1",1,trie.countAllWords());
	}
	
	/** Returns true if two lists hold exactly the same set of value.
	 * 
	 *  The ordering of the values in the lists are ignored.
	 * @param expected_list The values that should be in the list
	 * @param actual_list The actual values in the list
	 * @return true if the sets are equal
	 */
	private boolean listsEqual(List<String> expected_list, ArrayList<String> actual_list) {
		return (expected_list.size() == actual_list.size() && expected_list.containsAll(actual_list)
				&& actual_list.containsAll(expected_list));
	}
	
	@Test
	public void wordsWithPrefixTestNonEmptyBA() {
		trie.insert("balls");
		trie.insert("balloon");
		trie.insert("ball");
		trie.insert("football");
		ArrayList<String> actual_list = trie.wordsWithPrefix("ba");
		List<String> expected_list = Arrays.asList("balls", "balloon", "ball");
		assertTrue(listsEqual(expected_list, actual_list));
	}
	
	@Test
	public void wordsWithPrefixTestEmptyA() {
		trie.insert("balls");
		trie.insert("balloon");
		trie.insert("ball");
		trie.insert("football");
		ArrayList<String> actual_list = trie.wordsWithPrefix("a");
		List<String> expected_list = Arrays.asList();
		assertTrue(listsEqual(expected_list, actual_list));
	}
	
	@Test
	public void wordsWithPrefixTestEmptyBAN() {
		trie.insert("balls");
		trie.insert("balloon");
		trie.insert("ball");
		trie.insert("football");
		ArrayList<String> actual_list = trie.wordsWithPrefix("ban");
		List<String> expected_list = Arrays.asList();
		assertTrue(listsEqual(expected_list, actual_list));
	}
	
	@Test
	public void wordsWithPrefixTestEmptyNoString() {
		trie.insert("balls");
		trie.insert("balloon");
		trie.insert("ball");
		trie.insert("football");
		ArrayList<String> actual_list = trie.wordsWithPrefix("");
		List<String> expected_list = Arrays.asList("balls","balloon","ball","football");
		assertTrue(listsEqual(expected_list, actual_list));
	}
	
	@Test
	public void wordsWithPrefixTestNonEmptyF() {
		trie.insert("balls");
		trie.insert("balloon");
		trie.insert("ball");
		trie.insert("football");
		ArrayList<String> actual_list = trie.wordsWithPrefix("f");
		List<String> expected_list = Arrays.asList("football");
		assertTrue(listsEqual(expected_list, actual_list));
	}
	

	/*
	 * More trie tests
	 */

	@Test
	public void searchTrue() {
		trie.insert("balls");
		trie.insert("a");
		trie.insert("balloon");
		assertTrue(trie.search("balls"));
		assertTrue(trie.search("balloon"));
		assertTrue(trie.search("a"));
	}

	@Test
	public void searchFalse() {
		trie.insert("balls");
		assertFalse(trie.search("bug"));
	}

	@Test
	public void deleteSearch() {
		trie.insert("balls");
		trie.insert("a");
		trie.insert("balloon");
		trie.delete("a");
		assertTrue(trie.search("balls"));
		assertTrue(trie.search("balloon"));
		assertFalse(trie.search("a"));
		trie.delete("balls");
		assertFalse(trie.search("balls"));
		assertTrue(trie.search("balloon"));
		assertFalse(trie.search("a"));
		trie.delete("balloon");
		assertFalse(trie.search("balls"));
		assertFalse(trie.search("balloon"));
		assertFalse(trie.search("a"));
	}
	
	/*
	 * Optional part: uncomment the four tests below if you wish to test your
	 * implementation.
	 */

//	@Test
//	public void  areWordsWithPrefixTestTrue() {
//		trie.insertString("balls");
//		trie.insertString("balloon");
//		trie.insertString("ball");
//		trie.insertString("football");
//		assertTrue(trie.areWordsWithPrefix("ball"));
//	}

//	@Test
//	public void  areWordsWithPrefixTestFalse() {
//		trie.insertString("balls");
//		trie.insertString("balloon");
//		trie.insertString("ball");
//		trie.insertString("football");
//		assertFalse(trie.areWordsWithPrefix("baboon"));
//	}
	
//	@Test
//	public void wordsWithPrefixTestTrue() {
//		trie.insert("balls");
//		trie.insert("balloon");
//		trie.insert("ball");
//		trie.insert("football");
//		String[] strings = {"ball","balloon","balls"};
//		ArrayList<String> expected = new ArrayList<String>(Arrays.asList(strings));
//		assertEquals(expected, trie.wordsWithPrefix("ba"));
//	}
//	
//	@Test
//	public void wordsWithPrefixTestFalse() {
//		trie.insert("balls");
//		trie.insert("balloon");
//		trie.insert("ball");
//		trie.insert("football");
//		String[] strings = {"ball","balloon","balls"};
//		ArrayList<String> expected = new ArrayList<String>(Arrays.asList(strings));
//		assertNotEquals(expected, trie.wordsWithPrefix("foot"));
//	}

}
