package rigidbody;

import util.ErrorMessages;
import _math.Quaternion;
import _math.Real;
import _math.Vector3D;
import force.MassedObject;

/**
 * a <code>MassedObject</code> with a size. the position of the <code>RigidBody</code>
 * is its center of mass and all other vector quantities are relative to that center of mass
 */
public class RigidBody extends MassedObject {

	
	/**
	 * the angular orientation of this <code>RigidBody</code>
	 */
	private Quaternion m_orientation = Quaternion.ZERO;
	
	/**
	 * the angular velocity of this <code>RigidBody</code>
	 */
	private Vector3D m_angularVelocity = Vector3D.ZERO;
	
	/**
	 * the inverse of the moment of inertia of this <code>RigidBody</code>
	 */
	private Matrix3 m_invMomentOfInertia;
	
	/**
	 * the inverse of the moment of inertia of this <code>RigidBody</code>
	 * in world coordinates
	 */
	private Matrix3 m_invMomentOfInertiaWorld;
	
	/**
	 * the net torque on this <code>RigidBody</code>
	 */
	private Vector3D m_netTorque = Vector3D.ZERO;
	
	/**
	 * linear damping factor on this <code>RigidBody</code>
	 */
	private Real m_linearDamping = new Real( 0.999 );
	
	/**
	 * angular damping factor on this <code>RigidBody</code>
	 */
	private Real m_angularDamping = new Real( 0.999 );
	
	/**
	 * determines if this <code>RigidBody</code> can be put to sleep, i.e.
	 * be ignored by the physics engine if it's motion stabilizes and velocity
	 * approaches zero
	 */
	private boolean m_canSleep = false;
	
	/**
	 * the minimum motionS this <code>RigidBody</code> can have if it is to not be put
	 * to sleep
	 */
	private Real m_sleepThreshold = Real.NEGATIVE_ONE;
	
	/**
	 * the amount of motion this <code>RigidBody</code> has. it is used to compare
	 * to <code>m_sleepThreshold</code> to determine if this <code>RigidBody</code>
	 * should be set to sleep or not
	 * 
	 * @see				#m_sleepThreshold
	 */
	private Real m_motion = Real.ZERO;
	
	/**
	 * determines if this <code>RigidBody</code> is awake or not. Awake <code>RigidBody</code>
	 * objects are updates by the <code>act()</code> method and sleeping (not awake) ones
	 * are ignored
	 */
	private boolean m_isAwake = false;
	
	/**
	 * creates a <code>RigidBody</code> with the given mass, inverse moment of inertia,
	 *  and initial position. all other properties are set to their defaults:
	 * <p>
	 * velocity = [ 0 , 0 , 0 ]
	 * <br>
	 * acceleration = [ 0 , 0 , 0 ]
	 * <br>
	 * orientation = [ 1 , 0 , 0 , 0 ]
	 * <br>
	 * angular velocity = [ 0 , 0 , 0 ]
	 * <br>
	 * damping = 0.999
	 * 
	 * @param mass					mass of this <code>RigidBody</code>
	 * @param invMomentOfIneratia	the inverse of the moment of inertia of this <code>RigidBody</code>
	 * @param initialPos			initial position of this <code>RigidBody</code>
	 */
	public RigidBody( Real mass , Matrix3 invMomentOfInertia , Vector3D initialPos ) {
		super( mass , initialPos , Vector3D.ZERO , Vector3D.ZERO );
		this.m_invMomentOfInertia = invMomentOfInertia;
	}
	
