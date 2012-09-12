package Player;

import java.util.Random;

import Core.State;

public class RandomStrategy implements IStrategy{
	
	Random generator = new Random();

	/**
	 * Choose random action
	 * @return chosen action (FOLD, CALL, RAISE)
	 */
	@Override
	public PlayerAction.ACTION chooseAction() {
		PlayerAction.ACTION[] actionArray = PlayerAction.ACTION.values();
		return actionArray[generator.nextInt(actionArray.length)];
	}

	@Override
	public int calculateRaise(State state, int currentBet) {
		int raise = generator.nextInt(10 * state.getBigBlindSize());
		int toPay = raise + state.getBiggestRaise() - currentBet;
		return toPay;
	}

	public static void main(String[] args) {
		// for debugging purpose only
	}
}
