package rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.InputMismatchException;
import java.util.Scanner;

public class RMI_BattleClient {

	public static void main(String[] args) {
		try {
			Registry reg = LocateRegistry.getRegistry(1099);
			RMI_BattleInterface server = (RMI_BattleInterface) reg.lookup("BattleServer");
			server.resetGame();

			try (Scanner scanner = new Scanner(System.in)) {
				boolean gameOver = false;

				while (!gameOver) {
					System.out.println("Choose action: Attack - [1] <-||-> [2] - Flee");

					try {
						int choice = scanner.nextInt();

						switch (choice) {
						case 1:
							server.attackMonster();
							break;
						case 2:
							server.flee();
							break;
						default:
							System.err.println("Invalid choice. Please enter 1 to Attack or 2 to Flee.");
							break;
						}

						server.getPlayerHP();
						gameOver = server.isGameOver();

					} catch (InputMismatchException e) {
						System.err.println("Invalid input. Please enter a number (1 or 2).");
						scanner.next();
					}
				}
			}
			System.err.println("\nGame is over, exiting....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
