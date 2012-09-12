package Core;

public class Card implements Comparable<Card>{

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

	@Override
	public int compareTo(Card card){
//		if(sign.ordinal() > card.sign.ordinal())
//			return 1;
//		else if(sign.ordinal() < card.sign.ordinal())
//			return -1;
		
		if(value.ordinal() > card.value.ordinal())
			return 1;
		else if(value.ordinal() < card.value.ordinal())
			return -1;
		return 0;
	}
	
	public String toString(){
		return "(" + value + "|" + sign + ")";
	}
	
	public static void main(String[] args) {
		Card c1 = new Card(SIGN.DIAMONDS, VALUE.EIGHT);
		Card c2 = new Card(SIGN.HEARTS, VALUE.ACE);
		System.out.println(c2.compareTo(c2));
	}
}
