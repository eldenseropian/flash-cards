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
 * CONJUGATION      := INSTRUCTION end-of-line PRONOUNS end-of-line ANSWERS
 *                     end-of-line
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

  /** Delimiter used in verb files */
  private static final String LIST_DELIMITER = ",";
  private static final String HEADER_DELIMITER = ":";

  /** Error messages */
  private static final String FILE_FORMAT_ERROR = "File improperly formatted";
  private static final String HEADER_ERROR = "Incorrect header.";
  private static final String PARSE_ERROR = "Failed to parse file";

  /** Records what has been read for help in finding file format errors */
  private static StringBuilder forError;

  /**
   * 
   * @return
   */
  public static ConjugationDriver parseConjugation() {
    JFileChooser fileChooser = new JFileChooser(System.getProperty(
            Constants.USER_DIRECTORY));
    if(fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
      System.exit(0);
    }

    File file = fileChooser.getSelectedFile();
    List<String[]> pronounList = new ArrayList<String[]>();
    List<String> instructionList = new ArrayList<String>();
    List<String[]> conjugationList = new ArrayList<String[]>();
    forError = new StringBuilder();
    BufferedReader in = null;

    try{		
      in = new BufferedReader(new InputStreamReader(new FileInputStream(file),
              Constants.ENCODING));
      if(!in.readLine().equals(HEADER)) {
        throw new IllegalArgumentException(HEADER_ERROR);
      }

      while(in.ready()) {
        String instruction = processNextLine(in.readLine(), INSTRUCTION);

        String pronounLine = processNextLine(in.readLine(), PRONOUNS);
        String[] pronouns;
        if(pronounLine.indexOf(LIST_DELIMITER) != -1) { // pronouns manually in file
          pronouns = pronounLine.split(LIST_DELIMITER);
        }
        else { // default pronouns for specified language
          pronouns = Constants.getPronouns(pronounLine);
        }

        String answerLine = processNextLine(in.readLine(), ANSWERS);
        String[] answers = answerLine.split(LIST_DELIMITER);

        pronounList.add(pronouns);
        instructionList.add(instruction);
        conjugationList.add(answers);
      }
    }
    catch(IOException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(PARSE_ERROR);
    }
    finally {
      try{
        in.close();
      }
      catch(IOException e) {
        throw new RuntimeException();
      }
    }
    return new ConjugationDriver(pronounList, instructionList, conjugationList);
  }

  /**
   * Processes the next line read from the file by appending it to the file
   *   string, asserting the command matches the grammar, and returning the
   *   parameters following the command
   * @param nextLine the line to process
   * @param start what the line should start with
   * @return the arguments of the line
   */
  private static String processNextLine(String nextLine, String start) {
    forError.append(Constants.NEW_LINE + nextLine);
    if(!nextLine.startsWith(start)) {
      throw new IllegalArgumentException(FILE_FORMAT_ERROR + forError);
    }
    return nextLine.substring(nextLine.indexOf(HEADER_DELIMITER) + 1);
  }

  public static void main(String[] args) {
    parseConjugation();
  }
}
//TODO: add back button
//TODO: button order: clear, show answers, check, next
//TODO: make back/next triangles
//TODO: empty answer
//TODO: enter performs check, cntl+arrow keys navigate?
//TODO: auto-create file