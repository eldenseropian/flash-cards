//TODO: Replace all strings with constant vars
//TODO: Use enum
//TODO: Write tests

package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;


public class Driver {

  /** The flash cards to test */
  private static CardList cards;

  /** Where the user interaction takes place */
  private static BufferedReader console;

  /** Choices the user can make when using the console */
  public static enum Options {
    MARK_LEARNED ("L"), QUIT ("Q"), TRY_AGAIN ("T"), SEE_ANSWER ("A"),
    CONTINUE("C"), RESTART ("R"), FLIP ("F"), SWITCH_FILES ("S"), YES ("Y"),
    NO ("N");

    private final String command;

    Options(String command) {
      this.command = command;
    }

    public String toString() {
      return this.command;
    }

    /**
     * Return the Options corresponding to a command.
     * @param command the command to get the Options for
     * @return the Options corresponding to the command.
     * @throws IOException if the command is unknown.
     */
    static Options getOptionFromCommand(String command) throws IOException {
      for (Options option: Options.values()) {
        if (option.toString().equals(command.trim().toUpperCase())) {
          return option;
        }
      }
      throw new IOException(INVALID_COMMAND_ERROR);
    }
  };

  /** String constants in the card input file */
  private static final String SIDE_DELIMITER = "_";
  private static final String QUESTION_HEADER = "QUESTION: ";
  private static final String ANSWER_HEADER = "ANSWER: ";

  /** Messages printed during the interaction */
  private static final String START_QUIZ = "The cards are currently set like" +
          " this:\nFront: %s\nBack: %s\nWould you like to flip them? %s/%s";
  private static final String FINISHED = 
          "Congratulations! You've learned all the cards.";
  private static final String CORRECT = "Right! ";
  private static final String INCORRECT = "Wrong. ";
  private static final String EXIT_MESSAGE = "Program terminated.";
  private static final String CORRECT_ANSWER = "The correct answer is \"%s\"";
  private static final String CORRECT_COMMANDS =
          "'%s' to mark learned, '%s' to quit, %s to continue: ";
  private static final String INCORRECT_COMMANDS = 
          "'%s' to see the answer, '%s' to try again, '%s' to quit: ";
  private static final String FINISHED_COMMANDS = "'%s' to restart, " + 
          "'%s' to flip and restart, '%s' to switch files, '%s' to quit: ";

  /** Error messages */
  private static final String NO_FILE_SELECTED_ERROR =
          "You cannot run this program without a flashcard file.";
  private static final String FILE_CORRUPTED_ERROR = "Card file corrupted.";
  private static final String INVALID_START_ERROR =
          "%s to flip, %s to leave as is.";
  private static final String INVALID_COMMAND_ERROR = "Invalid command.";

  /**
   * Read a card file, shuffle the cards, and begin running the interactive
   *   flashcard program.
   */
  public static void main(String[] args) {
    cards = new CardList();
    readCards();
    cards.shuffle();
    run();
  }

