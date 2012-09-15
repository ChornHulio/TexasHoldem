package player;

import core.State;

public interface IStrategy {
	
	public enum AGGRESSIVITY{
		CONSERVATIVE { // TODO delete
			public String toString() {
				return "CONSERVATIVE";
			}
		},
		MODERATE {
			public String toString() {
				return "MODERATE";
			}
		},
		RISKY {
			public String toString() {
				return "RISKY";
			}
		};
	}

	PlayerAction chooseAction(State state, IPlayer player)
			throws Exception;
	
	String printLastAction();
	
	String printStrategy();
}
