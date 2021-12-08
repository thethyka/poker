import java.util.ArrayList;
import java.util.Scanner;

public class Player {
	private Scanner s;
	private String name;
	private ArrayList<Card> hand;
	private double money;
	private double wager;
	private int handValue;

	public Player(String name, double money) {
		s = new Scanner(System.in);
		this.name = name;
		wager = 0.0;
		this.money = money;
		hand = new ArrayList<>();
		handValue = 0;
	}

	public String getName() {
		return name;
	}

	public void addToHand(Card c) {
		hand.add(c);
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

	public String seeHand() {
		if (hand.size() == 0) {
			return "no cards!";
		}
		StringBuilder result = new StringBuilder();
		for (Card c : hand) {
			result.append(c.seeCard() + "\n");
		}
		return result.toString();
	}

	public void calculateHandValue() {
		for (Card c : hand) {
			handValue += c.getValue();
		}
	}

	public boolean hasAce() {
		for (Card c : hand) {
			if (c.getRank() == Card.Rank.Ace) {
				return true;
			}
		}
		return false;
	}

	public boolean hasBlackjack() {
		for (Card c : hand) {
			if (c.getRank() == Card.Rank.Ace) {
				if (handValue == 11) {
					return true;
				}
			}
		}
		return false;
	}

	public void clearHand() {
		hand.clear();
	}

	public double getMoney() {
		return money;
	}

	public void addMoney(double n) {
		money += n;
	}

	public double getWager() {
		return wager;
	}

	public void addWager(double w) {
		wager += w;
	}

	public void clearWager() {
		wager = 0;
	}

	public void addHandValue(int n) {
		handValue += n;
	}

	public int getHandValue() {
		return handValue;
	}

	public void clearHandValue() {
		handValue = 0;
	}

	public void examineAce() {
		if (hasAce()) {
			System.out.println("Do you want your ace to be worth 1 or 11?");
			String response = s.nextLine();
			if (response.equals("11")) {
				addHandValue(10);
				System.out.println("Your new hand value is: " + getHandValue());
			}
		}
	}

}
