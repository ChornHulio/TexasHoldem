package rollout;

import java.util.ArrayList;

import core.*;
import core.Card.SIGN;
import core.Card.VALUE;

public class Rollout {
	
	public void doRollouts(int iterations, int players, String dir) throws Exception {
		// your reader's tab size should be at least 8 for a human readable file format
		Writer wr = new Writer(dir);
		for (int i = 2; i <= players; i++) {
			wr.writeOutputFile(dir, i + "_suited.csv",computeProbabilites(iterations, i, true));
			wr.writeOutputFile(dir, i + "_unsuited.csv",computeProbabilites(iterations, i, false));
		}
	}
	
	private String computeProbabilites(int iterations, int players, boolean suited) throws Exception {
		Card.SIGN suitedSign = SIGN.HEARTS; // for all suited rollouts we draw 2x hearts
		Card.SIGN unsuitedSign = SIGN.DIAMONDS; // for all unsuited rollouts: 1x hearts and 1x diamonds
		
		String outputStr = "";
		for (int i = Card.VALUE.TWO.ordinal(); i <= Card.VALUE.ACE.ordinal(); i++) { // suited case
			int j = i;
			if (suited) {
				j = i + 1;
			}
			for (int k = 0; k < j; k++) {
				outputStr += "\t";
			}
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

	private double simulateHand(ArrayList<Card> holeCards, int iterations, int players) throws Exception {
		return simulateHandWithSharedCards(holeCards, null, iterations, players);
	}

	public double simulateHandWithSharedCards(ArrayList<Card> holeCards, ArrayList<Card> sharedCardsArg, int iterations, int players) throws Exception {
		double wins = 0;
		for (int i = 0; i < iterations; i++) {
			Deck deck = new Deck();
			deck.removeCards(holeCards);
			ArrayList<Card> sharedCards = new ArrayList<Card>();
			if(sharedCardsArg == null) {
				sharedCards = deck.drawCards(5);
			} else {
				sharedCards.addAll(sharedCardsArg);
				deck.removeCards(sharedCards);
			}
			
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
	
	
	
	public static void main(String[] args) throws Exception {
		int iterations = 100000;
		int maxPlayers = 10;
		long time = System.currentTimeMillis();
		new Rollout().doRollouts(iterations, maxPlayers, "./rollouts/");
		System.out.println("computation time: " + (System.currentTimeMillis() - time));
	}

}
