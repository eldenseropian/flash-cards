package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

/**
 * Creates a ConjugationDriver from a file.
 * 
 * GRAMMAR:
 * 
 * CONJUGATION_FILE := "CONJUGATION" end-of-line CONJUGATION+
 * CONJUGATION      := INSTRUCTION end-of-line PRONOUNS end-of-line ANSWERS end-of-line
 * INSTRUCTION      := "Instruction:" String
 * PRONOUNS         := "Pronouns:" {PRONOUN ","}* PRONOUN
 * PRONOUN          := String
 * ANSWERS          := "Answers:" {ANSWER ","}* ANSWER
 * ANSWER           := String
 */
public class ConjugationParser {

	/** The start of a Conjugation file */
	private static final String HEADER = "CONJUGATION";
	
	/** The start of an instruction line */
	private static final String INSTRUCTION = "Instruction:";
	
	/** The start of a pronouns line */
	private static final String PRONOUNS = "Pronouns:";
	
	/** The start of an answer line */
	private static final String ANSWERS = "Answers:";
	
	/** Delimiter used to separate pronoun and answer list */
	private static final String DELIMITER = ",";
	
	/** Error thrown if file does not match the grammar */
	private static final String FILE_FORMAT_ERROR = "File improperly formatted";
	
	public static ConjugationDriver parseConjugation() {
		List<String[]> pronounList = new ArrayList<String[]>();
		List<String> instructionList = new ArrayList<String>();
		List<String[]> conjugationList = new ArrayList<String[]>();
		try{
			JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
			if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						new FileInputStream(file), "UTF-8"));
				while(in.ready()) {
					if(!in.readLine().equals(HEADER)) {
						throw new IllegalArgumentException("Incorrect header");
					}
					String instruction = in.readLine();
					if(!instruction.startsWith(INSTRUCTION)) {
						throw new IllegalArgumentException(FILE_FORMAT_ERROR);
					}
					instruction = instruction.substring(instruction.indexOf(":") + 1);
					
					String pronounLine = in.readLine();
					if(!pronounLine.startsWith(PRONOUNS)) {
						throw new IllegalArgumentException(FILE_FORMAT_ERROR);
					}
					pronounLine = pronounLine.substring(pronounLine.indexOf(":") + 1);
					String[] pronouns = pronounLine.split(DELIMITER);
					
					String answerLine = in.readLine();
					if(!answerLine.startsWith(ANSWERS)) {
						throw new IllegalArgumentException(FILE_FORMAT_ERROR);
					}
					answerLine = answerLine.substring(answerLine.indexOf(":") + 1);
					String[] answers = answerLine.split(DELIMITER);
					
					pronounList.add(pronouns);
					instructionList.add(instruction);
					conjugationList.add(answers);
				}
				in.close();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Failed to parse file");
		}
		return new ConjugationDriver(pronounList, instructionList, conjugationList);
	}
	
	public static void main(String[] args) {
		parseConjugation();
	}
}