package Player;

import java.util.ArrayList;

import Core.Card;
import Core.State;

public class ComputerPlayer implements IPlayer {

	private IStrategy strategy;
	/**
	 * The amount of money that has been set by this Player during current round
	 * (not hand)
	 */
	private int currentBet = 0;
	private State state;
	private ArrayList<Card> holeCards;
	private boolean folded = false;
	private int money = 0;

	public ComputerPlayer(State state, IStrategy strategy) {
		this.strategy = strategy;
		this.state = state;
	}

	@Override
	public void newHand() {
		currentBet = 0;
		folded = false;
	}

	@Override
	public void newRound() {
		currentBet = 0;
	}

	private void makeRaise(int amount) {
		currentBet += amount;
		money -= amount;
	}

	@Override
	public void setBlind(int blindSize) {
		makeRaise(blindSize);
	}

	@Override
	public void setHoleCards(ArrayList<Card> cards) throws Exception {
		if (cards.size() != 2) {
			throw new Exception("Amount of hole cards != 2");
		}
		holeCards = cards;

	}

	@Override
	public PlayerAction makeBet(boolean raiseAllowed) {
		PlayerAction action = new PlayerAction();
		action.oldStake = currentBet;
		action.toPay = 0;
		if (folded) {
			action.action = PlayerAction.ACTION.FOLD;
			return action;
		}
		action.action = strategy.chooseAction();
		if (action.action == PlayerAction.ACTION.CALL
				|| (action.action == PlayerAction.ACTION.RAISE && !raiseAllowed)) {
			action.toPay = state.getBiggestRaise() - currentBet;
			makeRaise(action.toPay);
			return action;
		} else if (action.action == PlayerAction.ACTION.RAISE) {
			action.toPay = strategy.calculateRaise(state, currentBet);
			makeRaise(action.toPay);
			return action;
		} else { // Fold
			folded = true;
			action.action = PlayerAction.ACTION.FOLD;
			return action;
		}
	}

	public static void main(String[] args) {
		// for debugging purpose only
	}
}
