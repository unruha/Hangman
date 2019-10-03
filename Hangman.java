import java.util.*;

public class Hangman {
	
//	global variable numGuesses
	static int numGuesses;
	
//	variable determines if the secret word will be displayed on each turn for testing purposes
	private static final boolean testingMode = true;
	
//	MAIN METHOD
	public static void main (String args[]) {
		Scanner scan = new Scanner(System.in);
		int playTimes = 20;
		
//		loop that keeps track of the number of rounds the user has played, keeping track of whether the game has ended or not
		while (playTimes > 0) {
			
			boolean guessedCorrectly = false;
			String secretWord = RandomWord.newWord();
			String displayedWord = displayWord(secretWord);
			String difficulty = difficultySpecified();
			numGuesses = numGuess(difficulty);
			int numSpaces = numSpace(difficulty);
			int counter = 0;
//			used only to display the word when it is the user's first turn and testing mode is on (if testingMode==true)
			
//			loop that starts and ends a round, keeping track of whether the round has ended or not
			while (!guessedCorrectly && numGuesses > 0) {
				
				testingMode(testingMode, counter, secretWord, displayedWord);
				
				String locations = "";
				boolean validInput = false;
				char guessedLetter;
				int[] intLocations = new int[0];
				
//				loop runs once then runs repeatedly until the user has entered valid input
				do {
					guessedLetter = defineValidLetter(secretWord);
					
					if (guessedLetter == '$') {
						guessedCorrectly = true;
						break;
					}
					
					locations = defineLocationString();
					boolean validLocations = isLocationValid(locations);
					
					if (validLocations) {
						intLocations = locationToArray(locations);
					}
					else {
						System.out.println("Your input is not valid. Try again.");
						System.out.println("Guesses Remaining: " + numGuesses);
						continue;
					}
					//handles number out of range error
					boolean isLocationOutOfRange = locationOutOfRange(intLocations, secretWord);
					//handles too many numbers error
					boolean isValidSpaces = validSpaces(intLocations, numSpaces, isLocationOutOfRange);
					
					if (isValidSpaces && !isLocationOutOfRange) {
						validInput = true;
					}
					
				}	while(!validInput);
				
				if (guessedCorrectly) {
					break;
				}
				else {
					displayedWord = newDisplayWord(secretWord, intLocations, guessedLetter, displayedWord);
					if (displayedWord.equals(secretWord)) {
						guessedCorrectly = true;
					}
				}
				System.out.println("Guesses Remaining: " + numGuesses);
				counter ++;
			}
			endGame(guessedCorrectly);
			
			playTimes--;
		}
	}
	
//	METHOD numGuess
//	determines the amount of guesses that the user starts off with based on the difficulty specified
	public static int numGuess(String s) {
		int numGuesses;
		
		if (s.toUpperCase().equals("E")) {
			numGuesses = 15;
		}
		else if (s.toUpperCase().equals("I")) {
			numGuesses = 12;
		}
		else if (s.toUpperCase().equals("H")) {
			numGuesses = 10;
		}
		else {
//			numGuesses equals -1 when the game difficulty was not specified correctly
			numGuesses = -1;
		}
		return numGuesses;
	}
	
//	METHOD numSpace
//	determines the amount of spaces that the user can guess at once based on the difficulty specified
	public static int numSpace(String s) {
		int numSpaces;
		
		if (s.toUpperCase().equals("E")) {
			numSpaces = 4;
		}
		else if (s.toUpperCase().equals("I")) {
			numSpaces = 3;
		}
		else if (s.toUpperCase().equals("H")) {
			numSpaces = 2;
		}
		else {
//			numSpaces equals -1 when the game difficulty was not specified correctly
			numSpaces = -1;
		}
		return numSpaces;
	}
	
//	METHOD difficultySpecified
//	confirms that the difficulty was specified correctly and returns the abbreviation
	public static String difficultySpecified() {
		Scanner scan = new Scanner(System.in);
		
		boolean difficultySpecified = false;		
		char difficulty;
		String strDifficulty = "";
		
		while(!difficultySpecified) {
			System.out.println("Enter your difficulty: Easy (e), Intermediate (i), or Hard (h) ");
			difficulty = scan.next().charAt(0);
			strDifficulty = String.valueOf(difficulty).toUpperCase();
			
//			this if statement checks if difficulty is E, I, or H
			if (strDifficulty.equals("E") || strDifficulty.equals("I") || strDifficulty.equals("H")) {
				difficultySpecified = true;
			}
			else {
				System.out.println("Invalid difficulty. Try Again... ");
			}
		}
		return strDifficulty;
	}
	
//	METHOD checkLocation
//	returns a boolean that stores whether or not a the guessed character was found at one of the guessed locations
	public static boolean checkLocation(String secretWord, int[] locations, char guessedLetter) {
		boolean foundMatch = false;
		
		for (int i = 0; i < locations.length; i++) {
			if (secretWord.charAt(locations[i]) == guessedLetter) { 
				foundMatch = true;
			}
		}
		return foundMatch;
	}
	
//	METHOD displayWord
//	returns the string that is printed before any guesses have been made
	public static String displayWord(String word) {
		String displayedWord = "";
		
		for (int i = 0; i < word.length(); i++) {
			displayedWord = displayedWord + "-";
		}		
		return displayedWord;
	}
	
//	METHOD locationToArray
//	takes the locations stored as a string and puts them into an int array
	public static int[] locationToArray(String s) {
		String[] locations = s.split(" ");
		int[] intLocations = new int[locations.length];
		
		for (int i = 0; i < locations.length; i++) {
			intLocations[i] = Integer.parseInt(locations[i]); 
		}
		return intLocations;
	}
	
//	METHOD modifyDisplay
//	updates the string that is displayed to the user after they guess correctly
	public static String modifyDisplay(String displayedWord, char guessedWord, String secretWord, int[] intLocations) {
		for (int i = 0; i < secretWord.length(); i++) {
			boolean isInArray = false;
			
			for (int j = 0; j < intLocations.length; j++) {
				if (intLocations[j] == i) {
					isInArray = true;
				}
			}
			if (secretWord.charAt(i) == guessedWord && isInArray) {
				displayedWord = displayedWord.substring(0,i) + guessedWord + displayedWord.substring(i+1, displayedWord.length());
			}
		}
		return displayedWord;
	}
	
//	METHOD endGame
//	runs after a game has ended and determines if the user wants to play again or quit the program
	public static void endGame(boolean guessedCorrectly) {
		Scanner scanner = new Scanner(System.in);
		boolean playAgainBool = false;
		boolean validInput = false;
		
		if (guessedCorrectly == true) {
			System.out.println("You have guessed the word! Congratulations!");
			
			while (!validInput) {
			System.out.println("Would you like to play again? Yes(y) or No(n) ");
			String playAgain = scanner.nextLine();
			
				if (playAgain.toUpperCase().equals("Y")) {
					playAgainBool = true;
					validInput = true;
				}
				else if (playAgain.toUpperCase().equals("N")) {
					System.out.println("Goodbye");
					System.exit(0);
				}
				else {
					System.out.println("Your input is not valid. Please enter Yes(y) or No(n).");
				}
			}
		}
		else {
			System.out.println("You have failed to guess the word... :(");
			while (!validInput) {
				System.out.println("Would you like to play again? Yes(y) or No(n)");
				String playAgain = scanner.nextLine();
				
				if (playAgain.toUpperCase().equals("Y")) {
					playAgainBool = true;
					validInput = true;
				}
				else if (playAgain.toUpperCase().equals("N")) {
					System.out.println("Goodbye");
					System.exit(0);
				}
				else {
					System.out.println("Your input is not valid. Please enter Yes(y) or No(n).");
				}
			}
		}
	}
	
//	METHOD defineValidLetter
//	checks to see if input is a letter
	public static char defineValidLetter(String secretWord) {
		Scanner scan = new Scanner(System.in);
		boolean isLetter = false;
		char guessedLetter = '!';
//		compiler made me assign guessedLetter to something, not just initialize.
		boolean solved = false;
		
//		loop that verifies that the character or word the user has entered is a valid input
		while(!isLetter) {
			System.out.print("Please enter the letter you want to guess: ");
			String line = scan.nextLine();
			
			if (line.equals("solve")) {
				solved = solveWord(line, secretWord);
				if (solved == true) {
					guessedLetter = '$';
					isLetter = true;
				}
			}
			else if (line.length() > 0) {
				guessedLetter = line.charAt(0);
				
				if(Character.isLetter(guessedLetter)) {
					isLetter = true;
				}
				else {
					isLetter = false;
					System.out.println("Your input is not valid. Try again.");
					System.out.println("Guesses Remaining: " + numGuesses);
				}
			}
			else {
				isLetter = false;
				System.out.println("Your input is not valid. Try again.");
			}
		}
		return guessedLetter;
	}
	
//	METHOD testingMode
//	displays the secret word when the program is in testing mode
	public static void testingMode(boolean testingMode, int counter, String secretWord, String displayedWord) {
		if (testingMode == true && counter == 0) {
			System.out.println("The secret word is: " + secretWord);
		}
		if (counter == 0) {
			System.out.println("The word is: " + displayedWord);
		}
	}
	
//	METHOD validSpaces
//	prompts the user to input a valid number of spaces to check
	public static boolean validSpaces(int[] intLocations, int numSpaces, boolean isLocationOutOfRange) {
		Scanner scan = new Scanner(System.in);
		boolean validSpaces = false;
		
		if (intLocations.length != numSpaces && !isLocationOutOfRange) {
			System.out.println("Your input is not valid. Try again");
			System.out.println("Guesses Remaining: " + numGuesses);
		}
		else {
			validSpaces = true;
		}
		return validSpaces;
	}
	
//	METHOD locationOutofRange
//	checks if one of the locations entered is out of range of the secret word
	public static boolean locationOutOfRange(int[] intLocations, String secretWord) {
		boolean locationOutOfRange = false;
		
		for (int i = 0; i < intLocations.length; i++) {
			if (intLocations[i] > secretWord.length()-1 && !locationOutOfRange) {
				locationOutOfRange = true;
				System.out.println("Your input is not valid. Try again.");
				System.out.println("GuessesRemaining: " + numGuesses);
			}
		}
		return locationOutOfRange;
	}
	
//	METHOD defineLocationString
//	returns the spaces the user wants to check
	public static String defineLocationString() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Please enter the spaces you want to check (separated by spaces): ");
		String locations = scan.nextLine();
		return locations;
	}
	
