import java.util.ArrayList;
import java.util.Random;

public class Deck {

	private ArrayList<Card> cards;

	public Deck() {
		cards = new ArrayList<>();
	}

	public void makeDeck52() {
		for (int r = 0; r < 13; r++) {
			for (int s = 0; s < 4; s++) {
				cards.add(new Card(s, r));
			}
		}
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public String seeCards() {
		String str = "";
		for (Card c : cards) {
			str += c.seeCard() + "\n"; 
		}
		return str;
	}

	public Card giveCard() {
		Random random = new Random();
		if (cards.size() == 0) {
			throw new IllegalArgumentException("You out of cards fam.");
		}
		Card c = cards.get(random.nextInt(cards.size()));
		cards.remove(c);
		return c;
	}

	public void clear() {
		cards.clear();
	}

	public static void main(String[] args) {
		Deck d = new Deck();
		d.makeDeck52();
		System.out.println(d.seeCards());
	}

}
