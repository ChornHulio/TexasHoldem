package Player;

public class PlayerAction {
	
	public enum ACTION{
		FOLD,
		CALL,
		RAISE;
	}
	
	public ACTION action;
	public int oldStake;
	public int toPay;

}
