package core;

import java.util.ArrayList;
import java.util.Random;

import core.Card.SIGN;
import core.Card.VALUE;

public class Deck {

	private ArrayList<Card> cards = new ArrayList<Card>();
	Random generator = new Random();

	public Deck() {
		for (Card.SIGN sign : Card.SIGN.values()) {
			for (Card.VALUE value : Card.VALUE.values()) {
				cards.add(new Card(sign, value));
			}
		}
	}

	public ArrayList<Card> drawCards(int amount) {
		ArrayList<Card> returnCards = new ArrayList<Card>();
		for (int j = 0; j < amount; j++) {
			int index = generator.nextInt(cards.size());
			returnCards.add(cards.remove(index));
		}
		return returnCards;
	}

	public boolean removeCards(ArrayList<Card> cardsToRemove) throws Exception {
		for (Card card : cardsToRemove) {
			removeCard(card);
		}
		return true;
	}

	public boolean removeCard(Card card) throws Exception {
		for (int i = 0; i < cards.size(); ++i) {
			if (card.sign == cards.get(i).sign
					&& card.value == cards.get(i).value) {
				cards.remove(i);
				return true;
			}
		}
		throw new Exception("Card not in deck");
	}

	public static void main(String[] args) throws Exception {
		Card c1 = new Card(SIGN.CLUBS, VALUE.TEN);
		Card c2 = new Card(SIGN.CLUBS, VALUE.JACK);
		Card c3 = new Card(SIGN.CLUBS, VALUE.TEN);

		Deck deck = new Deck();
		System.out.println(deck.removeCard(c1));
		System.out.println(deck.removeCard(c2));
		System.out.println(deck.removeCard(c3));
	}
}
