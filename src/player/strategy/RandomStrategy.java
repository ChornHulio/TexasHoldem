package player.strategy;

import java.util.Random;

import player.IPlayer;
import player.PlayerAction;
import player.PlayerAction.ACTION;
import core.State;


public class RandomStrategy implements IStrategy{
	
	Random generator = new Random();
	PlayerAction lastAction;
	int playerID;

	public RandomStrategy(int i) {
		playerID = i;
	}

	/**
	 * Chooses action randomly
	 * @return Chosen action (FOLD, CALL, RAISE)
	 */
	@Override
	public PlayerAction chooseAction(State state, IPlayer player) {
		lastAction = new PlayerAction();
		lastAction.oldStake = player.getCurrentBet();
		PlayerAction.ACTION[] actionArray = PlayerAction.ACTION.values();
		lastAction.action = actionArray[generator.nextInt(actionArray.length)];
		
		if(state.getBiggestRaise() - player.getCurrentBet() <= 0 && lastAction.action == ACTION.FOLD) {
			lastAction.action = ACTION.CALL;
		}
		
		if(lastAction.action == ACTION.CALL) {
			lastAction.toPay = calculateCall(state, player.getCurrentBet());
		} else if(lastAction.action == ACTION.RAISE) {
			lastAction.toPay = calculateRaise(state, player.getCurrentBet());
		}
		return lastAction;
	}
	
	public int calculateCall(State state, int currentBet) {
		return state.getBiggestRaise() - currentBet;
	}

	public int calculateRaise(State state, int currentBet) {
		int raise = generator.nextInt(3 * state.getBigBlindSize()) + state.getBigBlindSize();
		int toPay = raise + state.getBiggestRaise() - currentBet;
		return toPay;
	}

	@Override
	public String printLastAction() {
		return "" + lastAction.toString();
	}

	@Override
	public String printStrategy() {
		return "Random";
	}
}
