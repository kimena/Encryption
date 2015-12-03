package matrix;

import ring.modInt.ModInt_M;

import matrix.matrixException.*;

/**A Class that performs modular arithmetic with matrices
 * @author Noah Kime
 */
public class GLn_Zm extends GLn<ModInt_M> {
	
	protected GLn_Zm inverse;
	
	
	/**Constructs a new GLn_Zm matrix using a given Grid
	 * @param nums The given Grid for this GLn_Zm
	 * @throws NoInverseException
	 * @throws MatrixException
	 */
	public GLn_Zm(Grid<ModInt_M> nums) throws InvalidDimException, InvalidDetException {
		super(nums);
		
		inverse = new GLn_Zm(inv.copyGrid(), det.getMultInverse(), this);
	} //END GLn_Zm (constructor)
	
	
	/**Constructs a new GLn_Zm matrix using a given Grid, a known determinate, and inverse
	 * @param nums The given Grid for this GLn_Zm
	 * @param det The known determinate of this GLn_Zm
	 * @param inv The inverse of this GLn_Zm
	 */
	private GLn_Zm(Grid<ModInt_M> nums, ModInt_M det, GLn_Zm inv) {
		super(nums, det, inv);
	} //END GLn_Zm (constructor)
	
	
	/**Returns the multiplicative inverse of this GLn_Zm
	 * @return The multiplicative inverse of this GLn_Zm
	 */
	@Override
	public GLn_Zm getInverse() {
		return inverse;
	} //END getInverse

	
	/**Returns an nxn identity matrix of ModInt_M integers
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
	
	
	/**Returns an nxn grid for an identity matrix of ModInt_M integers
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
	
} //END GLn_Zm
