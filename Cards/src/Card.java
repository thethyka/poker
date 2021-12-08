
public class Card {
	
	public enum Suit {
		Hearts, Diamonds, Spades, Clubs;
	}
	public enum Rank {
		Ace, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King;
	}
	

	private Suit suit;
	private Rank rank;
	private int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};
	private int value;

	public Card(int s, int r) {
		if(s < 0 || s > 3 ) {
			throw new IllegalArgumentException ("Invalid suit!!!!!! dummy!!");
		} else if (r < 0 || r > 12) {
			throw new IllegalArgumentException ("Invalid rank!!! sily");
		}
		suit = Suit.values()[s];
		rank = Rank.values()[r];
		value = values[r];
	}

	public Suit getSuit() {
		return suit;
	}

	public Rank getRank() {
		return rank;
	}

	public int getValue() {
		return value;
	}

	public String seeCard() {
		return "The " + rank + " of " + suit;
	}

	public void increaseValue(int n) {
		value += n;
	}
	
	
	public static void main(String[] args) {
		Card c = new Card(1,3);
		System.out.println(c.seeCard());
		System.out.println(c.getValue());
		System.out.println(c.toString());
	}

}
