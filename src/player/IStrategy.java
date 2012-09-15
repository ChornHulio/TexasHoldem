package player;

import core.State;

public interface IStrategy {
	
	public enum AGGRESSIVITY{
		CONSERVATIVE,
		MODERATE,
		RISKY;
	}

	PlayerAction chooseAction(State state, IPlayer player)
			throws Exception;
	
	String printLastAction();
	
	String printStrategy();
}
