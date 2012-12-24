package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

/**
 * JPanel that displays a setup for conjugating verbs. Includes functions for checking answers
 * and displaying correct ones.
 */
public class Conjugation extends JPanel implements ActionListener {

	/** Eclipse auto-generated serial ID */
	private static final long serialVersionUID = 7497640647858167887L;

	/** Color to display for correct answers */
	private static final Color GREEN = new Color(152, 251, 152);

	/** Color to display for incorrect answers */
	private static final Color RED = new Color(242, 167, 167);

	/** Text fields for inputting/displaying answers */
	private final JTextField[] answerFields;

	/** Buttons for performing actions */
	private final JButton check, showAnswers, clear, next;

	/** The instructions for each verb */
	private final List<String> instructions;

	/** 
	 * The correct answers 
	 * answers.size() = instructions.size()
	 * answers are arrays of length pronouns.size() for each verb, where answers are in the 
	 * same order as the pronouns.
	 */
	private final List<String[]> answers;

	/** The display for the instructions */
	private JLabel instructionLabel;

	/** 
	 * The index of the current verb in the instruction list 
	 * 0 <= currentQ < instructions.size()
	 */
	private int currentQ;

	/**
	 * Create a new conjugating panel
	 * @param pronouns the pronouns the verb is to be conjugated for
	 * @param instructions the instructions for each verb
	 * @param answers the correct conjugations
	 * @requires answers are separated into pronouns.size() blocks for each verb, where answers are 
	 * in the same order as the pronouns.
	 * @throws IllegalArgumentException if answers.size() != instructions.size()
	 */
	public Conjugation(String[] pronouns, List<String> instructions, List<String[]> answers) {
		if(pronouns == null || instructions == null || answers == null) {
			throw new IllegalArgumentException("Null parameter to Conjugation constructor");
		}
		if(pronouns.length == 0 || instructions.size() == 0) {
			throw new IllegalArgumentException("Empty parameter to Conjugation constructor");
		}
		if(instructions.size() != answers.size()) {
			throw new IllegalArgumentException("Unmatched number of questions to answers.");
		}

		this.instructions = instructions;
		this.answers = answers;
		currentQ = 0;

		instructionLabel = new JLabel(instructions.get(currentQ));
		JPanel verbPanel = new JPanel();
		verbPanel.add(instructionLabel);
		verbPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		final String[] PRONOUNS = pronouns;
		final int NUM_PRONOUNS = PRONOUNS.length;
		JLabel[] pronounLabels = new JLabel[NUM_PRONOUNS];
		answerFields = new JTextField[NUM_PRONOUNS];

		JPanel conjPanel = new JPanel();
		conjPanel.setLayout(new GridLayout(NUM_PRONOUNS/2, 4, 5, 5));

		for(int i=0; i<NUM_PRONOUNS; i++) {
			pronounLabels[i] = new JLabel(PRONOUNS[i]);
			answerFields[i] = new JTextField(); 
			conjPanel.add(pronounLabels[i]);
			conjPanel.add(answerFields[i]);
		}

		check = new JButton("Check");
		check.addActionListener(this);

		showAnswers = new JButton("Show Answers");
		showAnswers.addActionListener(this);

		clear = new JButton("Clear");
		clear.addActionListener(this);

		next = new JButton("Next");
		next.addActionListener(this);

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(verbPanel)
				.addComponent(conjPanel)
				.addGroup(
						layout.createSequentialGroup()
						.addComponent(check)
						.addComponent(showAnswers)
						.addComponent(clear)
						.addComponent(next)));
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(verbPanel)
				.addComponent(conjPanel)
				.addGroup(
						layout.createParallelGroup()
						.addComponent(check)
						.addComponent(showAnswers)
						.addComponent(clear)
						.addComponent(next)));
	}

	/**
	 * Respond to a button click on the Conjugation panel
	 * check highlights correct answers with green and incorrect answers with red
	 * showAnswers replaces all text fields with the correct values and highlight them with green
	 * clear clears all text fields
	 * next proceeds to the next verb
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Check the answers in the textfields and indicate correctness with color
		if(e.getSource() == check) {
			for(int i=0; i<answers.get(currentQ).length; i++) {
				if(answerFields[i].getText().equalsIgnoreCase(answers.get(currentQ)[i])) {
					answerFields[i].setBackground(GREEN);
				}
				else { 
					answerFields[i].setBackground(RED); 
				}
			}
		}
		// Display the correct answers
		else if(e.getSource() == showAnswers) { 
			for(int i=0; i<answers.get(currentQ).length; i++) {
				answerFields[i].setText(answers.get(currentQ)[i]);
				answerFields[i].setBackground(GREEN);
			}
		}
		// Clear all text fields
		else if(e.getSource() == clear) {
			for(int i=0; i<answers.get(currentQ).length; i++) {
				answerFields[i].setText("");
				answerFields[i].setBackground(Color.WHITE);
			}
		}
		// Move on to the next verb in the list
		else if(e.getSource() == next) { 
			currentQ = (currentQ+1) % instructions.size();
			instructionLabel.setText("Conjugate " + instructions.get(currentQ));
		}
	}
}
//TODO: add answers
//TODO: move control for the next button up a level