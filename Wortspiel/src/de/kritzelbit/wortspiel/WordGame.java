package de.kritzelbit.wortspiel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;


public final class WordGame {
	
	private String langLetters;
	private String gameLetters;
	private SortedSet<String> words;
	private Set<String> guessed;
	private long gameDuration;
	private long startTime;
	private boolean rndLetters;
	private int rndLettersCount;
	private int currentPoints;
	private boolean debugMsgs;
	private Random rnd;
	
	
	private static final WordGame INSTANCE = new WordGame();
	
	private WordGame() {
		gameDuration = 0;
		startTime = 0;
		debugMsgs = true;
		rnd = new Random();
	}
	
	public static WordGame getGameInstance() {
        return INSTANCE;
    }
	
	public void newGame(){
		//check setup
		if (!isSetupComplete()){
			print("[ERROR] game is not set up properly. the implementation has to call all setup...() methods before starting the game.", true);
			return;
		}
		
		//generate random game letters
		if (rndLetters){
			generateRandomGameLetters();
		}
		
		guessed = new HashSet<String>();
		currentPoints = 0;
		startTime = getTimeMillis();
		print("[WORDGAME] new game! letters: " + gameLetters, true);
	}
	
	public boolean isValid(String word){
		word = word.toUpperCase();
		for (int i = 0; i < word.length(); i++) {
			if (StringUtils.countMatches(word, word.charAt(i)+"") > StringUtils.countMatches(gameLetters, word.charAt(i)+"")){
				return false;
			}
		}
		return true;
	}
	
	public boolean isInList(String word){
		word = word.toUpperCase();
		return words.contains(word);
	}
	
	public boolean isAlreadyGuessed(String word){
		word = word.toUpperCase();
		return guessed.contains(word);
	}
	
	public void markAsGuessed(String word){
		guessed.add(word.toUpperCase());
	}
	
	public int addPoints(int points){
		currentPoints += points;
		return points;
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
	
	public String getGameLetters(){
		return gameLetters;
	}
	
	public void setGameLetters(String letters){
		letters = letters.replaceAll("[^A-Za-zÜÖÄüöä]", "");
		gameLetters = letters.toUpperCase();
	}
	
	public void shuffleGameLetters(){
		String shuffled = "";
		while (gameLetters.length() > 0){
			char c = gameLetters.charAt(rndInt(gameLetters.length()));
			shuffled += c;
			gameLetters = gameLetters.replaceFirst(c+"", "");
		}
		gameLetters = shuffled;
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
		print("[WORDGAME] setting up letters set... ", false);
		lettersString = lettersString.toUpperCase();
		langLetters = "";
		for (int i = 0; i < lettersString.length(); i++) {
			if (!langLetters.contains(lettersString.charAt(i)+"")){
				langLetters += lettersString.charAt(i);
			}
		}
		print("OK", true);
	}
	
	public void setupAvailableLettersFromWordList(){
		print("[WORDGAME] getting letters from words list (this may take a while)... ", false);
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
			print("[ERROR] no words list loaded to pick letters from!\n", true);
		}
		print("OK", true);
	}
	
	public String getRandomWordFromList(int length){
		SortedSet<String> temp = new TreeSet<String>();
		for (String s : words)
			if (s.length() == length)
				temp.add(s);
		return temp.toArray(new String[0])[rndInt(temp.size())];
	}
	
	public void setupWordsList(File wordsList){
		print("[WORDGAME] loading words list... ", false);
		if (!wordsList.exists() || !wordsList.isFile()){
			print("Error! Words list \"" + wordsList.getName() + "\" not found...\n", true);
		}
		
		words = new TreeSet<String>();
		
		try {
			Scanner scanner = new Scanner(wordsList);
			while (scanner.hasNextLine())
				words.add(scanner.nextLine().toUpperCase());
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		print("OK", true);
	}
	
	public void setupUseRandomGameLetters(boolean useRandomGameLetters, int letterCount){
		rndLetters = useRandomGameLetters;
		rndLettersCount = letterCount;
	}
	
	public void setPrintDebugMessages(boolean printDebugMessages){
		debugMsgs = printDebugMessages;
	}
	
	public Set<String> getAllPossibleWords(){
		Set<String> temp = new HashSet<String>();
		for (String word : words){
			if (isValid(word)) temp.add(word);
		}
		return temp;
	}

	private void generateRandomGameLetters(){
		int rndIndex;
		gameLetters = "";
		for (int i = 0; i < rndLettersCount; i++) {
			rndIndex = rndInt(langLetters.length());
			gameLetters += langLetters.charAt(rndIndex);
		}
		gameLetters = gameLetters.toUpperCase();
	}
	
	private int rndInt(int betweenZeroAndExclusive){
		return rnd.nextInt(betweenZeroAndExclusive);
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
	
	private void print(String msg, boolean lineBreak){
		if (!debugMsgs) return;
		if (lineBreak){
			System.out.println(msg);
		} else {
			System.out.print(msg);
		}
	}

}
