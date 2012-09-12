package Core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CardGroup implements Comparable<CardGroup>{

	public ArrayList<Card> cards = new ArrayList<Card>();
	
	public void add(Card card){
		cards.add(card);
	}
	
	public void sort(){
		Comparator comparator = Collections.reverseOrder();
		Collections.sort(cards, comparator); // highest card first
	}

	@Override
	public int compareTo(CardGroup cardGroup){
		if(cards.size() > cardGroup.cards.size())
			return 1;
		else if(cards.size() < cardGroup.cards.size())
			return -1;
		return 0;
	}

}
