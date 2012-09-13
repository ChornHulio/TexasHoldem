package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import core.Card.SIGN;
import core.Card.VALUE;


public class PowerRanking {

	public CardPower calcCardPower(ArrayList<Card> cards) throws Exception {

		if (cards.size() < 5) {
			throw new Exception("Less than 5 cards");
		}
		Comparator<Card> comparator = Collections.reverseOrder();
		Collections.sort(cards, comparator); // Highest cards first

		CardGroup[] signGroups = extractSameSigns(cards);
		int[] valueCounts = extractSameValues(cards);

		CardPower power = null;
		if (isFlush(signGroups) != null) {
			power = isStraight(signGroups[0].cards);
			if (power != null) {
				return new CardPower().add(8).add(power.getAt(1));
			}
		}
		power = isFourOfKind(valueCounts);
		if (power != null) {
			return power;
		}
		power = isFullHouse(valueCounts);
		if (power != null) {
			return power;
		}
		power = isFlush(signGroups);
		if (power != null) {
			return power;
		}
		power = isStraight(cards);
		if (power != null) {
			return power;
		}
		power = isThreeOfKind(valueCounts);
		if (power != null) {
			return power;
		}
		power = isTwoPairs(valueCounts);
		if (power != null) {
			return power;
		}
		power = isOnePair(valueCounts);
		if (power != null) {
			return power;
		}
		return highCards(valueCounts);
	}

	private CardPower highCards(int[] valueCounts) {
		CardPower power = new CardPower().add(0);
		int count = 5;
		for (int i = valueCounts.length - 1; i >= 0; i--) {
			if (valueCounts[i] > 0){
				power.add(i);
				count--;
				if(count <= 0){
					break;
				}
			}
		}
		
		return power;
	}

	private CardPower isOnePair(int[] valueCounts) {
		int highCard = -1;
		int kickerCardA = -1;
		int kickerCardB = -1;
		int kickerCardC = -1;
		for (int i = valueCounts.length - 1; i >= 0; i--) {
			if (valueCounts[i] == 2) {
				highCard = i;
				break;
			}
		}
		if (highCard >= 0) {
			for (int i = valueCounts.length - 1; i >= 0; i--) {
				if (i != highCard && valueCounts[i] > 0) {
					if (kickerCardB >= 0){
						kickerCardC = i;
						return new CardPower().add(1).add(highCard)
								.add(kickerCardA).add(kickerCardB).add(kickerCardC);
					}else if (kickerCardA >= 0) {
						kickerCardB = i;
					} else{
						kickerCardA = i;
					}
				}
			}
		}
		return null;
	}

	private CardPower isTwoPairs(int[] valueCounts) {
		int highCardA = -1;
		int highCardB = -1;
		int kickerCard = -1;
		for (int i = valueCounts.length - 1; i >= 0; i--) {
			if (valueCounts[i] == 2) {
				if (highCardA >= 0) {
					highCardB = i;
					break;
				}
				highCardA = i;
			}
		}
		if (highCardA >= 0 && highCardB >= 0) {
			for (int i = valueCounts.length - 1; i >= 0; i--) {
				if (valueCounts[i] > 0 && i != highCardA && i != highCardB) {
					kickerCard = i;
					return new CardPower().add(2).add(highCardA)
							.add(highCardB).add(kickerCard);
				}
			}
		}
		return null;
	}

	private CardPower isThreeOfKind(int[] valueCounts) {
		int highCard = -1;
		int kickerCardA = -1;
		int kickerCardB = -1;
		for (int i = valueCounts.length - 1; i >= 0; i--) {
			if (valueCounts[i] == 3) {
				highCard = i;
				break;
			}
		}
		if (highCard >= 0) {
			for (int i = valueCounts.length - 1; i >= 0; i--) {
				if (i != highCard && valueCounts[i] > 0) {
					if (kickerCardA >= 0) {
						kickerCardB = i;
						return new CardPower().add(3).add(highCard)
								.add(kickerCardA).add(kickerCardB);
					}
					kickerCardA = i;
				}
			}
		}
		return null;
	}

