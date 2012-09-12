package Core;

import Player.ComputerPlayer;
import Player.RandomStrategy;

public class Main {

	public static void main(String[] args) throws Exception {
		int bettingRounds = 3;
		int bigBlindSize = 2;
		int players = 4;
		int rounds = 100;
		Game g = new Game(bettingRounds, bigBlindSize);
		for (int i = 0; i < players; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new RandomStrategy()));
		}
		for (int i = 0; i < rounds; i++) {
			g.playHand();
		}
	}
}
