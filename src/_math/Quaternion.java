package _math;

import rigidbody.Matrix3;
import rigidbody.Matrix4;

/**
 * a <code>Quaternion</code> contains an w, x, y, z part. the
 * <code>Quaternion</code> represents the orientation of some object
 * <p>
 * w = cos( theta / 2 )
 * <br>
 * x = x coordinate * sin( theta / 2 )
 * <br>
 * y = y coordinate * sin( theta / 2 )
 * <br>
 * z = z coordinate * sin( theta / 2 )
 */
public class Quaternion {


	/**
	 * the Quaternion [ 1 , 0 , 0 , 0 ], representing something with 0 degree rotation
	 */
	final public static Quaternion ZERO = new Quaternion( Real.ONE , Real.ZERO , Real.ZERO , Real.ZERO );
	
	/**
	 * the r part of the <code>Quaternion</code>
	 */
	private Real m_w;
	
	/**
	 * i component of the <code>Quaternion</code>
	 */
	private Real m_x;
	
	/**
	 * j component of the <code>Quaternion</code>
	 */
	private Real m_y;
	
	/**
	 * k component of the <code>Quaternion</code>
	 */
	private Real m_z;
	
	/**
	 * creates a <code>Quaternion</code> with the given values
	 * 
	 * @param w
	 * @param x
	 * @param y
	 * @param z
	 */
	public Quaternion( Real w , Real x , Real y , Real z ) {
		this.m_w = w;
		this.m_x = x;
		this.m_y = y;
		this.m_z = z;
	}
	
	/**
	 * @return			the w part of this <code>Quaternion</code>
	 */
	public Real getW() {
		return this.m_w;
	}
	
	/**
	 * @return			the x component of this <code>Quaternion</code>
	 */
	public Real getX() {
		return this.m_x;
	}
	
	/**
	 * @return			the y component of this <code>Quaternion</code>
	 */
	public Real getY() {
		return this.m_y;
	}
	
	/**
	 * @return			the z component of this <code>Quaternion</code>
	 */
	public Real getZ() {
		return this.m_z;
	}
	
	/**
	 * @return			the magnitude of this <code>Quaternion</code>
	 */
	public Real magnitude() {
		return Real.sqrt( this.m_w.squared().add( this.m_x.squared() ).add( this.m_y.squared() ).add( this.m_z.squared() ) );
	}
	
	/**
	 * @return			this <code>Quaternion</code> scaled to unit length
	 */
	public Quaternion normalize() {
		Real magnitude = this.magnitude();
		if ( magnitude.equals( Real.ZERO ) ) {
			return ZERO;
		}
		Real newW = this.m_w.divide( magnitude );
		Real newX = this.m_x.divide( magnitude );
		Real newY = this.m_y.divide( magnitude );
		Real newZ = this.m_z.divide( magnitude );
		return new Quaternion( newW , newX , newY , newZ );
	}
	
	/**
	 * adds this <code>Quaternion</code> to the <code>augend</code> by
	 * adding respective components
	 * 
	 * [ w1 , x1 , y1 , z1 ] + [ w2 , x2 , y2 , z2 ] = [ w1 + w2 , x1 + x2 , y1 + y2 , z1 + z2 ]
	 * 
	 * @param augend		<code>Quaternion</code> to add
	 * @return				<code>this + augend </code>
	 */
	public Quaternion add( Quaternion augend ) {
		Real newW = this.m_w.add( augend.getW() );
		Real newX = this.m_x.add( augend.getX() );
		Real newY = this.m_y.add( augend.getY() );
		Real newZ = this.m_z.add( augend.getZ() );

		return new Quaternion( newW , newX , newY , newZ );
	}
	
