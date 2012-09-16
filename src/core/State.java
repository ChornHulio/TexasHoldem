package core;
import java.util.ArrayList;

import core.card.Card;

public class State {
	
	public enum STAGE{ // betting round
		PREFLOP,
		FLOP,
		TURN,
		RIVER,
		SHOWDOWN;
	}
	private STAGE stage;
	private int bigBlindSize;
	private int pot = 0;
	private int dealerPosition = -1; // at the beginning of each hand the dealer position is incremented
	private ArrayList<Card> sharedCards = new ArrayList<Card>();
	/**
	 * The highest amount, that has been set by a player during the current round of betting 
	 */
	private int biggestRaise = 0;
	private int playersNotFolded = 0;
	int numberOfRaises = 0;
	int numberOfRaisesPerRound = 0;
	
	public State(int bigBlindSize) throws Exception {
		if (bigBlindSize % 2 != 0) {
			throw new Exception("Big blind uneven!");
		}
		this.bigBlindSize = bigBlindSize;
	}	

	public STAGE getStage() {
		return stage;
	}

	public void setStage(STAGE stage) {
		this.stage = stage;
	}

	public int getBiggestRaise() {
		return biggestRaise;
	}

	public void setBiggestRaise(int biggestRaise) {
		this.biggestRaise = biggestRaise;
	}

	public int getBigBlindSize() {
		return bigBlindSize;
	}

	public void setBigBlindSize(int bigBlindSize) {
		this.bigBlindSize = bigBlindSize;
	}

	public int getPot() {
		return pot;
	}

	public void raisePot(int raise) {
		this.pot += raise;
	}

	public int getDealerPosition() {
		return dealerPosition;
	}
	
	public int getPlayersNotFolded() {
		return playersNotFolded;
	}
	
	public void setPlayersNotFolded(int players) {
		playersNotFolded = players;
	}
	
	public void decrementPlayersNotFolded() {
		playersNotFolded--;
	}

	public void setDealerPosition(int dealerPosition) {
		this.dealerPosition = dealerPosition;
	}

	public ArrayList<Card> getSharedCards() {
		return sharedCards;
	}

	public void addSharedCards(ArrayList<Card> cards) {
		this.sharedCards.addAll(cards);
	}
	
	public void initNewHand() {
		sharedCards.clear();
		biggestRaise = 0;
		stage = State.STAGE.PREFLOP;
		pot = 0;
		numberOfRaises = 0;
		numberOfRaisesPerRound = 0;
	}
	
	public int getNumberOfRaises() {
		return numberOfRaises;
	}
	
	public void resetNumberOfRaisesPerRound() {
		numberOfRaisesPerRound = 0;
	}
	
	public int getNumberOfRaisesPerRound() {
		return numberOfRaisesPerRound;
	}
	
	public void incrementNumberOfRaises() {
		numberOfRaises++;
		numberOfRaisesPerRound++;
	}
	
	public String toString(){
		return "Stage: " + stage + " | Pot: " + pot +  " | Dealer at " + dealerPosition + " | Shared cards: " + sharedCards;
	}
	
	public String getHandString() {
		return "Dealer at " + dealerPosition;
	}
	
	public String getRoundString() {
		return "\tStage: " + stage + " | Pot: " + pot +  " | Shared cards: " + sharedCards;
	}
}
