package _math;

/**
 * represents a 3-dimensional vector [x, y, z]
 */
public class Vector3D implements Comparable < Vector3D > {

	/**
	 * the vector [ 0 , 0 , 0 ]
	 */
	final public static Vector3D ZERO = new Vector3D( Real.ZERO , Real.ZERO , Real.ZERO );
	
	/**
	 * the vector [ 1 , 0 , 0 ]
	 */
	final public static Vector3D WORLD_X_AXIS = new Vector3D( Real.ONE , Real.ZERO , Real.ZERO );
	
	/**
	 * the vector [ 0 , 1 , 0 ]
	 */
	final public static Vector3D WORLD_Y_AXIS = new Vector3D( Real.ZERO , Real.ONE , Real.ZERO );
	
	/**
	 * the vector [ 0 , 0 , 1]
	 */
	final public static Vector3D WORLD_Z_AXIS = new Vector3D( Real.ZERO , Real.ZERO , Real.ONE );
	
	/**
	 * the first (x) value in this vector
	 */
	final private Real m_x;
	
	/**
	 * the second (y) value in this vector
	 */
	final private Real m_y;
	
	/**
	 * the third (z) value in this vector
	 */
	final private Real m_z;
	
	/**
	 * creates a 3-dimensional vector given an x, y, and z value
	 * 
	 * @param x			x value for this vector
	 * @param y			y value for this vector
	 * @param z			z value for this vector
	 */
	public Vector3D( Real x , Real y , Real z ) {
		this.m_x = x;
		this.m_y = y;
		this.m_z = z;
	}
	
	/**
	 * @return		the x value of this vector
	 */
	public Real getX() {
		return this.m_x;
	}
	
	/**
	 * @return		the y value of this vector
	 */
	public Real getY() {
		return this.m_y;
	}
	
	/**
	 * @return		the z value of this vector
	 */
	public Real getZ() {
		return this.m_z;
	}
	
	/**
	 * adds this <code>Vector3D</code> to another <code>Vector3D</code>
	 * <p>
	 * [ a , b , c] + [ d , e , f ] = [ a+d , b+e , c+f ]
	 * 
	 * @param augend			the <code>Vector3D</code> to add
	 * @return					the result of adding this <code>Vector3D</code> to the augend
	 */
	public Vector3D add( Vector3D augend ) {
		Real xSum = this.m_x.add( augend.getX() );
		Real ySum = this.m_y.add( augend.getY() );
		Real zSum = this.m_z.add( augend.getZ() );
		return new Vector3D( xSum , ySum , zSum );
	}
	
	/**
	 * subtracts this <code>Vector3D</code> from another <code>Vector3D</code>
	 * <p>
	 * [ a , b , c] - [ d , e , f ] = [ a-d , b-e , c-f ]
	 * 
	 * @param subtrahend		the <code>Vector3D</code> to subtract
	 * @return					the result of subtracting the subtrahend from this <code>Vector3D</code> fr
	 */
	public Vector3D subtract( Vector3D subtrahend ) {
		Real xDiff = this.m_x.subtract( subtrahend.getX() );
		Real yDiff = this.m_y.subtract( subtrahend.getY() );
		Real zDiff = this.m_z.subtract( subtrahend.getZ() );
		return new Vector3D( xDiff , yDiff , zDiff );
	}
	
	/**
	 * multiplies this <code>Vector3D</code> by a scalar
	 * <p>
	 * s*[ a , b , c] = [ a*s , b*s , c*s ]
	 * 
	 * @param scalar			the scalar by which to multiply this <code>Vector3D</code>
	 * @return					this <code>Vector3D</code> scaled by the scalar
	 */
	public Vector3D multiply( Real scalar ) {
		Real xProduct = this.m_x.multiply( scalar );
		Real yProduct = this.m_y.multiply( scalar );
		Real zProduct = this.m_z.multiply( scalar );
		return new Vector3D( xProduct , yProduct , zProduct );
	}
	
	/**
	 * multiplies this <code>Vector3D</code> by another <code>Vector3D</code>
	 * <p>
	 * [ a , b , c ] * [ d , e , f ] = [ a*d , b*e , c*f ]
	 * 
	 * @param multiplier		<code>Vector3D</code> by which to multiply
	 * @return					the component product of this <code>Vector3D</code> by the multiplier
	 */
	public Vector3D multiply( Vector3D multiplier ) {
		Real xProduct = this.m_x.multiply( multiplier.getX() );
		Real yProduct = this.m_y.multiply( multiplier.getY() );
		Real zProduct = this.m_z.multiply( multiplier.getZ() );
		return new Vector3D( xProduct , yProduct , zProduct );
	}
	
