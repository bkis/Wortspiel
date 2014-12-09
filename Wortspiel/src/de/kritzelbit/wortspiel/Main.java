package de.kritzelbit.wortspiel;

import java.io.File;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		File words = new File("data/de.txt");
		Scanner scanner = new Scanner(System.in);
		WordGame game = WordGame.getGameInstance();
		String input = "";
		
		//game setup
		game.setPrintDebugMessages(false);
		game.setupGameDurationInMinutes(3);
		game.setupWordsList(words);
		game.setupAvailableLettersFromWordList();
		game.setupUseRandomGameLetters(false, 0);
		
		//game.setGameLetters(game.getRandomWordFromList(10));
		
		//intro
		System.out.println("######################");
		System.out.println("#                    #");
		System.out.println("#    EL WORTSPIELO   #");
		System.out.println("#                    #");
		System.out.println("######################\n");
		
		System.out.print("Bitte gib das Quellwort ein: ");
		game.setGameLetters(scanner.nextLine());
		System.out.println("\n");
		
		//game loop
		game.newGame();
		while (!game.isTimeOut()){
			System.out.println("======================================");
			System.out.println("Zeit übrig: " + game.getTimeLeftInMinutesAndSeconds(":") + " Minuten");
			System.out.println("Buchstaben: " + game.getGameLetters());
			
			input = scanner.nextLine();
			
			if (input.equalsIgnoreCase("exit")){
				scanner.close();
				System.out.println("[WORTSPIEL] wird beendet...");
				System.exit(0);
			}
			
			System.out.println("======================================");
			
			if (!game.isValid(input)){
				System.out.println("Dieses Wort lässt sich nicht aus \"" + game.getGameLetters() + "\" bilden!");
			} else if (!game.isInList(input)) {
				System.out.println("Was soll das denn sein?!");
			} else if (game.isAlreadyGuessed(input)){
				System.out.println("Wort \"" + input.toUpperCase() + "\" bereits gefunden!");
			} else {
				System.out.println("RICHTIG! +" + game.addPoints(input.length()) + " Punkte!");
				game.markAsGuessed(input);
			}
			
			//game.shuffleGameLetters();
		}
		
		System.out.println("======================================");
		System.out.println("ZEIT ABGELAUFEN! PUNKTESTAND: " + game.getCurrentPoints());
		System.out.println("======================================");
		System.out.println("Diese W�rter w�ren m�glich gewesen:");
		
		for (String word : game.getAllPossibleWords()) System.out.print(word + " ");
	}
	
}
