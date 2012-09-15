package core;

import java.util.ArrayList;
import java.util.Collections;

import core.State.STAGE;

import player.IPlayer;
import player.PlayerAction;
import player.PlayerAction.ACTION;

public class Game {

	private int bettingRounds;
	private State state;
	private ArrayList<IPlayer> playerList = new ArrayList<IPlayer>();
	private Deck deck;
	private int numberOfHand;

	public Game(int bettingRounds, int bigBlindSize) throws Exception {
		this.bettingRounds = bettingRounds;
		state = new State(bigBlindSize);
	}

	public void playHand(int numberOfHand) throws Exception {
		this.numberOfHand = numberOfHand;
		deck = new Deck();
		anounceNewHand();
		dealHoleCards();
		playRound();
		if (state.getPlayersNotFolded() > 1) {
			anounceNewRound();
			state.addSharedCards(deck.drawCards(3)); // Flop
			playRound();
		}
		if (state.getPlayersNotFolded() > 1) {
			anounceNewRound();
			state.addSharedCards(deck.drawCards(1)); // Turn
			playRound();
		}
		if (state.getPlayersNotFolded() > 1) {
			anounceNewRound();
			state.addSharedCards(deck.drawCards(1)); // River
			playRound();
		}
		ArrayList<Integer> winners = null;
		if (state.getPlayersNotFolded() > 1) {
			state.setStage(STAGE.SHOWDOWN);
			winners = findWinner();
		} else {
			winners = new ArrayList<Integer>();
			for (int i = 0; i < playerList.size(); i++) {
				if (!playerList.get(i).hasFolded()) {
					winners.add(i);
					break;
				}
			}
		}
		sharePot(winners);
	}

	private void sharePot(ArrayList<Integer> winners) throws Exception {
		if (state.getStage() == STAGE.SHOWDOWN) {
			int splittedPot = state.getPot() / winners.size();
			for (Integer winner : winners) {
				playerList.get(winner).receiveMoney(splittedPot);
				ArrayList<Card> winnerCards = new ArrayList<Card>();
				winnerCards.addAll(playerList.get(winner).getHoleCards());
				winnerCards.addAll(state.getSharedCards());
				Logger.logDebug("\t\t(Showdown) Winner: "
						+ winner
						+ " with cards power: "
						+ new PowerRanking().calcCardPower(winnerCards)
								.toString() + " | win: " + splittedPot);
			}
		} else {
			playerList.get(winners.get(0)).receiveMoney(state.getPot());
			Logger.logDebug("\t\tWinner: " + winners.get(0) + " | win: "
					+ state.getPot());
		}

	}

	private ArrayList<Integer> findWinner() throws Exception {
		PowerRanking powerRanking = new PowerRanking();
		ArrayList<CardPower> cardPowers = new ArrayList<CardPower>();
		for (int i = 0; i < playerList.size(); i++) {
			if (!playerList.get(i).hasFolded()) {
				ArrayList<Card> playerCards = new ArrayList<Card>();
				playerCards.addAll(playerList.get(i).getHoleCards());
				playerCards.addAll(state.getSharedCards());
				cardPowers.add(powerRanking.calcCardPower(playerCards));
			} else {
				cardPowers.add(new CardPower().add(-1));
			}
		}

		ArrayList<Integer> winners = new ArrayList<Integer>();
		CardPower maxPower = Collections.max(cardPowers);
		for (int i = 0; i < cardPowers.size(); i++) {
			if (cardPowers.get(i).compareTo(maxPower) == 0) {
				winners.add(i);
			}
		}
		return winners;
	}

	public void anounceNewHand() throws Exception {
		state.setPlayersNotFolded(playerList.size());
		for (IPlayer player : playerList) {
			player.newHand();
		}
		state.initNewHand();
		incrementDealerPosition();
		setBlinds();
		Logger.logDebug("Hand: " + numberOfHand + " | " + state.getHandString());
	}

