package Player;

import Core.State;

public interface IStrategy {

	public PlayerAction.ACTION chooseAction();
	public int calculateRaise(State state, int currentBet);
}
