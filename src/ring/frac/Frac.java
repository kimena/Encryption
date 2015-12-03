package ring.frac;

import ring.Ring;

/**
 * @author Noah Kime
 */
public class Frac implements Ring<Frac>, Comparable<Frac> {
	
	private long p;
	private long q;
	
	
	/**
	 * Constructs a new Frac with numerator p and numerator q
	 * @param p The given numerator for this Frac
	 * @param q The given denominator for this Frac
	 */
	public Frac(int p, int q) {
		this.p = p;
		this.q = q;
	} //END Frac (constructor)
	
	
	/**
	 * @param p
	 * @param q
	 */
	public Frac(long p, long q) {
		this.p = p;
		this.q = q;
	} //END Frac (constructor)
	
	
	/**
	 * @param p
	 * @param q
	 */
	public Frac(int p, long q) {
		this.p = (long) p;
		this.q = q;
	} //END Frac (constructor)
	
	
	/**
	 * @param p
	 * @param q
	 */
	public Frac(long p, int q) {
		this.p = p;
		this.q = (long) q;
	} //END Frac (constructor)
	
	
	/**
	 * @param r
	 */
	public Frac(double r) {
		String s = String.valueOf(r);
	    int digitsDec = s.length() - 1 - s.indexOf('.');        
	    
	    q = 1;
	    
	    for(int i = 0; i < digitsDec; i++){
	        r *= 10;
	        q *= 10;
	    }
	   
	    p = (long) Math.round(r);
	    this.simplify();
	} //END Frac (constructor)
	
	
	/**
	 * @param r
	 */
	public Frac add(Frac r) {
		this.p += (r.p * this.q);
		this.q *= r.q;
		this.simplify();
		
		return this;
	} //END add
	
	
	/**
	 * @param r
	 */
	public void add(double r) {
		this.add(new Frac(r));
	} //END add
	
	
	/**
	 * @param r
	 */
	public void sub(Frac r) {
		this.p -= (r.p * this.q);
		this.q *= r.q;
		this.simplify();
	} //END sub
	
	
	/**
	 * @param r
	 */
	public void sub(double r) {
		this.sub(new Frac(r));
	} //END sub
	
	
	/**
	 * @param r
	 */
	public Frac mult(Frac r) {
		this.p *= r.p;
		this.p *= r.q;
		this.simplify();
		
		return this;
	} //END mult
	
	
	/**
	 * @param r
	 */
	public void mult(double r) {
		this.mult(new Frac(r));
	} //END mult
	
	
	/**
	 * @param r
	 */
	public void div(Frac r) {
		this.p *= r.q;
		this.q *= r.p;
		this.simplify();
	} //END div
	
	
	/**
	 * @param r
	 */
	public void div(double r) {
		this.div(new Frac(r));
	} //END div
	
	
	/**
	 * @return
	 */
	public Frac getAddInverse() {
		return new Frac(-p, q);
	} //END getAddInverse
	
	
	/**
	 *
	 */
	public Frac getAddIdentity() {
		return new Frac(0, 1);
	} //END getAddIdentity
	
	
	/**
	 * @return
	 * @throws NoInverseException 
	 */
	public Frac getMultInverse() {
		if (p == 0)
			return null;
		else
			return new Frac(q, p);
	} //END getMultInverse
	
	
	/**
	 *
	 */
	public Frac getMultIdentity() {
		return new Frac(1, 1);
	} //END getMultIdentity
	
	
	/**Returns a copy of this Frac
	 * @return A copy of this Frac
	 */
	public Frac copy() {
		return new Frac(new Long(p), new Long(q));
	} //END copy
	
	/**
	 * 
	 */
	private void simplify() {
		long k = gcd(p,q);
		while (k != 1) {
			this.p /= k;
			this.q /= k;
		
			k = gcd(p,q);
		}
	} //END simplify
	
	
	/**
	 * @param a
	 * @param b
	 * @return
	 */
	private long gcd(long a, long b) {
	    return (b == 0) ? a : gcd(b, a % b);
	} //END gcd
	
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Frac o) {
		if ( this.p / this.q == o.p / o.q ) 
			return 0;
		else if ( this.p / this.q < o.p / o.q )
			return -1;
		else
			return 1;
	} //END compareTo
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o instanceof Frac)
			if ( (this.p == ((Frac) o).p) && (this.q == ((Frac) o).q) )
				return true;
		
		if (o instanceof Double)
			return this.equals(new Frac((Double) o));
		
		return false;
	} //END equals
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return (q == 1) ? ("" + p) : (p + "/" + q);
	} //END toString
	
} //END Frac
