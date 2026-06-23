import java.util.ArrayList;
import java.util.Iterator;

public class Trie implements TrieADT {

	private TrieNode rootNode = null;
	static private final char ROOT_NODE_CHAR = '*'; // A dummy char to represent the Root Node

	private class TrieNode { // This is the class that represents a node in the trie
		private char value; // the character contained at this node
		private TrieNode[] subnodes; // the subtrees that stem from this node
		private boolean isValidEnd = false; // We need to know if this node represents the end point of a a valid word

		/**
		 * Constructor for a trie node. It initialises 26 children for A-Z child nodes.
		 * 
		 * @param c the character to exist inside the node
		 */
		public TrieNode(char c) {
			value = c;
			isValidEnd = false;
			subnodes = new TrieNode[26];
			for (int i = 0; i < 26; i++) { // initialise each node to null. 26 nodes for 26 letters
				subnodes[i] = null;
			}
		}

		/**
		 * Inserts a word into a trie
		 * 
		 * @param s the word being inserted into the trie
		 */
		public void insert(String s) {
			int positionOfNextNode = ((int) s.codePointAt(0)) - 97; // 97 is 'a' in ASCII //Where is this character
			// based in the array
			if (subnodes[positionOfNextNode] == null) { // add a new node for this value
				subnodes[positionOfNextNode] = new TrieNode(s.charAt(0));
			}
			if (s.length() == 1) // if this is the last character, and we don't need to add a node, then set the
				// end point to be valid
				subnodes[positionOfNextNode].isValidEnd = true;
			else
				subnodes[positionOfNextNode].insert(s.substring(1)); // add the substring from 1 on to that node
		}

		/**
		 * Removes a word from a trie
		 * 
		 * @param s the word to remove
		 * @return a trie node to return to a parent caller of this method
		 */
		public TrieNode delete(String s) {
			// similar to deleting a linked list, we rebuild the Trie as we return.
			if (s.length() == 0) { // this is the last char
				isValidEnd = false;
			} else {
				int positionOfNextNode = ((int) s.codePointAt(0)) - 97; // 97 is 'a' in ASCII
				if (subnodes[positionOfNextNode] == null) {
					return this; // we don't have the word at all
				} else { // there are still more characters
					subnodes[positionOfNextNode] = subnodes[positionOfNextNode].delete(s.substring(1));
				}
			}
			// As a final step. can we delete the nodes
			// remember. we can only delete a node at this point if it is not a valid end
			// point and it has no subtrees
			// otherwise we need to leave it alone.
			if (!isValidEnd) {
				boolean canDeleteNode = true;
				for (int i = 0; i < subnodes.length; i++) {
					if (subnodes[i] != null)
						canDeleteNode = false;
				}

				// if we can remove this node then return false
				if (canDeleteNode)
					return null;
				else
					return this;
			}
			return this;
		}

		/**
		 * Searches for a word in a trie
		 * 
		 * @param s the word being searched for
		 * @return true if the word is in the trie, false otherwise
		 */
		public boolean search(String s) {
			int positionOfNextNode = ((int) s.codePointAt(0)) - 97; // 97 is 'a' in ASCII
			if (subnodes[positionOfNextNode] == null) {
				return false; // we don't have the word
			} else { // there are still more characters
				if (s.length() == 1) {
					return subnodes[positionOfNextNode].isValidEnd;
				} else {
					return subnodes[positionOfNextNode].search(s.substring(1));
				}
			}
		}

		/**
		 * Extracts the words from the trie
		 * 
		 * @return a list of words (Strings)
		 */
		public ArrayList<String> returnAllWords() {
			ArrayList<String> al = new ArrayList<>();
			// if this is the root node, then we don't want to add that character on
			String prefixString = "";
			if (value == ROOT_NODE_CHAR) {
				prefixString = "";
			} else {
				prefixString = "" + value;
			}
			if (isValidEnd) { // if this is a valid end point we need to add the char we store as a string
				al.add(prefixString);
			}

			// Find all the substrings and add them on
			for (int i = 0; i < subnodes.length; i++) {
				if (subnodes[i] != null) {
					// there be substrings
					ArrayList<String> tempAL = subnodes[i].returnAllWords();
					Iterator<String> it = tempAL.iterator();
					while (it.hasNext()) {
						al.add(prefixString + it.next()); // add our prefix onto each suffix
					}
				}
			}
			return al;
		}