  /**
   * Read in FlashCards from a file following the grammar:
   * FLASHCARD := QUESTION "_" ANSWER end-of-line
   * QUESTION := "[^_]"
   * ANSWER := "[^_]"
   * @throws Exception for invalidly formatted file
   * Terminates if no file selected.
   */
  private static void readCards() {
    cards.clear();
    try {
      JFileChooser fileChooser = new JFileChooser(System.getProperty(
              Constants.USER_DIRECTORY));
      if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file), Constants.ENCODING));
        while(in.ready()) {
          String card = in.readLine();
          String q = card.substring(0, card.indexOf(SIDE_DELIMITER)).trim();
          String a = card.substring(card.indexOf(SIDE_DELIMITER) + 1).trim();
          cards.add(new FlashCard(q,a));
        }
        in.close();
      } else {
        System.err.println(NO_FILE_SELECTED_ERROR);
        quit();
        //TODO: add create new file option here
      }
    }
    //start with no cards
    catch (FileNotFoundException e) {}
    //start with no cards and print an error message
    catch(IOException e) {
      e.printStackTrace();
      throw new RuntimeException(FILE_CORRUPTED_ERROR);
    }
    catch(StringIndexOutOfBoundsException e) {
      e.printStackTrace();
      throw new RuntimeException(FILE_CORRUPTED_ERROR);
    }
  }

  /**
   * Run the interactive flash card program.
   */
  private static void run() {
    console = new BufferedReader(new InputStreamReader(System.in));
    System.out.println(String.format(START_QUIZ, cards.get(0).question(),
            cards.get(0).answer(), Options.YES, Options.NO));
    try {
      String toFlip = console.readLine();
      //TODO: make this function call driven
      while(Options.getOptionFromCommand(toFlip) != Options.YES &&
              Options.getOptionFromCommand(toFlip) != Options.NO) {
        System.out.println(INVALID_COMMAND_ERROR);
        System.out.println(String.format(INVALID_START_ERROR, Options.YES, 
                Options.NO));
        toFlip = console.readLine();
      }
      if(toFlip.equalsIgnoreCase(Options.YES.toString())) {
        cards.flip();
      }
    }
    catch(IOException e) {
      throw new RuntimeException(e.toString());
    }

    int i = 0;
    while(cards.numNotLearned() > 0) {
      if(!askQuestion(cards.get(i))) { i++; }
      if(i >= cards.numNotLearned() && cards.numNotLearned() > 0) {
        i %= cards.numNotLearned();
        cards.shuffle();
      }
    }
    System.out.println(FINISHED);
    switch (finishedMenu()) {
      case QUIT:
        quit();
        break;
      case SWITCH_FILES:
        readCards();
        break;
      case FLIP:
        cards.flip();
        break;
      default:
        throw new RuntimeException(INVALID_COMMAND_ERROR);
    }
    cards.reset();
    clearScreen();
    run();
  }

  /**
   * Display a question and respond to the user's input
   * @param card the FlashCard containing the question to be asked
   * @return true if a card has been learned
   */
  private static boolean askQuestion(FlashCard card) {
    System.out.println(Constants.NEW_LINE + QUESTION_HEADER + card.question());
    System.out.print(ANSWER_HEADER);
    try {
      String answer = console.readLine();
      if(answer.equalsIgnoreCase(Options.QUIT.toString())) { 
        quit(); 
      }
      if(card.isCorrect(answer)) {
        System.out.print(CORRECT);
        switch (correctAnswerMenu()) {
          case MARK_LEARNED:
            cards.markLearned(card); 
            return true; 
          case QUIT:
            quit();
          case CONTINUE:
            return false;
          default:
            throw new IOException(INVALID_COMMAND_ERROR);
        }
      } else {
        System.out.print(INCORRECT);
        switch (incorrectAnswerMenu()) {
          case SEE_ANSWER:
            System.out.println(String.format(CORRECT_ANSWER, card.answer())); 
            return false;
          case TRY_AGAIN:
            return askQuestion(card);
          case QUIT:
            quit();
          default:
            throw new IOException(INVALID_COMMAND_ERROR);
            //TODO: this should show the command options again instead of
            // asking the question again
        }
      }
    } catch(IOException e) {
      System.out.println(INVALID_COMMAND_ERROR);
      return askQuestion(card);
    }
  }

  /**
   * Display options after a correct answer is inputed
   * @return the menu item selected
   */
  private static Options correctAnswerMenu() {
    System.out.print(String.format(CORRECT_COMMANDS, Options.MARK_LEARNED,
            Options.QUIT, Options.CONTINUE));
    try {
      return Options.getOptionFromCommand(console.readLine());
    } catch (IOException e) {
      System.out.print(INVALID_COMMAND_ERROR);
      return correctAnswerMenu();
    }
  }

  /**
   * Display options after an incorrect answer is inputed
   * @return the menu item selected
   */
  private static Options incorrectAnswerMenu() {
    System.out.print(String.format(INCORRECT_COMMANDS, Options.SEE_ANSWER,
            Options.TRY_AGAIN, Options.QUIT));
    try {
      return Options.getOptionFromCommand(console.readLine());
    } catch (IOException e) {
      System.out.println(INVALID_COMMAND_ERROR);
      return incorrectAnswerMenu();
    }
  }

  /**
   * Display options when all cards have been answered correctly
   * @return the menu item selected
   */
  private static Options finishedMenu() {
    System.out.print(String.format(FINISHED_COMMANDS, Options.RESTART,
            Options.FLIP, Options.SWITCH_FILES, Options.QUIT));
    try {
      String choice = console.readLine();
      return Options.getOptionFromCommand(choice);
    } catch(IOException e) {}
    System.out.print(INVALID_COMMAND_ERROR);
    return finishedMenu();
  }

  /**
   * Exit the program.
   */
  private static void quit() {
    System.out.println(EXIT_MESSAGE);
    System.exit(0);
  }

  /**
   * Clear the screen by writing newlines.
   */
  private static void clearScreen() {
    for(int i=0; i<50; i++) {
      System.out.println(Constants.NEW_LINE);
    }
  }
}