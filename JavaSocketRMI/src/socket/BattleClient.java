package socket;

import java.io.*;
import java.net.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BattleClient {
    public static void main(String[] args) {
        BattleClient client = new BattleClient();
        client.start();
    }

    public void start() {
        try (Socket socket = new Socket("localhost", 9999);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to server.");

            boolean gameOver = false;

            while (!gameOver) {
                System.out.println("Choose action: Attack - [1] <-||-> [2] - Flee");

                try {
                    int choice = scanner.nextInt();

                    out.writeInt(choice);
                    out.flush();

                    String serverResponse = (String) in.readObject();
                    System.out.println(serverResponse);

                    if (in.readBoolean()) {
                        gameOver = true;
                    }

                } catch (InputMismatchException | ClassNotFoundException e) {
                    System.out.println("Invalid input or server response.");
                    scanner.next();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
