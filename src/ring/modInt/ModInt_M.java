package ring.modInt;

import ring.Ring;

/**A Class that implements a Ring and performs modular arithmetic
 * over the integers modulo m
 * @author Noah Kime
 */
public class ModInt_M implements Ring<ModInt_M> {
	
	private Z_M m;
	private int val;
	private int inv;

	
	/**Constructs an integer mod m
	 * @param val The given value for this integer
	 * @param m The given Z_M class with which to perform modular arithmetic 
	 */
	public ModInt_M(int val, Z_M m) {
		this.m = m;
		this.val = val;
		
		this.inv = m.getMultInverse(this.val);
	} //END ModInt_M (constructor)
	
	
	/**Constructs an integer mod m with a known inverse
	 * @param val The given value for this integer
	 * @param inv The known inverse for this integer 
	 * @param m The given Z_M class with which to perform modular arithmetic 
	 */
	private ModInt_M(int val, int inv, Z_M m) {
		this.m = m;
		this.val = val;
		this.inv = inv;
	} //END ModInt_M (constructor)
	
	
	/**Returns the sum of this integer and a given integer modulo m
	 * @param The given integer to be added with this integer
	 * @return The sum of this integer and the given integer modulo m
	 */
	public ModInt_M add(ModInt_M o) {
		return new ModInt_M(m.add(this.val, o.val), this.m);
	} //END getAdd
	
	
	/**Returns the additive inverse of this integer modulo m
	 * @return the additive inverse of this integer modulo m
	 */
	public ModInt_M getAddInverse() {
		return new ModInt_M(m.getAddInverse(val), m);
	} //END getAddInverse
	
	
	/**Returns the additive identity of the integers modulo m
	 * @return The additive inverse of the integers modulo m
	 */
	public ModInt_M getAddIdentity() {
		return new ModInt_M(0, m);
	} //END getAddIdentity
	
	
	/**Returns the product of this integer and a given integer modulo m
	 * @param The given integer to be multiplied with this integer
	 * @return The product of this integer and the given integer modulo m
	 */
	public ModInt_M mult(ModInt_M o) {
		return new ModInt_M(m.mult(this.val, o.val), this.m);
	} //END mult
	
	
	/**Returns the multiplicative inverse of this integer modulo m and null if no inverse exists
	 * @return The multiplicative inverse of this integer modulo m and null if no inverse exists
	 */
	public ModInt_M getMultInverse() {
		if (inv == -1)
			return null;
		else
			return new ModInt_M(inv, val, m);
	} //END getMultInverse
	

	/**Returns the multiplicative identity of the integers modulo m
	 * @return The multiplicative inverse of the integers modulo m
	 */
	public ModInt_M getMultIdentity() {
		return new ModInt_M(1, 1, m);
	} //END getMultIdentity
	
	
	/**Returns the integer value of this ModInt_M
	 * @return The integer value of this ModInt_M
	 */
	public int getVal() {
		return new Integer(val);
	} //END getVal
	
	
	/**Returns this ModInt_M's Z_M helper class
	 * @return This ModInt_M's Z_M helper class
	 */
	public Z_M getM() {
		return m;
	} //END getM
	
	
	/**Returns a copy of this ModInt_M
	 * @return A copy of this ModInt_M
	 */
	public ModInt_M copy() {
		return new ModInt_M(new Integer(val), new Integer(inv), m);
	} //END copy
	
	
	/**Determines whether this ModInt_M and a given object are equivalent
	 * @param o The given object to be compared with this
	 * @return True if this ModInt_M and the given object are equivalent and false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		
		if (o instanceof ModInt_M) {
			ModInt_M x = (ModInt_M) o;
			
			if ( (this.m.equals(x.m)) && (this.val == x.val) )
				return true;
		}
		
		return false;
	}
	
	
	/**Returns the value of this ModInt_M represented as a String
	 * @return The value of this ModInt_M represented as a String
	 */
	@Override
	public String toString() {
		return Integer.toString(val);
	} //END toString
	
} //END ModInt_M
