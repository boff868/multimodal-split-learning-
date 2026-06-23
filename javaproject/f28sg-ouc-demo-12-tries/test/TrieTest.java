import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TrieTest {

	Trie trie;

	@Before
	public void setup() {
		trie = new Trie();
	}

	@Test
	// write a test where areWordsWithPrefix returns true.
	public void areWordsWithPrefixTestTrue() {
	trie.insert("asd");
	trie.insert("a");
	trie.areWordsWithPrefix("a");
	}

	@Test
	// write a test where areWordsWithPrefix returns false.
	public void areWordsWithPrefixTestFalse() {
		trie.insert("asd");
		trie.insert("b");
		trie.areWordsWithPrefix("a");
	}

	@Test
	// write a test where containsString returns true for a non-empty trie.
	public void containsWordTrue() {
		// fail("not implemented yet");
		Trie myDictionary = new Trie();
		myDictionary.insert("rob");
		myDictionary.insert("laura");
		assertTrue(myDictionary.search("rob"));
	}

	@Test
	// write a test where containsString returns false for a non-empty trie.
	public void containsWordFalse() {
		Trie myDictionary = new Trie();
		myDictionary.insert("ball");
		assertFalse(myDictionary.search("apple"));
		
		myDictionary.printAllWords();
	}

}
