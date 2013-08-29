package gui;

import static org.junit.Assert.*;
import org.junit.Test;

public class FlashCardTest {
  
  @Test
  public void testSides() {
    FlashCard testCard = new FlashCard("front", "back");
    assertEquals("front", testCard.question());
    assertEquals("back", testCard.answer());
    testCard.flip();
    assertEquals("back", testCard.question());
    assertEquals("front", testCard.answer());
    testCard.flip();
    assertEquals("front", testCard.question());
    assertEquals("back", testCard.answer());
  }
  
  @Test
  public void testIsCorrect() {
    FlashCard testCard = new FlashCard("question", "answer");
    assertTrue(testCard.isCorrect("answer"));
    assertFalse(testCard.isCorrect("question"));
    assertFalse(testCard.isCorrect("random"));
  }
}
