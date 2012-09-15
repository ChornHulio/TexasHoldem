package core;

import java.util.ArrayList;

import player.ComputerPlayer;
import player.HandStrengthStrategy;
import player.IStrategy.AGGRESSIVITY;
import player.ImprovedHandStrengthStrategy;
import player.OpponentModellingStrategy;
import player.RandomStrategy;
import player.SimplePowerRankingStrategy;
import rollout.PreFlop;

public class Main {	

	public static void main(String[] args) throws Exception {
		
		Logger.DEBUG = false;
		
		int bettingRounds = 3;
		int bigBlindSize = 2;
		int hands = 10000;
		int initialMoney = 0;
		int iterationsOfRollouts = 20; // for ImprovedHandStrenghStrategy
		
		int randomPlayers = 0;
		int simplePowerRankingPlayersRisky = 0;
		int simplePowerRankingPlayersModerate = 0;
		int simplePowerRankingPlayersConservative = 0;
		int handStrengthPlayersRisky = 2;
		int handStrengthPlayersModerate = 2;
		int handStrengthPlayersConservative = 2;
		int improvedHandStrengthPlayersRisky = 0;
		int improvedHandStrengthPlayersModerate = 0;
		int improvedHandStrengthPlayersConservative = 0;
		int modellingPlayersConservativeRisky = 0;
		int modellingPlayersConservativeModerate = 0;
		int modellingPlayersConservativeConservative = 0;
		
		String pathnameToRollout = "./rollouts";
		ArrayList<PreFlop> preFlops = new ArrayList<PreFlop>();
		for (int i = 2; i <= 10; i++) {
			preFlops.add(new PreFlop(i, pathnameToRollout));
		}
				
		// init
		Game g = new Game(bettingRounds, bigBlindSize);
		for (int i = 0; i < randomPlayers; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new RandomStrategy(), initialMoney));
		}
		for (int i = 0; i < simplePowerRankingPlayersRisky; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new SimplePowerRankingStrategy(AGGRESSIVITY.RISKY), initialMoney));
		}
		for (int i = 0; i < simplePowerRankingPlayersModerate; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new SimplePowerRankingStrategy(AGGRESSIVITY.MODERATE), initialMoney));
		}
		for (int i = 0; i < simplePowerRankingPlayersConservative; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new SimplePowerRankingStrategy(AGGRESSIVITY.CONSERVATIVE), initialMoney));
		}
		for (int i = 0; i < handStrengthPlayersRisky; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new HandStrengthStrategy(preFlops,AGGRESSIVITY.RISKY), initialMoney));
		}
		for (int i = 0; i < handStrengthPlayersModerate; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new HandStrengthStrategy(preFlops, AGGRESSIVITY.MODERATE), initialMoney));
		}
		for (int i = 0; i < handStrengthPlayersConservative; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new HandStrengthStrategy(preFlops, AGGRESSIVITY.CONSERVATIVE), initialMoney));
		}
		for (int i = 0; i < improvedHandStrengthPlayersRisky; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new ImprovedHandStrengthStrategy(preFlops, AGGRESSIVITY.RISKY, iterationsOfRollouts), initialMoney));
		}
		for (int i = 0; i < improvedHandStrengthPlayersModerate; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new ImprovedHandStrengthStrategy(preFlops, AGGRESSIVITY.MODERATE, iterationsOfRollouts), initialMoney));
		}
		for (int i = 0; i < improvedHandStrengthPlayersConservative; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new ImprovedHandStrengthStrategy(preFlops, AGGRESSIVITY.CONSERVATIVE, iterationsOfRollouts), initialMoney));
		}
		for (int i = 0; i < modellingPlayersConservativeRisky; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new OpponentModellingStrategy(preFlops, AGGRESSIVITY.RISKY), initialMoney));
		}
		for (int i = 0; i < modellingPlayersConservativeModerate; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new OpponentModellingStrategy(preFlops, AGGRESSIVITY.MODERATE), initialMoney));
		}
		for (int i = 0; i < modellingPlayersConservativeConservative; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new OpponentModellingStrategy(preFlops, AGGRESSIVITY.CONSERVATIVE), initialMoney));
		}
		
		// play
		for (int i = 0; i < hands; i++) {
			if(i % 100 == 0 && i > 0) {
				Logger.logInfo("Hand " + i + " of " + hands);
			}
			g.playHand(i);
		}
		
		// print
		g.printCredits();
	}
}
