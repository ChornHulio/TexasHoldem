package Core;
import java.util.ArrayList;

public class State {
	
	public enum STAGE{
		PREFLOP,
		FLOP,
		TURN,
		RIVER;
	}
	private STAGE stage;
	private int bigBlindSize;
	private int pot = 0;
	private int dealerPosition = 0;
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

	public State(int bigBlindSize) throws Exception {
		if (bigBlindSize % 2 != 0) {
			throw new Exception("Big blind uneven!");
		}
		this.bigBlindSize = bigBlindSize;
	}

	public static void main(String[] args) {
		// for debugging purpose only
	}
}
