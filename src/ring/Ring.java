package ring;

/**An Interface that makes a Class operate with the characteristics of a ring
 * seen in the mathematical topic of ring theory.
 * @author Noah Kime
 * @param <T> Class should be parameterized to its own type to operate correctly
 */
public interface Ring<T> {
	
	/**Adds this Ring Object to a given Ring Object and returns the sum
	 * @param o The given Ring Object
	 * @return The sum of this and the given Ring Object
	 */
	public T add(T o);
	
	/**Multiplies this Ring Object and a given Ring Object and returns the product
	 * @param o The given Ring Object
	 * @return The product of this and the given Ring Object
	 */
	public T mult(T o);
	
	/**Returns the additive inverse of this Ring Object
	 * @return The additive inverse of this Ring Object
	 */
	public T getAddInverse();
	
	/**Returns the additive identity in this Ring
	 * @return the additive identity in this Ring
	 */
	public T getAddIdentity();
	
	/**Returns the multiplicative inverse of this Ring Object
	 * @return The multiplicative inverse of this Ring Object
	 * @throws NoInverseException Throws exception if there is no such inverse
	 */
	public T getMultInverse();
	
	/**Returns the multiplicative identity in this Ring
	 * @return The multiplicative identity in this Ring
	 */
	public T getMultIdentity();
	
	/**Returns a copy of this Ring
	 * @return A copy of this Ring
	 */
	public T copy();
	
} //END Ring
