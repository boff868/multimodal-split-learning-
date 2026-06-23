import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AutoCompleteGUI {
	static DefaultListModel<String> listModel;

	public static void main(String[] args) {
		/* create and populate a Trie */
		Trie trie = new Trie();
		trie.insert("banana");
		trie.insert("bread");
		trie.insert("food");
		trie.insert("edinburgh");
		trie.insert("lambda");
		trie.insert("table");
		trie.insert("hiking");
		trie.insert("hungry");
		trie.insert("flowers");
		trie.insert("flown");
		trie.insert("flowing");
		trie.insert("cat");
		trie.insert("detail");
		trie.insert("elephant");
		trie.insert("faith");
		trie.insert("fruits");
		trie.insert("frail");
		trie.insert("marking");
		trie.insert("mark");
		trie.insert("maps");
		trie.insert("maple");

		/* Create the GUI components */
		JFrame frame = new JFrame("Searching a Trie");
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JTextField textInput = new JTextField(10);
		JLabel inputLabel = new JLabel("Type a prefix:");
		JPanel inputPanel = new JPanel();
		inputPanel.add(inputLabel);
		inputPanel.add(textInput);
		textInput.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateSearchList(textInput.getText(), trie);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateSearchList(textInput.getText(), trie);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateSearchList(textInput.getText(), trie);
			}
		});

		/* initially populate the list of words in the Trie */
		listModel = new DefaultListModel<>();
		for (String word : trie.allWords()) {
			listModel.addElement(word);
		}

		JList<String> list = new JList<String>(listModel);

		/* create the Trie search results panel */
		JPanel resultsPanel = new JPanel(new BorderLayout());
		JLabel textAreaTrie = new JLabel("Trie entries");
		resultsPanel.add(textAreaTrie, BorderLayout.NORTH);
		resultsPanel.add(list, BorderLayout.CENTER);

		/* lay out the whole GUI */
		panel.add(inputPanel);
		panel.add(resultsPanel);
		frame.add(panel);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * Updates the JList with results from the Trie search
	 * 
	 * @param input the prefix being searched for
	 * @param trie  the Trie being searched
	 * 
	 *              I just use a for loop to add all the elements because everything
	 *              in the trie.wordsWithPrefix(prefix) is already string. which
	 *              also indicates that the complexity of this method is O(N)
	 */
	static void updateSearchList(String prefix, Trie trie) {
		listModel.removeAllElements();
		// get the element in the arraylist which are string and put them into the
		// listmodel
		for (String word : trie.wordsWithPrefix(prefix)) {
			listModel.addElement(word);
		}

	}
}