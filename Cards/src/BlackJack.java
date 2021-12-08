import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BlackJack {
	private Deck deck;
	private File file;
	private Dealer dealer;
	private ArrayList<Player> players;
	private boolean firstTime;
	private Scanner s;
	private Random r;
	private final double epsilon = 10e-4;

	public BlackJack(String filename) {
		deck = new Deck();
		deck.makeDeck52();
		file = new File(filename);
		players = new ArrayList<>();
		dealer = new Dealer("Mr. Dealer", 0);
		firstTime = true;
		s = new Scanner(System.in);
		r = new Random();
	}

	/**
	 * parses the player name then how much money they have eg file structure:
	 * --------------------- Jim, $245.2 Rupert, $31 ----------------------
	 */
	public void fileOrDefault() {
		System.out.println("Would you like to scan a file or use default players?");
		boolean done = false;
		while(!done) {
			System.out.println("Type 'scan' or 'default'");
			String response = s.nextLine();
			if(response.equals("scan")) {
				done = true;
			} else if(response.equals("default")) {
				done = true;
			}
		}
		
	}
	public void processFile() {
		
		ArrayList<String> lines = file.getLines();
		for (String line : lines) {
			for (int i = 0; i < line.length(); i++) {

				// find the , in the line that separates the player name and their money.
				if (line.charAt(i) == ',') {
					// name and money
					players.add(new Player(line.substring(0, i), Double.parseDouble(line.substring(i + 3))));
				}
			}
		}
		if (players.size() == 0) {
			throw new IllegalArgumentException("Error! No players found.");
		}
		System.out.println("File Processed!");
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public ArrayList<String> getFile() {
		return file.getLines();
	}

	public ArrayList<Card> getDeck() {
		return deck.getCards();
	}

	/**
	 * each player places their bets.
	 */
	public void wager() {
		for (Player p : players) {

			if (p.getMoney() < epsilon) {
				System.out.println("It seems you have no money left, " + p.getName());
				throw new IllegalArgumentException("no money");
			}

			System.out.println(p.getName() + ", Bet a random amount or choose?");
			System.out.println("You have " + p.getMoney() + " dollars left");

			String response = s.nextLine();
			boolean end = false;
			while (!end) {

				if ((response.equals("choose") || response.equals("random"))) {
					end = true;
				} else {
					System.out.println("choose or random?");
					response = s.nextLine();
				}
			}

			if (response.equalsIgnoreCase("choose")) {

				String number;
				boolean done = false;
				while (!done) {
					try {
						System.out.println("How much money do you want to bet?");
						number = s.nextLine();
						double numb = Double.parseDouble(number);

						if (numb > p.getMoney()) {
							System.out.println("You dont have that much money!");
							continue;
						} else if (numb < 0) {
							System.out.println("You cant bet -ive amounts!");
							continue;
						}
						done = true;
						p.addWager(numb);
						p.addMoney(-numb);
					} catch (Exception e) {
						System.out.println("Give me a number!");
					}
				}

			} else {
				p.addWager(r.nextInt((int) p.getMoney()));
				p.addMoney(-p.getWager());
			}

			System.out.println(p.getName() + " has bet $" + p.getWager());
			System.out.println("Your account now has $" + p.getMoney() + "\n");
		}
	}

	public void deal() {
		System.out.println("Type anything to continue.");
		s.nextLine();
		for (Player p : players) {
			p.addToHand(deck.giveCard());
			p.addToHand(deck.giveCard());
			System.out.println(p.getName() + " has\n" + p.seeHand());
		}
	}

	public void play() {
		for (Player p : players) {
			System.out.println("Now it's " + p.getName() + "'s turn.");

			// looking at hand.
			System.out.println(p.seeHand());
			p.calculateHandValue();

			// checking for blackjack
			if (p.hasBlackjack()) {
				p.addHandValue(10);
				System.out.println("Blackjack!");
				continue;
			}

			System.out.println("This hand is worth " + p.getHandValue() + " points");

			p.examineAce();
			String response;
			Card latestCard;
			boolean end = false;
			while (!end) {
				do {
					System.out.println("Hit or Stand?");
					response = s.nextLine();
				} while (!(response.equalsIgnoreCase("hit") || response.equalsIgnoreCase("stand")));
				if (response.equalsIgnoreCase("stand")) {
					end = true;
				}
				if (response.equalsIgnoreCase("hit")) {
					p.addToHand((deck.giveCard()));
					latestCard = p.getHand().get(p.getHand().size() - 1);
					p.addHandValue(latestCard.getValue());
					System.out.println(p.seeHand());

					// checking for ace.
					if (latestCard.getValue() == 1) {
						System.out.println("Do you want your ace to represent 1 or 11?");
						String res = s.nextLine();
						if (res.equals("11")) {
							p.addHandValue(10);
							System.out.println("Your new hand value is: " + p.getHandValue());
						}
					}
					System.out.println(p.getHandValue());
					if (p.getHandValue() == 21) {
						break;
					} else if (p.getHandValue() > 21) {
						System.out.println("Bust!\n");
						break;
					}
				}
			}
		}
	}

	public void dealer() {
		dealer.addToHand(deck.giveCard());
		dealer.addToHand(deck.giveCard());
		System.out.println("Now it's " + dealer.getName() + "'s turn.");

		// looking at hand.
		System.out.println(dealer.seeHand());
		dealer.calculateHandValue();

		// checking for blackjack
		if (dealer.hasBlackjack()) {
			dealer.addHandValue(10);
			System.out.println("Blackjack!");
			return;
		}

		System.out.println("This hand is worth " + dealer.getHandValue() + " points");
		// checking for the ace.
		if (dealer.hasAce()) {
			System.out.println("Do you want your ace to represent 1 or 11?");
			String response = s.nextLine();
			if (response.equals("11")) {
				dealer.addHandValue(10);
				System.out.println("Your new hand value is: " + dealer.getHandValue());
			}
		}

		// hitting or standing.
		String response;
		Card latestCard;
		boolean end = false;
		while (!end) {
			do {
				System.out.println("Hit or Stand?");
				response = s.nextLine();
			} while (!(response.equalsIgnoreCase("hit") || response.equalsIgnoreCase("stand")));
			if (response.equalsIgnoreCase("stand")) {
				end = true;
			}
			if (response.equalsIgnoreCase("hit")) {
				dealer.addToHand(deck.giveCard());
				latestCard = dealer.getHand().get(dealer.getHand().size() - 1);
				dealer.addHandValue(latestCard.getValue());
				System.out.println(dealer.seeHand());

				// checking for ace.
				if (latestCard.getValue() == 1) {
					System.out.println("Do you want your ace to represent 1 or 11?");
					String res = s.nextLine();
					if (res.equals("11")) {
						dealer.addHandValue(10);
						System.out.println("Your new hand value is: " + dealer.getHandValue());
					}
				}
				System.out.println(dealer.getHandValue());
				if (dealer.getHandValue() == 21) {
					break;
				} else if (dealer.getHandValue() > 21) {
					System.out.println("Bust!\n");
					break;
				}
			}
		}
	}

	public void results() {
		for (Player p : players) {
			System.out.println(p.getName() + ", Your final value was " + p.getHandValue() + " while " + dealer.getName()
					+ "'s final value was " + dealer.getHandValue());
			if ((p.getHandValue() < dealer.getHandValue() && dealer.getHandValue() < 22) || p.getHandValue() > 21) {
				System.out.println("Sorry, you lost your wager!\n");
			} else if (p.getHandValue() == dealer.getHandValue()) {
				System.out.println("Your wager was bumped back, you lost no money.");
				p.addMoney(p.getWager());
			} else if (p.hasBlackjack()) {
				System.out.println("You got Blackjack! Your wager was returned 1.5 fold!");
				p.addMoney(p.getWager() * 1.5);
			} else {
				System.out.println("You won! Your wager was retuned 2 fold!");
				p.addMoney(p.getWager() * 2);
			}
			dealer.addMoney(p.getWager());
			p.clearHandValue();
			p.clearWager();
			p.clearHand();
			System.out.println("You now have $" + p.getMoney() + " remaining.\n");

		}
		System.out.println(dealer.getName() + " now has $" + dealer.getMoney());
		dealer.clearHandValue();
		dealer.clearHand();
		deck.clear();
		deck = new Deck();
		deck.makeDeck52();
		System.out.println("Play again?");
		String again = s.nextLine();
		if (again.equalsIgnoreCase("yes") || again.equalsIgnoreCase("ok") || again.equalsIgnoreCase("sure")) {
			firstTime = false;
			playGame();
		}

	}

	public void playGame() {

		if (firstTime) {
			processFile();
		}

		wager();
		deal();
		play();
		dealer();
		results();
		s.close();
	}

	public static void main(String[] args) {
		BlackJack b = new BlackJack("blackjacktest0.txt");
		b.playGame();
	}
}