	/**
	 * @param multiplier		<code>Quaternion</code> by which to multiply
	 * @return					<code>this * Quaternion</code>
	 */
	public Quaternion multiply( Quaternion multiplier ) {
		
		Real w1 = this.m_w;
		Real w2 = multiplier.getW();
		Real x1 = this.m_x;
		Real x2 = multiplier.getX();
		Real y1 = this.m_y;
		Real y2 = multiplier.getY();
		Real z1 = this.m_z;
		Real z2 = multiplier.getZ();
		
		Real productW = w1.multiply( w2 ).subtract( x1.multiply( x2 ) ).subtract( y1.multiply( y2 ) ).subtract( z1.multiply( z2 ) );
		Real productX = w1.multiply( x2 ).add( x1.multiply( w2 ) ).add( y1.multiply( z2 ) ).subtract( z1.multiply( y2 ) );
		Real productY = w1.multiply( y2 ).subtract( x1.multiply( z2 ) ).add( y1.multiply( w2 ) ).add( z1.multiply( x2 ) );
		Real productZ = w1.multiply( z2 ).add( x1.multiply( y2 ) ).subtract( y1.multiply( x2 ) ).add( z1.multiply( w2 ) );
		
		return new Quaternion( productW , productX , productY , productZ );
	}
	
	/**
	 * multiplies each component in this <code>Quaternion</code> by the given <code>scalar</code>
	 * 
	 * @param scalar		value by which to scale
	 * @return				<code>this * scalar</code>
	 */
	public Quaternion multiply( Real scalar ) {
		Real newW = this.m_w.multiply( scalar );
		Real newX = this.m_x.multiply( scalar );
		Real newY = this.m_y.multiply( scalar );
		Real newZ = this.m_z.multiply( scalar );
		return new Quaternion( newW , newX , newY , newZ );
	}
	
	/**
	 * divides each component in this <code>Quaternion</code> by the given <code>scalar</code>
	 * 
	 * @param scalar		value by which to scale
	 * @return				<code>this / scalar</code>
	 */
	public Quaternion divide( Real scalar ) {
		return this.multiply( Real.ONE.divide( scalar ) );
	}
	
	/**
	 * @param rotation			<code>Vector3D</code> by which to rotate
	 * @return					this <code>Quaternion</code> rotated by <code>rotation</code>
	 */
	public Quaternion rotate( Vector3D rotation ) {
		Quaternion rotationQuaternion = new Quaternion( Real.ZERO , rotation.getX() , rotation.getY() , rotation.getZ() );
		return this.multiply( rotationQuaternion );
	}
	
	/**
	 * updates the angular position using the formula
	 * <p>
	 * theta_f = theta_i + dt/2 * omega * theta_i
	 * <p>
	 * where theta is position and omega is angular velocity multiplied by a dt.
	 * 
	 * @param omega 			angular velocity times a change in time (dt)
	 * @return
	 */
	public Quaternion add( Vector3D omega ) {
		
		//convert the omega vector into a Quaternion
		Quaternion quaternionOmega = new Quaternion( Real.ZERO , omega.getX() , omega.getY() , omega.getZ() );
		
		//calculate initial theta
		Quaternion initialAngularPosition = this;
		
		//calculate delta theta, which is dt/2 * omega * theta_i
		//Quaternion changeInAngularPosition = initialAngularPosition.multiply( quaternionOmega ).divide( Real.TWO );
		Quaternion changeInAngularPosition = quaternionOmega.multiply( initialAngularPosition ).divide( Real.TWO );
		
		//add initial theta to delta theta
		Quaternion finalAngularPosition = initialAngularPosition.add( changeInAngularPosition );

		//return the result
		return finalAngularPosition;
	}
	
	/**
	 * updates the angular position in the same way as <code>add( Vector3D omega )</code>,
	 * except this method takes the opposite of omega and then adds it.
	 * 
	 * @param omega
	 * @return
	 * @see							#add(Vector3D)
	 */
	public Quaternion subtract( Vector3D omega ) {
		return add( omega.multiply( Real.NEGATIVE_ONE ) );
	}
	
