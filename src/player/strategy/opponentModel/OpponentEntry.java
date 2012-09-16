package player.strategy.opponentModel;

import player.PlayerAction;
import core.State.STAGE;

public class OpponentEntry {

	private Context context;
	private PlayerAction action;
	private double averageHandStrength = -1; // used only by model, not by
												// history
	private int count = 0;

	public OpponentEntry(Context context, PlayerAction action) {
		this.context = context;
		this.action = action;
	}

	public OpponentEntry(Context context, PlayerAction action,
			double handStrength) {
		this.context = context;
		this.action = action;
		this.averageHandStrength = handStrength;
		count = 1;
	}

	public void update(double handStrength) {
		averageHandStrength = (averageHandStrength * count + handStrength)
				/ (count + 1);
		count++;
	}

	public double getHandStrength() {
		return averageHandStrength;
	}

	public void setHandStrength(double handStrength) {
		this.averageHandStrength = handStrength;
	}

	public Context getContext() {
		return context;
	}

	public PlayerAction getAction() {
		return action;
	}

	public STAGE getStage() {
		return context.getStage();
	}

	public int getNumberOfPlayers() {
		return context.getNumberOfPlayers();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;

		if (!(o instanceof OpponentEntry))
			return false;
		OpponentEntry that = (OpponentEntry) o;

		if (this.context.equals(that.context) && this.action.equals(that.action)) {
			return true; // equals
		}
		return false;
	}
	
	@Override
	public String toString() {
		return context.toString() + ", " + action.toString() + " : " + averageHandStrength + ", " + count;
	}
}
