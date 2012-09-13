package player;

import player.PlayerAction.ACTION;
import rollout.PreFlop;
import rollout.Rollout;

import core.State;
import core.State.STAGE;


public class HandStrengthStrategy implements IStrategy{
	
	PreFlop preFlop;
	double callMin = 0.5;
	double callMax = 1.2;
	double lambda = 4.0;
	int iterationsOfRollout = 1000;
	
	public HandStrengthStrategy(PreFlop preFlop) {
		this.preFlop = preFlop;
	}

	/**
	 * Choose action
	 * @return chosen action (FOLD, CALL, RAISE)
	 * @throws Exception 
	 */
	@Override
	public PlayerAction chooseAction(State state, IPlayer player) throws Exception {
		PlayerAction action = new PlayerAction();
		action.oldStake = player.getCurrentBet();
		
		// to pay to call
		int toPay = Math.max(state.getBiggestRaise(),state.getBigBlindSize()*2) - player.getCurrentBet();
		
		double handStrengh = 0;
		
		if (state.getStage() == STAGE.PREFLOP) { // Preflop: look at preflop rollout
			handStrengh = preFlop.getStrength(player.getHoleCards());
		} else {
			handStrengh = new Rollout().simulateHandWithSharedCards(player.getHoleCards(), state.getSharedCards(), iterationsOfRollout, state.getPlayersLeft());
		}
		int willingToPay = (int) Math.exp(lambda * handStrengh);
		if(willingToPay < toPay * callMin) {
			action.action = ACTION.FOLD;
		} else if(willingToPay < toPay * callMax) {
			action.action = ACTION.CALL;
			action.toPay = state.getBiggestRaise() - player.getCurrentBet();
		} else {
			action.action = ACTION.RAISE;
			action.toPay = willingToPay;
		}
		return action;
	}
}