	/**
	 * creates a <code>RigidBody</code> with the given mass, inverse moment of inertia,
	 * initial position, and initial velocity. all other properties are set to their defaults:
	 * <p>
	 * acceleration = [ 0 , 0 , 0 ]
	 * <br>
	 * orientation = [ 1 , 0 , 0 , 0 ]
	 * <br>
	 * angular velocity = [ 0 , 0 , 0 ]
	 * <br>
	 * damping = 0.999
	 * 
	 * @param mass					mass of this <code>RigidBody</code>
	 * @param invMomentOfIneratia	the inverse of the moment of inertia of this <code>RigidBody</code>
	 * @param initialPos			initial position of this <code>RigidBody</code>
	 * @param initialVel			initial velocity of this <code>RigidBody</code>
	 */
	public RigidBody( Real mass , Matrix3 invMomentOfInertia , Vector3D initialPos , Vector3D initialVel ) {
		super( mass , initialPos , initialVel , Vector3D.ZERO );
		this.m_invMomentOfInertia = invMomentOfInertia;
	}
	
	/**
	 * creates a <code>RigidBody</code> with the given mass, inverse moment of inertia,
	 * initial position, initial velocity, and initial acceleration. all other properties are set
	 * to their defaults:
	 * <p>
	 * orientation = [ 1 , 0 , 0 , 0 ]
	 * <br>
	 * angular velocity = [ 0 , 0 , 0 ]
	 * <br>
	 * damping = 0.999
	 * 
	 * @param mass					mass of this <code>RigidBody</code>
	 * @param invMomentOfIneratia	the inverse of the moment of inertia of this <code>RigidBody</code>
	 * @param initialPos			initial position of this <code>RigidBody</code>
	 * @param initialVel			initial velocity of this <code>RigidBody</code>
	 * @param initialAccel			initial acceleration of this <code>RigidBody</code>
	 */
	public RigidBody( Real mass , Matrix3 invMomentOfInertia , Vector3D initialPos , Vector3D initialVel , Vector3D initialAccel ) {
		super( mass , initialPos , initialVel , initialAccel );
		this.m_invMomentOfInertia = invMomentOfInertia;
	}
	
	/**
	 * creates a <code>RigidBody</code> with the given mass, inverse moment of inertia,
	 * initial position, initial velocity, initial acceleration and damping. all other
	 * properties are set to their defaults:
	 * <p>
	 * orientation = [ 1 , 0 , 0 , 0 ]
	 * <br>
	 * angular velocity = [ 0 , 0 , 0 ]
	 * 
	 * @param mass					mass of this <code>RigidBody</code>
	 * @param invMomentOfIneratia	the inverse of the moment of inertia of this <code>RigidBody</code>
	 * @param initialPos			initial position of this <code>RigidBody</code>
	 * @param initialVel			initial velocity of this <code>RigidBody</code>
	 * @param initalAccel			initial acceleration of this <code>RigidBody</code>
	 * @param damping				damping for this <code>RigidBody</code>
	 */
	public RigidBody( Real mass , Matrix3 invMomentOfInertia , Vector3D initialPos , Vector3D initialVel , Vector3D initialAccel , Real damping ) {
		this( mass , invMomentOfInertia , initialPos , initialVel , initialAccel );
		this.m_linearDamping = damping;
	}
	
	/**
	 * creates a <code>RigidBody</code> with the given mass, inverse moment of inertia, 
	 * initial position, initial velocity, initial acceleration, and initial orientation. all
	 * other properties are set to their defaults:
	 * <p>
	 * angular velocity = [ 0 , 0 , 0 ]
	 * <br>
	 * damping = 0.999
	 * 
	 * @param mass					the mass of this <code>RigidBody</code>
	 * @param invMomentOfIneratia	the inverse of the moment of inertia of this <code>RigidBody</code>
	 * @param initialPos			the initial position of this <code>RigidBody</code>
	 * @param initialVel			the initial velocity of this <code>RigidBody</code>
	 * @param initialAccel			the initial acceleration of this <code>RigidBody</code>
	 * @param initialOrientation	the initial orientation of this <code>RigidBody</code>
	 */
	public RigidBody( Real mass , Matrix3 invMomentOfInertia , Vector3D initialPos , Vector3D initialVel , Vector3D initialAccel , Quaternion initialOrientation ) {
		this( mass , invMomentOfInertia , initialPos , initialVel , initialAccel );
		this.m_orientation = initialOrientation;
	}
	
