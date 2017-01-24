package matrix;

import ring.Ring;

import matrix.matrixException.*;

/**
 * A Class that defines nxn matrices with multiplicative inverses and entries of type T
 * @author Noah Kime
 * @param <T>
 */
public class GLn<T extends Ring<T>> extends Matrix<T> {
	
	protected T det;
	protected GLn<T> inv;
	
	
	/**
	 * Constructs a new GLn matrix using a given Grid
	 * @param nums The given Grid for this GLn
	 * @throws InvalidDimException Thrown if given grid nums does not have equivalent
	 * 	columns and rows
	 * @throws InvalidDetException Thrown if given grid corresponds to a Singular (noninvertible)
	 *  Matrix
	 */
	public GLn(Grid<T> nums) throws InvalidDimException, InvalidDetException {
		super(nums);
		
		if (m != n)
			throw new InvalidDimException();
		
		this.genDet();
	} //END GLn_Zm (constructor)
	
	
	/**
	 * Constructs a new GLn matrix using a given Grid, a known determinate, and inverse
	 * @param nums The given Grid for this GLn
	 * @param det The known determinate of this GLn
	 * @param inv The inverse of this GLn
	 */
	protected GLn(Grid<T> nums, T det, GLn<T> inv) {
		super(nums);
		
		this.det = det;
		this.inv = inv;
	} //END GLn_Zm (constructor)
	
	
	/**
	 * Returns the transpose of this GLn
	 * @return The transpose of this GLn
	 * @throws InvalidDimException Thrown if this GLn has invalid dimensions
	 * @throws InvalidDetException Thrown if this GLn has an invalid determinant 
	 */
	public GLn<T> getTranspose() throws InvalidDimException, InvalidDetException {
		return(new GLn<T>(this.nums.transpose()));
	} //END getTranspose
	
	
	/**
	 * Returns the determinant of this GLn
	 * @return The determinant of this GLn
	 */
	public T getDet() {
		return det;
	} //END getDet
	
	
	/**
	 * Returns the multiplicative inverse of this GLn
	 * @return The multiplicative inverse of this GLn
	 */
	public GLn<T> getInverse() {
		return inv;
	} //END getInverse

	
	/**
	 * Returns an nxn identity matrix
	 * @return An nxn identity matrix
	 */
	public GLn<T> getIdentity() {
		Grid<T> temp = this.getIdentityGrid();
		
		try {
			return new GLn<T>(temp);
		}
		catch (Exception e){
			return null;
		}
	} //END getIdentity
	
	
	/**
	 * Returns an nxn grid for an identity matrix
	 * @return An nxn grid for an identity matrix
	 */
	protected Grid<T> getIdentityGrid() {
		Grid<T> temp = new Grid<T>(n,n);
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				temp.set(i,j, 
				(i == j) ? 	this.nums.get(0,0).getMultIdentity(): 
							this.nums.get(0,0).getAddIdentity());
			}
		}
		
		return temp;
	} //END getIdentityGrid
	
	
	/**
	 * Calculates the determinate and inverse of this GLn simultaneously
	 * @throws InvalidDetException Thrown if this GLn is not invertible
	 */
	protected void genDet() throws InvalidDetException {
		//01 INITIALIZE TEMPORARY MEMORY
		Grid<T> tempNums = this.copyGrid();
		Grid<T> tempGrid = this.getIdentityGrid();
		T tempNum;
		
		//02 REDUCE TO ECHELON FORM
		for (int i = 0; i < n-1; i++) {
			tempNum = tempNums.get(i,i);
			
			//02.1 HANDLE CASE WHERE LEADING ENTRY IS NOT INVERTIBLE
			if ( tempNum.getMultInverse() == null ) {
				int k = i+1;

				//02.1.1   TRY ROW SWAP
				//02.1.1a  FIND ROW WITH INVERTIBLE ENTRY IN iTH COLUMN
				while ( (k < n) && (tempNums.get(k,i).getMultInverse() == null) ) {
					k++;
				}
			
				//02.1.1b IF ROW IS FOUND, SWAP IT WITH THE iTH ROW
				if (k < n) {
					T tempSwap;
					for (int j = 0; j < n; j++) {
						tempSwap = tempNums.get(i,j);
						tempNums.set(i,j, tempNums.get(k,j));
						tempNums.set(k,j, tempSwap);
						
						tempSwap = tempGrid.get(i,j);
						tempGrid.set(i,j, tempGrid.get(k,j));
						tempGrid.set(k,j, tempSwap);
					}
				}
				
				//02.1.2 IF ROW IS NOT FOUND, TRY SUMS OF COMBINATIONS OF ROWS TO MAKE INVERTIBLE ENTRY IN iTH COLUMN
				else {
					//02.1.2a DEFINE COMBINATIONS
					//p IS NUMBER OF ROWS FOLLOWING ROW i
					//q IS NUMBER OF ROWS TO BE CONSIDERED
					//comb IS CURRENT COMBINTATION
					int p = n-i-1;
					int q = 1;
					int[] comb = strtCombination(p,q);
					
					//0.2.1.2b TRY COMBINATIONS OF ROWS UNTIL AN INVERTIBLE SUM IS FOUND
					//	IF NO COMBINATION IS FOUND, THROW INVALID DETERMINATE EXCEPTION
					do {
						tempNum = tempNums.get(i,i);
						
						//IF NO MORE COMBINATIONS OF SIZE q AND q < p,
						//	INCREMENT q
						if (comb == null && q < p) {
							q++;
							comb = strtCombination(p,q);
						}
						//IF NO MORE COMBINATIONS OF SIZE q and q = p,
						//	THROW INVALID DETERMINATE EXCEPTION
						else if (comb == null && q >= p) {
							throw new InvalidDetException();
						}
						//OTHERWISE, SEE IF THE SUM OF THE COMBINATION OF ROWS' ith ENTRIES IS INVERTIBLE 
						else {
							for (int l = 0; l < q; l++)
								tempNum = tempNum.add(tempNums.get(i+comb[l],i));
							
							//IF INVERTIBLE, SUM ROWS IN tempNums AND tempGrid AND BREAK WHILE LOOP
							if (tempNum.getMultInverse() != null) {
								for (int l = 0; l < q; l++) {
									for (int j = 0; j < n; j++) {
										tempNums.set(i,j, tempNums.get(i,j).add(tempNums.get(i+comb[l],j)));
										tempGrid.set(i,j, tempGrid.get(i,j).add(tempGrid.get(i+comb[l],j)));
									}
								}
								break;
							}
							//OTHERWISE TRY NEXT COMBINATION
							else
								comb = nextCombination(comb,p,q);
						}
					} while (q <= p); //END do while (p > 0 && q <= p)
				} //END else 02.1.2
			} //END if ( tempNum.equals(nums.get(0,0).getAddIdentity()) )
			
			//02.2 REDUCE iTH ROW TO HAVE LEADING 1
			tempNum = tempNums.get(i,i).getMultInverse();	
	
			for (int j = 0; j < n; j++) {
				tempNums.set(i, j, tempNums.get(i,j).mult(tempNum));
				tempGrid.set(i, j, tempGrid.get(i,j).mult(tempNum));
			}

			//02.3 ELIMINATE ALL ENTRIES IN iTH COLUMN AND BELOW iTH ROW
			for (int k = i+1; k < n; k++) {
				tempNum = tempNums.get(k,i).copy();

				//02.3.1 ENTRY k,j IS THE SUM OF ENTRY k,j AND THE ADDITIVE INVERSE OF THE PRODUCT OF ENTRY i,j
				//	AND ENTRY k,i
				for (int j = 0; j < n; j++) {
					tempNums.set(k,j, tempNums.get(k,j).add(tempNums.get(i,j).mult(tempNum).getAddInverse()));
					tempGrid.set(k,j, tempGrid.get(k,j).add(tempGrid.get(i,j).mult(tempNum).getAddInverse()));
				}
			}
		}

		//03 SET DETERMINATE
		//	Reduce last row
		this.det = tempNums.get(n-1,n-1).copy();
		
		tempNum = tempNums.get(n-1,n-1).getMultInverse();
		tempNums.set(n-1,n-1, tempNums.get(n-1,n-1).mult(tempNum));
		
		for (int j = 0; j < n; j++) {
			tempGrid.set(n-1,j, tempGrid.get(n-1,j).mult(tempNum));
		}
		
		//04 REDUCE TO REDUCED ECHELON FORM
		for (int i = 0; i < n; i++) {
			
			//04.1 ELIMINATE ALL ENTRIES IN iTH COLUMN AND ABOVE ith ROW
			for (int k = i-1; k >= 0; k--) {
				tempNum = tempNums.get(k,i).copy();
				
				//04.1.1 ENTRY k,j IS THE SUM OF ENTRY k,j AND THE ADDITIVE INVERSE OF THE PRODUCT OF ENTRY i,j
				//	AND ENTRY k,i
				for (int j = 0; j < n; j++) {
					tempNums.set(k,j, tempNums.get(k,j).add(tempNums.get(i,j).mult(tempNum).getAddInverse()));
					tempGrid.set(k,j, tempGrid.get(k,j).add(tempGrid.get(i,j).mult(tempNum).getAddInverse()));
				}
			}
		}
		
		//05 SET INVERSE
		this.inv = new GLn<T>(tempGrid, this.det.getMultInverse(), this);
	} //END getDet
	
	
	/**
	 * Generates the first combination of k integers chosen from n integers
	 * @param n The number of integers to choose from
	 * @param k The number of integers to be chosen
	 * @return The first combination i.e. {1,2,...,k} if k<=n
	 */
	private static int[] strtCombination(int n, int k) {
		if (k > n) {
			return null;
		}
		else {
			int[] comb = new int[k];
			
			for (int i = 0; i < k; i++)
				comb[i] = i+1;
			
			return comb;
		}
	} //END strtCombination
	
	
	/**
	 * Generates the next combination of k integers chosen from n integers given an
	 * existing combination of k integers
	 * @param comb A given combination of k integers
	 * @param n The number of integers to choose from
	 * @param k The number of integers to be chosen
	 * @return
	 */
	private static int[] nextCombination(int[] comb, int n, int k) {
		int i = k-1;

		while (i >= 0) {
			if (comb[i] < n-k+i+1) {
				comb[i]++;
				break;
			}
			i--;
		}

		if (i < 0)
			return null;
		else
			for (int j = 1; j < k-i; j++)
				comb[i+j] = comb[i]+j;
			
		return comb;
	} //END nextCombination
	
} //END GLn
