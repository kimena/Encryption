package cipher;

import java.util.Random;

import matrix.GLn_Zm;
import matrix.Grid;
import matrix.matrixException.*;

import ring.modInt.ModInt_M;

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
		try { this.a = new GLn_Zm(getNonSingular()); }
		catch (InvalidDimException|InvalidDetException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Generates a random nonsingular matrix uniformly using a modified algorithm
	 * Algorithm credit: Dana Randall, Efficient Generation of Nonsingular Matrices,
	 * http://www.eecs.berkeley.edu/Pubs/TechRpts/1991/CSD-91-658.pdf
	 * @return A random nonsingular matrix of ModInt_M
	 */
	private Grid<ModInt_M> getNonSingular() {
		Grid<ModInt_M> mtxA = new Grid<ModInt_M>(cipherSize, cipherSize);
		Grid<ModInt_M> mtxT = new Grid<ModInt_M>(cipherSize, cipherSize);
		Random rand = new Random();
		
		genNonSingular(mtxA, mtxT, cipherSize, rand);
		
		Grid<ModInt_M> temp = new Grid<ModInt_M>(cipherSize, cipherSize);
		initializeGrid(temp);

		for (int i = 0; i < cipherSize; i++)
			for (int j = 0; j < cipherSize; j++)
				for (int k = 0; k < cipherSize; k++)
					temp.set( i, j, temp.get(i, j).add(mtxA.get(i,k).mult(mtxT.get(k,j))) );
		return temp;
	} //END getNonSingular
	
	
	/**
	 * Recursively generates a random nonsingular matrix uniformly using a modified algorithm
	 * Algorithm credit: Dana Randall, Efficient Generation of Nonsingular Matrices,
	 * http://www.eecs.berkeley.edu/Pubs/TechRpts/1991/CSD-91-658.pdf
	 * @param mtxA Matrix A
	 * @param mtxT Matrix T
	 * @param n	The size of this matrix minor
	 * @param rand A random number generator
	 */
	private void genNonSingular(Grid<ModInt_M> mtxA, Grid<ModInt_M> mtxT, int n, Random rand) {
		if (n == 1) {
			int r = -1;
			for (int i = 0; i < cipherSize; i++) {
				if (mtxA.get(cipherSize-n, i) == null) {
					mtxA.set(cipherSize-n, i, new ModInt_M(modA.getMultIdentity(), modA));
					r = i;
				}
			}
			
			for (int i = 0; i < cipherSize; i++) {
				if (mtxT.get(i, r) == null) {
					mtxT.set(i, r, new ModInt_M(modA.getMultSet().get(rand.nextInt(modA.getMultSet().size())), modA));
				}
			}		
		} //END if (n == 1)
		else {
			int temp;
			int r = -1;
			ModInt_M[] v = new ModInt_M[n];
			
			//Randomize vector v
			//Get r, first invertible entry's coordinate
			//If no invertible entries, try again.
			while (r == -1) {
				for (int i = 0; i < n; i++) {
					temp = rand.nextInt(modA.getM());
					r = (r == -1 && modA.getMultSet().contains(temp)) ? i : r;
					v[i] = new ModInt_M(temp, modA);
				}
			}

			int realR = r;
			int skip = 0;
			for (int i = 0; i < cipherSize; i++) {
				if ( (i-skip <= r) && mtxA.get(cipherSize-n, i) != null) {
					realR++;
					skip++;
				}
			}

			//Fill first empty row  of A with e_r, that is, all entries are zero except the rth
			skip = 0;
			for (int i = 0; i < n; i++) {
				while ( mtxA.get(cipherSize - n, i+skip) != null ) {
					skip++;
				}
				
				if (i == r)
					mtxA.set(cipherSize - n, i+skip, new ModInt_M(modA.getMultIdentity(), modA));
				else
					mtxA.set(cipherSize - n, i+skip, new ModInt_M(modA.getAddIdentity(), modA));
			}
			
			for (int i = 1; i < n; i++) {
				mtxA.set(i+cipherSize-n, realR, new ModInt_M(modA.getAddIdentity(), modA));
			}

			//Fill realRth row of T with v
			skip = 0;
			for (int i = 0; i < n; i++) {
				while ( mtxT.get(realR, i+skip) != null ) {
					skip++;
				}
				
				mtxT.set(realR, i+skip, v[i]);
			}

			skip = 0;
			for (int i = 0; i < n-1; i++) {
				while ( mtxT.get(i+skip, realR) != null ) {
					skip++;
				}
				mtxT.set(i+skip, realR, new ModInt_M(modA.getAddIdentity(), modA));
			}

			genNonSingular(mtxA, mtxT, n-1, rand);
		}
	} //END genNonSingular
	
	
	/**
	 * Initializes a given grid's entries to the additive inverse of the modA group
	 * @param grid A given grid to be initialized
	 */
	private void initializeGrid(Grid<ModInt_M> grid) {
		for (int i = 0; i < grid.getRows(); i++)
			for (int j = 0; j < grid.getCols(); j++)
				grid.set(i,j, new ModInt_M(modA.getAddIdentity(), modA));
	} //END initializeGrid
	
} //END MatrixCipher
