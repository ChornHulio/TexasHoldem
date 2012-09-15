package player;

import java.util.ArrayList;

import player.PlayerAction.ACTION;
import rollout.PreFlop;
import rollout.Rollout;

import core.State;
import core.State.STAGE;


public class HandStrengthStrategy implements IStrategy{
	
	ArrayList<PreFlop> preFlop;
	double callMin = 0.5;
	double callMax = 1.2;
	double lambda = 10.0; // multiplier for e function
	int iterationsOfRollout = 1000;
	PlayerAction lastAction;
	double lastHandStrength;
	
	public HandStrengthStrategy(ArrayList<PreFlop> preFlops) {
		this.preFlop = preFlops;
	}

	/**
	 * Choose action
	 * @return chosen action (FOLD, CALL, RAISE)
	 * @throws Exception 
	 */
	@Override
	public PlayerAction chooseAction(State state, IPlayer player) throws Exception {
		lastAction = new PlayerAction();
		lastAction.oldStake = player.getCurrentBet();
		
		// minimum raise
		int payToCall = state.getBiggestRaise() - player.getCurrentBet();
		
		double handStrengh = 0;
		
		if (state.getStage() == STAGE.PREFLOP) { // Preflop: look at preflop rollout
			handStrengh = preFlop.get(state.getPlayersNotFolded() - 2).getStrength(player.getHoleCards());
		} else {
			handStrengh = new Rollout().simulateHandWithSharedCards(player.getHoleCards(), state.getSharedCards(), state.getPlayersNotFolded());
		}
		int willingToPay = (int) Math.exp(lambda * handStrengh);
		//TODO: max_value irgendwie raus
		if (willingToPay > Integer.MAX_VALUE) {
			willingToPay = Integer.MAX_VALUE;
		}
		if(willingToPay < payToCall * callMin) {
			lastAction.action = ACTION.FOLD;
		} else if(Math.max(willingToPay,state.getBigBlindSize()) < payToCall * callMax) {
			lastAction.action = ACTION.CALL;
			lastAction.toPay = state.getBiggestRaise() - player.getCurrentBet();
		} else {
			lastAction.action = ACTION.RAISE;
			lastAction.toPay = Math.max(willingToPay,state.getBigBlindSize());
		}
		lastHandStrength = handStrengh;
		return lastAction;
	}

	@Override
	public String printLastAction() {
		if(lastAction.action == ACTION.FOLD) {
			return "" + lastAction.toString();
		}
		return "strength: " + lastHandStrength + " | " + lastAction.toString();
	}
}
