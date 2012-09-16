package player.strategy.opponentModel;

import java.util.ArrayList;

import player.PlayerAction;
import rollout.PreFlop;
import rollout.Rollout;
import core.State;
import core.State.STAGE;
import core.card.Card;

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

	public void addEntry(int currentPlayer, State state, PlayerAction action) {
		Context context = new Context();
		context.setStage(state.getStage());
		context.setNumberOfPlayers(state.getPlayersNotFolded());
		context.setNumberOfRaises(state.getNumberOfRaisesPerRound());
		context.setPotOdd(action.getPotOdd());
		histories.get(currentPlayer).addEntry(context, action);
	}
	
	public void showdown(int currentPlayer, ArrayList<Card> holeCards, ArrayList<Card> sharedCards) throws Exception {
		for (OpponentEntry entry : histories.get(currentPlayer).getHistory()) {
			double handStrength;
			if(entry.getStage() == STAGE.PREFLOP) {
				handStrength = preFlops.get(entry.getNumberOfPlayers() - 2).getStrength(holeCards);
			} else { // flop, turn or river
				ArrayList<Card> sharedCardsForStage = new ArrayList<Card>();
				sharedCardsForStage.addAll(sharedCards.subList(0, entry.getStage().ordinal() + 2));
				handStrength = new Rollout().simulateHandWithSharedCards(holeCards, sharedCardsForStage, entry.getNumberOfPlayers());
			}
			model.get(currentPlayer).addEntry(entry, handStrength);
		}
	}

	public OpponentEntry getLastEntry(int playerID) {
		return histories.get(playerID).getLastEntry();
		
	}

}
