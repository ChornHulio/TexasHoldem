package player.strategy.opponentModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

import player.IPlayer;
import player.PlayerAction;
import player.PlayerAction.ACTION;
import player.strategy.IStrategy;
import rollout.PreFlop;
import rollout.Rollout;
import core.State;
import core.State.STAGE;

public class OpponentModelStrategy implements IStrategy {

	ArrayList<PreFlop> preFlop;
	double lambda = 6.0; // multiplier for e function
	AGGRESSIVITY aggressivity = AGGRESSIVITY.MODERATE;
	PlayerAction lastAction;
	double lastHandStrength;
	double lastPotOdd;
	ArrayList<OpponentModel> opponentModels;
	int playerID;

	public OpponentModelStrategy(int i, ArrayList<PreFlop> preFlops, AGGRESSIVITY aggressivity, ArrayList<OpponentModel> opponentModels) {
		playerID = i;
		this.preFlop = preFlops;
		this.aggressivity = aggressivity;
		this.opponentModels = opponentModels;
	}

	/**
	 * Chooses action on the basis of the opponent model, the hand strength, pot odds and number of raises
	 * 
	 * @return chosen action (FOLD, CALL, RAISE)
	 * @throws Exception
	 */
	@Override
	public PlayerAction chooseAction(State state, IPlayer player) throws Exception {
		lastAction = new PlayerAction();
		lastAction.oldStake = player.getCurrentBet();

		// minimum raise
		double payToCall = state.getBiggestRaise() - player.getCurrentBet();
		lastPotOdd = payToCall / (payToCall + state.getPot());

		// own hand strength
		double handStrength = calculateHandStrength(state, player, state.getPlayersNotFolded());

		// estimating opponents' hand strength
		ArrayList<Double> opponentStrengths = new ArrayList<Double>();
		for (int i = 0; i < opponentModels.size(); i++) {
			if (i != playerID) {
				double oppenentHandStrength = opponentModels.get(i).estimateHandStrength();
				if (oppenentHandStrength >= 0) {
					opponentStrengths.add(oppenentHandStrength);
				}
			}
		}

		// count how many players are better/worse than me
		int betterThanMe = 0;
		int worseThanMe = 0;
		for (Double opponentStrength : opponentStrengths) {
			if (opponentStrength >= handStrength) {
				betterThanMe++;
			} else {
				worseThanMe++;
			}
		}

		// int willingToPay = (int) Math.exp((aggressivity.ordinal() + lambda) * handStrength);
		// int willingToPay = (int) (500.0 * (aggressivity.ordinal() + 1) * handStrength);
		// int willingToPay = (int) (Math.tanh(handStrength*2) * 500.0 * (aggressivity.ordinal() + 1));
		int willingToPay = (int) (Math.exp((handStrength * 3) - 3) * 100.0 * (aggressivity.ordinal() + 1));
		
		// pay less/more, if there are player who have better/worse cards than you
		willingToPay *= (1.0 + (double) (worseThanMe - betterThanMe) / 10);
		if (willingToPay > Integer.MAX_VALUE || willingToPay < 0) {
			willingToPay = Integer.MAX_VALUE;
		}

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

	private double calculateHandStrength(State state, IPlayer player, int numberOfPlayers) throws Exception {
		double handStrengh;
		if (state.getStage() == STAGE.PREFLOP) { 
			numberOfPlayers = Math.min(numberOfPlayers, 10);
			handStrengh = preFlop.get(numberOfPlayers - 2).getStrength(player.getHoleCards());
		} else {
			handStrengh = new Rollout().simulateHandWithSharedCards(player.getHoleCards(), state.getSharedCards(), numberOfPlayers);
		}
		return handStrengh;
	}

	@Override
	public String printLastAction() {
		DecimalFormat douF = new DecimalFormat("#.###");
		return "strength: " + douF.format(lastHandStrength) + " | potOdd: " + douF.format(lastPotOdd) + " | " + lastAction.toString();
	}

	@Override
	public String printStrategy() {
		return "OpponentModel | " + aggressivity;
	}
}
