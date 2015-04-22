package force;

import _math.Real;
import _math.Vector3D;

/**
 * any object with a mass
 */
abstract public class MassedObject {

	/**
	 * the mass of the object
	 */
	private Real m_mass;
	
	/**
	 * the inverse (reciprocol) of the mass of the object
	 */
	private Real m_inverseMass;
	
	/**
	 * position of the <code>MassedObject</code>
	 */
	private Vector3D m_position;
	
	/**
	 * velocity of the <code>MassedObject</code>
	 */
	private Vector3D m_velocity;
	
	/**
	 * acceleration of the <code>MassedObject</code>
	 */
	private Vector3D m_acceleration;
	
	/**
	 * acceleration of the <code>MassedObject</code> last frame
	 */
	private Vector3D m_lastFrameAcceleration = Vector3D.ZERO;
	
	/**
	 * the net force on this <code>MassedObject</code>
	 */
	private Vector3D m_netForce = Vector3D.ZERO;
	
	/**
	 * creates a <code>MassedObject</code> with the given mass
	 * 
	 * @param mass			the mass of the object
	 */
	public MassedObject( Real mass , Vector3D position , Vector3D velocity , Vector3D acceleration ) {
		if ( mass.equals( Real.ZERO ) ) {
			throw new IllegalArgumentException();
		}
		this.m_mass = mass;
		this.m_inverseMass = Real.ONE.divide( mass );
		this.m_position = position;
		this.m_velocity = velocity;
		this.m_acceleration = acceleration;
	}
	
	/**
	 * @return			the mass of this <code>MassedObject</code>
	 */
	public Real getMass() {
		return this.m_mass;
	}
	
	/**
	 * sets a new mass for this <code>MassedObject</code>
	 * 
	 * @param newMass			the new mass for this <code>MassedObject</code>
	 */
	public void setMass( Real newMass ) {
		this.m_mass = newMass;
		this.m_inverseMass = newMass.inverse();
	}
	
	/**
	 * @return			the inverse of the mass of this <code>MassedObject</code>
	 */
	public Real getInverseMass() {
		return this.m_inverseMass;
	}
	
	/**
	 * sets a new inverse mass for this <code>MassedObject</code>
	 * 
	 * @param newInverseMass		the new inverse mass for this <code>MassedObject</code>
	 */
	public void setInverseMass( Real newInverseMass ) {
		this.m_inverseMass = newInverseMass;
		
		//also modify the mass of this MassedObject if the inverse mass is not zero
		if ( newInverseMass.equals( Real.ZERO ) ) {
			this.m_mass = newInverseMass.inverse();
		}
	}
	
	/**
	 * @return			the position of this <code>MassedObject</code>
	 */
	public Vector3D getPosition() {
		return this.m_position;
	}
	
	/**
	 * sets the position of this <code>MassedObject</code> to a new position 
	 * 
	 * @param newPosition			the new position for this <code>MassedObject</code>
	 */
	public void setPosition( Vector3D newPosition ) {
		this.m_position = newPosition;
	}
	
	/**
	 * @return			the velocity of this <code>MassedObject</code>
	 */
	public Vector3D getVelocity() {
		return this.m_velocity;
	}
	
	/**
	 * sets the velocity of this <code>MassedObject</code> to a new velocity
	 * 
	 * @param newVelocity		the new velocity for this <code>MassedObject</code>
	 */
	public void setVelocity( Vector3D newVelocity ) {
		this.m_velocity = newVelocity;
	}
	
	/**
	 * @return			the acceleration of this <code>MassedObject</code>
	 */
	public Vector3D getAcceleration() {
		return this.m_acceleration;
	}
	
	/**
	 * sets the acceleration of this <code>MassedObject</code> to a new acceleration
	 * 
	 * @param newAcceleration		the new acceleration for this <code>MassedObject</code>
	 */
	public void setAcceleration( Vector3D newAcceleration ) {
		this.m_acceleration = newAcceleration;
	}
	
	/**
	 * @return		the acceleration of this <code>MassedObject</code> during the last frame
	 */
	public Vector3D getLastFrameAcceleration() {
		return this.m_lastFrameAcceleration;
	}
	
	/**
	 * sets the acceleration of this <code>MassedObject</code> during the last frame
	 * to a new acceleration
	 * 
	 * @param newLastFrameAcceleration		the acceleration of this <code>MassedObject</code> during the last frame
	 */
	public void setLastFrameAcceleration( Vector3D newLastFrameAcceleration ) {
		this.m_lastFrameAcceleration = newLastFrameAcceleration;
	}
	
	/**
	 * adds a given force to the net force acting on this <code>MassedObject</code>
	 * 
	 * @param aForce			the <code>Force</code> to add to this massed object
	 */
	public void addForceVector( Vector3D aForce ) {
		this.m_netForce = this.m_netForce.add( aForce );
	}
	
	/**
	 * @return		the net force on this <code>MassedObject</code>
	 */
	public Vector3D getNetForce() {
		return this.m_netForce;
	}
	
	/**
	 * resets the net force on this <code>MassedObject</code> to zero
	 */
	public void resetNetForce() {
		this.m_netForce = Vector3D.ZERO;
	}
	
	/**
	 * updates the object by moving it forward in time by the given duration
	 * 
	 * @param duration			the amount to move forward in time
	 */
	abstract public void act( Real duration );
}
