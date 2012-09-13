package player;

public class PlayerAction {
	
	public enum ACTION{
		FOLD,
		CALL,
		RAISE;
	}
	
	public ACTION action;
	public int oldStake;
	public int toPay;
	
	
	@Override
	public String toString() {
		if(action == ACTION.FOLD) {
			return "" + action + " | loss: " + (toPay + oldStake);
		}
		return "" + action + " | bet: " + (toPay + oldStake);
	}
}