	/**
	 * creates a <code>RigidBody</code> with the given mass, inverse moment of inertia,
	 * initial position, initial velocity, initial acceleration, initial orientation, 
	 * and initial angular velocity. all other properties are set to their defaults:
	 * <p>
	 * damping = 0.999
	 * 
	 * @param mass						the mass of this <code>RigidBody</code>
	 * @param invMomentOfIneratia		the inverse of the moment of inertia of this <code>RigidBody</code>
	 * @param initialPos				the initial position of this <code>RigidBody</code>
	 * @param initialVel				the initial velocity of this <code>RigidBody</code>
	 * @param initialAccel				the initial acceleration of this <code>RigidBody</code>
	 * @param initialOrientation		the initial orientation of this <code>RigidBody</code>
	 * @param initialAngularVelocity	the initial angular velocity of this <code>RigidBody</code>
	 */
	public RigidBody( Real mass , Matrix3 invMomentOfInertia , Vector3D initialPos , Vector3D initialVel , Vector3D initialAccel , Quaternion initialOrientation , Vector3D initialAngularVelocity ) {
		this( mass , invMomentOfInertia , initialPos , initialVel , initialAccel , initialOrientation );
		this.m_angularVelocity = initialAngularVelocity;
	}
	
	/**
	 * creates a <code>RigidBody</code> with the given mass, inverse moment of inertia, 
	 * initial position, initial velocity, initial acceleration, initial orientation, initial
	 * angular velocity, and damping.
	 * 
	 * @param mass						the mass of this <code>RigidBody</code>
	 * @param invMomentOfIneratia		the inverse of the moment of inertia of this <code>RigidBody</code>
	 * @param initialPos				the initial position of this <code>RigidBody</code>
	 * @param initialVel				the initial velocity of this <code>RigidBody</code>
	 * @param initialAccel				the initial acceleration of this <code>RigidBody</code>
	 * @param initialOrientation		the initial orientation of this <code>RigidBody</code>
	 * @param initialAngularVelocity	the initial angular velocity of this <code>RigidBody</code>
	 * @param damping					the damping factor on this <code>RigidBody</code>
	 */
	public RigidBody( Real mass , Matrix3 invMomentOfInertia , Vector3D initialPos , Vector3D initialVel , Vector3D initialAccel , Quaternion initialOrientation , Vector3D initialAngularVelocity , Real damping ) {
		this( mass , invMomentOfInertia , initialPos , initialVel , initialAccel , initialOrientation , initialAngularVelocity );
		this.m_linearDamping = damping;
	}
	
	
	/**
	 * @return		the damping factor on this <code>RigidBody</code>
	 */
	public Real getDamping() {
		return this.m_linearDamping;
	}
	
	/**
	 * sets the damping factor on this <code>RigidBody</code>
	 * 
	 * @param newDamping		the new damping factor for this <code>RigidBody</code>
	 */
	public void setDamping( Real newDamping ) {
		this.m_linearDamping = newDamping;
	}
	
	/**
	 * @return			the orientation of this <code>RigidBody</code>
	 */
	public Quaternion getOrientation() {
		return this.m_orientation;
	}
	
	/**
	 * sets a new orientation for this <code>RigidBody</code>
	 * 
	 * @param newOrientation
	 */
	public void setOrientation( Quaternion newOrientation ) {
		this.m_orientation = newOrientation.normalize();
	}
	
	/**
	 * @return			the angular velocity of this <code>RigidBody</code>
	 */
	public Vector3D getAngularVelocity() {
		return this.m_angularVelocity;
	}
	
	/**
	 * sets a new angular velocity for this <code>RigidBody</code>
	 * 
	 * @param newAngularVelocity
	 */
	public void setAngularVelocity( Vector3D newAngularVelocity ) {
		this.m_angularVelocity = newAngularVelocity;
	}
	
