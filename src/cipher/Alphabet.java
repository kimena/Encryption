package cipher;

import java.util.ArrayList;
import java.util.Collections;

public class Alphabet {

	private ArrayList<Character> alph;
	
	private final String DEFAULT_PRINTABLE_CHARS =
			"\n !\"#$%&'()*+,-./0123456789:;<=>?@"
			+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`"
			+ "abcdefghijklmnopqrstuvwxyz{|}~";
	
	public Alphabet() {
		this.alph = new ArrayList<Character>();
		this.addStringChars(DEFAULT_PRINTABLE_CHARS);
		this.sort();
	} //END Alphabet (constructor)
	
	
	public Alphabet(String s) {
		this.alph = new ArrayList<Character>();
		this.addStringChars(s);
		this.sort();
	} //END Alphabet (constructor)
	
	
	public Character getChar(int i) {
		return alph.get(i);
	} //END getChar
	
	
	public boolean containsChar(Character c) {
		return alph.contains(c);
	} //END containsChar
	
	
	public int getCharNum(Character c) {
		return alph.indexOf(c);
	} //END getCharNum
	
	
	public int getSize() {
		return alph.size();
	} //END getSize
	
	
	private void sort() {
		Collections.sort(alph);
	} //END sort

	
	private void addStringChars(String s) {
		for (int i = 0; i < s.length(); i++)
			if (!alph.contains(s.charAt(i)))
				alph.add(s.charAt(i));
	} //END addStringChars
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		
		for (int i = 0; i < alph.size() - 1; i++)
			sb.append("'" + alph.get(i) + "', ");
		
		sb.append("'" + alph.get(alph.size()-1) + "']");
		
		return sb.toString();
	} //END toString
	
} //END alphabet
