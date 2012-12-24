package gui;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 * Used to demonstrate the Conjugation JPanel
 */
public class ConjugationDriver extends JFrame {
	

	/** Eclipse generated serial ID */
	private static final long serialVersionUID = -5497774309982785817L;

	/**
	 * Create a new driver for the Conjugation panel
	 * @param pronouns the pronouns to conjugate for
	 * @param verb the verb to be conjugated
	 * @param verbList the list of the conjugations of the verb
	 * @requires the order of verbList matches the order of pronouns
	 */
	public ConjugationDriver(String[] pronouns, List<String> verb, List<String[]> verbList) {
		Conjugation conj = new Conjugation(pronouns, verb, verbList);
		add(conj);
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Create and show a Spanish conjugator for the present tense of the verb venir
	 */
	public static void main(String[] args) {
		String[] pronouns = {"Yo", "Nosotros", "Tœ", "Vosotros", "ƒl/Ella/Usted", "Ellos/Ellas/Ustedes"}; 
		String[] verbs = {"vengo", "venimos", "vienes", "ven’s", "viene", "vienen"};
		ArrayList<String[]> verbList = new ArrayList<String[]>();
		verbList.add(verbs);
		ArrayList<String> verb = new ArrayList<String>();
		verb.add("Conjugate venir in the present tense.");
		new ConjugationDriver(pronouns, verb, verbList);
	}
}