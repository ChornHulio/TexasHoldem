package Player;

import java.util.ArrayList;

import Core.Card;

public interface IPlayer {

	public void newHand();
	public void newRound();
	public void setBlind(int blindSize);
	public void setHoleCards(ArrayList<Card> cards) throws Exception;
	public PlayerAction makeBet(boolean raiseAllowed);
}
