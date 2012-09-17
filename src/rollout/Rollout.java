package rollout;

import java.util.ArrayList;

import core.Deck;
import core.PowerRanking;
import core.card.Card;
import core.card.Card.SIGN;
import core.card.Card.VALUE;
import core.card.CardPower;

public class Rollout {
	
	public void doRollouts(int iterations, int players, String dir) throws Exception {
		// the reader's tab size should be at least 8 for a human readable file format
		Writer wr = new Writer(dir);
		for (int i = 2; i <= players; i++) {
			wr.writeFile(dir, i + "_suited.csv",computeProbabilites(iterations, i, true));
			wr.writeFile(dir, i + "_unsuited.csv",computeProbabilites(iterations, i, false));
		}
	}
	
	private String computeProbabilites(int iterations, int players, boolean suited) throws Exception {
		Card.SIGN suitedSign = SIGN.HEARTS; // for all suited rollouts we draw 2x hearts
		Card.SIGN unsuitedSign = SIGN.DIAMONDS; // for all unsuited rollouts: 1x hearts and 1x diamonds
		
		String outputStr = "";
		// loop over the cards (two to ace) for the FIRST hole card
		for (int i = Card.VALUE.TWO.ordinal(); i <= Card.VALUE.ACE.ordinal(); i++) {
			int j = i; // unsuited case: the hole cards CAN have the same sign 
			if (suited) { // suited case: the hole cards CAN'T have the same sign 
				j = i + 1;
			}
			for (int k = 0; k < j; k++) {
				outputStr += "\t";
			}
			// loop over the cards (two to ace) for the SECOND hole card
			for (; j <= Card.VALUE.ACE.ordinal(); j++) {
				ArrayList<Card> holeCards = new ArrayList<Card>();
				holeCards.add(new Card(suitedSign, VALUE.values()[i]));
				if (suited) {
					holeCards.add(new Card(suitedSign, VALUE.values()[j]));
				} else {

					holeCards.add(new Card(unsuitedSign, VALUE.values()[j]));
				}
				
				outputStr += Double.toString(simulateHand(holeCards, iterations, players)).concat("00000").substring(0, 7) + "\t";
			}
			outputStr += "\n";
		}
		return outputStr;
	}

	/**
	 * Estimates the strength of given hole cards at preFlop
	 * @param holeCards Hole cards
	 * @param iterations Number of rollouts
	 * @param players Number of opponents
	 * @return Hole card's strength
	 * @throws Exception
	 */
	private double simulateHand(ArrayList<Card> holeCards, int iterations, int players) throws Exception {
		double wins = 0;
		for (int i = 0; i < iterations; i++) {
			Deck deck = new Deck();
			deck.removeCards(holeCards);
			ArrayList<Card> sharedCards = deck.drawCards(5);
			ArrayList<Card> sharedAndHoleCards = new ArrayList<Card>();
			sharedAndHoleCards.addAll(holeCards);
			sharedAndHoleCards.addAll(sharedCards);
			
			CardPower power = new PowerRanking().calcCardPower(sharedAndHoleCards);
			
			int winners = 1;
			for (int j = 0; j < players-1; j++) {
				ArrayList<Card> opponentHoleCards = deck.drawCards(2);
				ArrayList<Card> opponentSharedAndHoleCards = new ArrayList<Card>();
				opponentSharedAndHoleCards.addAll(opponentHoleCards);
				opponentSharedAndHoleCards.addAll(sharedCards);
				CardPower opponentPower = new PowerRanking().calcCardPower(opponentSharedAndHoleCards);
				if (opponentPower.compareTo(power) == 0) { // tie
					winners++; 
				} else if(opponentPower.compareTo(power) > 0) { // loss
					winners = -1; 
					break;
				}
			}
			if (winners > 0) {
				wins += 1.0 / winners;
			}
		}
		return wins / iterations;
	}

