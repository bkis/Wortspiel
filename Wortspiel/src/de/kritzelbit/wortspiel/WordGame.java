package de.kritzelbit.wortspiel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;


public final class WordGame {
	
	private String langLetters;
	private String gameLetters;
	private SortedSet<String> words;
	private long gameDuration;
	private long startTime;
	private boolean rndLetters;
	private int rndLettersCount;
	private int currentPoints;
	
	
	private static final WordGame INSTANCE = new WordGame();
	
	private WordGame() {
		gameDuration = 0;
		startTime = 0;
	}
	
	public static WordGame getInstance() {
        return INSTANCE;
    }
	
	public void newGame(){
		//check setup
		if (!isSetupComplete()){
			System.out.println("[ERROR] game is not set up properly. the implementation has to call all setup* methods before starting the game.");
			return;
		}
		
		//generate random game letters
		if (rndLetters){
			generateRandomGameLetters();
		}
		
		currentPoints = 0;
		startTime = getTimeMillis();
		System.out.println("[WORDGAME] new game! letters: " + gameLetters);
	}
	
	public int getTimeLeftInSeconds(){
		return (int)(gameDuration - (getTimeMillis() - startTime)) / 1000;
	}
	
	public String getTimeLeftInMinutesAndSeconds(String divider){
		int sec = getTimeLeftInSeconds();
		return (int)(sec/60) + divider + (int)(sec%60);
	}
	
	public int getCurrentPoints(){
		return currentPoints;
	}
	
	public boolean isTimeOut(){
		if ((getTimeMillis() - startTime) >= gameDuration){
			return true;
		}
		return false;
	}
	
	public void setupGameDurationInMinutes(int gameDuration){
		this.gameDuration = ((long)gameDuration)*60000;
	}
	
	public void setupAvailableLetters(String lettersString){
		System.out.print("[WORDGAME] setting up letters set... ");
		lettersString = lettersString.toUpperCase();
		langLetters = "";
		for (int i = 0; i < lettersString.length(); i++) {
			if (!langLetters.contains(lettersString.charAt(i)+"")){
				langLetters += lettersString.charAt(i);
			}
		}
		System.out.println("OK");
	}
	
	public void setupAvailableLettersFromWordList(){
		System.out.print("[WORDGAME] getting letters from words list (this may take a while)... ");
		if (words != null && words.size() > 0){
			langLetters = "";
			String curr;
			for (String word : words){
				curr = word.toUpperCase();
				for (int i = 0; i < curr.length(); i++) {
					if (!langLetters.contains(curr.charAt(i)+"")){
						langLetters += curr.charAt(i);
					}
				}
			}
		} else {
			System.out.println("[ERROR] no words list loaded to pick letters from!\n");
		}
		System.out.println("OK");
	}
	
	public SortedSet<String> setupWordsList(File wordsList){
		System.out.print("[WORDGAME] loading words list... ");
		if (!wordsList.exists() || !wordsList.isFile()){
			System.out.print("Error! Words list \"" + wordsList.getName() + "\" not found...\n");
			return null;
		}
		
		SortedSet<String> words = new TreeSet<String>();
		
		try {
			Scanner scanner = new Scanner(wordsList);
			while (scanner.hasNextLine())
				words.add(scanner.nextLine().toUpperCase());
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("OK");
		return words;
	}
	
	public void setupUseRandomGameLetters(boolean useRandomGameLetters, int letterCount){
		rndLetters = useRandomGameLetters;
		rndLettersCount = letterCount;
	}
	
	public void setGameLetters(String letters){
		gameLetters = letters;
	}
	
	private void generateRandomGameLetters(){
		int rndIndex;
		gameLetters = "";
		for (int i = 0; i < rndLettersCount; i++) {
			rndIndex = (int)((Math.random() * langLetters.length()) + 1);
			gameLetters += langLetters.charAt(rndIndex);
		}
		gameLetters = gameLetters.toUpperCase();
	}
	
	private boolean isSetupComplete(){
		if (langLetters != null
			&& langLetters.length() > 0
			&& words != null
			&& words.size() > 0
			&& gameDuration != 0){
				return true;
			}
		return false;
	}
	
	private long getTimeMillis(){
		return System.currentTimeMillis();
	}
	
	

}
