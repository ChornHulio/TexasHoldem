package player.strategy.opponentModel;

import java.util.ArrayList;

import player.PlayerAction;

/**
 * History of all actions a player has made during the game
 *
 */
public class OpponentHistory {
	
	private int playerID;

	/// List of all actions a player has made during the game
	ArrayList<OpponentEntry> entries = new ArrayList<OpponentEntry>();

	public OpponentHistory(int playerID) {
		this.playerID = playerID;
	}
	
	public int getPlayerID() {
		return playerID;
	}

	public void addEntry(Context context, PlayerAction action) {
		entries.add(new OpponentEntry(context, action));
	}

	public ArrayList<OpponentEntry> getHistory() {
		return entries;
	}

	public OpponentEntry getLastEntry() {
		if (entries.isEmpty()) {
			return null;
		}
		return entries.get(entries.size()-1);
	}

}
