package rigidbody;

import _math.Matrix;
import _math.Quaternion;
import _math.Real;
import _math.Vector3D;
import util.ErrorMessages;

/**
 * a 4 by 4 <code>Matrix</code>
 */
public class Matrix4 extends Matrix {

	final public static Matrix4 ZERO = Quaternion.ZERO.toOrientationAndPositionMatrix( Vector3D.ZERO );
	
	/**
	 * creates a 4 by 4 <code>Matrix</code> with all data initialized to <code>null</code>
	 */
	public Matrix4() {
		super( 4 , 4 );
	}
	
	/**
	 * creates a 4 by 4 <code>Matrix</code> with the given data
	 * 
	 * @param data							the initial data for the <code>Matrix</code>
	 * @throws IllegalArgumentException		if the data is not 4 by 4
	 */
	public Matrix4( Real[][] data ) {
		super( data );
		if ( data.length != 4 || data[0].length != 4 ) {
			throw new IllegalArgumentException( ErrorMessages.Math.Matrix.INVALID_MATRIX4_DIMENSION );
		}
	}
	
	
	@Override
	public Matrix4 add( Matrix augend ) {
		return new Matrix4( calculateAddData( augend ) );
	}
	
	@Override
	public Matrix4 subtract( Matrix subtrahend ) {
		return new Matrix4( calculateSubtractData( subtrahend ) );
	}
	
	@Override
	public Matrix4 multiply( Real scalar ) {
		return new Matrix4( calculateMultiplyData( scalar ) );
	}
	
	@Override
	public Matrix multiply( Matrix multiplier ) {
		if ( multiplier instanceof Matrix4 ) {
			return new Matrix4( calculateMultiplyData( multiplier ) );
		} else {
			return new Matrix( calculateMultiplyData( multiplier ) );
		}
	}
	
	@Override
	public Matrix4 divide( Real scalar ) {
		return new Matrix4( calculateDivideData( scalar ) );
	}
	
