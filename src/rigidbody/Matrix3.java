package rigidbody;

import _math.Matrix;
import _math.Real;
import _math.Vector3D;
import util.ErrorMessages;

/**
 * a 3 by 3 <code>Matrix</code>
 */
public class Matrix3 extends Matrix {

	/**
	 * creates a 3 by 3 <code>Matrix</code> with no data
	 */
	public Matrix3() {
		super( 3 , 3 );
	}
	
	/**
	 * creates a 3 by 3 <code>Matrix</code> with the given data
	 * 
	 * @param data						the initial elements in this <code>Matrix3</code>
	 * @throws IllegalArgumentException	if the data is not 3 by 3
	 */
	public Matrix3( Real[][] data ) {
		super( data );
		if ( data.length != 3 || data[0].length != 3 ) {
			throw new IllegalArgumentException( ErrorMessages.Math.Matrix.INVALID_MATRIX3_DIMENSION );
		}
	}
	
	/**
	 * creates a 3 by 3 <code>Matrix3</code> with from the given vectors. if we are given
	 * x = [ a , b , c ] , y = [ d , e , f ] and z = [ g , h , i ], then the resulting <code>Matrix3</code>
	 * is
	 * <pre>
	 * [ a , d , g ]
	 * [ b , e , h ]
	 * [ c , f , i ]
	 * </pre>
	 * 
	 * @param x			a vector
	 * @param y			a vector
	 * @param z			a vector
	 */
	public Matrix3( Vector3D x , Vector3D y , Vector3D z ) {
		this();
		Real[][] matrixData = { { x.getX() , y.getX() , z.getX() } ,
								{ x.getY() , y.getY() , z.getY() } ,
								{ x.getZ() , y.getZ() , z.getZ() } };
		setData( matrixData );
	}
	
	@Override
	public Matrix3 add( Matrix augend ) {
		return new Matrix3( calculateAddData( augend ) );
	}
	
	@Override
	public Matrix3 subtract( Matrix subtrahend ) {
		return new Matrix3( calculateSubtractData( subtrahend ) );
	}
	
	@Override
	public Matrix3 multiply( Real scalar ) {
		return new Matrix3( calculateMultiplyData( scalar ) );
	}
	
	@Override
	public Matrix multiply( Matrix multiplier ) {
		if ( multiplier instanceof Matrix3 ) {
			return new Matrix3( calculateMultiplyData( multiplier ) );
		} else {
			return new Matrix( calculateMultiplyData( multiplier ) );
		}
	}
	
	@Override
	public Matrix3 divide( Real scalar ) {
		return new Matrix3( calculateDivideData( scalar ) );
	}
	
	/**
	 * converts the given vector into its skew-symmetric matrix form:
	 * <p>
	 * <pre>
	 * [ 0 , -z , y ]
	 * [ c , 0 , -a ]
	 * [ -b , a , 0 ]
	 * </pre>
	 * </p>
	 * 
	 * @param vector
	 * @return				the given vector in its skew symmetric matrix form
	 */
	public static Matrix3 toSkewSymmetricMatrix( Vector3D vector ) {
		Real[][] skewSymmetricData = { { Real.ZERO , vector.getZ().multiply( Real.NEGATIVE_ONE ) , vector.getY() } ,
									   { vector.getZ() , Real.ZERO , vector.getX().multiply( Real.NEGATIVE_ONE ) } ,
									   { vector.getY().multiply( Real.NEGATIVE_ONE ) , vector.getX() , Real.ZERO } };
		Matrix3 skewSymmetricMatrix = new Matrix3( skewSymmetricData );
		return skewSymmetricMatrix;
	}
	
	@Override
	public Real[][] calculateInverseData() {
		
		Real a = this.get( 0 , 0 );
		Real b = this.get( 0 , 1 );
		Real c = this.get( 0 , 2 );
		Real d = this.get( 1 , 0 );
		Real e = this.get( 1 , 1 );
		Real f = this.get( 1 , 2 );
		Real g = this.get( 2 , 0 );
		Real h = this.get( 2 , 1 );
		Real i = this.get( 2 , 2);
		
		Real determinant = a.multiply( e ).multiply( i )
				     .add( d.multiply( h ).multiply( c ) )
				     .add( g.multiply( b ).multiply( f ) )
				.subtract( a.multiply( h ).multiply( f ) )
				.subtract( g.multiply( e ).multiply( c ) )
				.subtract( d.multiply( b ).multiply( i ) );
		
		if ( determinant.equals( Real.ZERO ) ) {
			throw new IllegalStateException( ErrorMessages.Math.Matrix.NO_EXISTING_INVERSE );
		}
		
		Real[][] unscaledData = new Real[ this.getRows() ][ this.getColumns() ];
		
		unscaledData[ 0 ][ 0 ] = e.multiply( i ).subtract( f.multiply( h ) );
		unscaledData[ 0 ][ 1 ] = c.multiply( h ).subtract( b.multiply( i ) );
		unscaledData[ 0 ][ 2 ] = b.multiply( f ).subtract( c.multiply( e ) );
		
		unscaledData[ 1 ][ 0 ] = f.multiply( g ).subtract( d.multiply( i ) );
		unscaledData[ 1 ][ 1 ] = a.multiply( i ).subtract( c.multiply( g ) );
		unscaledData[ 1 ][ 2 ] = c.multiply( d ).subtract( a.multiply( f ) );
		
		unscaledData[ 2 ][ 0 ] = d.multiply( h ).subtract( e.multiply( g ) );
		unscaledData[ 2 ][ 1 ] = b.multiply( g ).subtract( a.multiply( h ) );
		unscaledData[ 2 ][ 2 ] = a.multiply( e ).subtract( b.multiply( d ) );
		
		Matrix unscaledRtn = new Matrix( unscaledData );
		Matrix scaledRtn = unscaledRtn.divide( determinant );
		return scaledRtn.getData();
	}
	
	
	@Override
	public Matrix3 inverse() {
		return new Matrix3( calculateInverseData() );
	}
	
	/**
	 * transforms the given <code>Vector3D</code> by this <code>Matrix3</code>
	 * 
	 * @param vector
	 * @return
	 */
	public Vector3D transform( Vector3D vector ) {
		Real[][] vectorData = { { vector.getX() } ,
								{ vector.getY() } ,
								{ vector.getZ() } };
		Matrix multiplier = new Matrix( vectorData );
		Matrix product = super.multiply( multiplier );
		Real transformX = product.get( 0 , 0 );
		Real transformY = product.get( 1 , 0 );
		Real transformZ = product.get( 2 , 0 );
		return new Vector3D( transformX , transformY , transformZ );
	}
	
	@Override
	public Matrix3 transpose() {
		return new Matrix3( calculateTransposeData() );
	}
}
