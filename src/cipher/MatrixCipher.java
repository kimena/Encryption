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
		//00 INITIALIZE MATRIX GRIDS
		Grid<ModInt_M> mtxA = new Grid<ModInt_M>(cipherSize, cipherSize);
		Grid<ModInt_M> mtxT = new Grid<ModInt_M>(cipherSize, cipherSize);
		Random rand = new Random();
		
		//01 GENERATE NONSINGULAR ENTRIES
		genNonSingular(mtxA, mtxT, cipherSize, rand);
		
		//02.1 INITIALIZE TEMPORARY GRID
		Grid<ModInt_M> temp = new Grid<ModInt_M>(cipherSize, cipherSize);
		initializeGrid(temp);

		//02.2 MULTIPLY mtxA*mtxT
		for (int i = 0; i < cipherSize; i++)
			for (int j = 0; j < cipherSize; j++)
				for (int k = 0; k < cipherSize; k++)
					temp.set( i, j, temp.get(i, j).add(mtxA.get(i,k).mult(mtxT.get(k,j))) );
		
		//03 RETURN NONSINGULAR MATRIX, THE PRODUCT A*T
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
			
			//00.1 SET FINAL ENTRY OF mtxA TO THE MULTIPLICITIVE IDENTITY
			//	SET r AS COLUMN COORDINATE OF FINAL ENTRY OF mtxA
			for (int i = 0; i < cipherSize; i++) {
				if (mtxA.get(cipherSize-n, i) == null) {
					mtxA.set(cipherSize-n, i, new ModInt_M(modA.getMultIdentity(), modA));
					r = i;
				}
			}
			
			//00.2 SET FINAL ENTRY OF mtxT, IN THE rTH COLUMN, TO A RANDOM INVERTIBLE VALUE
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
			
			//01.1	RANDOMIZE VECTOR v
			//	GET r, THE FIRST INVERTIBLE ENTRY'S COORDINATE
			//	IF NO INVERTIBLE ENTRY, TRY AGAIN
			while (r == -1) {
				for (int i = 0; i < n; i++) {
					temp = rand.nextInt(modA.getM());
					r = (r == -1 && modA.getMultSet().contains(temp)) ? i : r;
					v[i] = new ModInt_M(temp, modA);
				}
			}

			//01.2 FIND CORRESPONDING COORDINATE FOR r, realR, WHICH INDICATES
			//	THE TRUE COLUMN COORDINATE AND NOT THE MINOR'S COORDINATE 
			int realR = r;
			int skip = 0;
			for (int i = 0; i < cipherSize; i++) {
				if ( (i-skip <= r) && mtxA.get(cipherSize-n, i) != null) {
					realR++;
					skip++;
				}
			}

			//02.1 FILL THE FIRST EMPTRY ROW OF mtxA WITH e_r, THAT IS, SET ALL ENTRIES TO ZERO EXCEPT THE rTH
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
			
			//02.2 FILL THE realRTH COLUMN OF mtxA WITH ZERO
			for (int i = 1; i < n; i++) {
				mtxA.set(i+cipherSize-n, realR, new ModInt_M(modA.getAddIdentity(), modA));
			}

			//03.1 FILL THE realRTH ROW OF mtxT WITH v
			skip = 0;
			for (int i = 0; i < n; i++) {
				while ( mtxT.get(realR, i+skip) != null ) {
					skip++;
				}
				
				mtxT.set(realR, i+skip, v[i]);
			}
			
			//03.2 FILL THE realRTH COLUMN of mtxT WITH THE ADDITIVE IDDENTITY
			skip = 0;
			for (int i = 0; i < n-1; i++) {
				while ( mtxT.get(i+skip, realR) != null ) {
					skip++;
				}
				mtxT.set(i+skip, realR, new ModInt_M(modA.getAddIdentity(), modA));
			}

			//04 RECURSE OVER NEXT MINOR
			genNonSingular(mtxA, mtxT, n-1, rand);
		} //END else (n == 1)
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
