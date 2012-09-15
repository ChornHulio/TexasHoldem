package player;

import java.util.ArrayList;

import player.PlayerAction.ACTION;

import core.Card;
import core.State;
import core.State.STAGE;

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

	public ComputerPlayer(State state, IStrategy strategy, int initialMoney) {
		this.strategy = strategy;
		this.state = state;
		this.money = initialMoney;
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
	public ArrayList<Card> getHoleCards() {
		return holeCards;
	}

	@Override
	public PlayerAction makeBet(boolean raiseAllowed) throws Exception {
		PlayerAction action = new PlayerAction();
		action.oldStake = currentBet;
		if (folded) {
			action.action = PlayerAction.ACTION.FOLD;
			return action;
		}
		action = strategy.chooseAction(state, this);
		if (action.action == PlayerAction.ACTION.CALL
				|| (action.action == PlayerAction.ACTION.RAISE && !raiseAllowed)) {
			action.action = PlayerAction.ACTION.CALL;
			action.toPay = state.getBiggestRaise() - currentBet;
			makeRaise(action.toPay);
			return action;
		} else if (action.action == PlayerAction.ACTION.RAISE) {
			makeRaise(action.toPay);
			return action;
		} else { // Fold
			folded = true;
			action.action = PlayerAction.ACTION.FOLD;
			return action;
		}
	}
	
	@Override
	public boolean hasFolded() {
		return folded;
	}

	@Override
	public int getMoney() {
		return money;
	}

	@Override
	public void receiveMoney(int money) {
		this.money += money;
	}

	@Override
	public int getCurrentBet() {
		return currentBet;
	}

	@Override
	public String printLastAction() {
		return strategy.printLastAction();
	}

	@Override
	public String printStrategy() {
		return strategy.printStrategy();
	}
}
