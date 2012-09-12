package Core;

import java.util.ArrayList;

public class CardPower implements Comparable<CardPower>{

	public ArrayList<Integer> power = new ArrayList<Integer>();
	
	@Override
	public int compareTo(CardPower cp){
		for (int i = 0; i < power.size(); i++) {
			if(power.get(i) > cp.get(i))
				return 1;
			else if(power.get(i) < cp.get(i))
				return -1;
		}
		return 0;
	}
	
	public int get(int index){
		return power.get(index);
	}
	
	public CardPower add(int p){
		power.add(p);
		return this;
	}
	
	public String toString(){
		String out = "[";
		for (Integer p : power) {
			out += p + ", ";
		}
		return out.substring(0, out.length()-2)+"]";
	}
	
	public static void main(String[] args) {
		CardPower p1 = new CardPower();
		p1.add(1).
		add(10).
		add(14);
		CardPower p2 = new CardPower();
		p2.add(8)
		.add(10)
		.add(14)
		.add(9);
		
		System.out.println(p2.compareTo(p1));

	}

}
