package player;

import java.util.ArrayList;

import player.PlayerAction.ACTION;
import rollout.PreFlop;
import rollout.Rollout;

import core.State;
import core.State.STAGE;


public class HandStrengthStrategy implements IStrategy{
	
	ArrayList<PreFlop> preFlop;
	double lambda = 6.0; // multiplier for e function
	AGGRESSIVITY aggressivity = AGGRESSIVITY.MODERATE;
	PlayerAction lastAction;
	double lastHandStrength;
	double lastPotOdd;
	
	public HandStrengthStrategy(ArrayList<PreFlop> preFlops, AGGRESSIVITY aggressivity) {
		this.preFlop = preFlops;
		this.aggressivity = aggressivity;
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
		if(payToCall < 0) {// TODO delete
			System.out.println("----"); 
		}
		
		double handStrengh = 0;
		
		if (state.getStage() == STAGE.PREFLOP) { // Preflop: look at preflop rollout
			handStrengh = preFlop.get(state.getPlayersNotFolded() - 2).getStrength(player.getHoleCards());
		} else {
			handStrengh = new Rollout().simulateHandWithSharedCards(player.getHoleCards(), state.getSharedCards(), state.getPlayersNotFolded());
		}
		int willingToPay = (int) Math.exp((aggressivity.ordinal() + lambda) * handStrengh);
		if (willingToPay > Integer.MAX_VALUE || willingToPay < 0) {
			willingToPay = Integer.MAX_VALUE;
		}
		
		lastPotOdd = (double) payToCall / (payToCall + state.getPot());
		if(willingToPay < payToCall * lastPotOdd) {
			lastAction.action = ACTION.FOLD;
		} else if(Math.max(willingToPay,state.getBigBlindSize()) < payToCall * (1 / (1 + state.getNumberOfRaises()))) {
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
		return "strength: " + lastHandStrength + " | potOdd: " + lastPotOdd + " | " + lastAction.toString();
	}

	@Override
	public String printStrategy() {
		return "HandStrengh | " + aggressivity;
	}
}
