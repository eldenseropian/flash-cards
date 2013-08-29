package gui;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An iterable list of flash cards.
 */
public class CardList {
  private ArrayList<FlashCard> learned;
  private ArrayList<FlashCard> notLearned;

  private static final String NULL_ERROR = "Attempted to add null FlashCard.";
  private static final String DUPLICATE_CARD_ERROR = "Card already in list.";
  private static final String CARD_NOT_FOUND_ERROR = "Card not found in not " +
          "learned list.";

  /**
   * Creates an empty list of FlashCards
   */
  public CardList() {
    learned = new ArrayList<FlashCard>();
    notLearned = new ArrayList<FlashCard>();
  }

  /**
   * Adds a new FlashCard to the not learned list
   * @param card the card to add.
   * @throws NullPointerException if card is null
   * @throws IllegalArgumentException if card is already in the list
   */
  public void add(FlashCard card) {
    if(card == null) {
      throw new NullPointerException(NULL_ERROR);
    } else if (notLearned.contains(card) || learned.contains(card)) {
      throw new IllegalArgumentException(DUPLICATE_CARD_ERROR);
    }
    notLearned.add(card);
  }

  /**
   * Flip all the cards in the list.
   */
  public void flip() {
    for(FlashCard card: learned) {
      card.flip(); 
    }
    for(FlashCard card: notLearned) { 
      card.flip(); 
    }
  }

  /**
   * Get the number of total cards
   * @return total number of cards
   */
  public int size() {
    return learned.size() + notLearned.size();
  }

  /**
   * Get the number of not learned cards
   * @return number of not learned cards
   */
  public int getNumNotLearned() {
    return notLearned.size();
  }

  /**
   * Get the not learned card located at the specified index
   * @param index the index of the card to get. Requires 0 <= index < size()
   * @return the card at index
   */
  public FlashCard get(int index) {
    return notLearned.get(index);
  }

  public List<FlashCard> getCards() {
    ArrayList<FlashCard> allCards = new ArrayList<FlashCard>();
    allCards.addAll(notLearned);
    allCards.addAll(learned);
    return allCards;
  }

  /**
   * Remove all cards from the list.
   */
  public void clear() {
    notLearned.clear();
    learned.clear();
  }

  /**
   * Mark a card as learned.
   * @param card the card to mark learned.
   * @throws IllegalArgumentException if card is not in unlearned list.
   */
  public void markLearned(FlashCard card) {
    if (!notLearned.contains(card)) {
      throw new IllegalArgumentException(CARD_NOT_FOUND_ERROR);
    }
    learned.add(card);
    notLearned.remove(card);
  }

  /**
   * Shuffle the not learned cards
   */
  public void shuffle() {
    Collections.shuffle(notLearned);
  }

  public void reset() {
    notLearned.addAll(learned);
    learned.clear();
    shuffle();
  }
}