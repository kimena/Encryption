package cipher;

/**
 * A class used for encrypting and decrypting messages
 * @author Noah Kime
 */
public abstract class Cipher {

	protected Alphabet alph;
	
	/**
	 * Encrypts a given message String using this Cipher strategy and returns the encoded result 
	 * @param msg The given message String to be encrypted
	 * @return The encoded version of the given message String
	 */
	public abstract String encrypt(String msg);
	
	/**
	 * Decrypts a given message String using this Cipher strategy and returns the decoded result
	 * @param msg The given message String to be decoded
	 * @return The decoded version of the given message String
	 */
	public abstract String decrypt(String msg);
	
} //END Cipher