	private CardPower isFullHouse(int[] valueCounts) {
		int highCard = -1;
		int kickerCard = -1;
		for (int i = valueCounts.length - 1; i >= 0; i--) {
			if (valueCounts[i] == 3) {
				highCard = i;
				break;
			}
		}
		for (int i = valueCounts.length - 1; i >= 0; i--) {
			if (valueCounts[i] == 2) {
				kickerCard = i;
				break;
			}
		}
		if (highCard >= 0 && kickerCard >= 0) {
			return new CardPower().add(6).add(highCard).add(kickerCard);
		}
		return null;
	}

	private CardPower isFourOfKind(int[] valueCounts) {
		int highCard = -1;
		int kickerCard = -1;
		for (int i = valueCounts.length - 1; i >= 0; i--) {
			if (valueCounts[i] == 4) {
				highCard = i;
				break;
			}
		}
		if (highCard >= 0) {
			for (int i = valueCounts.length - 1; i >= 0; i--) {
				if (i != highCard && valueCounts[i] > 0) {
					kickerCard = i;
					return new CardPower().add(7).add(highCard).add(kickerCard);
				}
			}
		}
		return null;
	}

	private CardPower isFlush(CardGroup[] signGroups) {
		if (signGroups[0].cards.size() >= 5) {
			CardPower power = new CardPower().add(5);
			for (int i = 0; i < 5; i++) { // only the highest cards of the flush
				power.add(signGroups[0].cards.get(i).value.ordinal());
			}
			return power;
		} else {
			return null;
		}
	}

	private CardPower isStraight(ArrayList<Card> cards) {

		int count = 1;
		int lastCard = cards.get(0).value.ordinal();
		for (int i = 1; i < cards.size(); i++) {

			if (cards.get(i).value.ordinal() == lastCard) {
				continue;
			} else if (cards.get(i).value.ordinal() == --lastCard) {
				count++;
			} else {
				count = 1;
				lastCard = cards.get(i).value.ordinal();
			}

			if (count >= 5) {
				return new CardPower().add(4).add(lastCard + 4);
			}
		}
		if (lastCard == 0 && cards.get(0).value == VALUE.ACE && count == 4) {
			return new CardPower().add(4).add(3);
		}
		return null;
	}

	private CardGroup[] extractSameSigns(ArrayList<Card> cards) {
		CardGroup[] groups = new CardGroup[4];
		for (int i = 0; i < groups.length; i++) {
			groups[i] = new CardGroup();
		}
		for (Card card : cards) {
			groups[card.sign.ordinal()].add(card);
		}
		for (CardGroup cardGroup : groups) {
			cardGroup.sort();
		}

		Comparator<CardGroup> comparator = Collections.reverseOrder();
		Arrays.sort(groups, comparator); // biggest group first
		return groups;
	}

	private int[] extractSameValues(ArrayList<Card> cards) {
		int[] valueCount = new int[VALUE.values().length];
		for (int i = 0; i < valueCount.length; i++) {
			valueCount[i] = 0;
		}
		// sort highest first
		for (int i = 0; i < cards.size(); ++i) {
			valueCount[cards.get(i).value.ordinal()]++;
		}
		return valueCount;
	}

	public static void main(String[] args) throws Exception {
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card(SIGN.HEARTS, VALUE.TWO));
		hand.add(new Card(SIGN.DIAMONDS, VALUE.QUEEN));
		hand.add(new Card(SIGN.DIAMONDS, VALUE.FOUR));
		hand.add(new Card(SIGN.DIAMONDS, VALUE.FIVE));
		hand.add(new Card(SIGN.DIAMONDS, VALUE.ACE));
		hand.add(new Card(SIGN.CLUBS, VALUE.FIVE));

		System.out.println(new PowerRanking().calcCardPower(hand));

	}

}