	/**
	 * use setInverseMass( Real , Matrix3 ) instead
	 * 
	 * @throws IllegalStateException 		if this method is invoked. use setInverseMass( Real , Matrix3 ) instead
	 * @see									#setInverseMass(Real, Matrix3)
	 */
	@Override
	public void setInverseMass( Real newMass ) throws IllegalStateException {
		throw new IllegalStateException();
	}
	
	/**
	 * properly updates the inverse mass and inverse moment of inertia of 
	 * this <code>RigidBody</code>
	 * 
	 * @param newMass							the new mass of this <code>RigidBody</code>
	 * @param newInverseMomentOfInertia			the new moment of inertia of this <code>RigidBody</code>
	 */
	public void setInverseMass( Real newMass , Matrix3 newInverseMomentOfInertia ) {
		super.setInverseMass( newMass );
		if ( !newMass.equals( Real.ZERO ) ) {
			throw new IllegalArgumentException();
		}
		this.m_invMomentOfInertia = newInverseMomentOfInertia;
	}
	
	/**
	 * @return			the inverse moment of inertia of this <code>RigidBody</code>
	 */
	public Matrix3 getInverseMomentOfInertia() {
		return this.m_invMomentOfInertia;
	}
	
	public Matrix3 getInverseMomentOfInertiaWorld() {
		calculateInverseMomentOfInertiaWorld();
		return this.m_invMomentOfInertiaWorld;
	}
	
