package player;

import java.util.ArrayList;

import player.PlayerAction.ACTION;
import rollout.PreFlop;
import rollout.Rollout;

import core.State;
import core.State.STAGE;

public class HandStrengthStrategy implements IStrategy {

	ArrayList<PreFlop> preFlop;
	double lambda = 6.0; // multiplier for e function
	AGGRESSIVITY aggressivity = AGGRESSIVITY.MODERATE;
	PlayerAction lastAction;
	double lastHandStrength;
	double lastPotOdd;

	public HandStrengthStrategy(ArrayList<PreFlop> preFlops,
			AGGRESSIVITY aggressivity) {
		this.preFlop = preFlops;
		this.aggressivity = aggressivity;
	}

	/**
	 * Choose action
	 * 
	 * @return chosen action (FOLD, CALL, RAISE)
	 * @throws Exception
	 */
	@Override
	public PlayerAction chooseAction(State state, IPlayer player)
			throws Exception {
		lastAction = new PlayerAction();
		lastAction.oldStake = player.getCurrentBet();

		// minimum raise
		int payToCall = state.getBiggestRaise() - player.getCurrentBet();
		double handStrengh = 0;

		if (state.getStage() == STAGE.PREFLOP) { // Preflop: look at preflop
													// rollout
			handStrengh = preFlop.get(state.getPlayersNotFolded() - 2)
					.getStrength(player.getHoleCards());
		} else {
			handStrengh = new Rollout().simulateHandWithSharedCards(
					player.getHoleCards(), state.getSharedCards(),
					state.getPlayersNotFolded());
		}
		int willingToPay = (int) Math.exp((aggressivity.ordinal() + lambda)
				* handStrengh);
		if (willingToPay > Integer.MAX_VALUE || willingToPay < 0) {
			willingToPay = Integer.MAX_VALUE;
		}

		lastPotOdd = (double) payToCall / (payToCall + state.getPot());
		int minimumRaise = Math.max(state.getBiggestRaise(), // TODO: bug? mindesens um big blind raisen?
				state.getBigBlindSize());
		if (willingToPay < payToCall * lastPotOdd) {
			lastAction.action = ACTION.FOLD;
		} else if (Math.max(willingToPay, minimumRaise) < payToCall
				* (1.0 / (1.0 + state.getNumberOfRaises())) || minimumRaise >= willingToPay) {
			lastAction.action = ACTION.CALL;
			lastAction.toPay = state.getBiggestRaise() - player.getCurrentBet();
		} else {
			lastAction.action = ACTION.RAISE;
			lastAction.toPay = Math.max(willingToPay, minimumRaise); // TODO: bug. raise obwohl call // fixed durch || minimumRaise >= willingToPay (drüber)
		}
		lastHandStrength = handStrengh;
		return lastAction;
	}

	@Override
	public String printLastAction() {
		if (lastHandStrength > 1) {
			System.out.println("wtf!"); // TODO: delete
		}
		return "strength: "
				+ Double.toString(lastHandStrength).concat("00000") // TODO: handStrength anzeige 2.472 E-4
						.substring(0, 5) + " | potOdd: "
				+ Double.toString(lastPotOdd).concat("00000").substring(0, 5)
				+ " | " + lastAction.toString();
	}

	@Override
	public String printStrategy() {
		return "HandStrength | " + aggressivity;
	}
}
