package de.kritzelbit.wortspiel;

import java.io.File;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		File words = new File("de.txt");
		Scanner scanner = new Scanner(System.in);
		WordGame game = WordGame.getGameInstance();
		String input = "";
		
		//game setup
		game.setupGameDurationInMinutes(3);
		game.setupUseRandomGameLetters(true, 8);
		game.setupWordsList(words);
		game.setupAvailableLettersFromWordList();
		
		//intro
		System.out.println("######################");
		System.out.println("#                    #");
		System.out.println("#    EL WORTSPIELO   #");
		System.out.println("#                    #");
		System.out.println("######################\n");
		
		//game loop
		while (!input.equalsIgnoreCase("exit")){
			System.out.println("[WORTSPIEL] Zeit übrig: " + game.getTimeLeftInMinutesAndSeconds(":") + " Minuten");
			System.out.println("[WORTSPIEL] Buchstaben: " + game.getGameLetters());
			
		}
		
		//cleanup
		scanner.close();
	}

}
