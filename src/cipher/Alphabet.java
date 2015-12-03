package cipher;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A class that adapts a String of characters into an indexed series of characters 
 * @author Noah Kime
 */
public class Alphabet {

	private ArrayList<Character> alph;
	
	//A default String of printable ASCII Characters
	private final String DEFAULT_PRINTABLE_CHARS =
			"\n !\"#$%&'()*+,-./0123456789:;<=>?@"
			+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`"
			+ "abcdefghijklmnopqrstuvwxyz{|}~";
	
	/**
	 * Creates a new Alphabet with a set of default printable ASCII characters
	 */
	public Alphabet() {
		this.alph = new ArrayList<Character>();
		this.addStringChars(DEFAULT_PRINTABLE_CHARS);
		this.sort();
	} //END Alphabet (constructor)
	
	
	/**
	 * Creates a new Alphabet using only characters from a given String
	 * @param s The given String of characters for this Alphabet
	 */
	public Alphabet(String s) {
		this.alph = new ArrayList<Character>();
		this.addStringChars(s);
		this.sort();
	} //END Alphabet (constructor)
	
	
	/**
	 * Returns the ith character of this ALphabet
	 * @param i The index of the desired character
	 * @return The ith character of this Alphabet
	 */
	public Character getChar(int i) {
		return alph.get(i);
	} //END getChar
	
	
	/**
	 * Returns true if a given Character is in this Alphabet
	 * @param c The given character
	 * @return True if the given Character is in this Alphabet, false otherwise
	 */
	public boolean containsChar(Character c) {
		return alph.contains(c);
	} //END containsChar
	
	
	/**
	 * Returns the index number of a given Character and -1 if it is not present
	 * @param c The given Character
	 * @return
	 */
	public int getCharNum(Character c) {
		return alph.indexOf(c);
	} //END getCharNum
	
	
	/**
	 * Returns the number of Characters in this Alphabet
	 * @return The number of Characters in this Alphabet
	 */
	public int getSize() {
		return alph.size();
	} //END getSize
	
	
	/**
	 * Sorts this Alphabet from least ASCII value to greatest
	 */
	private void sort() {
		Collections.sort(alph);
	} //END sort

	
	/**
	 * Adds each distict Character from a given String to this Alphabet
	 * @param s The given String
	 */
	private void addStringChars(String s) {
		for (int i = 0; i < s.length(); i++)
			if (!alph.contains(s.charAt(i)))
				alph.add(s.charAt(i));
	} //END addStringChars
	
	
	/**Returns the String representation of this Alphabet
	 * @return The String representation of this Alphabet
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		
		for (int i = 0; i < alph.size() - 1; i++)
			sb.append("'" + alph.get(i) + "', ");
		
		sb.append("'" + alph.get(alph.size()-1) + "']");
		
		return sb.toString();
	} //END toString
	
} //END alphabet
