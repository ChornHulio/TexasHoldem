package player;

import java.util.ArrayList;

import rollout.PreFlop;
import core.State;

public class OpponentModellingStrategy implements IStrategy {
	
	ArrayList<PreFlop> preFlop;
	double lambda = 6.0; // multiplier for e function
	AGGRESSIVITY aggressivity = AGGRESSIVITY.MODERATE;
	PlayerAction lastAction;
	double lastHandStrength;
	double lastPotOdd;

	public OpponentModellingStrategy(ArrayList<PreFlop> preFlops, AGGRESSIVITY aggressivity) {
		this.preFlop = preFlops;
		this.aggressivity = aggressivity;
	}

	@Override
	public PlayerAction chooseAction(State state, IPlayer player)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String printLastAction() {
		return "strength: "
				+ Double.toString(lastHandStrength).concat("00000")
						.substring(0, 5) + " | potOdd: "
				+ Double.toString(lastPotOdd).concat("00000").substring(0, 5)
				+ " | " + lastAction.toString();
	}

	@Override
	public String printStrategy() {
		return "OpponentModelling | " + aggressivity;
	}

}
