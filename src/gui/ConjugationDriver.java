//TODO: Replace all strings with constant vars
//TODO: write tests

package gui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Used to demonstrate the Conjugation JPanel
 */
public class ConjugationDriver extends JFrame implements ActionListener {


  /** Eclipse generated serial ID */
  private static final long serialVersionUID = -5497774309982785817L;

  /** 
   * Which verb the user is currently on.
   * 0 <= currentIndex < instructions.size()
   */
  private int currentIndex;

  /** The number of verbs */
  private final int NUM_VERBS;

  /** The panel the Conjugation panels go on; uses CardLayout */
  private final JPanel panel;

  /**
   * Create a new driver for the Conjugation panel
   * @param pronouns the pronouns to conjugate for
   * @param instructions instruction detailing the verb to be conjugated and the
   *     desired tense
   * @param conjugations the list of the conjugations of the verb
   * @requires the order of verbList matches the order of pronouns
   */
  public ConjugationDriver(List<String[]> pronouns, List<String> instructions, 
          List<String[]> conjugations) {
    currentIndex = 0;
    NUM_VERBS = instructions.size();

    panel = new JPanel();
    panel.setLayout(new CardLayout());

    for(int i=0; i<NUM_VERBS; i++) {
      Conjugation conj = new Conjugation(pronouns.get(i), instructions.get(i), 
              conjugations.get(i));
      conj.addActionListenerToNext(this);
      panel.add(conj, ""+i);
    }

    add(panel);
    pack();
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
   * Create and show a Spanish conjugator for the present tense of the verb venir
   */
  public static void main(String[] args) {
    //TODO: Choose file from system
    String[] pronouns = {"Yo", "Nosotros", "Tœ", "Vosotros", "ƒl/Ella/Usted", 
    "Ellos/Ellas/Ustedes"}; 
    String[] pronouns2 = {"Test", "Nosotros", "Tœ", "Vosotros", "ƒl/Ella/Usted", 
    "Ellos/Ellas/Ustedes"};
    String[] venir = {"vengo", "venimos", "vienes", "ven’s", "viene", "vienen"};
    String[] hablar = {"hablo", "hablamos", "hablas", "habl‡is", "habla",
            "hablan"};
    ArrayList<String[]> verbList = new ArrayList<String[]>();
    verbList.add(venir);
    verbList.add(hablar);
    ArrayList<String> verb = new ArrayList<String>();
    verb.add("Conjugate venir in the present tense.");
    verb.add("Conjugate hablar in the present tense.");
    List<String[]> pronounList = new ArrayList<String[]>();
    pronounList.add(pronouns);
    pronounList.add(pronouns2);
    new ConjugationDriver(pronounList, verb, verbList);
  }

  /**
   * Change to the next verb when the user presses the "Next" button.
   * Wraps around.
   */
  @Override
  public void actionPerformed(ActionEvent arg0) {
    currentIndex = (currentIndex + 1) % NUM_VERBS;
    ((CardLayout) panel.getLayout()).show(panel, "" + currentIndex);
    pack();
  }
}