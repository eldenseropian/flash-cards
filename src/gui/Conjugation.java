package gui;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class Conjugation extends JFrame implements ActionListener {

	private static final long serialVersionUID = -1644384619939394248L;
	private static final Color GREEN = new Color(152, 251, 152);
	private static final Color RED = new Color(242, 167, 167);
	
	private final JTextField[] answerFields;
	private final JButton check, next;
	private final List<String[]> answers;
	private final List<String> verbs;
	
	private JLabel verbLabel;
	private int currentQ;
	
	public Conjugation(String[] pronouns, List<String> verbs, List<String[]> answers) {
		if(pronouns == null || verbs == null || answers == null) {
			throw new IllegalArgumentException("Null parameter to Conjugation constructor");
		}
		if(pronouns.length == 0 || verbs.size() == 0 || answers.size() == 0) {
			throw new IllegalArgumentException("Empty parameter to Conjugation constructor");
		}
		if(verbs.size() != answers.size()) {
			throw new IllegalArgumentException("Unmatched number of questions.");
		}
		
		this.verbs = verbs;
		this.answers = answers;
		currentQ = 0;
		
		verbLabel = new JLabel("Conjugate " + verbs.get(currentQ));
		JPanel verbPanel = new JPanel();
		verbPanel.add(verbLabel);
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
		
		next = new JButton("Next");
		next.addActionListener(this);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(verbPanel)
				.addComponent(conjPanel)
				.addGroup(
					layout.createSequentialGroup()
						.addComponent(check)
						.addComponent(next)));
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(verbPanel)
				.addComponent(conjPanel)
				.addGroup(
					layout.createParallelGroup()
						.addComponent(check)
						.addComponent(next)));

		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		String[] ps = {"Yo", "Nosotros", "Tœ", "Vosotros", "ƒl/Ella/Usted", "Ellos/Ellas/Ustedes"}; 
		String[] as = {"vengo", "venimos", "vienes", "ven’s", "viene", "vienen"};
		ArrayList<String[]> a = new ArrayList<String[]>();
		a.add(as);
		ArrayList<String> v = new ArrayList<String>();
		v.add("venir");
		new Conjugation(ps, v, a);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == check) {
			for(int i=0; i<answers.get(currentQ).length; i++) {
				if(answerFields[i].getText().equalsIgnoreCase(answers.get(currentQ)[i])) {
					answerFields[i].setBackground(GREEN);
				}
				else { answerFields[i].setBackground(RED); }
			}
		}
		else if(e.getSource() == next) {
			currentQ = (currentQ+1)%verbs.size();
			verbLabel.setText("Conjugate " + verbs.get(currentQ));
		}
	}
}