	/**
	 * @return			the inverse of a 4x4 matrix with row 4 as [0 0 0 1]
	 */
	@Override
	public Real[][] calculateInverseData() {
		float a11 = this.get( 0 , 0 ).value();
		float a12 = this.get( 0 , 1 ).value();
		float a13 = this.get( 0 , 2 ).value();
		float a14 = this.get( 0 , 3 ).value();
		float a21 = this.get( 1 , 0 ).value();
		float a22 = this.get( 1 , 1 ).value();
		float a23 = this.get( 1 , 2 ).value();
		float a24 = this.get( 1 , 3 ).value();
		float a31 = this.get( 2 , 0 ).value();
		float a32 = this.get( 2 , 1 ).value();
		float a33 = this.get( 2 , 2 ).value();
		float a34 = this.get( 2 , 3 ).value();
		float a41 = this.get( 3 , 0 ).value();
		float a42 = this.get( 3 , 1 ).value();
		float a43 = this.get( 3 , 2 ).value();
		float a44 = this.get( 3 , 3 ).value();
		
		float determinant = 
				  a11 * a22 * a33 * a44 
				+ a11 * a23 * a34 * a42 
				+ a11 * a24 * a32 * a43
				
				+ a12 * a21 * a34 * a43
				+ a12 * a23 * a31 * a44
				+ a12 * a24 * a33 * a41
				
				+ a13 * a21 * a32 * a44
				+ a13 * a22 * a34 * a41
				+ a13 * a24 * a31 * a42
				
				+ a14 * a21 * a33 * a42 
				+ a14 * a22 * a31 * a43 
				+ a14 * a23 * a32 * a41
				
				- a11 * a22 * a34 * a43
				- a11 * a23 * a32 * a44
				- a11 * a24 * a33 * a42
				
				- a12 * a21 * a33 * a44
				- a12 * a23 * a34 * a41
				- a12 * a24 * a31 * a43
				
				- a13 * a21 * a34 * a42
				- a13 * a22 * a31 * a44
				- a13 * a24 * a32 * a41
				
				- a14 * a21 * a32 * a43
				- a14 * a22 * a33 * a41
				- a14 * a23 * a31 * a42;
		
		if ( determinant == 0 ) {
			throw new IllegalArgumentException( ErrorMessages.Math.Matrix.NO_EXISTING_INVERSE );
		}
		
		float b11 = 
				  a22 * a33 * a44
				+ a23 * a34 * a42
				+ a24 * a32 * a43
				- a22 * a34 * a43 
				- a23 * a32 * a44
				- a24 * a33 * a42;
				
		float b12 = a12 * a34 * a43 
				+ a13 * a32 * a44
				+ a14 * a33 * a42
				- a12 * a33 * a44
				- a13 * a34 * a42
				- a14 * a32 * a43;
		
		float b13 = a12 * a23 * a44
				+ a13 * a24 * a42
				+ a14 * a22 * a43
				- a12 * a24 * a43
				- a13 * a22 * a44
				- a14 * a23 * a42;
		
		float b14 = a12 * a24 * a33
				+ a13 * a22 * a34
				+ a14 * a23 * a32
				- a12 * a23 * a34
				- a13 * a24 * a32
				- a14 * a22 * a33;
		
		float b21 = a21 * a34 * a43
				+ a23 * a31 * a44
				+ a24 * a33 * a41
				- a21 * a33 * a44
				- a23 * a34 * a41
				- a24 * a31 * a43;
		
		float b22 = a11 * a33 * a44
				+ a13 * a34 * a41
				+ a14 * a31 * a43
				- a11 * a34 * a43
				- a13 * a31 * a44
				- a14 * a33 * a41;
		
		float b23 = a11 * a24 * a43
				+ a13 * a21 * a44
				+ a14 * a23 * a41
				- a11 * a23 * a44
				- a13 * a24 * a41
				- a14 * a21 * a43;
		
		float b24 = a11 * a23 * a34
				+ a13 * a24 * a31
				+ a14 * a21 * a33
				- a11 * a24 * a33
				- a13 * a21 * a34
				- a14 * a23 * a31;
		
		float b31 = a21 * a32 * a44
				+ a22 * a34 * a41
				+ a24 * a31 * a42
				- a21 * a34 * a42
				- a22 * a31 * a44
				- a24 * a32 * a41;
		
		float b32 = a11 * a34 * a42
				+ a12 * a31 * a44
				+ a14 * a32 * a41
				- a11 * a32 * a44
				- a12 * a34 * a41
				- a14 * a41 * a42;
		
		float b33 = a11 * a22 * a44
				+ a12 * a24 * a41
				+ a14 * a21 * a42
				- a11 * a24 * a42
				- a12 * a21 * a44
				- a14 * a22 * a41;
		
		float b34 = a11 * a24 * a32
				+ a12 * a21 * a34
				+ a14 * a22 * a31
				- a11 * a22 * a34
				- a12 * a24 * a31
				- a14 * a21 * a32;
		
		float b41 = a21 * a33 * a42
				+ a22 * a31 * a43
				+ a23 * a32 * a41
				- a21 * a32 * a43
				- a22 * a33 * a41
				- a23 * a31 * a42;
		
		float b42 = a11 * a32 * a43
				+ a12 * a33 * a41
				+ a13 * a31 * a42
				- a11 * a33 * a42
				- a12 * a31 * a43
				- a13 * a32 * a41;
		
		float b43 = a11 * a23 * a42
				+ a12 * a21 * a43
				+ a13 * a22 * a41
				- a11 * a22 * a43
				- a12 * a23 * a41
				- a13 * a21 * a42;
		
		float b44 = a11 * a22 * a33
				+ a12 * a23 * a31
				+ a13 * a21 * a32
				- a11 * a23 * a32
				- a12 * a21 * a33
				- a13 * a22 * a31;
		
		Real[][] unscaled = new Real[ 4 ][ 4 ];
		unscaled[ 0 ][ 0 ] = new Real( b11 );
		unscaled[ 0 ][ 1 ] = new Real( b12 );
		unscaled[ 0 ][ 2 ] = new Real( b13 );
		unscaled[ 0 ][ 3 ] = new Real( b14 );
		
		unscaled[ 1 ][ 0 ] = new Real( b21 );
		unscaled[ 1 ][ 1 ] = new Real( b22 );
		unscaled[ 1 ][ 2 ] = new Real( b23 );
		unscaled[ 1 ][ 3 ] = new Real( b24 );
		
		unscaled[ 2 ][ 0 ] = new Real( b31 );
		unscaled[ 2 ][ 1 ] = new Real( b32 );
		unscaled[ 2 ][ 2 ] = new Real( b33 );
		unscaled[ 2 ][ 3 ] = new Real( b34 );
		
		unscaled[ 3 ][ 0 ] = new Real( b41 );
		unscaled[ 3 ][ 1 ] = new Real( b42 );
		unscaled[ 3 ][ 2 ] = new Real( b43 );
		unscaled[ 3 ][ 3 ] = new Real( b44 );
		
		Matrix scaled = new Matrix( unscaled ).divide( new Real( determinant ) );
		return scaled.getData();			 
	}
	
	@Override
	public Matrix4 inverse() {
		return new Matrix4( calculateInverseData() );
	}
	
	/**
	 * transforms the given <code>Vector3D</code> by this <code>Matrix4</code>
	 * 
	 * @param vector
	 * @return
	 */
	private Vector3D transform( Vector3D vector ) {
		Real[][] vectorData = { { vector.getX() } ,
								{ vector.getY() } ,
								{ vector.getZ() } ,
								{ Real.ONE } };
		Matrix multiplier = new Matrix( vectorData );
		Matrix product = this.multiply( multiplier );
		Real transformX = product.get( 0 , 0 );
		Real transformY = product.get( 1 , 0 );
		Real transformZ = product.get( 2 , 0 );
		
		return new Vector3D( transformX , transformY , transformZ );
	}
	
