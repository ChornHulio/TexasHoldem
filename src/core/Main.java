package core;

import java.util.ArrayList;

import player.ComputerPlayer;
import player.HandStrengthStrategy;
import player.RandomStrategy;
import player.SimplePowerRankingStrategy;
import player.SimplePowerRankingStrategy.AGGRESSIVITY;
import rollout.PreFlop;

public class Main {
	
	public static final boolean DEBUG = true;

	public static void main(String[] args) throws Exception {
		int bettingRounds = 3;
		int bigBlindSize = 2;
		int hands = 10000;
		int initialMoney = 0;
		
		int randomPlayers = 2;
		int simplePowerRankingPlayersConservative = 2;
		int simplePowerRankingPlayersModerate = 2;
		int simplePowerRankingPlayersRisky = 2;
		int handStrengthPlayers = 2;
		
		int playersInTotal = 10;
		String pathnameToRollout = "./rollouts";
		ArrayList<PreFlop> preFlops = new ArrayList<PreFlop>();
		for (int i = 2; i <= playersInTotal; i++) {
			preFlops.add(new PreFlop(i, pathnameToRollout));
		}
		
		// init
		Game g = new Game(bettingRounds, bigBlindSize);
		for (int i = 0; i < randomPlayers; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new RandomStrategy(), initialMoney));
		}
		for (int i = 0; i < simplePowerRankingPlayersConservative; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new SimplePowerRankingStrategy(AGGRESSIVITY.CONSERVATIVE), initialMoney));
		}
		for (int i = 0; i < simplePowerRankingPlayersModerate; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new SimplePowerRankingStrategy(AGGRESSIVITY.MODERATE), initialMoney));
		}
		for (int i = 0; i < simplePowerRankingPlayersRisky; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new SimplePowerRankingStrategy(AGGRESSIVITY.RISKY), initialMoney));
		}
		for (int i = 0; i < handStrengthPlayers; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new HandStrengthStrategy(preFlops), initialMoney));
		}
		
		// play
		for (int i = 0; i < hands; i++) {
			g.playHand(i);
		}
		
		// print
		g.printCredits();
	}
}
