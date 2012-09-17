package player.strategy.opponentModel;

import java.util.ArrayList;

import player.PlayerAction.ACTION;

/**
 * The behavior model of a player
 *
 */
public class OpponentModel {

	ArrayList<OpponentEntry> entries = new ArrayList<OpponentEntry>();
	OpponentLogger opponentLogger;
	int playerID;

	public OpponentModel(int playerID) {
		this.playerID = playerID;
	}

	public void addEntry(OpponentEntry entryArg, double handStrength) {
		for (OpponentEntry entry : entries) {
			if (entry.equals(entryArg)) {
				entry.update(handStrength);
				return;
			}
		}

		entries.add(new OpponentEntry(entryArg.getContext(), entryArg.getAction(), handStrength));
	}

	/**
	 * The logger is forwarded to the model not to commit player's move history,
	 * but in order that the model knows what the last action of a player was(CALL, RAISE etc.)
	 */
	public void setLogger(OpponentLogger opponentLogger) {
		this.opponentLogger = opponentLogger;
		
	}

	public double estimateHandStrength() {
		OpponentEntry currentEntry = opponentLogger.getLastEntry(playerID);
		if (currentEntry == null || currentEntry.getAction().action == ACTION.FOLD) {
			return -1;
		} 

		for (OpponentEntry entry : entries) {
			if (entry.equals(currentEntry)) {
				return entry.getHandStrength();
			}
		}
		return -1;
	}
	
	@Override
	public String toString() {
		String out = "";
		for (OpponentEntry entry : entries) {
			out += "[ " + entry.toString() + " ]\n";
		}
		return out;
	}

}
