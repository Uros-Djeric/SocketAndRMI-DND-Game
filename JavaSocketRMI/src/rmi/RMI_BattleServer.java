package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class RMI_BattleServer extends UnicastRemoteObject implements RMI_BattleInterface {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int playerHP = 50;
    private int monsterHP = 150;
    private boolean gameOver = false;

    public RMI_BattleServer() throws RemoteException {
        super();
        resetGame();
    }

    @Override
    public void attackMonster() throws RemoteException {
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

    @Override
    public void flee() throws RemoteException {
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

    @Override
    public void getPlayerHP() throws RemoteException {
        System.out.println("Player HP: " + playerHP);
        System.out.println("Monster HP: " + monsterHP);
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

    public static void main(String[] args) {
        try {
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("BattleServer", new RMI_BattleServer());
            System.out.println("Server started.\n");
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
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isGameOver() throws RemoteException {
        return playerHP <= 0 || monsterHP <= 0;
    }

	@Override
	public void resetGame() throws RemoteException {
		playerHP = 50;
        monsterHP = 150;
        gameOver = false;
        System.out.println("Game has been reset.\n\n");
    }
}

