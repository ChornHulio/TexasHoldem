package core;

public class Card implements Comparable<Card> {

	public enum SIGN {
		HEARTS {
			public String toString() {
				return "\u2764";
			}
		},
		DIAMONDS{
			public String toString() {
				return "\u2666";
			}
		},
		SPADES{
			public String toString() {
				return "\u2660";
			}
		},
		CLUBS{
			public String toString() {
				return "\u2663";
			}
		};
	}

	public enum VALUE {
		TWO {
			public String toString() {
				return "2";
			}
		},
		THREE {
			public String toString() {
				return "3";
			}
		},
		FOUR {
			public String toString() {
				return "4";
			}
		},
		FIVE {
			public String toString() {
				return "5";
			}
		},
		SIX {
			public String toString() {
				return "6";
			}
		},
		SEVEN {
			public String toString() {
				return "7";
			}
		},
		EIGHT {
			public String toString() {
				return "8";
			}
		},
		NINE {
			public String toString() {
				return "9";
			}
		},
		TEN {
			public String toString() {
				return "10";
			}
		},
		JACK {
			public String toString() {
				return "J";
			}
		},
		QUEEN {
			public String toString() {
				return "Q";
			}
		},
		KING {
			public String toString() {
				return "K";
			}
		},
		ACE {
			public String toString() {
				return "A";
			}
		};
	}

	public SIGN sign;
	public VALUE value;

	public Card(SIGN sign, VALUE value) {
		this.sign = sign;
		this.value = value;
	}

	@Override
	public int compareTo(Card card) {
		if (value.ordinal() > card.value.ordinal())
			return 1;
		else if (value.ordinal() < card.value.ordinal())
			return -1;
		return 0;
	}

	public String toString() {
		return "(" + value + " " + sign + ")";
	}

	public static void main(String[] args) {
		Card c1 = new Card(SIGN.DIAMONDS, VALUE.EIGHT);
		Card c2 = new Card(SIGN.HEARTS, VALUE.ACE);
		System.out.println(c2.compareTo(c2));
	}
}
