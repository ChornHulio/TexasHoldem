package player.strategy.opponentModel;

import java.util.ArrayList;

import player.PlayerAction;
import rollout.PreFlop;
import rollout.Rollout;
import core.State;
import core.State.STAGE;
import core.card.Card;

/**
 * Tracks all moves off all player during one hand is played.
 * Only the information about players who reach the showdown is
 * committed to the model.
 *
 */
public class OpponentLogger {
	
	ArrayList<OpponentHistory> histories = new ArrayList<OpponentHistory>();
	ArrayList<PreFlop> preFlops;
	ArrayList<OpponentModel> model;

	public OpponentLogger(int numberOfPlayers, ArrayList<PreFlop> preFlops, ArrayList<OpponentModel> opponentModel) {
		for (int i = 0; i < numberOfPlayers; i++) {
			histories.add(new OpponentHistory(i));
		}
		this.preFlops = preFlops;
		this.model = opponentModel;
	}

	/**
	 * Log player's action
	 * @param currentPlayer A player
	 * @param state The state of the game
	 * @param action The action a player has made
	 */
	public void addEntry(int currentPlayer, State state, PlayerAction action) {
		Context context = new Context();
		context.setStage(state.getStage());
		context.setNumberOfPlayers(state.getPlayersNotFolded());
		context.setNumberOfRaises(state.getNumberOfRaisesPerRound());
		context.setPotOdd(action.getPotOdd());
		histories.get(currentPlayer).addEntry(context, action);
	}
	
	/**
	 * Commit all the actions the current player has made during the hand to the model (inclusive calculated hand strength)
	 * @param currentPlayer The current player
	 * @param holeCards The current player's hole cards
	 * @param sharedCards The current shared cards
	 */
	public void showdown(int currentPlayer, ArrayList<Card> holeCards, ArrayList<Card> sharedCards) throws Exception {
		for (OpponentEntry entry : histories.get(currentPlayer).getHistory()) {
			double handStrength;
			if(entry.getStage() == STAGE.PREFLOP) {
				handStrength = preFlops.get(entry.getNumberOfPlayers() - 2).getStrength(holeCards);
			} else { // flop, turn or river
				ArrayList<Card> currentSharedCards = new ArrayList<Card>();
				currentSharedCards.addAll(sharedCards.subList(0, entry.getStage().ordinal() + 2));
				handStrength = new Rollout().simulateHandWithSharedCards(holeCards, currentSharedCards, entry.getNumberOfPlayers());
			}
			model.get(currentPlayer).addEntry(entry, handStrength);
		}
	}

	/**
	 * 
	 * @param playerID A player
	 * @return The last action a player has made
	 */
	public OpponentEntry getLastEntry(int playerID) {
		return histories.get(playerID).getLastEntry();
	}

}
