package core;

import java.util.ArrayList;
import java.util.Random;

import core.card.Card;
import core.card.Card.SIGN;
import core.card.Card.VALUE;

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

	/**
	 * Draw not random but specific cards from deck
	 * @param cardsToRemove Cards being drawn from deck
	 * @throws Exception if the cards being drawn are not in the deck
	 */
	public boolean removeCards(ArrayList<Card> cardsToRemove) throws Exception {
		for (Card card : cardsToRemove) {
			removeCard(card);
		}
		return true;
	}

	/**
	 * Draw one specific card
	 * @param card Card to Drow
	 * @throws if the card being drawn are not in the deck
	 */
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

	/**
	 * Computes all possible hole card combinations the deck can have
	 * @return All possible hole card combinations the deck can have
	 */
	public ArrayList<ArrayList<Card>> holeCardsCombinations() {
		ArrayList<ArrayList<Card>> combinations = new ArrayList<ArrayList<Card>>();
		for (int i = 0; i < cards.size(); i++) { 
			for (int j = i + 1; j < cards.size(); j++) {
				ArrayList<Card> combination = new ArrayList<Card>();
				combination.add(cards.get(i));
				combination.add(cards.get(j));
				combinations.add(combination);
			}
		}
		return combinations;
	}
}
