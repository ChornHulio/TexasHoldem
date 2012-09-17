package player.strategy.opponentModel;

import core.State.STAGE;

public class Context {

	private STAGE stage;
	private int numberOfPlayers;
	private int numberOfRaises; // per round
	private double potOdd;

	public void setStage(STAGE stage) {
		this.stage = stage;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	public void setNumberOfRaises(int numberOfRaises) {
		this.numberOfRaises = numberOfRaises;
	}

	public void setPotOdd(double potOdd) {
		this.potOdd = potOdd;
	}

	public STAGE getStage() {
		return stage;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	@Override
	/**
	 * Defines thresholds for considering contexts being equal
	 * E.g. two contexts where the pot odds are 0.1 and 0.11 should be considered equal
	 */
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;

		if (!(o instanceof Context))
			return false;
		Context that = (Context) o;

		if (this.stage != that.stage) {
			return false;
		}

		// number of players
		int[] numberOfPlayersThresholds = { 0, 4, 8 };
		for (int i = 1; i < numberOfPlayersThresholds.length; i++) {
			if (((this.numberOfPlayers < numberOfPlayersThresholds[i] && this.numberOfPlayers >= numberOfPlayersThresholds[i - 1]) && !(that.numberOfPlayers < numberOfPlayersThresholds[i] && that.numberOfPlayers >= numberOfPlayersThresholds[i - 1]))) {
				return false;
			}
		}
		if ((this.numberOfPlayers >= numberOfPlayersThresholds[numberOfPlayersThresholds.length - 1])
				&& !(that.numberOfPlayers >= numberOfPlayersThresholds[numberOfPlayersThresholds.length - 1])) {
			return false;
		}

		// number of raises
		int[] raiseThresholds = { 0, 3 };
		for (int i = 1; i < raiseThresholds.length; i++) {
			if (((this.numberOfRaises < raiseThresholds[i] && this.numberOfRaises >= raiseThresholds[i - 1]) && !(that.numberOfRaises < raiseThresholds[i] && that.numberOfRaises >= raiseThresholds[i - 1]))) {
				return false;
			}
		}
		if ((this.numberOfRaises >= raiseThresholds[raiseThresholds.length - 1])
				&& !(that.numberOfRaises >= raiseThresholds[raiseThresholds.length - 1])) {
			return false;
		}

		// pot odds
		double[] potOddsThresholds = { 0.0, 0.3 };
		for (int i = 1; i < potOddsThresholds.length; i++) {
			if (((this.potOdd < potOddsThresholds[i] && this.potOdd >= potOddsThresholds[i - 1]) && !(that.potOdd < potOddsThresholds[i] && that.potOdd >= potOddsThresholds[i - 1]))) {
				return false;
			}
		}
		if ((this.potOdd >= potOddsThresholds[potOddsThresholds.length - 1])
				&& !(that.potOdd >= potOddsThresholds[potOddsThresholds.length - 1])) {
			return false;
		}

		// note: stage not important for HandStrenghStrategy

		return true;
	}

	@Override
	public String toString() {
		return stage + " | No.Players " + numberOfPlayers + " | No.Raises "
				+ numberOfRaises + " | PotOdd " + potOdd;
	}

	public static void main(String[] args) {
		// poor man's unit tests
		Context c1 = new Context();
		c1.setNumberOfPlayers(1);
		Context c2 = new Context();
		c2.setNumberOfPlayers(2);
		Context c3 = new Context();
		c3.setNumberOfPlayers(3);

		System.out.println(c1.equals(c2) == true);
		System.out.println(c1.equals(c3) == false);

		c3.setNumberOfPlayers(2);
		System.out.println(c1.equals(c3) == true);

		c1.setNumberOfRaises(2);
		c2.setNumberOfRaises(4);
		c3.setNumberOfRaises(3);
		System.out.println(c1.equals(c2) == false);
		System.out.println(c1.equals(c3) == true);
		c1.setNumberOfRaises(44);
		c2.setNumberOfRaises(44);
		c3.setNumberOfRaises(9);
		System.out.println(c1.equals(c2) == true);
		System.out.println(c1.equals(c3) == false);
		c3.setNumberOfRaises(44);

		c1.setPotOdd(0.0);
		c2.setPotOdd(0.1);
		c3.setPotOdd(0.2);
		System.out.println(c1.equals(c2) == true);
		System.out.println(c1.equals(c3) == false);
		c1.setPotOdd(0.2);
		c2.setPotOdd(0.3);
		c3.setPotOdd(0.1);
		System.out.println(c1.equals(c2) == true);
		System.out.println(c1.equals(c3) == false);
		c1.setPotOdd(1);
		c2.setPotOdd(1);
		c3.setPotOdd(0.4);
		System.out.println(c1.equals(c2) == true);
		System.out.println(c1.equals(c3) == false);
	}
}
