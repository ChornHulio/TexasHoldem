package Core;
import java.util.ArrayList;

import Player.IPlayer;
import Player.PlayerAction;

public class Game {

	private int bettingRounds;
	private State state;
	private ArrayList<IPlayer> playerList = new ArrayList<IPlayer>();
	private Deck deck = new Deck();

	public Game(int bettingRounds, int bigBlindSize) throws Exception {
		this.bettingRounds = bettingRounds;
		state = new State(bigBlindSize);
	}

	public void playHand() throws Exception {
		anounceNewHand();
		dealHoleCards();
		playRound();
		anounceNewRound();
		state.addSharedCards(deck.drawCards(3)); // Flop
		playRound();
		anounceNewRound();
		state.addSharedCards(deck.drawCards(1)); // Turn
		playRound();
		anounceNewRound(); // TODO deal flop, turn, river and playRound()
		state.addSharedCards(deck.drawCards(1)); // River
		playRound();
		
		appointWinner();	
	}
	
	private void appointWinner() {
		// TODO do it
		
	}

	public void anounceNewHand() throws Exception{
		for (IPlayer player : playerList) {
			player.newHand();
		}
		state.setBiggestRaise(0);
		state.setStage(State.STAGE.PREFLOP);
		incrementDealerPosition();
		setBlinds();
	}
	
	public void anounceNewRound(){
		for (IPlayer player : playerList) {
			player.newRound();
		}
		state.setBiggestRaise(0);
		int nextState = state.getStage().ordinal() + 1;
		state.setStage(State.STAGE.values()[nextState]);
	}

	private void playRound() {
		int playerDecrement = playerList.size();
		
		// the first player to bet is the 3. after dealer
		int currentPlayer = (state.getDealerPosition() + 3) % playerList.size(); 
		for (int i = 0; i < bettingRounds; i++) {
			for (IPlayer player : playerList) {
				if(((playerDecrement--)) <= 0){
					// in order to leave both loops:
					i = bettingRounds;
					break;
				}
				PlayerAction action = getPlayer(currentPlayer++).makeBet(true);
				if ( action.action == PlayerAction.ACTION.CALL || action.action == PlayerAction.ACTION.RAISE) {
					playerDecrement = playerList.size();
					state.raisePot(action.toPay);
					state.setBiggestRaise(action.oldStake + action.toPay);
				}
			}
		}
		// if the betting rounds are over but not every player has checked or folded
		// there is one last round where you can either call or fold (not raise)
		for (int i = 0; i < playerDecrement-1; i++) {
			PlayerAction action = getPlayer(currentPlayer++).makeBet(false); // raise NOT allowed
			if(action.action == PlayerAction.ACTION.CALL){
				state.raisePot(action.toPay);
			}
		}
		
	}

	private void dealHoleCards() throws Exception {
		for (IPlayer player : playerList) {
			player.setHoleCards(deck.drawCards(2));
		}		
	}

	public void incrementDealerPosition() throws Exception {
		if (playerList.isEmpty()) {
			throw new Exception("PlayerList empty!");
		}
		
		int newDealerPosition = (state.getDealerPosition() + 1) % playerList.size();
		state.setDealerPosition(newDealerPosition);
		
	}
	
	private void setBlinds() {
		getPlayer(state.getDealerPosition() + 1).setBlind(state.getBigBlindSize());
		getPlayer(state.getDealerPosition() + 2).setBlind(state.getBigBlindSize() / 2);
		state.raisePot((int)(state.getBigBlindSize() * 1.5));
		state.setBiggestRaise(state.getBigBlindSize());
	}
	
	public IPlayer getPlayer(int currentplayer){
		return playerList.get(currentplayer % playerList.size());
	}
	
	public void addPlayer(IPlayer player) {
		playerList.add(player);
	}
	
	public State getState(){
		return this.state;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
