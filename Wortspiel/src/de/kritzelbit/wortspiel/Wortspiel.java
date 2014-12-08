package de.kritzelbit.wortspiel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class Wortspiel {
	
	

	private SortedSet<String> readWords(String path){
		System.out.print("[WORTSPIEL] lade Wortliste... ");
		
		File f = new File(path);
		if (!f.exists() || !f.isFile()){
			System.out.println("[ERROR] Worstliste \"de.txt\" konnte nicht gefunden werden...");
			return null;
		}
		
		SortedSet<String> words = new TreeSet<String>();
		
		try {
			Scanner scanner = new Scanner(new File(path));
			while (scanner.hasNextLine())
				words.add(scanner.nextLine());
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("OK");
		return words;
	}

}