	private void calculateInverseMomentOfInertiaWorld() {
		Quaternion orientation = this.getOrientation();
		Matrix3 iitBody = this.getInverseMomentOfInertia();
		Matrix4 rotmat = orientation.toOrientationAndPositionMatrix( this.getPosition() );
		
		Real t04 = rotmat.get( 0 ).multiply( iitBody.get( 0 ) ).add( 
				 rotmat.get( 1 ).multiply( iitBody.get( 3 ) ) ).add(
				 rotmat.get( 2 ).multiply( iitBody.get( 6 ) ) );
		
		Real t09 = rotmat.get( 0 ).multiply( iitBody.get( 1 ) ).add(
				 rotmat.get( 1 ).multiply( iitBody.get( 4 ) ) ).add(
				 rotmat.get( 2 ).multiply( iitBody.get( 7 ) ) );
		
		Real t14 = rotmat.get( 0 ).multiply( iitBody.get( 2 ) ).add(
				 rotmat.get( 1 ).multiply( iitBody.get( 5 ) ) ).add(
				 rotmat.get( 2 ).multiply( iitBody.get( 8 ) ) );
		
		Real t28 = rotmat.get( 4 ).multiply( iitBody.get( 0 ) ).add(
				 rotmat.get( 5 ).multiply( iitBody.get( 3 ) ) ).add(
				 rotmat.get( 6 ).multiply( iitBody.get( 6 ) ) );
		
		Real t33 = rotmat.get( 4 ).multiply( iitBody.get( 1 ) ).add(
				 rotmat.get( 5 ).multiply( iitBody.get( 4 ) ) ).add(
				 rotmat.get( 6 ).multiply( iitBody.get( 7 ) ) );
		
		Real t38 = rotmat.get( 4 ).multiply( iitBody.get( 2 ) ).add(
				 rotmat.get( 5 ).multiply( iitBody.get( 5 ) ) ).add(
				 rotmat.get( 6 ).multiply( iitBody.get( 8 ) ) );
		
		Real t52 = rotmat.get( 8 ).multiply( iitBody.get( 0 ) ).add(
				 rotmat.get( 9 ).multiply( iitBody.get( 3 ) ) ).add(
				 rotmat.get( 10 ).multiply( iitBody.get( 6 ) ) );
		
		Real t57 = rotmat.get( 8 ).multiply( iitBody.get( 1 ) ).add(
				 rotmat.get( 9 ).multiply( iitBody.get( 4 ) ) ).add(
				rotmat.get( 10 ).multiply( iitBody.get( 7 ) ) );
		
		Real t62 = rotmat.get( 8 ).multiply( iitBody.get( 2 ) ).add(
				 rotmat.get( 9 ).multiply( iitBody.get( 5 ) ) ).add(
				rotmat.get( 10 ).multiply( iitBody.get( 8 ) ) );
						 
		
		Real rtn00 = t04.multiply( rotmat.get( 0 ) ).add(
				t09.multiply( rotmat.get( 1 ) ) ).add( 
				t14.multiply( rotmat.get( 2 ) ) );
		
		Real rtn01 = t04.multiply( rotmat.get( 4 ) ).add(
				t09.multiply( rotmat.get( 5 ) ) ).add( 
				t14.multiply( rotmat.get( 6 ) ) );
		
		Real rtn02 = t04.multiply( rotmat.get( 8 ) ).add(
				t09.multiply( rotmat.get( 9 ) ) ).add( 
				t14.multiply( rotmat.get( 10 ) ) );
		
		Real rtn10 = t28.multiply( rotmat.get( 0 ) ).add(
				t33.multiply( rotmat.get( 1 ) ) ).add( 
				t38.multiply( rotmat.get( 2 ) ) );
		
		Real rtn11 = t28.multiply( rotmat.get( 4 ) ).add(
				t33.multiply( rotmat.get( 5 ) ) ).add( 
				t38.multiply( rotmat.get( 6 ) ) );
		
		Real rtn12 = t28.multiply( rotmat.get( 8 ) ).add(
				t33.multiply( rotmat.get( 9 ) ) ).add( 
				t38.multiply( rotmat.get( 10 ) ) );
		
		Real rtn20 = t52.multiply( rotmat.get( 0 ) ).add(
				t57.multiply( rotmat.get( 1 ) ) ).add( 
				t62.multiply( rotmat.get( 2 ) ) );
		
		Real rtn21 = t52.multiply( rotmat.get( 4 ) ).add(
				t57.multiply( rotmat.get( 5 ) ) ).add( 
				t62.multiply( rotmat.get( 6 ) ) );
		
		Real rtn22 = t52.multiply( rotmat.get( 8 ) ).add(
				t57.multiply( rotmat.get( 9 ) ) ).add( 
				t62.multiply( rotmat.get( 10 ) ) );
		
		Real[][] worldData = { { rtn00 , rtn01 , rtn02 } ,
							   { rtn10 , rtn11 , rtn12 } ,
							   { rtn20 , rtn21 , rtn22 } };
		this.m_invMomentOfInertiaWorld = new Matrix3( worldData );
	}
	
	/**
	 * @param axis		the direction of the axis about which to calculate the moment of inertia
	 * @param origin	the origin of the axis
	 * @return			the moment of inertia of this <code>RigidBody</code> as a number,
	 * 					calculated about the given <code>axis</code>
	 */
	public Real getMomentOfInertia( Vector3D axis , Vector3D origin ) {
		
		//determine the location of the origin relative to the center of mass of this body
		Vector3D relativePosition = origin.subtract( this.getPosition() );
		
		//determine the impulsive torque generated per unit of impulse using the
		//formula torque per unit impulse = r (cross) dp, where r is the vector from the origin
		//to this body and dp is the change in momentum vector. in this case, dp has
		//magnitude 1 because the impulse is 1 unit, so dp can be replaced by
		//the direction of the contact normal
		Vector3D impulsiveTorquePerUnitImpulse = relativePosition.cross( axis );
		
		//determine the change in angular velocity per unit of impulse using the formula
		//delta omega = torque/I, where
		//torque is the impulsive torque calculated in the previous step and
		//I is the moment of inertia. in this case, we transform the torque
		//by the inverse moment of inertia
		Vector3D changeInAngularVelocityPerUnitImpulse = this.m_invMomentOfInertia.transform( impulsiveTorquePerUnitImpulse );
		
		//the moment of inertia is how much the angular velocity of this object changes
		//when forces are applied
		return changeInAngularVelocityPerUnitImpulse.cross( relativePosition ).dot( axis );
	}
	
