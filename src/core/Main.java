package core;

import java.util.ArrayList;

import player.ComputerPlayer;
import player.strategy.HandStrengthStrategy;
import player.strategy.IStrategy.AGGRESSIVITY;
import player.strategy.ImprovedHandStrengthStrategy;
import player.strategy.RandomStrategy;
import player.strategy.SimplePowerRankingStrategy;
import player.strategy.opponentModel.OpponentModel;
import player.strategy.opponentModel.OpponentModelStrategy;
import rollout.PreFlop;

// TODO comments

public class Main {	

	public static void main(String[] args) throws Exception {
		
		Logger.DEBUG = false;
		
		int bettingRounds = 3;
		int bigBlindSize = 20;
		int hands = 10000;
		int initialMoney = 0;
		int iterationsOfRollouts = 20; // for ImprovedHandStrenghStrategy
		
		int randomPlayers = 0;
		int simplePowerRankingPlayersRisky = 0;
		int simplePowerRankingPlayersModerate = 0;
		int simplePowerRankingPlayersConservative = 0;
		int handStrengthPlayersRisky = 0;
		int handStrengthPlayersModerate = 2;
		int handStrengthPlayersConservative = 0;
		int improvedHandStrengthPlayersRisky = 0;
		int improvedHandStrengthPlayersModerate = 0;
		int improvedHandStrengthPlayersConservative = 0;
		int modellingPlayersRisky = 0;
		int modellingPlayersModerate = 2;
		int modellingPlayersConservative = 0;
		
		String pathnameToRollout = "./rollouts";
		ArrayList<PreFlop> preFlops = new ArrayList<PreFlop>();
		for (int i = 2; i <= 10; i++) {
			preFlops.add(new PreFlop(i, pathnameToRollout));
		}

		
		ArrayList<OpponentModel> opponentModels = new ArrayList<OpponentModel>();
				
		// init
		int count = 0;
		Game g = new Game(bettingRounds, bigBlindSize, preFlops, opponentModels);
		for (int i = 0; i < randomPlayers; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new RandomStrategy(count++), initialMoney));
		}
		for (int i = 0; i < simplePowerRankingPlayersRisky; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new SimplePowerRankingStrategy(count++, AGGRESSIVITY.RISKY), initialMoney));
		}
		for (int i = 0; i < simplePowerRankingPlayersModerate; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new SimplePowerRankingStrategy(count++, AGGRESSIVITY.MODERATE), initialMoney));
		}
		for (int i = 0; i < simplePowerRankingPlayersConservative; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new SimplePowerRankingStrategy(count++, AGGRESSIVITY.CONSERVATIVE), initialMoney));
		}
		for (int i = 0; i < handStrengthPlayersRisky; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new HandStrengthStrategy(count++, preFlops,AGGRESSIVITY.RISKY), initialMoney));
		}
		for (int i = 0; i < handStrengthPlayersModerate; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new HandStrengthStrategy(count++, preFlops, AGGRESSIVITY.MODERATE), initialMoney));
		}
		for (int i = 0; i < handStrengthPlayersConservative; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new HandStrengthStrategy(count++, preFlops, AGGRESSIVITY.CONSERVATIVE), initialMoney));
		}
		for (int i = 0; i < improvedHandStrengthPlayersRisky; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new ImprovedHandStrengthStrategy(count++, preFlops, AGGRESSIVITY.RISKY, iterationsOfRollouts), initialMoney));
		}
		for (int i = 0; i < improvedHandStrengthPlayersModerate; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new ImprovedHandStrengthStrategy(count++, preFlops, AGGRESSIVITY.MODERATE, iterationsOfRollouts), initialMoney));
		}
		for (int i = 0; i < improvedHandStrengthPlayersConservative; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new ImprovedHandStrengthStrategy(count++, preFlops, AGGRESSIVITY.CONSERVATIVE, iterationsOfRollouts), initialMoney));
		}
		for (int i = 0; i < modellingPlayersRisky; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new OpponentModelStrategy(count++, preFlops, AGGRESSIVITY.RISKY, opponentModels), initialMoney));
		}
		for (int i = 0; i < modellingPlayersModerate; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new OpponentModelStrategy(count++, preFlops, AGGRESSIVITY.MODERATE, opponentModels), initialMoney));
		}
		for (int i = 0; i < modellingPlayersConservative; i++) {
			g.addPlayer(new ComputerPlayer(g.getState(), new OpponentModelStrategy(count++, preFlops, AGGRESSIVITY.CONSERVATIVE, opponentModels), initialMoney));
		}
		
		// init models
		for (int i = 0; i < g.getNumberOfPlayers(); i++) {
			opponentModels.add(new OpponentModel(i));
		}
		
		// play
		for (int i = 0; i < hands; i++) {
			if(i % 100 == 0 && i > 0) {
				Logger.logInfo("Hand " + i + " of " + hands);
			}
			if(i % 1000 == 0 && i > 0) {
				g.printCredits();
			}
			g.playHand(i);
		}
		
		int index = 0;
		for (OpponentModel opponentModel : opponentModels) {
			Logger.logDebug("Model of opponent " + index++ + ":\n" + opponentModel);
		}
		
		// print
		g.printCredits();
	}
}
