package cipher;

import java.util.ArrayList;
import java.util.Random;

import ring.modInt.*;

/**
 * A class used for encrypting and decrypting messages using an Affine cipher
 * @author Noah Kime
 */
public class AffineCipher extends Cipher {
	
	private Z_M modA;
	private ModInt_M a;
	private ModInt_M b;
	
	
	/**
	 * Creates a new AffineCipher with random parameters over a given Alphabet
	 * @param alph The given Alphabet for this AffineCipher
	 */
	public AffineCipher(Alphabet alph) {
		this.alph = alph;
		this.modA = new Z_M(alph.getSize());
		
		this.setA();
		this.setB();
	} //END AffineCipher (constructor)
	
	
	/**
	 * Encrypts a given message String using this AffineCipher and returns the encoded result 
	 * @param msg The given message String to be encrypted
	 * @return The encoded version of the given message String
	 */
	public String encrypt(String msg) {
		StringBuilder msgSB = new StringBuilder(msg);
		StringBuilder encSB = new StringBuilder();
		
		ModInt_M temp;
		
		for (int i = 0; i < msgSB.length(); i++) {
			temp = new ModInt_M(alph.getCharNum(msgSB.charAt(i)), modA);
			temp = a.mult(temp).add(b);
			
			encSB.append(alph.getChar(temp.getVal()));
		}
		
		return encSB.toString();
	} //END encrypt

	
	/**
	 * Decrypts a given message String using this AffineCipher and returns the decoded result
	 * @param msg The given message String to be decoded
	 * @return The decoded version of the given message String
	 */
	public String decrypt(String msg) {
		StringBuilder msgSB = new StringBuilder(msg);
		StringBuilder decSB = new StringBuilder();
		
		ModInt_M temp;
		
		for (int i = 0; i < msgSB.length(); i++) {	
			temp = new ModInt_M(alph.getCharNum(msgSB.charAt(i)), modA);
			temp = temp.add(b.getAddInverse()).mult(a.getMultInverse());
			
			decSB.append(alph.getChar(temp.getVal()));
		}
		
		return decSB.toString();
	} //END decrypt

	
	/**
	 * Randomly generates the multiplicative parameter, a, using the given Alphabet size
	 */
	private void setA() {
		Random rand = new Random();
		ArrayList<Integer> multSet = modA.getMultSet();
		
		a = new ModInt_M(multSet.get(rand.nextInt(multSet.size())), modA);
	} //END setA
	
	
	/**
	 * Randomly generates the additive parameter, b
	 */
	private void setB() {
		Random rand = new Random();
		
		b = new ModInt_M(rand.nextInt(alph.getSize() - 1) + 1, modA);
	} //END setB
	
} //END AffineCipher
