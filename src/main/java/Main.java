import Player.Plumber;
import Player.Saboteur;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();

        Scanner scanner = new Scanner(System.in);

        System.out.println("How may plumbers are there?");
        int plumbers = scanner.nextInt();
        scanner.nextLine();

        System.out.println("How many saboteurs are there?");
        int saboteurs = scanner.nextInt();
        scanner.nextLine();

        for(int i = 0; i < plumbers; i++) {
            game.addPlayer(new Plumber("Plumber" + i));
        }

        for(int i = 0; i < saboteurs; i++) {
            game.addPlayer(new Saboteur("Saboteur" + i));
        }

        game.start();
    }
}
