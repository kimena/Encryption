package ring.modInt;

import java.util.ArrayList;

/**A helper Class used to assist in mathematics modulo m for given value of m
 * @author Noah Kime
 */
public class Z_M {
	
	private int m;
	private ArrayList<Integer> multSet;
	
	
	/**Constructs a Z_M to perform mathematics modulo a given integer
	 * @param m The given integer
	 */
	public Z_M(int m) {
		this.m = m;
		this.multSet = this.genMultSet();
	} //END Z_M (constructor)
	
	
	/**Returns the sum of two given integers
	 * @param x A given integer
	 * @param y A given integer
	 * @return The sum of two given integers
	 */
	public int add(int x, int y) {
		return modM(x + y);
	} //END add
	
	
	/**Returns the additive inverse of a given integer
	 * @param x The given integer
	 * @return The additive inverse of the given integer
	 */
	public int getAddInverse(int x) {
		return modM(m - x);
	} //END getAddInverse
	
	
	/**Returns the additive identity, zero
	 * @return The additive identity, zero
	 */
	public int getAddIdentity() {
		return 0;
	} //END getAddIdentity
	
	
	/**Returns the product of two given integers modulo m
	 * @param x A given integer
	 * @param y A given integer
	 * @return The product of the given integers modulo m
	 */
	public int mult(int x, int y) {
		return modM(x * y);
	} //END mult
	
	
	/**Returns the multiplicative inverse of a given integer modulo m or -1 if no inverse exists
	 * @param x The given integer
	 * @return The multiplicative inverse of a given integer modulo m or -1 if no inverse exists
	 */
	public int getMultInverse(int x) {
		if (this.multSet.contains(x)) {
			for (int i = 0; i < this.multSet.size(); i++) {
				if (mult(x, this.multSet.get(i)) == 1)
					return this.multSet.get(i);
			}
		}
			
		return -1;
	} //END getMultInverse
	
	
	/**Returns the multiplicative identity, 1
	 * @return The multiplicative identity, 1
	 */
	public int getMultIdentity() {
		return 1;
	} //END getMultIdentity
	
	
	/**
	 * @return
	 */
	public int getM() {
		return new Integer(m);
	} //END getM
	
	
	/**Determines whether two given integers are congruent modulo m
	 * @param x A given integer
	 * @param y A given integer
	 * @return True if the given integers are congruent modulo m and false otherwise
	 */
	public boolean congruent(int x, int y) {
		return (this.modM(x) == this.modM(y));
	} //END equals
	
	
	/**Determines whether this Z_M and a given Object are logically equivalent
	 * @return True if this and the given Object are equivalent and false otherwise
	 */
	public boolean equals(Object o) {
		if (this == o)
			return true;
		
		if (o instanceof Z_M)
			if (this.m == ((Z_M) o).m)
				return true;
		
		return false;
	} //END equals
	
	
	/**Returns the smallest positive value of a given integer modulo m
	 * @param x The given integer
	 * @return The smallest positive value of a given integer modulo m
	 */
	private int modM(int x) {
		return (x % m + m) % m;
	} //END modM
	
	
	/**Recursively solves for the greatest common denominator of two given integers
	 * Uses the Euclidean algorithm
	 * @param a A given integer
	 * @param b A given integer
	 * @return The greatest common denominator of the given integers
	 */
	private int gcd(int a, int b) {
	    return (b == 0) ? a : gcd(b, a % b);
	} //END gcd
	
	
	/**Returns the set of integers that form a Group under multiplication modulo m
	 * @return The set of integers that form a Group under multiplication modulo m
	 */
	public ArrayList<Integer> getMultSet() {
		return multSet;
		//TODO make copy
	} //END getMultSet
	
	
	/**Generates the set of integers that form a Group under multiplication modulo m
	 */
	private ArrayList<Integer> genMultSet() {
		ArrayList<Integer> multSet = new ArrayList<Integer>();
		int temp;
		
		for (int i = 1; i < m; i++) {
			temp = gcd(i, m);
			if (temp == 1)
				multSet.add(i);
		}
		
		return multSet;
	} //END getMultSet
	
} //END Z_M