	/**
	 * takes the dot-product of this <code>Vector3D</code> and another <code>Vector3D<code>
	 * <p>
	 * [ a , b , c] (dot) [ d , e , f ] = a*d + b*e + c*f
	 * 
	 * @param vector			the other <code>Vector3D</code> in this dot-product
	 * @return					the dot-product of this <code>Vector3D</code> with the given <code>Vector3D</code>
	 */
	public Real dot( Vector3D vector ) {
		Real xProduct = this.m_x.multiply( vector.getX() );
		Real yProduct = this.m_y.multiply( vector.getY() );
		Real zProduct = this.m_z.multiply( vector.getZ() );
		Real dotProduct = xProduct.add( yProduct ).add( zProduct );
		return dotProduct;
	}
	
	/**
	 * takes the cross-product of this <code>Vector3D</code> and another <code>Vector3D</code>
	 * <p>
	 * [ a , b , c] (cross) [ d , e , f ] = [ b*f - c*e , a*f - c*d , a*e - b*d ] 
	 * 
	 * @param vector			the other <code>Vector3D</code> in this cross-product
	 * @return					the cross-product of this <code>Vector3D</code> with the given <code>Vector3D</code>
	 */
	public Vector3D cross( Vector3D vector ) {
		Real xProduct = this.m_y.multiply( vector.getZ() ).subtract( this.m_z.multiply( vector.getY() ) );
		Real yProduct = this.m_z.multiply( vector.getX() ).subtract( this.m_x.multiply( vector.getZ() ) );
		Real zProduct = this.m_x.multiply( vector.getY() ).subtract( this.m_y.multiply( vector.getX() ) );
		return new Vector3D( xProduct , yProduct , zProduct );
	}
	
	/**
	 * divides this <code>Vector3D</code> by a scalar
	 * 
	 * @param scalar			value by which to shrink
	 * @return					this <code>Vector3D</code> scaled by the 1 / <code>scalar</code>
	 */
	public Vector3D divide( Real scalar ) {
		Real xQuotient = this.m_x.divide( scalar );
		Real yQuotient = this.m_y.divide( scalar );
		Real zQuotient = this.m_z.divide( scalar );
		return new Vector3D( xQuotient , yQuotient , zQuotient );
	}
	
	/**
	 * inverts this <code>Vector3D</code>, i.e. turns it around.
	 * <p>
	 * invert( [ a , b, c] ) = [ -a , -b , -c]
	 * 
	 * @return		this <code>Vector3D</code> except in the opposite direction
	 */
	public Vector3D invert() {
		return this.multiply( Real.NEGATIVE_ONE );
	}
	
	/**
	 * @return		this <code>Vector3D</code> scaled to unit length (1)
	 */
	public Vector3D normalize() {
		Real scale = Real.ONE.divide( this.magnitude() );
		return this.multiply( scale );
	}
	
	/**
	 * @return			the magnitude of this <code>Vector3D</code>
	 */
	public Real magnitude() {
		return Real.sqrt( this.magnitudeSquared() );
	}
	
	/**
	 * @return			the square of the magnitude of this <code>Vector3D</code>
	 */
	public Real magnitudeSquared() {
		return this.m_x.multiply( this.m_x ).add( this.m_y.multiply( this.m_y ).add( this.m_z.multiply( this.m_z ) ) );
	}
	
	@Override
	public String toString() {
		return "[ " + this.m_x + " , " + this.m_y + " , " + this.m_z + " ]"; 
	}
	
	
	/**
	 * determines if the components of this <code>Vector3D</code> are the same
	 * as the components of the given <code>Vector3D</code>
	 */
	@Override
	public boolean equals( Object toCompare ) {
		if ( toCompare instanceof Vector3D ) {
			Vector3D vectorToCompare = ( Vector3D ) toCompare;
			return this.m_x.equals( vectorToCompare.getX() ) &&
					this.m_y.equals( vectorToCompare.getY() ) &&
					this.m_z.equals( vectorToCompare.getZ() );
		} else {
			return super.equals( toCompare );
		}
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	/**
	 * compares the magnitude of this <code>Vector3D</code> with the magnitude
	 * of the given <code>Vector3D</code>
	 */
	@Override
	public int compareTo( Vector3D toCompare ) {
		return this.magnitudeSquared().compareTo( toCompare.magnitudeSquared() );
	}
	
	@Override
	public Vector3D clone() {
		return new Vector3D( this.getX() , this.getY() , this.getZ() );
	}
	
	/**
	 * creates a vector that is perpendicular to two given vectors 
	 * 
	 * @param x							a vector
	 * @param y							another vector
	 * @return							z, a third vector that is perpendicular to both the x and y vectors.
	 * @throws IllegalArgumentException	if x and y are parallel, and therefore, no orthonormal basis can be created
	 */
	public static Vector3D makeOrthonormalBasis( Vector3D x , Vector3D y ) throws IllegalArgumentException {
		Vector3D xAxis = x;
		Vector3D yAxis = y;
		Vector3D zAxis = xAxis.cross( yAxis );
		if ( zAxis.magnitudeSquared().equals( Real.ZERO ) ) {
			throw new IllegalArgumentException();
		} else {
			return zAxis;
		}
	}
}
