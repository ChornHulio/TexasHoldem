package player;

import java.util.ArrayList;
import java.util.Random;

import player.PlayerAction.ACTION;

import core.Card;
import core.CardPower;
import core.PowerRanking;
import core.State;
import core.State.STAGE;


public class SimplePowerRankingStrategy implements IStrategy{
	
	public enum AGGRESSIVITY{
		CONSERVATIVE,
		MODERATE,
		RISKY;
	}
	
	Random generator = new Random();
	AGGRESSIVITY aggressivity = AGGRESSIVITY.MODERATE;
	CardPower[][] thresholds;
	
	final int callIndex = 0;
	final int raiseIndex = 1;
	
	public SimplePowerRankingStrategy(AGGRESSIVITY aggressivity) {
		this.aggressivity = aggressivity;
		thresholds = new CardPower[AGGRESSIVITY.values().length][];
		for (int i = 0; i < thresholds.length; i++) {
			thresholds[i] = new CardPower[raiseIndex+1];
			for (int j = 0; j < thresholds[i].length; j++) {
				thresholds[i][j] = new CardPower();
			}
		}

		thresholds[AGGRESSIVITY.CONSERVATIVE.ordinal()][callIndex].add(2).add(-1); // calls if >= two pairs
		thresholds[AGGRESSIVITY.CONSERVATIVE.ordinal()][raiseIndex].add(3).add(-1);  // raises >= three of a kind

		thresholds[AGGRESSIVITY.MODERATE.ordinal()][callIndex].add(1).add(-1); // calls if >= pairs
		thresholds[AGGRESSIVITY.MODERATE.ordinal()][raiseIndex].add(2).add(-1); // raises if >= two pairs

		thresholds[AGGRESSIVITY.RISKY.ordinal()][callIndex].add(0).add(10).add(-1); // calls if >= king high
		thresholds[AGGRESSIVITY.RISKY.ordinal()][raiseIndex].add(1).add(-1); // raises if >= pairs
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
		if (state.getStage() == STAGE.PREFLOP) { // Preflop: random choise
			PlayerAction.ACTION[] actionArray = PlayerAction.ACTION.values();
			action.action = actionArray[generator.nextInt(actionArray.length)];	
			
			// is fold necassary
			if(state.getBiggestRaise() - player.getCurrentBet() <= 0 && action.action == ACTION.FOLD) {
				action.action = ACTION.CALL;
			}
			
			if(action.action == ACTION.CALL) {
				action.toPay = calculateCall(state, player.getCurrentBet());
			} else if(action.action == ACTION.RAISE) {
				action.toPay = calculateRandomRaise(state, player.getCurrentBet());
			}
		} else {
			ArrayList<Card> cards = new ArrayList<Card>();
			cards.addAll(player.getHoleCards());
			cards.addAll(state.getSharedCards());
			CardPower cardPower = new PowerRanking().calcCardPower(cards);
			if(cardPower.compareTo(thresholds[aggressivity.ordinal()][raiseIndex]) > 0) {
				action.action = ACTION.RAISE;
			} else if(cardPower.compareTo(thresholds[aggressivity.ordinal()][callIndex]) > 0) {
				action.action = ACTION.CALL;
			} else {
				action.action = ACTION.FOLD;
			}
			
			// is fold necassary
			if(state.getBiggestRaise() - player.getCurrentBet() <= 0 && action.action == ACTION.FOLD) {
				action.action = ACTION.CALL;
			}
			
			if(action.action == ACTION.CALL) {
				action.toPay = calculateCall(state, player.getCurrentBet());
			} else if(action.action == ACTION.RAISE) {
				int raise = (int) (Math.exp(cardPower.getAt(0))) * (aggressivity.ordinal() + 1) + state.getBigBlindSize();
				action.toPay = raise + state.getBiggestRaise() - player.getCurrentBet();
			}
		}
		return action;
	}
	
	public int calculateCall(State state, int currentBet) {
		return state.getBiggestRaise() - currentBet;
	}

	public int calculateRandomRaise(State state, int currentBet) {
		int raise = generator.nextInt(10 * state.getBigBlindSize()) + 1;
		int toPay = raise + state.getBiggestRaise() - currentBet;
		return toPay;
	}
}
