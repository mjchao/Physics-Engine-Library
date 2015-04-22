package _math;

import java.math.BigDecimal;

/**
 * represents a real number such as 1, 0.5, -1000 or 3.14
 */
final public class Real implements Comparable < Real > {

	/**
	 * the value -1
	 */
	final public static Real NEGATIVE_ONE = new Real( -1 );
	
	/**
	 * the value 0
	 */
	final public static Real ZERO = new Real( 0 );
	
	/**
	 * the value 1/12 or 0.0833333...
	 */
	final public static Real ONE_TWELFTH = new Real( 0.083333333333333f );
	
	/**
	 * the value 1/10000 or 0.0001
	 */
	final public static Real ONE_TEN_THOUSANDTH = new Real( 0.0001 );
	
	/**
	 * the value 1/1000 or 0.001
	 */
	final public static Real ONE_THOUSANDTH = new Real( 0.001 );
	
	/**
	 * the value 1/2 or 0.5
	 */
	final public static Real ONE_HALF = new Real ( 0.5 );
	
	/**
	 * the value 1
	 */
	final public static Real ONE = new Real( 1 );
	
	/**
	 * the value 2
	 */
	final public static Real TWO = new Real( 2 );
	
	/**
	 * the mathematical constant e
	 */
	final public static Real E = new Real( Math.E );
	
	/**
	 * the value 3
	 */
	final public static Real THREE = new Real( 3 );
	
	/**
	 * the value 4
	 */
	final public static Real FOUR = new Real( 4 );
	
	/**
	 * the value 5
	 */
	final public static Real FIVE = new Real( 5 );
	
	/**
	 * the value 10
	 */
	final public static Real TEN = new Real( 10 );
	
	/**
	 * the value 3.4028235 E38
	 */
	final public static Real MAX_VALUE = new Real( Float.MAX_VALUE );
	
	/**
	 * value of this <code>Real</code> number
	 */
	final private float m_value;
	
	/**
	 * constructs a <code>Real</code> value given a <code>float</code>
	 * 
	 * @param value			a numerical value
	 */
	public Real( float value ) {
		this.m_value = value;
	}
	
	/**
	 * constructs a <code>Real</code> value given a <code>double</code>
	 * 
	 * @param value			a numerical value
	 */
	public Real( double value ) {
		this.m_value = ( float ) value;
	}
	
	/**
	 * constructs a <code>Real</code> value given a <code>BigDecimal</code>
	 * 
	 * @param value			a numerical value
	 */
	public Real( BigDecimal value ) {
		this.m_value = value.floatValue();
	}
	
	/**
	 * @return				the value of this <code>Real</code> number
	 */
	public float value() {
		return this.m_value;
	}
	
	/**
	 * @param augend		value to add
	 * @return				the sum of this <code>Real</code> and the augend
	 */
	public Real add( Real augend ) {
		float sum = this.m_value + augend.value();
		return new Real( sum );
	}
	
	/**
	 * @param subtrahend	value to subtract
	 * @return				the difference between this <code>Real</code> and the subtrahend
	 */
	public Real subtract( Real subtrahend ) {
		float difference = this.m_value - subtrahend.value();
		return new Real( difference );
	}
	
	/**
	 * @param multiplier	value by which to multiply
	 * @return				the product of this <code>Real</code> and the multiplicand
	 */
	public Real multiply( Real multiplier ) {
		float product = this.m_value * multiplier.value();
		return new Real( product );
	}
	
	public Vector3D multiply( Vector3D vectorToScale ) {
		return vectorToScale.multiply( this );
	}
	
	/**
	 * @param dividend		value by which to divide
	 * @return				the quotient of this <code>Real</code> divided by the dividend
	 */
	public Real divide( Real dividend ) {
		float quotient = this.m_value / dividend.value();
		return new Real( quotient );
	}
	
	public static Real negative( Real value ) {
		float product = value.value() * Real.NEGATIVE_ONE.value();
		return new Real( product );
	}
	
	public Real squared() {
		float power = this.m_value * this.m_value;
		return new Real( power );
	}
	
	public Real pow( Real exponent ) {
		float power = ( float ) Math.pow( this.m_value , exponent.value() );
		return new Real( power );
	}
	
	/**
	 * @return			the inverse (reciprocol) of this <code>Real</code>
	 */
	public Real inverse() {
		return Real.ONE.divide( this );
	}
	
	/**
	 * @param value			value of which to take the square root
	 * @return				the square root of <code>value</code>
	 */
	public static Real sqrt( Real value ) {
		double doubleValue = value.value();
		return new Real( Math.sqrt( doubleValue ) );
	}
	
	public static Real abs( Real value ) {
		if ( value.compareTo( ZERO ) < 0 ) {
			return value.multiply( Real.NEGATIVE_ONE );
		} else {
			return value;
		}
	}
	
	/**
	 * @param value1
	 * @param value2
	 * @return			the greater of the two: <code>value1</code> or <code>value2</code>
	 */
	public static Real max( Real value1 , Real value2 ) {
		if ( value1.compareTo( value2 ) >= 0 ) {
			return value1;
		} else {
			return value2;
		}
	}
	
	/**
	 * @param value			value of which to take the cosine
	 * @return				the cosine of <code>value</code>
	 */
	public static Real cos( Real value ) {
		double cosine = Math.cos( value.value() );
		return new Real( cosine );
	}
	
	/**
	 * @param value			value of which to take the sine
	 * @return				the sine of <code>value</code>
	 */
	public static Real sin( Real value ) {
		double sine = Math.sin( value.value() );
		return new Real( sine );
	}
	
	/**
	 * @param value
	 * @return				the sign of <code>value</code>. 1 is it is positive,
	 * 						0 if it is 0 and -1 if it is negative
	 */
	public static Real sgn( Real value ) {
		int comparison = value.compareTo( Real.ZERO );
		if ( comparison > 0 ) {
			return Real.ONE;
		} else if ( comparison == 0 ) {
			return Real.ZERO;
		} else {
			return Real.NEGATIVE_ONE;
		}
	}
	
	@Override
	public String toString() {
		return String.valueOf( this.m_value );
	}

	/**
	 * determines if the value of this <code>Real</code> equals value of the given
	 * <code>Real</code>
	 */
	@Override
	public boolean equals( Object toCompare ) {
		if ( toCompare instanceof Real ) {
			Real realToCompare = ( Real ) toCompare;
			return this.m_value == realToCompare.value();
		} else {
			return super.equals( toCompare );
		}
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	/**
	 * compares the values of this <code>Real</code> with another <code>Real</code>
	 */
	@Override
	public int compareTo( Real toCompare ) {
		if ( this.m_value > toCompare.value() ) {
			return 1;
		} else if ( this.m_value == toCompare.value() ) {
			return 0;
		} else {
			return -1;
		}
	}
	
	@Override
	public Real clone() {
		return new Real( this.value() );
	}
}