	/**
	 * @return			the net torque on this <code>RigidBody</code>
	 */
	public Vector3D getNetTorque() {
		return this.m_netTorque;
	}
	
	/**
	 * adds the given torque <code>Vector3D</code> to the net torque on this
	 * <code>RigidBody</code>
	 * 
	 * @param torque
	 */
	public void addTorqueVector( Vector3D torque ) {
		this.m_netTorque = this.m_netTorque.add( torque );
	}
	
	/**
	 * applies the given force at the center of mass of this <code>RigidBody</code>.
	 * no torque is generated
	 * 
	 * @param force			the force applied at the center of mass of this <code>RigidBody</code>
	 */
	@Override
	public void addForceVector( Vector3D force ) {
		super.addForceVector( force );
	}
	
	/**
	 * applies a force to this <code>RigidBody</code> at a given point
	 * 
	 * @param force			the force to apply <b>in world coordinates</b>
	 * @param point			the point at which to apply the force, <b>relative to the center of mass</b>
	 */
	public void addForceAtPoint( Vector3D force , Vector3D point ) {
		
		//add the given force to the net force
		this.addForceVector( force );
		
		//calcualte the torque
		//torque = F (cross) r
		//the center of mass is defined as [ 0 , 0 , 0 ] for this rigidbody
		//so the radial vector is also the point vector
		Vector3D torque = force.cross( point );
		
		//add the torque to the net torque
		addTorqueVector( torque );
	}
	
	/**
	 * resets the net torque on this <code>RigidBody</code> to <code>Vector3D.ZERO</code>
	 * 
	 * @see			Vector3D#ZERO
	 */
	public void resetNetTorque() {
		this.m_netTorque = Vector3D.ZERO;
	}

	@Override
	public void act( Real duration ) {
		
		//calculate the linear acceleration from last frame
		Vector3D lastFrameAcceleration = this.getAcceleration().add( this.getNetForce().multiply( this.getInverseMass() ) );
		this.setLastFrameAcceleration( lastFrameAcceleration );
		
		//calculate linear acceleration
		Vector3D linearAcceleration = this.getNetForce().multiply( this.getInverseMass() );
		
		//calculate angular acceleration from torque = I * alpha
		//or alpha = torque / I
		Vector3D angularAcceleration = this.m_invMomentOfInertia.transform( this.getNetTorque() );
		
		//adjust linear velocity
		Vector3D deltaV = linearAcceleration.multiply( duration );
		this.setVelocity( this.getVelocity().add( deltaV ) );
		
		//adjust angular velocity
		this.setAngularVelocity( this.getAngularVelocity().add( angularAcceleration.multiply( duration ) ) );
		
		//add linear drag
		this.setVelocity( this.getVelocity().multiply( this.getDamping().pow( duration ) ) );
		
		//add rotational drag
		this.setAngularVelocity( this.getAngularVelocity().multiply( this.m_angularDamping.pow( duration ) ) );
		
		//modify linear position
		Vector3D deltaPos = this.getVelocity().multiply( duration );
		this.setPosition( this.getPosition().add( deltaPos ) );
		
		//modify angular position
		Vector3D deltaTheta = this.getAngularVelocity().multiply( duration );
		this.setOrientation( this.getOrientation().add( deltaTheta ) );

		//clear net force and torque
		clearAccumulators();
		
		//determine the kinetic energy if this RigidBody may need to be
		//put to sleep
		if ( this.m_canSleep ) {
			
			//calculate kinetic energy
			Real kineticEnergy = this.getVelocity().magnitudeSquared().add( this.getAngularVelocity().magnitudeSquared() );
			
			//determine the bias to determine a weighted average of
			//recent kinetic energies to figure out
			//if the RigidBody is approaching a steady velocity
			Real bias = Real.ONE_HALF.pow( duration );
			this.m_motion = bias.multiply( this.m_motion ).add( Real.ONE.subtract( bias ).multiply( kineticEnergy ) );
			
			//determine if the RigidBody shoud be put to sleep
			if ( this.m_motion.compareTo( this.m_sleepThreshold ) < 0 ) {
				this.setAsleep();
				
			//prevent the weighted average from skyrocketing
			} else if ( this.m_motion.compareTo( this.m_sleepThreshold.multiply( Real.TEN ) ) > 0 ) {
				this.m_motion = this.m_sleepThreshold.multiply( Real.TEN );
			}
		}
		
		
	}
	
