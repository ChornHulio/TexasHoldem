package player;

import java.util.ArrayList;

import core.Card;


public interface IPlayer {

	public void newHand();
	public void newRound();
	public void setBlind(int blindSize);
	public void setHoleCards(ArrayList<Card> cards) throws Exception;
	public PlayerAction makeBet(boolean raiseAllowed) throws Exception;
	public ArrayList<Card> getHoleCards();
	public void receiveMoney(int money);
	public boolean hasFolded();
	public int getMoney();
	public int getCurrentBet();
	public String printLastAction();
}
