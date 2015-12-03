package cipher;

import java.util.ArrayList;
import java.util.Random;

import ring.modInt.*;

/**
 * @author Noah Kime
 */
public class AffineCipher extends Cipher {
	
	private Z_M modA;
	private ModInt_M a;
	private ModInt_M b;
	
	
	/**
	 * @param alph
	 */
	public AffineCipher(Alphabet alph) {
		this.alph = alph;
		this.modA = new Z_M(alph.getSize());
		
		this.setA();
		this.setB();
	} //END AffineCipher (constructor)
	
	
	/**
	 * @param msg
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
	 * @param msg
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
	 * 
	 */
	private void setA() {
		Random rand = new Random();
		ArrayList<Integer> multSet = modA.getMultSet();
		
		a = new ModInt_M(multSet.get(rand.nextInt(multSet.size())), modA);
	} //END setA
	
	
	/**
	 * 
	 */
	private void setB() {
		Random rand = new Random();
		
		b = new ModInt_M(rand.nextInt(alph.getSize() - 1) + 1, modA);
	} //END setB
	
} //END AffineCipher