	/**
	 * converts this <code>Quaternion</code> into a <code>Matrix3</code> in world coordinates
	 * 
	 * @return			this <code>Quaternion</code> in a <code>Matrix3</code> representation
	 */
	public Matrix3 toOrientationMatrix() {
		Real result00 = Real.ONE.subtract( Real.TWO.multiply( this.m_y.squared() ).add( Real.TWO.multiply( this.m_z.squared() ) ) );
		Real result01 = Real.TWO.multiply( this.m_x ).multiply( this.m_y ).add( Real.TWO.multiply( this.m_z ).multiply( this.m_w ) );
		Real result02 = Real.TWO.multiply( this.m_x ).multiply( this.m_z ).subtract( Real.TWO.multiply( this.m_y ).multiply( this.m_w ) );
		
		Real result10 = Real.TWO.multiply( this.m_x ).multiply( this.m_y ).subtract( Real.TWO.multiply( this.m_z ).multiply( this.m_w ) );
		Real result11 = Real.ONE.subtract( Real.TWO.multiply( this.m_x.squared() ).add( Real.TWO.multiply( this.m_z.squared() ) ) );
		Real result12 = Real.TWO.multiply( this.m_y ).multiply( this.m_z ).add( Real.TWO.multiply( this.m_x ).multiply( this.m_w ) );
		
		Real result20 = Real.TWO.multiply( this.m_x.multiply( this.m_z ) ).add( Real.TWO.multiply( this.m_y ).multiply( this.m_w ) );
		Real result21 = Real.TWO.multiply( this.m_y.multiply( this.m_z ) ).subtract( Real.TWO.multiply( this.m_x.multiply( this.m_w ) ) );
		Real result22 = Real.ONE.subtract( Real.TWO.multiply( this.m_x.squared() ).add( Real.TWO.multiply( this.m_y.squared() ) ) );
		
		Real[][] matrixData = { { result00 , result01 , result02 } ,
								{ result10 , result11 , result12 } ,
								{ result20 , result21 , result22 } };
		return new Matrix3( matrixData );
	}
	
	/**
	 * converts this <code>Quaternion</code> into a <code>Matrix4</code> in world
	 * coordinates. it also factors in a <code>Vector3D</code> position
	 * 
	 * @param position			the position of this <code>Quaternion</code> in world coordinates
	 * @return
	 */
	public Matrix4 toOrientationAndPositionMatrix( Vector3D position ) {
		Matrix3 rotationMatrix = this.toOrientationMatrix();
		
		Real result00 = rotationMatrix.get( 0 , 0 );
		Real result01 = rotationMatrix.get( 0 , 1 );
		Real result02 = rotationMatrix.get( 0 , 2 );
		Real result03 = position.getX();
		
		Real result10 = rotationMatrix.get( 1 , 0 );
		Real result11 = rotationMatrix.get( 1 , 1 );
		Real result12 = rotationMatrix.get( 1 , 2 );
		Real result13 = position.getY();
		
		Real result20 = rotationMatrix.get( 2 , 0 );
		Real result21 = rotationMatrix.get( 2 , 1 );
		Real result22 = rotationMatrix.get( 2 , 2 );
		Real result23 = position.getZ();
		
		Real result30 = Real.ZERO;
		Real result31 = Real.ZERO;
		Real result32 = Real.ZERO;
		Real result33 = Real.ONE;
		
		Real[][] matrixData = { { result00 , result01 , result02 , result03 } ,
								{ result10 , result11 , result12 , result13 },
								{ result20 , result21 , result22 , result23 },
								{ result30 , result31 , result32 , result33 } };
		
		return new Matrix4( matrixData );
	}
	
	@Override
	public Quaternion clone() {
		return new Quaternion( this.getW() , this.getX() , this.getY() , this.getZ() );
	}
	
	@Override
	public String toString() {
		String rtn = "[ " + this.m_w + ", " + this.m_x + ", " + this.m_y + ", " + this.m_z + " ]";
		return rtn;
	}
}
