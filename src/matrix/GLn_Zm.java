package matrix;

import ring.modInt.ModInt_M;
import ring.modInt.Z_M;

import java.util.Random;

import matrix.matrixException.*;

/**
 * A Class that performs modular arithmetic with matrices
 * @author Noah Kime
 */
public class GLn_Zm extends GLn<ModInt_M> {
	
	protected GLn_Zm inverse;
	
	
	/**
	 * Constructs a new GLn_Zm matrix using a given Grid
	 * @param nums The given Grid for this GLn_Zm
	 * @throws InvalidDimException Thrown if given grid nums does not have equivalent
	 * 	columns and rows
	 * @throws InvalidDetException Thrown if given grid corresponds to a Singular (noninvertible)
	 *  Matrix
	 */
	public GLn_Zm(Grid<ModInt_M> nums) throws InvalidDimException, InvalidDetException {
		super(nums);
		
		inverse = new GLn_Zm(inv.copyGrid(), det.getMultInverse(), this);
	} //END GLn_Zm (constructor)
	
	
	/**
	 * Generates a random GLn_Zm with given size n over the ring Z_M
	 * @param n The given size for this random GLn_Zm
	 * @param z The given Z_M to randomize this GLn_Zm over
	 * @throws InvalidDimException Thrown if this GLn_Zm has invalid dimensions
	 * @throws InvalidDetException Thrown if this GLn_Zm has an invalid determinant
	 * @throws OutOfBoundsException Thrown if randomization includes invalid matrix multiplication
	 */
	public GLn_Zm(int n, Z_M z) throws InvalidDimException, InvalidDetException, OutOfBoundsException {
		super();
		
		this.n = n;
		this.m = n;
		
		this.nums = genRandGrid(n,z);
		genDet();
	}
	
	
	/**
	 * Constructs a new GLn_Zm matrix using a given Grid, a known determinate, and inverse
	 * @param nums The given Grid for this GLn_Zm
	 * @param det The known determinate of this GLn_Zm
	 * @param inv The inverse of this GLn_Zm
	 */
	private GLn_Zm(Grid<ModInt_M> nums, ModInt_M det, GLn_Zm inv) {
		super(nums, det, inv);
	} //END GLn_Zm (constructor)
	
	
	/**
	 * Returns the transpose of this GLn_Zm
	 * @return The Transpose of this GLn_Zm
	 * @throws InvalidDimException Thrown if this GLn_Zm has invalid dimensions
	 * @throws InvalidDetException Thrown if this GLn_Zm has an invalid determinant
	 */
	public GLn_Zm getTranspose() throws InvalidDimException, InvalidDetException {
		return(new GLn_Zm(this.nums.transpose()));
	} //END getTranspose
	
	
	/**
	 * Returns the multiplicative inverse of this GLn_Zm
	 * @return The multiplicative inverse of this GLn_Zm
	 */
	@Override
	public GLn_Zm getInverse() {
		return inverse;
	} //END getInverse

	
	/**
	 * Returns an nxn identity matrix of ModInt_M integers
	 * @return An nxn identity matrix of ModInt_M integers
	 */
	@Override
	public GLn_Zm getIdentity() {
		Grid<ModInt_M> temp = this.getIdentityGrid();
		
		try {
			return new GLn_Zm(temp);
		}
		catch (Exception e){
			return null;
		}
	} //END getIdentity
	
	
	/**
	 * Returns an nxn grid for an identity matrix of ModInt_M integers
	 * @return An nxn grid for an identity matrix of ModInt_M integers
	 */
	@Override
	protected Grid<ModInt_M> getIdentityGrid() {
		Grid<ModInt_M> temp = new Grid<ModInt_M>(n,n);
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				temp.set(i,j, 
				(i == j) ? 	new ModInt_M(this.nums.get(0,0).getMultIdentity().getVal(), this.nums.get(0,0).getM()): 
							new ModInt_M(this.nums.get(0,0).getAddIdentity().getVal(), this.nums.get(0,0).getM()));
			}
		}
		
		return temp;
	} //END getIdentityGrid
	
	
	/**
	 * Generates a random invertible Grid of ModInt_M with given size n
	 * @param n The given size for the random Grid
	 * @param z The given Z_M to 
	 * @return A random invertible Grid of ModInt_M with size n
	 * @throws InvalidDimException Thrown if random Grid has invalid dimensions
	 * @throws InvalidDetException Thrown if random Grid has an invalid determinant
	 * @throws OutOfBoundsException Thrown if multiplication of triangular matrices is invalid
	 */
	private Grid<ModInt_M> genRandGrid(int n, Z_M z) throws InvalidDimException, InvalidDetException, OutOfBoundsException {
		GLn_Zm upper  = new GLn_Zm(getNonSingular(n,z));
		GLn_Zm lower  = new GLn_Zm(getNonSingular(n,z)).getTranspose();
		
		return(lower.multRRet(upper).asGrid());
	} //END genRandGrid
	
	
	/**
	 * Generates a random nonsingular matrix uniformly using a modified algorithm
	 * Algorithm credit: Dana Randall, Efficient Generation of Nonsingular Matrices,
	 * http://www.eecs.berkeley.edu/Pubs/TechRpts/1991/CSD-91-658.pdf
	 * @return A random nonsingular matrix of ModInt_M
	 */
	private Grid<ModInt_M> getNonSingular(int n, Z_M z) {
		//00 INITIALIZE MATRIX GRIDS
		Grid<ModInt_M> mtxA = new Grid<ModInt_M>(n, n);
		Grid<ModInt_M> mtxT = new Grid<ModInt_M>(n, n);
		Random rand = new Random();
		
		//01 GENERATE NONSINGULAR ENTRIES
		genNonSingular(mtxA, mtxT, n, n, z, rand);
		
		//02.1 INITIALIZE TEMPORARY GRID
		Grid<ModInt_M> temp = new Grid<ModInt_M>(n, n);
		initializeGrid(temp, new ModInt_M(z.getAddIdentity(),z));

		//02.2 MULTIPLY mtxA*mtxT
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				for (int k = 0; k < n; k++)
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
	private void genNonSingular(Grid<ModInt_M> mtxA, Grid<ModInt_M> mtxT, int N, int n, Z_M z, Random rand) {
		if (n == 1) {
			int r = -1;
			
			//00.1 SET FINAL ENTRY OF mtxA TO THE MULTIPLICITIVE IDENTITY
			//	SET r AS COLUMN COORDINATE OF FINAL ENTRY OF mtxA
			for (int i = 0; i < N; i++) {
				if (mtxA.get(N-n, i) == null) {
					mtxA.set(N-n, i, new ModInt_M(z.getMultIdentity(), z));
					r = i;
				}
			}
			
			//00.2 SET FINAL ENTRY OF mtxT, IN THE rTH COLUMN, TO A RANDOM INVERTIBLE VALUE
			for (int i = 0; i < N; i++) {
				if (mtxT.get(i, r) == null) {
					mtxT.set(i, r, new ModInt_M(z.getMultSet().get(rand.nextInt(z.getMultSet().size())), z));
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
					temp = rand.nextInt(z.getM());
					r = (r == -1 && z.getMultSet().contains(temp)) ? i : r;
					v[i] = new ModInt_M(temp, z);
				}
			}

			//01.2 FIND CORRESPONDING COORDINATE FOR r, realR, WHICH INDICATES
			//	THE TRUE COLUMN COORDINATE AND NOT THE MINOR'S COORDINATE 
			int realR = r;
			int skip = 0;
			for (int i = 0; i < N; i++) {
				if ( (i-skip <= r) && mtxA.get(N-n, i) != null) {
					realR++;
					skip++;
				}
			}

			//02.1 FILL THE FIRST EMPTRY ROW OF mtxA WITH e_r, THAT IS, SET ALL ENTRIES TO ZERO EXCEPT THE rTH
			skip = 0;
			for (int i = 0; i < n; i++) {
				while ( mtxA.get(N - n, i+skip) != null ) {
					skip++;
				}
				
				if (i == r)
					mtxA.set(N - n, i+skip, new ModInt_M(z.getMultIdentity(), z));
				else
					mtxA.set(N - n, i+skip, new ModInt_M(z.getAddIdentity(), z));
			}
			
			//02.2 FILL THE realRTH COLUMN OF mtxA WITH ZERO
			for (int i = 1; i < n; i++) {
				mtxA.set(i+N-n, realR, new ModInt_M(z.getAddIdentity(), z));
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
				mtxT.set(i+skip, realR, new ModInt_M(z.getAddIdentity(), z));
			}

			//04 RECURSE OVER NEXT MINOR
			genNonSingular(mtxA, mtxT, N, n-1, z, rand);
		} //END else (n == 1)
	} //END genNonSingular
	
} //END GLn_Zm
