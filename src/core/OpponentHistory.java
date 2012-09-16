package core;

import java.util.ArrayList;

import player.PlayerAction;

public class OpponentHistory {
	
	private int playerID;
	
	ArrayList<OpponentEntry> entries = new ArrayList<OpponentEntry>();

	public OpponentHistory(int playerID) {
		this.playerID = playerID;
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