//	METHOD isLocationValid
//	checks if the location string contains anything other than integers
	public static boolean isLocationValid(String location) {
		boolean validLocation = true;
		
		for (int i = 0; i < location.length(); i++) {
			if (!Character.isDigit(location.charAt(i)) && location.charAt(i) != (' ') ) {
				validLocation = false;
			}
		}
		return validLocation;
	}
	
//	METHOD solveWord
//	allows user to solve the word
	public static boolean solveWord(String guess, String secretWord) {
		Scanner scan = new Scanner(System.in);
		System.out.print("Please solve the word: ");
		String wordGuess = scan.nextLine();
		boolean solved = false;
		
		if (wordGuess.equals(secretWord)) {
			solved = true;
			System.out.println("You win!");
		}
		else {
			System.out.println("That is not the secret word.");
			numGuesses --;
			System.out.println("Guesses Remaining: " + numGuesses);
		}
		return solved;
	}
	
//	METHOD newDisplayWord
//	uses the modifyDisplay to change the displayed word depending on whether the guess was in the word
	public static String newDisplayWord(String secretWord, int[] intLocations, char guessedLetter, String displayedWord) {
		
		if (checkLocation(secretWord, intLocations, guessedLetter) == true) {
			System.out.println("Your guess is in the word!");
			
			displayedWord = modifyDisplay(displayedWord, guessedLetter, secretWord, intLocations);
			System.out.println("The updated word is: " + displayedWord);
			
		}
		else {
			System.out.println("Your letter was not found in the spaces you provided.");
			numGuesses--;
		}
		return displayedWord;
	}
}	


