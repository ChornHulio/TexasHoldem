package core.card;

import java.util.ArrayList;
import java.util.Collections;

public class CardPower implements Comparable<CardPower>{

	public ArrayList<Integer> power = new ArrayList<Integer>();
	
	@Override
	public int compareTo(CardPower cp){
		for (int i = 0; i < power.size(); i++) {
			if(power.get(i) > cp.getAt(i))
				return 1;
			else if(power.get(i) < cp.getAt(i))
				return -1;
		}
		return 0;
	}
	
	public int getAt(int index){
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
		p1.add(8).
		add(12).
		add(11).
		add(10).
		add(9).
		add(6);
		CardPower p2 = new CardPower();
		p2.add(8).
		add(12).
		add(11).
		add(10).
		add(9).
		add(3).
		add(6);
		
		System.out.println(p2.compareTo(p1));
		ArrayList<CardPower> cp = new ArrayList<CardPower>();
		cp.add(p1);
		cp.add(p2);
		System.out.println(Collections.max(cp));
		
		CardPower maxPower = Collections.max(cp);
		for (int i = 0; i < cp.size(); i++) {
			if(cp.get(i).compareTo(maxPower) == 0) {
				System.out.println(i);
			}
		}
		

	}

}
