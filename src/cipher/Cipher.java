package cipher;


public abstract class Cipher {

	protected Alphabet alph;
	
	public abstract String encrypt(String msg);
	
	public abstract String decrypt(String msg);
	
} //END Cipher