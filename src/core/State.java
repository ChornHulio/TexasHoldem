package core;
import java.util.ArrayList;

public class State {
	
	public enum STAGE{
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
	}

	public State(int bigBlindSize) throws Exception {
		if (bigBlindSize % 2 != 0) {
			throw new Exception("Big blind uneven!");
		}
		this.bigBlindSize = bigBlindSize;
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

	public static void main(String[] args) {
		// for debugging purpose only
	}
}