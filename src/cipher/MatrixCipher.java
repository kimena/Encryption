package cipher;

import matrix.GLn_Zm;
import matrix.matrixException.*;

/**
 * A class used for encrypting and decrypting messages using a custom matrix cipher
 * @author Noah Kime
 */
public class MatrixCipher extends VigenereCipher {
		
	/**
	 * Creates a new MatrixCipher with random parameters over a given Alphabet
	 * @param alph
	 */
	public MatrixCipher(Alphabet alph) {
		super(alph);
	} //END MatrixCipher
	
	
	/**
	 * Randomly generates the multiplicative parameter, a, using the given Alphabet size
	 */
	protected void setA() {
		try { 
			this.a = new GLn_Zm(cipherSize,modA);
		}
		catch (InvalidDimException|InvalidDetException|OutOfBoundsException e) {
			e.printStackTrace();
		}
	} //END setA

} //END MatrixCipher