	/**
	 * Estimates the strength of given hole cards with consideration of given shared cards
	 * (if there are less then 5 shared cards the remaining cards WILL NOT be drawn)
	 * @param holeCards Hole cards
	 * @param sharedCards Shared cards
	 * @param players Number of opponents
	 * @return hand strength
	 * @throws Exception
	 */
	public double simulateHandWithSharedCards(ArrayList<Card> holeCards, ArrayList<Card> sharedCards, int players) throws Exception {
		if (sharedCards.size() < 3) {
			throw new Exception("Less then 3 shared cards");
		}
		
		Deck deck = new Deck();
		deck.removeCards(holeCards);
		deck.removeCards(sharedCards);
		ArrayList<Card> sharedAndHoleCards = new ArrayList<Card>();
		sharedAndHoleCards.addAll(holeCards);
		sharedAndHoleCards.addAll(sharedCards);
		
		CardPower power = new PowerRanking().calcCardPower(sharedAndHoleCards);
		
		ArrayList<ArrayList<Card>> holeCardsCombinations = deck.holeCardsCombinations();
		
		int wins = 0;
		int ties = 0;
		int losses = 0;
		for (ArrayList<Card>  opponentHoleCards: holeCardsCombinations) {
			
			ArrayList<Card> opponentSharedAndHoleCards = new ArrayList<Card>();
			opponentSharedAndHoleCards.addAll(opponentHoleCards);
			opponentSharedAndHoleCards.addAll(sharedCards);
			CardPower opponentPower = new PowerRanking().calcCardPower(opponentSharedAndHoleCards);
			if (opponentPower.compareTo(power) == 0) { // tie
				ties++; 
			} else if(opponentPower.compareTo(power) > 0) { // loss
				losses++; 
			} else { // win
				wins++;
			}
		}
		return Math.pow((wins + 0.5 * ties) / ( wins + ties + losses), players);
	}

	/**
	 * Estimates the strength of given hole cards with consideration of given shared cards
	 * (if there are less then 5 shared cards the remaining cards WILL be drawn)
	 * @param holeCards Hole cards
	 * @param sharedCardsArg Shared cards
	 * @param players Number of opponents
	 * @param iteration Number of rollouts
	 * @return hand strength
	 * @throws Exception
	 */
	public double simulateHandWithSharedCardsAndRandom(ArrayList<Card> holeCards, ArrayList<Card> sharedCardsArg, int players, int iterations) throws Exception {
		int wins = 0;
		int ties = 0;
		int losses = 0;
		for (int i = 0; i <= iterations*(5-sharedCardsArg.size()); i++) {
			Deck deck = new Deck();
			deck.removeCards(holeCards);
			deck.removeCards(sharedCardsArg);
			
			ArrayList<Card> sharedCards = deck.drawCards(5-sharedCardsArg.size());
			sharedCards.addAll(sharedCardsArg);			
			
			ArrayList<Card> sharedAndHoleCards = new ArrayList<Card>();
			sharedAndHoleCards.addAll(holeCards);
			sharedAndHoleCards.addAll(sharedCards);
			
			CardPower power = new PowerRanking().calcCardPower(sharedAndHoleCards);
			
			ArrayList<ArrayList<Card>> holeCardsCombinations = deck.holeCardsCombinations();
			
			for (ArrayList<Card>  opponentHoleCards: holeCardsCombinations) {
				
				ArrayList<Card> opponentSharedAndHoleCards = new ArrayList<Card>();
				opponentSharedAndHoleCards.addAll(opponentHoleCards);
				opponentSharedAndHoleCards.addAll(sharedCards);
				CardPower opponentPower = new PowerRanking().calcCardPower(opponentSharedAndHoleCards);
				if (opponentPower.compareTo(power) == 0) { // tie
					ties++; 
				} else if(opponentPower.compareTo(power) > 0) { // loss
					losses++; 
				} else { // win
					wins++;
				}
			}
		}		
		return Math.pow((wins + 0.5 * ties) / ( wins + ties + losses), players);
	}
	
	public static void main(String[] args) throws Exception {
		int iterations = 100000;
		int maxPlayers = 10;
		long time = System.currentTimeMillis();
		new Rollout().doRollouts(iterations, maxPlayers, "./rolloutsDebug/");
		System.out.println("computation time: " + (System.currentTimeMillis() - time));
	}

}
