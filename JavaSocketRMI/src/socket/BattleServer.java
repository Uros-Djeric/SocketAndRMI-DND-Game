package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class BattleServer {
	private int playerHP = 50;
	private int monsterHP = 150;
	private boolean gameOver = false;

	public static void main(String[] args) {
		BattleServer server = new BattleServer();
		try {
			server.start();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() throws ClassNotFoundException {
		try (ServerSocket serverSocket = new ServerSocket(9999)) {
			System.out.println("Server started.\n\n");
			System.out.println("        ,     \\    /      ,        \r\n"
            		+ "       / \\    )\\__/(     / \\       \r\n"
            		+ "      /   \\  (_\\  /_)   /   \\      \r\n"
            		+ " ____/_____\\__\\@  @/___/_____\\____ \r\n"
            		+ "|             |\\../|              |\r\n"
            		+ "|              \\VV/               |\r\n"
            		+ "|        -----D N D-------        |\r\n"
            		+ "|_________________________________|\r\n"
            		+ " |    /\\ /      \\\\       \\ /\\    | \r\n"
            		+ " |  /   V        ))       V   \\  | \r\n"
            		+ " |/     `       //        '     \\| \r\n"
            		+ " `              V                '");

			while (!Thread.interrupted()) {
				try (Socket socket = serverSocket.accept();
						ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
						ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

					System.out.println("Client connected.");

					while (!gameOver) {
						int choice = in.readInt();

						switch (choice) {
						case 1:
							attackMonster();
							break;
						case 2:
							flee();
							break;
						default:
							out.writeObject("Invalid choice. Please enter 1 to Attack or 2 to Flee.");
							break;
						}

						out.writeObject("Player HP: " + playerHP + "\nMonster HP: " + monsterHP);
						out.writeBoolean(isGameOver());
						out.flush();
					}
					resetGame();
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void attackMonster() {
		if (gameOver) {
			return;
		}

		Random rand = new Random();
		int roll = rand.nextInt(20) + 1;

		System.out.println("Dice rolled: " + roll);

		if (roll >= 1 && roll <= 10) {
			monsterHP -= 10;
			System.out.println("\nPlayer attacked the monster, dealing 10 damage!");
		} else if (roll >= 11 && roll <= 13) {
			playerHP -= 7;
			System.out.println("\nDemogorgon attacked the player, dealing 7 damage!");
		} else if (roll == 15 || roll == 16 || roll == 17) {
			playerHP += 2;
			System.out.println("\nPlayer gained 2 HP from the encouragement!");
		} else if (roll == 18) {
			monsterHP -= 20;
			System.out.println("\nPlayer delivered a critical strike, dealing 20 damage to the monster!");
		} else if (roll == 19) {
			playerHP -= 10;
			System.out.println("\nDemogorgon critically hit the player, dealing 10 damage!");
		} else if (roll == 20) {
			System.out.println("\nLucky roll! Nothing happens this turn.");
		}

		checkGameStatus();
	}

	private void flee() {
		if (gameOver) {
			return;
		}

		Random rand = new Random();
		int roll = rand.nextInt(20) + 1;

		System.out.println("Dice rolled: " + roll);

		if (roll >= 11 && roll <= 13) {
			playerHP -= 2;
			System.out.println("\nPlayer hesitated and got injured while trying to flee, losing 2 HP!");
		} else if (roll == 19) {
			playerHP -= 7;
			System.out.println("\nPlayer was injured while trying to flee, losing 7 HP!");
		} else {
			System.out.println("\nPlayer cautiously takes a step back, avoiding any damage.");
		}

		checkGameStatus();
	}

	private boolean isGameOver() {
		return playerHP <= 0 || monsterHP <= 0;
	}

	private void checkGameStatus() {
		if (playerHP <= 0) {
			System.out.println("\nPlayer was defeated! Game over.");
			System.out.println("███████████████████████████\r\n"
					+ "███████▀▀▀░░░░░░░▀▀▀███████\r\n"
					+ "████▀░░░░░░░░░░░░░░░░░▀████\r\n"
					+ "███│░░░░░░░░░░░░░░░░░░░│███\r\n"
					+ "██▌│░░░░░░░░░░░░░░░░░░░│▐██\r\n"
					+ "██░└┐░░░░░░░░░░░░░░░░░┌┘░██\r\n"
					+ "██░░└┐░░░░░░░░░░░░░░░┌┘░░██\r\n"
					+ "██░░┌┘▄▄▄▄▄░░░░░▄▄▄▄▄└┐░░██\r\n"
					+ "██▌░│██████▌░░░▐██████│░▐██\r\n"
					+ "███░│▐███▀▀░░▄░░▀▀███▌│░███\r\n"
					+ "██▀─┘░░░░░░░▐█▌░░░░░░░└─▀██\r\n"
					+ "██▄░░░▄▄▄▓░░▀█▀░░▓▄▄▄░░░▄██\r\n"
					+ "████▄─┘██▌░░░░░░░▐██└─▄████\r\n"
					+ "█████░░▐█─┬┬┬┬┬┬┬─█▌░░█████\r\n"
					+ "████▌░░░▀┬┼┼┼┼┼┼┼┬▀░░░▐████\r\n"
					+ "█████▄░░░└┴┴┴┴┴┴┴┘░░░▄█████\r\n"
					+ "███████▄░░░░░░░░░░░▄███████\r\n"
					+ "██████████▄▄▄▄▄▄▄██████████\r\n"
					+ "███████████████████████████");
			gameOver = true;
		} else if (monsterHP <= 0) {
			System.out.println("\nMonster was defeated! Congratulations, you won!");
			System.out.println("\n\\( ﾟヮﾟ)/🏆");
			gameOver = true;
		}
	}

	private void resetGame() {
		playerHP = 50;
		monsterHP = 150;
		gameOver = false;
		System.out.println("\n\n[Game has been reset. Player HP: " + playerHP + ", Monster HP: " + monsterHP + "]");
	}
}
