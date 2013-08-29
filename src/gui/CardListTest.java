package gui;

import static org.junit.Assert.*;
import org.junit.Test;

public class CardListTest {

  @Test (expected = IllegalArgumentException.class)
  public void testAdd() {
    CardList testList = new CardList();
    FlashCard testCard = new FlashCard("front", "back");
    try {
      // If this doesn't throw a NullPointerException, the call that generates
      // the IllegalArgumentException won't be made and the test will fail.
      testList.add(null);
    } catch (NullPointerException e) {
      testList.add(testCard);
      testList.add(testCard);
    }
  }

  @Test
  public void testFlip() {
    CardList testList = new CardList();
    FlashCard[] testCards = {
            new FlashCard("not flipped", "flipped"),
            new FlashCard("not flipped", "flipped"),
            new FlashCard("not flipped", "flipped"),
            new FlashCard("not flipped", "flipped")
    };
    for (FlashCard testCard: testCards) {
      testList.add(testCard);
    }
    testList.markLearned(testCards[0]);
    testList.markLearned(testCards[1]);
    testList.flip();
    for (FlashCard testCard: testList.getCards()) {
      assertEquals("flipped", testCard.question());
    }
  }
  
  @Test
  public void testSize() {
    CardList testList = new CardList();
    FlashCard testCard = new FlashCard("a", "b");
    assertEquals(0, testList.size());
    testList.add(testCard);
    assertEquals(1, testList.size());
    testList.markLearned(testCard);
    assertEquals(1, testList.size());
    testList.add(new FlashCard("c", "d"));
    assertEquals(2, testList.size());
  }
}