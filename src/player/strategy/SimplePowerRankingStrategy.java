package player.strategy;

import java.util.ArrayList;
import java.util.Random;

import player.IPlayer;
import player.PlayerAction;
import player.PlayerAction.ACTION;
import core.PowerRanking;
import core.State;
import core.State.STAGE;
import core.card.Card;
import core.card.CardPower;


public class SimplePowerRankingStrategy implements IStrategy{
	
	Random generator = new Random();
	AGGRESSIVITY aggressivity = AGGRESSIVITY.MODERATE;
	CardPower[][] thresholds;
	PlayerAction lastAction;
	int playerID;
	
	final int callIndex = 0;
	final int raiseIndex = 1;
	
	public SimplePowerRankingStrategy(int index, AGGRESSIVITY aggressivity) {
		playerID = index;
		this.aggressivity = aggressivity;
		thresholds = new CardPower[AGGRESSIVITY.values().length][];
		for (int i = 0; i < thresholds.length; i++) {
			thresholds[i] = new CardPower[raiseIndex+1];
			for (int j = 0; j < thresholds[i].length; j++) {
				thresholds[i][j] = new CardPower();
			}
		}

		thresholds[AGGRESSIVITY.CONSERVATIVE.ordinal()][callIndex].add(3).add(-1); // calls if >= two pairs // TODO: adjust comments
		thresholds[AGGRESSIVITY.CONSERVATIVE.ordinal()][raiseIndex].add(4).add(-1);  // raises >= three of a kind

		thresholds[AGGRESSIVITY.MODERATE.ordinal()][callIndex].add(2).add(-1); // calls if >= pairs
		thresholds[AGGRESSIVITY.MODERATE.ordinal()][raiseIndex].add(3).add(-1); // raises if >= two pairs

		thresholds[AGGRESSIVITY.RISKY.ordinal()][callIndex].add(1).add(-1); // calls if >= king high
		thresholds[AGGRESSIVITY.RISKY.ordinal()][raiseIndex].add(2).add(-1); // raises if >= pairs
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
		if (state.getStage() == STAGE.PREFLOP) { // Preflop: random choise
			PlayerAction.ACTION[] actionArray = PlayerAction.ACTION.values();
			lastAction.action = actionArray[generator.nextInt(actionArray.length)];	
			
			// is fold necassary
			if(state.getBiggestRaise() - player.getCurrentBet() <= 0 && lastAction.action == ACTION.FOLD) {
				lastAction.action = ACTION.CALL;
			}
			
			if(lastAction.action == ACTION.CALL) {
				lastAction.toPay = calculateCall(state, player.getCurrentBet());
			} else if(lastAction.action == ACTION.RAISE) {
				lastAction.toPay = calculateRandomRaise(state, player.getCurrentBet());
			}
		} else {
			ArrayList<Card> cards = new ArrayList<Card>();
			cards.addAll(player.getHoleCards());
			cards.addAll(state.getSharedCards());
			CardPower cardPower = new PowerRanking().calcCardPower(cards);
			if(cardPower.compareTo(thresholds[aggressivity.ordinal()][raiseIndex]) > 0) {
				lastAction.action = ACTION.RAISE;
			} else if(cardPower.compareTo(thresholds[aggressivity.ordinal()][callIndex]) > 0) {
				lastAction.action = ACTION.CALL;
			} else {
				lastAction.action = ACTION.FOLD;
			}
			
			// is fold necassary
			if(state.getBiggestRaise() - player.getCurrentBet() <= 0 && lastAction.action == ACTION.FOLD) {
				lastAction.action = ACTION.CALL;
			}
			
			if(lastAction.action == ACTION.CALL) {
				lastAction.toPay = calculateCall(state, player.getCurrentBet());
			} else if(lastAction.action == ACTION.RAISE) {
//				int raise = (int) (Math.exp(cardPower.getAt(0))) * (aggressivity.ordinal() + 1) + state.getBigBlindSize();
				int raise = (int) (Math.exp((cardPower.getAt(0) / 3) - 3) * 100.0 * (aggressivity.ordinal() + 1))+ state.getBigBlindSize();
				lastAction.toPay = raise + state.getBiggestRaise() - player.getCurrentBet();
			}
		}
		return lastAction;
	}
	
	public int calculateCall(State state, int currentBet) {
		return state.getBiggestRaise() - currentBet;
	}

	public int calculateRandomRaise(State state, int currentBet) {
		int raise = generator.nextInt(3 * state.getBigBlindSize()) + 1;
		int toPay = raise + state.getBiggestRaise() - currentBet;
		return toPay;
	}

	@Override
	public String printLastAction() {
		return "" + lastAction.toString();
	}

	@Override
	public String printStrategy() {
		return "SimplePowerRanking | " + aggressivity;
	}
}
