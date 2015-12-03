package cipher;

import java.util.ArrayList;
import java.util.Random;

import matrix.*;
import matrix.matrixException.*;
import ring.modInt.ModInt_M;
import ring.modInt.Z_M;

public class VigenereCipher extends Cipher {

	protected Z_M modA;
	protected GLn_Zm a;
	protected Matrix<ModInt_M> b;
	
	protected int cipherSize;
	protected final int MIN_SIZE = 10;
	protected final int SIZE_RNG = 90;
	
	private final String EXCEPTION_MESSAGE = "MESSAGE FAILED TO BE PROCESSED: ";
	
	/**
	 * @param alph
	 */
	public VigenereCipher(Alphabet alph) {
		this.alph = alph;
		this.modA = new Z_M(alph.getSize());
		
		Random rand = new Random();
		this.cipherSize = rand.nextInt(SIZE_RNG) + MIN_SIZE;
		
		this.setA();
		this.setB();
	} //END VigenereCipher
	
	
	/* (non-Javadoc)
	 * @see cipher.Cipher#encrypt(java.lang.String)
	 */
	public String encrypt(String msg) {
		StringBuilder msgSB = new StringBuilder(msg);
		StringBuilder encSB = new StringBuilder();
		
		try {
			Matrix<ModInt_M> charsVector;
		
			while (msgSB.length() > 0) {
				charsVector = this.fillVector(msgSB);
				charsVector = a.multRRet(charsVector).addRet(b);
				
				for (int i = 0; i < cipherSize; i++)
					encSB.append(alph.getChar(charsVector.get(i,0).getVal()));
				
				msgSB = (msgSB.length() >= cipherSize) 
						?	new StringBuilder(msgSB.substring(cipherSize))
						:	new StringBuilder(msgSB.substring(msgSB.length()));
			}
			
			return encSB.toString();
		} //END try
		catch (OutOfBoundsException e) {
			return EXCEPTION_MESSAGE + "OutOfBoundsException";
		}
		catch (InvalidDimException e) {
			return EXCEPTION_MESSAGE + "InvalidDimException";
		}
	} //END encrypt

	
	/* (non-Javadoc)
	 * @see cipher.Cipher#decrypt(java.lang.String)
	 */
	public String decrypt(String msg) {
		StringBuilder msgSB = new StringBuilder(msg);
		StringBuilder decSB = new StringBuilder();
		
		try {
			Matrix<ModInt_M> charsVector;
			 
			while (msgSB.length() > 0) {
				charsVector = fillVector(msgSB);		
				charsVector = a.getInverse().multRRet(charsVector.addRet(b.multRet(new ModInt_M(-1, modA))));

				for (int i = 0; i < cipherSize; i++) {
					decSB.append(alph.getChar(charsVector.get(i,0).getVal()));	
				}
				
				msgSB = (msgSB.length() >= cipherSize) 
						?	new StringBuilder(msgSB.substring(cipherSize))
						:	new StringBuilder(msgSB.substring(msgSB.length()));
			}

			return decSB.toString().trim();
		}
		catch (OutOfBoundsException e) {
			return EXCEPTION_MESSAGE + "OutOfBoundsException";
		}
		catch (InvalidDimException e) {
			return EXCEPTION_MESSAGE + "InvalidDimException";
		}
	} //END decrypt

	
	/**
	 * Sets a to a random nxn matrix with invertible ModInt_M down the diagonal
	 */
	protected void setA() {
		try {
			Random rand = new Random();
			ArrayList<Integer> multSet = modA.getMultSet();
			
			Grid<ModInt_M> aGrid = new Grid<ModInt_M>(cipherSize,cipherSize);
			
			for (int i = 0; i < cipherSize; i++) {
				for (int j = 0; j < cipherSize; j++) {
					if (i == j)
						aGrid.set(i,j, new ModInt_M(multSet.get(rand.nextInt(multSet.size())), modA));
					else
						aGrid.set(i,j, new ModInt_M(modA.getAddIdentity(), modA));
				}
			}
			
			this.a = new GLn_Zm(aGrid);
		}
		catch (InvalidDetException|InvalidDimException e) {
			this.setA();
		}
	} //END setA
	
	
	private void setB() {
		Random rand = new Random();
		
		Grid<ModInt_M> bGrid = new Grid<ModInt_M>(cipherSize,1);
		
		for (int i = 0; i < cipherSize; i++)
			bGrid.set(i,0, new ModInt_M(rand.nextInt(alph.getSize() - 1) + 1, modA));
		
		this.b = new Matrix<ModInt_M>(bGrid);
	} //END setB
	
	
	private Matrix<ModInt_M> fillVector(StringBuilder msgSB) {
		Grid<ModInt_M> tempGrid = new Grid<ModInt_M>(cipherSize, 1);

		for (int i = 0; i < cipherSize; i++) {
			if (i < msgSB.length())
				tempGrid.set(i,0, new ModInt_M(alph.getCharNum(msgSB.charAt(i)), modA));
			else
				tempGrid.set(i,0, new ModInt_M(alph.getCharNum(' '), modA));
		}
		
		return new Matrix<ModInt_M>(tempGrid);
	}
	
} //END VigenereCipher
