package gui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CardListTest {
  private CardList testList;
  private FlashCard[] testCards;

  @Before
  public void setUp() {
    testList = new CardList();
    testCards = new FlashCard[5];
    for (int i = 0; i < testCards.length; i++) {
      testCards[i] = new FlashCard("front" + i, "back" + i);
    }
  }

  @Test (expected = IllegalArgumentException.class)
  public void testAdd() {
    try {
      // If this doesn't throw a NullPointerException, the call that generates
      // the IllegalArgumentException won't be made and the test will fail.
      testList.add(null);
    } catch (NullPointerException e) {
      testList.add(testCards[0]);
      testList.add(testCards[0]);
    }
  }

  @Test
  public void testFlip() {
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
    assertEquals(0, testList.size());
    testList.add(testCards[0]);
    assertEquals(1, testList.size());
    testList.markLearned(testCards[0]);
    assertEquals(1, testList.size());
    testList.add(testCards[1]);
    assertEquals(2, testList.size());
  }

  @Test
  public void testGetNumNotLearned() {
    assertEquals(0, testList.getNumNotLearned());
    testList.add(testCards[0]);
    assertEquals(1, testList.getNumNotLearned());
    testList.markLearned(testCards[0]);
    assertEquals(0, testList.getNumNotLearned());
    testList.add(testCards[1]);
    assertEquals(1, testList.getNumNotLearned());
  }

  @Test
  public void testGet() {
    for (FlashCard testCard: testCards) {
      testList.add(testCard);
    }
    for (int i = 0; i < testCards.length; i++) {
      assertEquals(testCards[i], testList.get(i));
    }
  }

  @Test
  public void testGetCards() {
    for (FlashCard testCard: testCards) {
      testList.add(testCard);
    }
    testList.markLearned(testCards[0]);
    assertEquals(testCards.length, testList.getCards().size());
  }

  @Test
  public void testClear() {
    for (FlashCard testCard: testCards) {
      testList.add(testCard);
    }
    assertEquals(testCards.length, testList.size());
    testList.clear();
    assertEquals(0, testList.size());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMarkLearned() {
    for (FlashCard testCard: testCards) {
      testList.add(testCard);
    }
    assertEquals(testCards.length, testList.getNumNotLearned());
    testList.markLearned(testCards[1]);
    assertEquals(testCards.length - 1, testList.getNumNotLearned());
    testList.markLearned(new FlashCard("front", "back"));
  }

  @Test
  public void testReset() {
    for (FlashCard testCard: testCards) {
      testList.add(testCard);
    }
    testList.markLearned(testCards[1]);
    assertEquals(testCards.length - 1, testList.getNumNotLearned());
    testList.reset();
    assertEquals(testCards.length, testList.getNumNotLearned());
  }
}