	public void anounceNewRound() {
		for (IPlayer player : playerList) {
			player.newRound();
		}
		state.setBiggestRaise(0);
		int nextState = state.getStage().ordinal() + 1;
		state.setStage(State.STAGE.values()[nextState]);
	}

	private void playRound() throws Exception {
		Logger.logDebug(state.getRoundString());
		for (int i = 0; i < playerList.size(); i++) {
			if (!playerList.get(i).hasFolded()) {
				Logger.logDebug("\tPlayer " + i + ": hole cards: "
						+ playerList.get(i).getHoleCards().toString()
						+ " | cash: " + playerList.get(i).getMoney());
			}
		}

		int playerDecrement = playerList.size();

		// the first player to bet is the 3. after dealer
		int currentPlayer = (state.getDealerPosition() + 3) % playerList.size();
		for (int i = 0; i < bettingRounds; i++) {
			for (IPlayer player : playerList) {
				if (((playerDecrement--)) <= 0) {
					// in order to leave both loops:
					i = bettingRounds;
					break;
				}
				if (getPlayer(currentPlayer).hasFolded()) {
					if (state.getPlayersNotFolded() <= 1) {
						return;
					}
				} else {
					PlayerAction action = getPlayer(currentPlayer).makeBet(
							true);
					Logger.logDebug("\t\tPlayer: "
							+ (currentPlayer)
							% playerList.size()
							+ " | "
							+ playerList.get(
									(currentPlayer) % playerList.size())
									.printLastAction());
					if (action.action == ACTION.CALL
							|| action.action == ACTION.RAISE) {
						state.raisePot(action.toPay);
						state.setBiggestRaise(action.oldStake + action.toPay);
						if (action.action == ACTION.RAISE) {
							playerDecrement = playerList.size() - 1;
							state.incrementNumberOfRaises();
						}
					} else { // fold
						state.decrementPlayersNotFolded();
						if (state.getPlayersNotFolded() <= 1) {
							return;
						}
					}
				}
				currentPlayer++;
			}
		}
		// if the betting rounds are over but not every player has checked or
		// folded there is one last round where you can either call or fold (not
		// raise)
		for (int i = 0; i < playerDecrement; i++) {
			if (!getPlayer(currentPlayer).hasFolded()) {
				PlayerAction action = getPlayer(currentPlayer).makeBet(false); // raise
																				// NOT
																				// allowed
				Logger.logDebug("\t\tPlayer: "
						+ (currentPlayer)
						% playerList.size()
						+ " | "
						+ " | "
						+ playerList.get(
								(currentPlayer - 1) % playerList.size())
								.printLastAction());
				if (action.action == ACTION.CALL) {
					state.raisePot(action.toPay);
				} else { // fold
					state.decrementPlayersNotFolded();
					if (state.getPlayersNotFolded() <= 1) {
						return;
					}
				}
			}
			currentPlayer++;
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

		int newDealerPosition = (state.getDealerPosition() + 1)
				% playerList.size();
		state.setDealerPosition(newDealerPosition);

	}

	private void setBlinds() {
		getPlayer(state.getDealerPosition() + 1).setBlind(
				state.getBigBlindSize() / 2);
		getPlayer(state.getDealerPosition() + 2).setBlind(
				state.getBigBlindSize());
		state.raisePot((int) (state.getBigBlindSize() * 1.5));
		state.setBiggestRaise(state.getBigBlindSize());
	}

	public IPlayer getPlayer(int currentplayer) {
		return playerList.get(currentplayer % playerList.size());
	}

	public void addPlayer(IPlayer player) throws Exception {
		playerList.add(player);
		if (playerList.size() > 10) {
			throw new Exception("Too many players");
		}
	}

	public State getState() {
		return this.state;
	}

	public void printCredits() {
		Logger.logDebug("\n=== Player Credits ====");
		for (int i = 0; i < playerList.size(); i++) {
			Logger.logInfo("Player " + i + " ("
					+ playerList.get(i).printStrategy() + ") : "
					+ playerList.get(i).getMoney());
		}

	}
}
