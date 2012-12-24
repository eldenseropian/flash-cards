package gui;

public class FlashCard {
	private String front, back;
	
	/**
	 * Creates a new 2-sided FlashCard
	 * @param front the "question" side of the card
	 * @param back the "answer" side of the card
	 */
	public FlashCard(String front, String back) {
		this.front = front;
		this.back = back;
	}
	
	/**
	 * Get the front of the card
	 * @return the front of the card
	 */
	public String question() {
		return front;
	}
	
	/**
	 * Get the back of the card
	 * @return the back of the card
	 */
	public String answer() {
		return back;
	}
	
	/**
	 * Flip the card: make the front the back and vice versa
	 */
	public void flip() {
		String temp = front;
		front = back;
		back = temp;
	}
	
	/**
	 * Check whether an answer matches the back of the card
	 * @param answer the potential answer to the front of the card
	 * @return true if answer matches the back of the card
	 */
	public boolean isCorrect(String answer) {
		return answer.equalsIgnoreCase(back);
	}
}
