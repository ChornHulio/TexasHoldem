package Core;

public class Card {

	public enum SIGN {
		HEARTS,
		DIAMONDS,
		SPADES,
		CLUBS;
	}
	
	public enum VALUE {
		TWO,
		THREE,
		FOUR,
		FIVE,
		SIX,
		SEVEN,
		EIGHT,
		NINE,
		TEN,
		JACK,
		QUEEN,
		KING,
		ACE;
	}
	
	public SIGN sign;
	public VALUE value;
	
	public Card(SIGN sign, VALUE value){
		this.sign = sign;
		this.value = value;
	}
}
