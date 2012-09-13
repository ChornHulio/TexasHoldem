package player;

import core.State;

public interface IStrategy {

	public PlayerAction chooseAction(State state, IPlayer player) throws Exception;
}