	/**
	 * converts the given local vector, relative to the center of mass of a particular 
	 * <code>RigidBody</code> into world coordinates 
	 *  
	 * @param localVector
	 * @return
	 */
	public Vector3D convertLocalToWorld( Vector3D localVector ) {
		return this.transform( localVector );
	}
	
	/**
	 * transforms the given vector by the inverse of this matrix
	 * 
	 * @param vector
	 * @return
	 */
	private Vector3D inverseAndTransform( Vector3D vector ) {
		Vector3D tmp = new Vector3D( vector.getX().subtract( this.get( 0 , 3 ) ) , vector.getY().subtract( this.get( 1 , 3 ) ) , vector.getZ().subtract( this.get( 2 , 3 ) ) );
		Real productX = tmp.getX().multiply( this.get( 0 , 0 ) ).add( tmp.getY().multiply( this.get( 1 , 0 ) ) ).add( tmp.getZ().multiply( this.get( 2 , 0 ) ) );
		Real productY = tmp.getX().multiply( this.get( 0 , 1 ) ).add( tmp.getY().multiply( this.get( 1 , 1 ) ) ).add( tmp.getZ().multiply( this.get( 2 , 1 ) ) );
		Real productZ = tmp.getX().multiply( this.get( 0 , 2 ) ).add( tmp.getY().multiply( this.get( 1 , 2 ) ) ).add( tmp.getZ().multiply( this.get( 2 , 2 ) ) );
		return new Vector3D( productX , productY , productZ );
	}
	
	/**
	 * converts the given world vector, relative to the world's x, y, and z axes
	 * into a vector relative to the center of mass of a particular <code>Rigidbody</code>
	 * 
	 * @param worldVector
	 * @return
	 */
	public Vector3D convertWorldToLocal( Vector3D worldVector ) {
		return inverseAndTransform( worldVector );
	}
	
	/**
	 * transforms the given <code>Vector3D</code> by this <code>Matrix</code> by
	 * multiplying:
	 * <br>
	 * <pre>
	 * [ a b c d ]    [ x ]     [ ax + by + cz ]
	 * [ e f g h ] *  [ y ] =   [ ex + fy + gz ]
	 * [ i j k l ]    [ z ]     [ ix + jy + kz ]
	 * [ m n o p ]    [ 0 ]
	 * </pre>
	 * @param vector
	 * @return
	 */
	private Vector3D transformForDirection( Vector3D vector ) {
		Real productX = this.get( 0 , 0 ).multiply( vector.getX() ).add( this.get( 0 , 1 ).multiply( vector.getY() ) ).add( this.get( 0 , 2 ).multiply( vector.getZ() ) );
		Real productY = this.get( 1 , 0 ).multiply( vector.getX() ).add( this.get( 1 , 1 ).multiply( vector.getY() ) ).add( this.get( 1 , 2 ).multiply( vector.getZ() ) );
		Real productZ = this.get( 2 , 0 ).multiply( vector.getX() ).add( this.get( 2 , 1 ).multiply( vector.getY() ) ).add( this.get( 2 , 2 ).multiply( vector.getZ() ) );
		return new Vector3D( productX , productY , productZ );
	}
	
	/**
	 * transform a local direction vector into world coordinates using
	 * this matrix.
	 * 
	 * @param direction
	 * @return
	 */
	public Vector3D localDirectionToWorld( Vector3D direction ) {
		return this.transformForDirection( direction );
	}
	
	/**
	 * assumes this <code>Matrix4</code> stores a rotation and a translation
	 * only, so uses some simplifications to obtain the inverse matrix, and then
	 * transforms the vector
	 * 
	 * @param vector
	 * @return
	 */
	private Vector3D transformInverseForDirection( Vector3D vector ) {
		Real productX = vector.getX().multiply( this.get( 0 , 0 ) ).add( vector.getY().multiply( this.get( 1 , 0 ) ) ).add( vector.getZ().multiply( this.get( 2 , 0 ) ) );
		Real productY = vector.getX().multiply( this.get( 0 , 1 ) ).add( vector.getY().multiply( this.get( 1 , 1 ) ) ).add( vector.getZ().multiply( this.get( 2 , 1 ) ) );
		Real productZ = vector.getX().multiply( this.get( 0 , 2 ) ).add( vector.getY().multiply( this.get( 1 , 2 ) ) ).add( vector.getZ().multiply( this.get( 2 , 2 ) ) );
		return new Vector3D( productX , productY , productZ );
	}
	
	/**
	 * transforms a world direction vector into local coordinates using this
	 * matrix.
	 * 
	 * @param direction
	 * @return
	 */
	public Vector3D worldDirectionToLocal( Vector3D direction ) {
		return this.transformInverseForDirection( direction );
	}
	
	@Override
	public Matrix4 transpose() {
		return new Matrix4( calculateTransposeData() );
	}
}
