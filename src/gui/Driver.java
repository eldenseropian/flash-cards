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
  private static CardList cards;
  private static BufferedReader console;
  private static final int MARK_LEARNED = 0, QUIT = 1, TRY_AGAIN = 2,
          SEE_ANSWER = 3, CONTINUE = 4, RESTART = 5, FLIP = 6, SWITCH_FILES = 7;

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
              "user.dir"));
      if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file), "UTF-8"));
        while(in.ready()) {
          String card = in.readLine();
          String q = card.substring(0, card.indexOf("_")).trim();
          String a = card.substring(card.indexOf("_") + 1).trim();
          cards.add(new FlashCard(q,a));
        }
        in.close();
      }
      else {
        System.err.println(
                "You cannot run this program without a flashcard file.");
        quit();
        //TODO: add create new file option here
      }
    }
    //start with no cards
    catch (FileNotFoundException e) {}
    //start with no cards and print an error message
    catch(IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Error: card file corrupted");
    }
    catch(StringIndexOutOfBoundsException e) {
      e.printStackTrace();
      throw new RuntimeException("Error: card file corrupted");
    }
  }

  /**
   * Run the interactive flashcard program.
   */
  private static void run() {
    console = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("The cards are currently set like this:");
    System.out.println("Front: " + cards.get(0).question());
    System.out.println("Back: " + cards.get(0).answer());
    System.out.println("Would you like to flip them? Y/N");
    try {
      String toFlip = console.readLine();
      while(!toFlip.equalsIgnoreCase("Y") && !toFlip.equalsIgnoreCase("N")) {
        System.out.println("Invalid input. Y to flip, N to leave as is.");
        toFlip = console.readLine();
      }
      if(toFlip.equalsIgnoreCase("Y")) { cards.flip(); }
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
    System.out.println("Congratulations! You've learned all the cards.");
    int choice = finishedMenu();
    if(choice == QUIT) { quit(); }
    else if(choice == SWITCH_FILES) { readCards(); }
    else if(choice == FLIP) { cards.flip(); }
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
    System.out.println("\nQUESTION: " + card.question());
    System.out.print("ANSWER: ");
    try {
      String answer = console.readLine();
      if(answer.equalsIgnoreCase("quit")) { quit(); }
      int choice;
      if(card.isCorrect(answer)) {
        System.out.print("Right! ");
        choice = correctAnswerMenu();
        if(choice == MARK_LEARNED) { 
          cards.markLearned(card); 
          return true; 
        }
      }
      else {
        System.out.print("Wrong. ");
        choice = incorrectAnswerMenu();
        if(choice == SEE_ANSWER) { 
          System.out.println("The correct answer is \"" + card.answer() + "\""); 
          return false;
        }
        else if(choice == TRY_AGAIN) { return askQuestion(card); }
      }
      if(choice == QUIT) { quit(); }
    }
    catch(IOException e) {
      System.out.println("Error: invalid input.");
      return askQuestion(card);
    }
    return false;
  }

  /**
   * Display options after a correct answer is inputted
   * @return the menu item selected
   */
  private static int correctAnswerMenu() {
    System.out.print("'L' to mark learned, 'Q' to quit, Enter to continue: ");
    try {
      String choice = console.readLine();
      if(choice.equalsIgnoreCase("L")) { return MARK_LEARNED; }
      else if(choice.equalsIgnoreCase("Q")) { return QUIT; }
      else if(choice.equalsIgnoreCase("")) { return CONTINUE; }
    }
    catch (IOException e) { }

    //do this if input caused exception or if input is not a valid choice
    System.out.print("Invalid input. ");
    return correctAnswerMenu();
  }

  /**
   * Display options after an incorrect answer is inputted
   * @return the menu item selected
   */
  private static int incorrectAnswerMenu() {
    System.out.print(
            "'A' to see the answer, Enter to try again, 'Q' to quit: ");
    try {
      String choice = console.readLine();
      if(choice.equalsIgnoreCase("A")) { return SEE_ANSWER; }
      else if(choice.equalsIgnoreCase("")) { return TRY_AGAIN; }
      else if(choice.equalsIgnoreCase("Q")) { return QUIT; }
    }
    catch (IOException e) {}

    //do this if input caused exception or if input is not a valid choice
    System.out.print("Invalid input. ");
    return incorrectAnswerMenu();
  }

  /**
   * Display options when all cards have been answered correctly
   * @return the menu item selected
   */
  private static int finishedMenu() {
    System.out.print("'R' to restart, 'F' to flip and restart, " + 
            "'S' to switch files, 'Q' to quit: ");
    try {
      String choice = console.readLine();
      if(choice.equalsIgnoreCase("R")) { return RESTART; }
      else if (choice.equalsIgnoreCase("F")) { return FLIP; }
      else if (choice.equalsIgnoreCase("S")) { return SWITCH_FILES; }
      else if (choice.equalsIgnoreCase("Q")) { return QUIT; }
    }
    catch(IOException e) {}
    //do this if input caused exception or if input is not a valid choice
    System.out.print("Invalid input. ");
    return finishedMenu();
  }

  /**
   * Exit the program.
   */
  private static void quit() {
    System.out.println("Program terminated.");
    System.exit(0);
  }

  /**
   * Clear the screen by writing newlines.
   */
  private static void clearScreen() {
    for(int i=0; i<50; i++) { System.out.println("\n"); }
  }
}