package player;

import java.util.Random;

import player.PlayerAction.ACTION;

import core.State;


public class RandomStrategy implements IStrategy{
	
	Random generator = new Random();

	/**
	 * Choose random action
	 * @return chosen action (FOLD, CALL, RAISE)
	 */
	@Override
	public PlayerAction chooseAction(State state, IPlayer player) {
		PlayerAction action = new PlayerAction();
		action.oldStake = player.getCurrentBet();
		PlayerAction.ACTION[] actionArray = PlayerAction.ACTION.values();
		action.action = actionArray[generator.nextInt(actionArray.length)];
		
		if(state.getBiggestRaise() - player.getCurrentBet() <= 0 && action.action == ACTION.FOLD) {
			action.action = ACTION.CALL;
		}
		
		if(action.action == ACTION.CALL) {
			action.toPay = calculateCall(state, player.getCurrentBet());
		} else if(action.action == ACTION.RAISE) {
			action.toPay = calculateRaise(state, player.getCurrentBet());
		}
		return action;
	}
	
	public int calculateCall(State state, int currentBet) {
		return state.getBiggestRaise() - currentBet;
	}

	public int calculateRaise(State state, int currentBet) {
		int raise = generator.nextInt(10 * state.getBigBlindSize()) + 1;
		int toPay = raise + state.getBiggestRaise() - currentBet;
		return toPay;
	}
}