	/**
	 * resets the net force and torque on this <code>RigidBody</code> to <code>Vector3D.ZERO</code>
	 * 
	 * @see			Vector3D#ZERO
	 */
	public void clearAccumulators() {
		this.resetNetForce();
		this.resetNetTorque();
	}
	
	/**
	 * converts a vector from the local coordinates of this <code>RigidBody</code>
	 * into world coordinates
	 * 
	 * @param vector
	 * @return				<code>vector</code> converted from local coordinates to world coordinates
	 */
	public Vector3D convertLocalToWorld( Vector3D vector ) {
		Matrix4 rotationMatrix = this.getOrientation().toOrientationAndPositionMatrix( this.getPosition() );
		Vector3D rotatedVector = rotationMatrix.convertLocalToWorld( vector );
		return rotatedVector;
	}
	
	/**
	 * converts a vector from world coordinates to the local coordinates of this
	 * <code>RigidBody</code>
	 * @param vector
	 * @return
	 */
	public Vector3D convertWorldToLocal( Vector3D vector ) {
		Matrix4 rotationMatrix = this.getOrientation().toOrientationAndPositionMatrix( this.getPosition() );
		Vector3D rotatedVector = rotationMatrix.convertWorldToLocal( vector );
		return rotatedVector;
	}
	
	/**
	 * sets this <code>RigidBody</code> as an object that can be
	 * put to sleep
	 * @param sleepThreshold		the maximum velocity this <code>RigidBody</code>
	 * 								may have if it is to be put to sleep
	 * @see 						#m_canSleep
	 */
	public void setSleepable( Real sleepThreshold ) {
		this.m_canSleep = true;
		this.m_sleepThreshold = sleepThreshold;
	}
	
	/**
	 * wakes this <code>RigidBody</code> up so that the <code>act()</code>
	 * method no longer ignores this <code>RigidBody</code>
	 */
	public void setAwake() {
		this.m_isAwake = true;
		
		//add some motion to the object so that the integrator does not
		//immediately set this back to sleep again
		this.m_motion = this.m_sleepThreshold.multiply( Real.TWO );
	}
	
	/**
	 * sets this <code>RigidBody</code> into sleep state. the <code>act()</code>
	 * method then ignores this <code>RigidBody</code>, making it more efficient
	 * 
	 * @throws IllegalStateException			if this <code>RigidBody</code> cannot
	 * 											be put to sleep, i.e. <code>m_canSleep</code> is false
	 */
	public void setAsleep() throws IllegalStateException {
		if ( this.m_canSleep ) {
			this.m_isAwake = false;
			this.setVelocity( Vector3D.ZERO );
			this.setAngularVelocity( Vector3D.ZERO );
		} else {
			throw new IllegalStateException( ErrorMessages.RigidBody.CANNOT_SLEEP );
		}
	}
	
	/**
	 * @return			if this <code>RigidBody</code> is awake
	 */
	public boolean isAwake() {
		return this.m_isAwake;
	}
	
	@Override
	public String toString() {
		String rtn = "";
		//rtn += "Position: " + this.getPosition() + "\n";
		//rtn += "Velocity: " + this.getVelocity() + "\n";
		rtn += "Rotation: " + this.m_orientation + "\n";
		return rtn;
	}
}
