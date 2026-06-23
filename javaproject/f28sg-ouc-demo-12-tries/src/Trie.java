
import java.util.ArrayList;
import java.util.Iterator;

public class Trie implements TrieADT {

	private TrieNode rootNode = null;
	static private final char ROOT_NODE_CHAR = '*'; // A dummy char to represent the Root Node

	// This is the class that represents a node in the trie
	private class TrieNode {
		// the character contained at this node
		private char value;
		// the subtrees that stem from this node
		private TrieNode[] subnodes;
		// We need to know if this node represents the end point of a a valid word
		private boolean isValidEnd = false;

		public TrieNode(char c) {
			value = c;
			isValidEnd = false;
			subnodes = new TrieNode[26];
			// initialise each node to null. 26 nodes for 26 letters
			for (int i = 0; i < 26; i++) {
				subnodes[i] = null;
			}
		}

		/**
		 * Inserts a word into a trie
		 * 
		 * @param s the word being inserted into the trie
		 */
		public void insert(String s) {
			// 97 is 'a' in ASCII //Where is this character based in the array
			int positionOfNextNode = ((int) s.codePointAt(0)) - 97;

			// add a new node for this value
			if (subnodes[positionOfNextNode] == null) {
				// TODO
				subnodes[positionOfNextNode]=new TrieNode(s.charAt(0));
			}

			// if this is the last character, and we don't need to add a node
			// then set the end point to be valid
			if (s.length() == 1) {
				// TODO
				subnodes[positionOfNextNode].isValidEnd=true;
			} else {
				// add the substring from 1 on to that node
				// TODO
				subnodes[positionOfNextNode].insert(s.substring(1));
			}
		}

		/**
		 * Removes a word from a trie
		 * 
		 * @param s the word to remove
		 * @return a trie node to return to a parent caller of this method
		 */
		public TrieNode delete(String s) {
			// similar to deleting a linked list, we rebuild the Trie as we return.

			// this is the last char
			if (s.length() == 0) {
				isValidEnd = false;
			} else {
				// 97 is 'a' in ASCII
				int positionOfNextNode = ((int) s.codePointAt(0)) - 97;

				// we don't have the word at all
				if (subnodes[positionOfNextNode] == null) {
					return this;
				}

				// there are still more characters
				else {
					subnodes[positionOfNextNode] = subnodes[positionOfNextNode].delete(s.substring(1));
				}
			}

			// As a final step. can we delete the nodes
			//
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
			// 97 is 'a' in ASCII
			int positionOfNextNode = ((int) s.codePointAt(0)) - 97;

			// we don't have the word
			if (subnodes[positionOfNextNode]==null) {
				return false;
			}

			// there are still more characters
			else {

				if (s.length() == 1) {
					return subnodes[positionOfNextNode].isValidEnd;
				}

				// keep going recursively on the substring
				else {
					return subnodes[positionOfNextNode].search(s.substring(1));
				}
			}
		}

		// very similar to containsWord method
		public boolean areWordsWithPrefix(String s) {
			// 97 is 'a' in ASCII
			int positionOfNextNode = ((int) s.codePointAt(0)) - 97;

			// check if we don't have the word
			if (subnodes[positionOfNextNode]==null) {
				return false;
			}

			// there are still more characters
			else {
				// are we at the last character of the prefix string?
				if (s.length()==1) {
					return true;
				}

				// we're not at the end of the prefix string
				else {
					return subnodes[positionOfNextNode].areWordsWithPrefix(s.substring(1));
				}
			}
		}

		/**
		 * Extracts the words from the trie
		 * 
		 * @return a list of words (Strings)
		 */
		public ArrayList returnAllWords() {
			ArrayList al = new ArrayList();

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
					ArrayList tempAL = subnodes[i].returnAllWords();
					Iterator it = tempAL.iterator();
					while (it.hasNext()) {
						al.add(prefixString + it.next()); // add our prefix onto each suffix
					}
				}
			}
			return al;
		}

		// for students to complete for Q1 of lab 7
		//
		// similar to returnAllWords (but simpler)
		public int countAllWords() {
			int numberOfStrings = 0;

			// we've found the end of a string
			if (isValidEnd) {
				numberOfStrings+=1;
			}

			// look for more strings going from A-Z through the subnodes
			for (int i = 0; i < subnodes.length; i++) {
				// if there are substrings...
				if (subnodes[i] != null) {
					numberOfStrings+=subnodes[i].countAllWords();
				}
			}
			return numberOfStrings;
		}
	} // End of TrieNode Class

	// Returns true if there are words in the trie with the given prefix string
	public boolean areWordsWithPrefix(String str) {
		if (rootNode == null) {
			return false;
		} else {
			return rootNode.areWordsWithPrefix(str);
		}
	}

	
	public int countAllWords() {
		if (rootNode == null) {
			return 0;
		} else {
			return rootNode.countAllWords();
		}
	}

	/**
	 * Prints all of the words in the Trie to the console
	 */
	public void printAllWords() {
		if (rootNode == null) {
			return;
		} else {
			ArrayList al = rootNode.returnAllWords();

			Iterator it = al.iterator();
			while (it.hasNext()) {
				System.out.println(it.next());
			}
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

	public static void main(String[] str) {
		Trie t = new Trie();
		t.insert("hello");
		t.insert("hell");
		t.insert("zebra");
		t.printAllWords();
	}
}
