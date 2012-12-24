package gui;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class CardList implements Iterable<FlashCard> {
	private ArrayList<FlashCard> learned;
	private ArrayList<FlashCard> notLearned;
	
	/**
	 * Creates an empty list of FlashCards
	 */
	public CardList() {
		learned = new ArrayList<FlashCard>();
		notLearned = new ArrayList<FlashCard>();
	}
	
	/**
	 * Adds a FlashCard to the not learned list
	 * @param card the card to add
	 */
	public void add(FlashCard card) {
		if(card == null) {
			throw new NullPointerException("Attempted to add null FlashCard.");
		}
		notLearned.add(card);
	}
	
	/**
	 * Flip all the cards in the list.
	 */
 	public void flip() {
		for(FlashCard card: learned) { card.flip(); }
		for(FlashCard card: notLearned) { card.flip(); }
	}

 	/**
 	 * Get an iterator over the list of not learned cards
 	 * @return iterator over the list of not learned cards
 	 */
	public Iterator<FlashCard> iterator() {
		return notLearned.iterator();
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
	public int numNotLearned() {
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
	
	/**
	 * Remove all cards from the list.
	 */
	public void clear() {
		notLearned.clear();
		learned.clear();
	}
	
	public void markLearned(FlashCard card) {
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
