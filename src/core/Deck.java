package core;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
	
	private ArrayList<Card> cards = new ArrayList<Card>();
	Random generator = new Random();
	
	public Deck(){
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
}
