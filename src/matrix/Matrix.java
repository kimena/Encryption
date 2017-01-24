package matrix;

import matrix.matrixException.InvalidDimException;
import matrix.matrixException.OutOfBoundsException;
import ring.Ring;

/**
 * A Class that defines the basic operation and characteristics of matrices
 * Entries in this matrix are of type T which must implement a Ring
 * @author Noah Kime
 * @param <T> The type of Ring Object that are this matrix's elements
 */
public class Matrix<T extends Ring<T>> {
	
	protected int m;
	protected int n;
	
	protected Grid<T> nums;
	
	
	/**
	 * Constructs a new Matrix using a given Grid of Ring Objects
	 * @param nums A given Grid of Ring Objects
	 */
	public Matrix(Grid<T> nums) {
		this.m = nums.getRows();
		this.n = nums.getCols();
		this.nums = nums;
	} //END Matrix (constructor)
	
	
	/**
	 * Returns the entry in the ith row and the jth column of this Matrix
	 * @param i The index of the ith row
	 * @param j The index of the jth column
	 * @return The entry at position (i,j) of this Matrix
	 * @throws OutOfBoundsException If i or j is an invalid index number 
	 */
	public T get(int i, int j) throws OutOfBoundsException {
		if (i < m && i >= 0 && j < n && j >= 0)
			return nums.get(i,j);
		else
			throw new OutOfBoundsException();
	} //END get
	
	
	/**
	 * Returns the number of rows in this Matrix
	 * @return The number of rows in this Matrix
	 */
	public int getM() {
		return m;
	} //END getM
	
	
	/**
	 * Returns the number of columns in this Matrix
	 * @return The number of columns in this Matrix
	 */
	public int getN() {
		return n;
	} //END getN
	
	
	/**
	 * Sets the entry in the ith row and the jth column of this Matrix
	 * @param i The index of the ith row
	 * @param j The index of the jth column
	 * @param k The value for entry (i,j) to be set to
	 * @throws OutOfBoundsException If i or j is an invalid index number 
	 */
	public void set(int i, int j, T k) throws OutOfBoundsException {
		if (i < n && i >= 0 && j < m && j >= 0)
			nums.set(i,j,k);
		else
			throw new OutOfBoundsException();
	} //END set
	
	
	/**
	 * Copies the existing Matrix into a new Matrix
	 * @return The new copy of this Matrix
	 */
	public Matrix<T> copy() {
		return new Matrix<T>(this.copyGrid());
	} //END copy
	
	
	/**
	 * Returns a copy of the nums Grid
	 * @return A copy of the nums Grid
	 */
	protected Grid<T> copyGrid() {
		Grid<T> tempGrid = new Grid<T>(n,n);
		for (int i = 0; i < n; i++)	
			for (int j = 0; j < n; j++)
				tempGrid.set(i,j, nums.get(i, j).copy());
		
		return tempGrid;
	} //END copyGrid
	
	
	/**
	 * Adds a given Matrix to this Matrix and returns the sum as a new Matrix
	 * @param x The given Matrix to be added
	 * @return The sum of this Matrix and the given Matrix
	 * @throws InvalidDimException Thrown if given Matrix does not have same dimensions as this Matrix
	 * @throws OutOfBoundsException Thrown if add function fails due to invalid dimensions and indices
	 */
	public Matrix<T> addRet(Matrix<T> x) throws InvalidDimException, OutOfBoundsException {
		return new Matrix<T>(add(x, this));
	} //END addRet
	
	
	/**
	 * Adds a given Matrix to this Matrix and sets this Matrix to the resulting sum
	 * @param x The given Matrix to be added
	 * @throws InvalidDimException Thrown if given Matrix does not have same dimensions as this Matrix
	 * @throws OutOfBoundsException Thrown if add function fails due to invalid dimensions and indices
	 */
	public void add(Matrix<T> x) throws InvalidDimException, OutOfBoundsException {
		this.nums = add(x, this);
	} //END add
	
	
	/**
	 * Adds two given Matrices of corresponding dimensions and returns the sum.
	 * @param x A given Matrix to be added
	 * @param y A given Matrix to be added
	 * @return The sum of the two given Matrices
	 * @throws InvalidDimException Thrown if Matrices do not have same dimensions
	 * @throws OutOfBoundsException Thrown if add fails due to invalid dimensions and indices
	 */
	private Grid<T> add(Matrix<T> x, Matrix<T> y) throws InvalidDimException, OutOfBoundsException {
		if (x.m != y.m || x.n != y.n)
			throw new InvalidDimException();
		
		Grid<T> temp = new Grid<T>(x.m, x.n);
		
		for (int i = 0; i < x.m; i++) {
			for (int j = 0; j < x.n; j++) {
				temp.set(i,j, (T) x.get(i,j).add(y.get(i,j)));
			}
		}
		
		return temp;
	} //END add
	
	
	/**
	 * Multiplies the given Matrix on the left of this Matrix and returns the result
	 * @param x The given Matrix to be multiplied on the left of this Matrix
	 * @return The product of this Matrix with the given Matrix multiplied on the left
	 * @throws InvalidDimException Thrown if given Matrix does not have corresponding dimensions as this Matrix
	 * @throws OutOfBoundsException Thrown if add function fails due to invalid dimensions and indices
	 */
	public Matrix<T> multLRet(Matrix<T> x) throws InvalidDimException, OutOfBoundsException {
		return multRet(x, this);
	} //END multLRet
	
	
	/**
	 * Multiplies the given Matrix on the left of this Matrix and sets this Matrix to the resulting product
	 * @param x The given Matrix to be multiplied on the left of this Matrix
	 * @throws InvalidDimException Thrown if given Matrix does not have corresponding dimensions as this Matrix
	 * @throws OutOfBoundsException Thrown if add function fails due to invalid dimensions and indices
	 */
	public void multL(Matrix<T> x) throws InvalidDimException, OutOfBoundsException {
		Grid<T> temp = mult(x, this);
		
		this.m = temp.getRows();
		this.n = temp.getCols();
		this.nums = temp;
	} //END multL
	
	
	/**
	 * Multiplies the given Matrix on the right of this Matrix and returns the result
	 * @param x The given Matrix to be multiplied on the right of this Matrix
	 * @return The product of this Matrix with the given Matrix multiplied on the right
	 * @throws InvalidDimException Thrown if given Matrix does not have corresponding dimensions as this Matrix
	 * @throws OutOfBoundsException Thrown if add function fails due to invalid dimensions and indices
	 */
	public Matrix<T> multRRet(Matrix<T> x) throws InvalidDimException, OutOfBoundsException {
		return multRet(this, x);
	} //END multRRet
	
	
	/**
	 * Multiplies the given Matrix on the right of this Matrix and sets this Matrix to the resulting product
	 * @param x The given Matrix to be multiplied on the right of this Matrix
	 * @throws InvalidDimException Thrown if given Matrix does not have corresponding dimensions as this Matrix
	 * @throws OutOfBoundsException Thrown if add function fails due to invalid dimensions and indices
	 */
	public void multR(Matrix<T> x) throws InvalidDimException, OutOfBoundsException {
		Grid<T> temp = mult(this, x);
		
		this.m = temp.getRows();
		this.n = temp.getCols();
		this.nums = temp;
	} //END multR
	
	
	/**
	 * Multiplies two matrices x and y
	 * @param x Given Matrix x to be multiplied on the left
	 * @param y Given Matrix y to be multiplied on the right
	 * @return The product of the matrices as a new Matrix
	 * @throws InvalidDimException Thrown Matrices do not have corresponding dimensions
	 * @throws OutOfBoundsException Thrown if add function fails due to invalid dimensions and indices
	 */
	private Matrix<T> multRet(Matrix<T> x, Matrix<T> y) throws InvalidDimException, OutOfBoundsException {
		Grid<T> temp = mult(x,y);
		return new Matrix<T>(temp);
	} //END multRet
	
	
	/**
	 * Multiplies two matrices x and y and returns the resulting Matrix' array
	 * @param x Given Matrix x to be multiplied on the left
	 * @param y Given Matrix y to be multiplied on the right
	 * @return The Grid representation of the resulting Matrix
	 * @throws InvalidDimException Thrown Matrices do not have corresponding dimensions
	 * @throws OutOfBoundsException Thrown if add function fails due to invalid dimensions and indices
	 */
	private Grid<T> mult(Matrix<T> x, Matrix<T> y) throws InvalidDimException, OutOfBoundsException {
		if (x.n != y.m)
			throw new InvalidDimException();
		
		Grid<T> temp = new Grid<T>(x.m, y.n);
		this.initializeGrid(temp);
		
		for (int i = 0; i < x.m; i++) {
			for (int j = 0; j < y.n; j++) {
				for (int k = 0; k < x.n; k++)
					temp.set( i, j, temp.get(i, j).add(x.get(i,k).mult(y.get(k,j))) );
			}
		}
		
		return temp;
	} //END mult
	
	
	/**
	 * Multiplies this Matrix by a given scalar
	 * @param scalar The given scalar to multiply this Matrix by
	 */
	public void mult(T scalar) {
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				nums.set(i,j, nums.get(i,j).mult(scalar));
			}
		}
	} //END mult
	
	
	/**
	 * Multiplies this Matrix by a given scalar and returns the result
	 * @param scalar The given scalar to multiply this Matrix by
	 * @return Returns the product of this Matrix and the given scalar
	 */
	public Matrix<T> multRet(T scalar) {
		Grid<T> temp = new Grid<T>(m,n);
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				temp.set(i,j, nums.get(i,j).mult(scalar));
			}
		}
		
		return new Matrix<T>(temp);
	} //END multRet
	
	
	/**
	 * Returns the transpose of this matrix
	 * @return The transpose of this matrix
	 */
	public Matrix<T> transpose() {	
		return(new Matrix<T>(this.nums.transpose()));
	} //END getTranspose
	
	
	/**
	 * Returns this Matrix as a Grid
	 * @return This Matrix as a Grid
	 */
	public Grid<T> asGrid() {
		return this.nums.copy();
	} //END asGrid
	
	
	/**
	 * Initializes a given Grid to have the additive identity in every position
	 * @param grid A given Grid to be initialized
	 */
	private void initializeGrid(Grid<T> grid) {
		T temp = nums.get(0,0);
		for (int i = 0; i < grid.getRows(); i++)
			for (int j = 0; j < grid.getCols(); j++)
				grid.set(i,j, temp.getAddIdentity());
	} //END initializeGrid
	
	
	/**
	 * Determines if this Matrix and a given Object are logically equivalent
	 * @param o The given object to be compared to this Matrix
	 * @return True if the given object and this Matrix are equivalent
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		
		if (!(o instanceof Matrix<?>))
			return false;
		
		Matrix<?> obj = (Matrix<?>) o;
		
		if (this.m != obj.m || this.n != obj.n)
			return false;
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				try {
					if ( !this.get(i,j).equals(obj.get(i,j)) )
						return false;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		
		return true;
	} //END equals
	
	
	/**
	 * Returns the String representation of this Matrix
	 * @return The String representation of this Matrix
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < m; i++) {
			sb.append("[");
			for (int j = 0; j < n-1; j++) {
				sb.append(nums.get(i,j) + ", ");
			}
			sb.append(nums.get(i,n-1) + "]\n");
		}
		
		return sb.toString();
	} //END toString
	
} //END Matrix
