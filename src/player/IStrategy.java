package player;

import core.State;

public interface IStrategy {

	PlayerAction chooseAction(State state, IPlayer player)
			throws Exception;
}