		/**
		 * Returns all suffixes of a prefix
		 * 
		 * E.g. if a Trie includes "banana", "ban" and "forest" then
		 * 
		 * suffixesOfPrefix("ba")
		 * 
		 * should return a list containing strings "nana" and "n".
		 *
		 * @param s The prefix
		 * @return the list of suffixes of the given prefix
		 */
		/**
		 * Retrieves all suffixes corresponding to a given prefix in the trie.
		 * 
		 * @param s the prefix string to query (assumed to contain only lowercase
		 *          letters 'a'-'z')
		 * @return an ArrayList of all suffixes matching the prefix; returns an empty
		 *         list if the prefix does not exist
		 * 
		 *         the complexity is O(N) because I need to go through the arraylist to
		 *         give a value to each node
		 */
		public ArrayList<String> suffixesOfPrefix(String s) {
			ArrayList<String> suffixes = new ArrayList<>();

			// Base case: when the prefix is fully matched (s is empty)
			if (s.isEmpty()) {
				// If current node is the end of a valid word, add empty string as a suffix
				if (isValidEnd) {
					suffixes.add("");
				}
				// Recursively collect suffixes from all child nodes
				for (int i = 0; i < subnodes.length; i++) {
					TrieNode child = subnodes[i];
					if (child != null) {
						char currentChar = (char) ('a' + i); // Convert index to corresponding character
						// Get suffixes from child and prepend current character
						for (String subSuffix : child.suffixesOfPrefix("")) {
							suffixes.add(currentChar + subSuffix);
						}
					}
				}
				return suffixes;
			}

			// Recursive case: continue matching the next character in the prefix
			char firstChar = s.charAt(0);
			int index = firstChar - 'a'; // Calculate index in subnodes array
			// If the next character in prefix doesn't exist, return empty list
			if (subnodes[index] == null) {
				return suffixes;
			}
			// Recurse with the remaining part of the prefix (excluding the first character)
			return subnodes[index].suffixesOfPrefix(s.substring(1));
		}

		/**
		 * Counts all the words in the trie
		 * 
		 * Implement this for Q1
		 * 
		 * @return the number of words in the trie
		 */
		/*
		 * this method can count all the words in a trie. First initialize a count to
		 * memorize the number. If meet a valid end, the count should plus one. Then I
		 * use a loop to do this recursively until it meet a null point. The complexity
		 * is O(N) because I need to look through the whole array
		 */
		public int countAllWords() {
			int count=0;
			if(isValidEnd) {
				count+=1;
			}
			for(int i=0;i<subnodes.length;i++) {
				if(subnodes[i]!=null) {
					return subnodes[i].countAllWords();
				}
			}
			return count;
			}
	}

	/**
	 * Prints all of the words in the Trie to the console
	 */
	public void printAllWords() {
		if (rootNode == null) {
			return;
		} else {
			ArrayList<String> al = rootNode.returnAllWords();

			Iterator<String> it = al.iterator();
			while (it.hasNext()) {
				System.out.println(it.next());
			}
		}
	}

	/**
	 * Returns all of the words in the Trie as a list
	 */
	public ArrayList<String> allWords() {
		if (rootNode == null) {
			return new ArrayList<String>();
		} else {
			return rootNode.returnAllWords();
		}
	}

	/**
	 * returns all words in the Trie with a given prefix. If the prefix is the empty
	 * string, this method should return all words in the Trie.
	 *
	 * @param prefix the prefix of all words to be returned
	 * @return the words that have the given prefix
	 */
	public ArrayList<String> wordsWithPrefix(String prefix) {
		if (rootNode == null) {
			return new ArrayList<String>();
		} else if (prefix.isEmpty()) {
			return rootNode.returnAllWords();
		} else {
			ArrayList<String> suffixes = rootNode.suffixesOfPrefix(prefix);
			Iterator<String> it = suffixes.iterator();
			ArrayList<String> words = new ArrayList<String>();
			while (it.hasNext()) {
				words.add(prefix + it.next());
			}
			return words;
		}
	}

	/**
	 * inserts a word into the trie
	 * 
	 * @param s the word to be inserted
	 */
	public void insert(String s) {
		if (rootNode == null) {
			rootNode = new TrieNode(ROOT_NODE_CHAR);
		}
		rootNode.insert(s.toLowerCase());
	}

	/**
	 * search for a word in the trie
	 * 
	 * @param s the word being searched for
	 * @return true if word is in thrie, false otherwise
	 */
	public boolean search(String s) {
		return rootNode.search(s.toLowerCase());
	}

	/**
	 * delete a word from the trie
	 * 
	 * @param s the word to be deleted
	 */
	public void delete(String s) {
		rootNode.delete(s.toLowerCase());
	}

	/**
	 * Count all the words in a trie
	 * 
	 * This is part of Question 1
	 * 
	 * @return the count of words in the trie
	 */
	public int countAllWords() {
		if (rootNode == null) {
			return 0;
		} else {
			return rootNode.countAllWords();
		}
	}

	public static void main(String[] args) {
		Trie t = new Trie();
		t.insert("hello");
		t.insert("hell");
		t.insert("zebra");
		t.printAllWords();
	}
}
