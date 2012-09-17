package player.strategy;

import java.text.DecimalFormat;
import java.util.ArrayList;

import player.IPlayer;
import player.PlayerAction;
import player.PlayerAction.ACTION;
import rollout.PreFlop;
import rollout.Rollout;
import core.State;
import core.State.STAGE;


public class ImprovedHandStrengthStrategy implements IStrategy{
	
	ArrayList<PreFlop> preFlop;
	double lambda = 6.0; // multiplier for e function
	AGGRESSIVITY aggressivity = AGGRESSIVITY.MODERATE;
	int iterationsOfRollout = 1000;
	PlayerAction lastAction;
	double lastHandStrength;
	double lastPotOdd;
	int playerID;
	
	public ImprovedHandStrengthStrategy(int i, ArrayList<PreFlop> preFlops, AGGRESSIVITY aggressivity, int iterationsOfRollout) {
		playerID = i;
		this.preFlop = preFlops;
		this.aggressivity = aggressivity;
		this.iterationsOfRollout = iterationsOfRollout;
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
		double payToCall = state.getBiggestRaise() - player.getCurrentBet();
		double handStrength = 0;

		if (state.getStage() == STAGE.PREFLOP) {
			handStrength = preFlop.get(state.getPlayersNotFolded() - 2).getStrength(player.getHoleCards());
		} else {
			// the only difference between HandStrengthStrategy and ImprovedHandStrengthStrategy is this line
			// here the function simulateHandWithSharedCardsAndRandom is called, which makes several rollouts for FLOP and TURN
			handStrength = new Rollout().simulateHandWithSharedCardsAndRandom(player.getHoleCards(), state.getSharedCards(), state.getPlayersNotFolded(), iterationsOfRollout);
		}
		
//		int willingToPay = (int) Math.exp((aggressivity.ordinal() + lambda) * handStrength);
//		int willingToPay = (int) (500.0 * (aggressivity.ordinal() + 1) * handStrength);
//		int willingToPay = (int) (Math.tanh(handStrength*2) * 500.0 * (aggressivity.ordinal() + 1));
		// the amount to pay is calculated with an exp-function
		int willingToPay = (int) (Math.exp((handStrength * 3) - 3) * 100.0 * (aggressivity.ordinal() + 1));

		if (willingToPay > Integer.MAX_VALUE || willingToPay < 0) {
			willingToPay = Integer.MAX_VALUE;
		}

		lastPotOdd = payToCall / (payToCall + state.getPot());
		int minimumRaise = Math.max(state.getBiggestRaise(), state.getBigBlindSize());
		
		//the pod odd has influence on the decision whether to fold or to stay in the game
		if (willingToPay < payToCall * lastPotOdd) {
			lastAction.action = ACTION.FOLD;
		} 
		// the number of raises has influence on the decision whether to call or to raise
		else if (Math.max(willingToPay, minimumRaise) < payToCall * (1.0 / (1.0 + state.getNumberOfRaises())) || minimumRaise >= willingToPay) {
			lastAction.action = ACTION.CALL;
			lastAction.toPay = state.getBiggestRaise() - player.getCurrentBet();
		} else {
			lastAction.action = ACTION.RAISE;
			lastAction.toPay = Math.max(willingToPay, minimumRaise);
		}
		lastHandStrength = handStrength;
		return lastAction;
	}

	@Override
	public String printLastAction() {
		DecimalFormat douF = new DecimalFormat("#.###");
		return "strength: " + douF.format(lastHandStrength) + " | potOdd: " + douF.format(lastPotOdd) + " | " + lastAction.toString();
	}

	@Override
	public String printStrategy() {
		return "HandStrength | " + aggressivity;
	}
}
