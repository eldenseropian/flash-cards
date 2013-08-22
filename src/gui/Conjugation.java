package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

/**
 * JPanel that displays a setup for conjugating verbs. Includes functions for
 *     checking answers
 * and displaying correct ones.
 */
public class Conjugation extends JPanel implements ActionListener {

  /** Eclipse auto-generated serial ID */
  private static final long serialVersionUID = 7497640647858167887L;

  /** Error messages */
  private static final String NULL_ERROR =
          "Null parameter to Conjugation constructor";
  private static final String EMPTY_ERROR =
          "Empty parameter to Conjugation constructor";
  private static final String MISSING_QUESTION_OR_ANSWER_ERROR = 
          "Unmatched number of questions to answers.";

  /** Button labels */
  private static final String CHECK_TEXT = "Check";
  private static final String SHOW_ANSWERS_TEXT = "Show Answers";
  private static final String CLEAR_TEXT = "Clear";
  private static final String NEXT_TEXT = "Next";

  private static final String CLEARED_FIELD_TEXT = "";

  /** Color to display for correct answers */
  private static final Color GREEN = new Color(152, 251, 152);

  /** Color to display for incorrect answers */
  private static final Color RED = new Color(242, 167, 167);

  /** Text fields for inputting/displaying answers */
  private final JTextField[] answerFields;

  /** Buttons for performing actions */
  private final JButton check, showAnswers, clear, next;

  /** 
   * The correct answers 
   * answers.length == pronouns.length
   * answers are in the same order as the pronouns.
   */
  private final String[] answers;

  /** The display for the instructions */
  private JLabel instructionLabel;

  /** Spacing for the pronouns and text fields */
  private static final int GRID_LAYOUT_MARGIN = 5;

  /** Two columns of pronouns and two of the text fields */
  private static final int NUM_COLS = 4;

  /**
   * Create a new conjugating panel
   * @param pronouns the pronouns the verb is to be conjugated for
   * @param instructions the instructions for conjugating the verb
   * @param answers the correct conjugations
   * @requires answers are in the same order as the pronouns.
   * @throws IllegalArgumentException if answers.length != pronouns.length or
   *     any parameters are empty or null
   */
  public Conjugation(String[] pronouns, String instructions, String[] answers) {
    if(pronouns == null || instructions == null || answers == null) {
      throw new IllegalArgumentException(NULL_ERROR);
    }
    if(pronouns.length == 0 || instructions.length() == 0) {
      throw new IllegalArgumentException(EMPTY_ERROR);
    }
    if(pronouns.length != answers.length) {
      for(String p: pronouns) {
        System.out.print(p);
      }
      System.out.println();
      for(String a: answers) {
        System.out.print(a);
      }
      System.out.println();
      throw new IllegalArgumentException(MISSING_QUESTION_OR_ANSWER_ERROR);
    }

    this.answers = answers;

    // Create instruction panel at the top
    instructionLabel = new JLabel(instructions);
    JPanel verbPanel = new JPanel();
    verbPanel.add(instructionLabel);
    verbPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

    JLabel[] pronounLabels = new JLabel[pronouns.length];
    answerFields = new JTextField[pronouns.length];

    JPanel conjPanel = new JPanel();
    // pronoun | text field | pronoun | text field
    conjPanel.setLayout(new GridLayout((int) Math.ceil(pronouns.length/2.0),
            NUM_COLS, GRID_LAYOUT_MARGIN, GRID_LAYOUT_MARGIN));

    for(int i=0; i<pronouns.length; i++) {
      pronounLabels[i] = new JLabel(pronouns[i]);
      answerFields[i] = new JTextField(); 
      conjPanel.add(pronounLabels[i]);
      conjPanel.add(answerFields[i]);
    }

    check = new JButton(CHECK_TEXT);
    check.addActionListener(this);

    showAnswers = new JButton(SHOW_ANSWERS_TEXT);
    showAnswers.addActionListener(this);

    clear = new JButton(CLEAR_TEXT);
    clear.addActionListener(this);

    next = new JButton(NEXT_TEXT);
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
   * showAnswers replaces all text fields with the correct values and highlight
   *     them with green
   * clear clears all text fields
   * next proceeds to the next verb
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    // Check the answers in the textfields and indicate correctness with color
    if(e.getSource() == check) {
      for(int i=0; i<answers.length; i++) {
        if(answerFields[i].getText().equalsIgnoreCase(answers[i])) {
          answerFields[i].setBackground(GREEN);
        }
        else { 
          answerFields[i].setBackground(RED); 
        }
      }
    }
    // Display the correct answers
    else if(e.getSource() == showAnswers) { 
      for(int i=0; i<answers.length; i++) {
        answerFields[i].setText(answers[i]);
        answerFields[i].setBackground(GREEN);
      }
    }
    // Clear all text fields
    else if(e.getSource() == clear || e.getSource() == next) {
      clear();
    }
  }

  /** Add an ActionListener to the "Next" button */
  public void addActionListenerToNext(ActionListener listener) {
    next.addActionListener(listener);
  }

  /**
   * Set the text of all text fields to empty and set the color to white
   */
  private void clear() {
    for(int i=0; i<answers.length; i++) {
      answerFields[i].setText(CLEARED_FIELD_TEXT);
      answerFields[i].setBackground(Color.WHITE);
    }
  }
}