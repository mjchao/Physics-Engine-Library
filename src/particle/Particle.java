package particle;

import util.ErrorMessages;
import _math.Real;
import _math.Vector3D;
import force.MassedObject;

/**
 * a point mass
 */
public class Particle extends MassedObject {
	
	/**
	 * damping factor on this <code>Particle</code>
	 */
	private Real m_damping = new Real( 0.999 );
	
	/**
	 * creates a <code>Particle</code> with an initial mass and position. all
	 * other properties are set to their defaults:
	 * <p>
	 * velocity = [ 0 , 0 , 0 ]
	 * <br>
	 * acceleration = [ 0 , 0 , 0 ]
	 * <br>
	 * damping = 0.999
	 * 
	 * @param initialMass			initial mass of the <code>Particle</code>
	 * @param initialPos			initial position of the <code>Particle</code>
	 */
	public Particle( Real initialMass , Vector3D initialPos ) {
		super( initialMass , initialPos , Vector3D.ZERO , Vector3D.ZERO );
	}
	
	/**
	 * creates a <code>Particle</code> with an initial mass, position, and velocity.
	 * all other properties are set to their defaults:
	 * <p>
	 * acceleration = [ 0 , 0 , 0 ]
	 * <br>
	 * damping = 0.999
	 * 
	 * @param initialMass			initial mass of the <code>Particle</code>
	 * @param initialPos			initial position of the <code>Particle</code>
	 * @param initialVelocity		initial velocity of the <code>Particle</code>
	 */
	public Particle( Real initialMass , Vector3D initialPos , Vector3D initialVelocity ) {
		super( initialMass , initialPos , initialVelocity , Vector3D.ZERO ); 
	}
	
	/**
	 * creates a <code>Particle</code> with an initial mass, position, velocity, and acceleration.
	 * all other properties are set to their defaults:
	 * <p>
	 * damping = 0.999
	 * 
	 * @param initialMass			initial mass of the <code>Particle</code>
	 * @param initialPos			initial position of the <code>Particle</code>
	 * @param initialVelocity		initial velocity of the <code>Particle</code>
	 * @param initialAccel			initial acceleration of the <code>Particle</code>
	 */
	public Particle( Real initialMass , Vector3D initialPos , Vector3D initialVelocity , Vector3D initialAccel ) {
		super( initialMass , initialPos , initialVelocity , initialAccel );
	}
	
	/**
	 * creates a <code>Particle</code> with an initial mass, position, velocity, acceleration and
	 * damping factor
	 * 
	 * @param initialMass			initial mass of the <code>Particle</code>
	 * @param initialPos			initial position of the <code>Particle</code>
	 * @param initialVelocity		initial velocity of the <code>Particle</code>
	 * @param initialAccel			initial acceleration of the <code>Particle</code>
	 * @param damping				initial damping factor on the <code>Particle</code>
	 */
	public Particle( Real initialMass , Vector3D initialPos , Vector3D initialVelocity , Vector3D initialAccel , Real damping ) {
		this ( initialMass , initialPos , initialVelocity , initialAccel );
		this.m_damping = damping;
	}
	
	/**
	 * @return			the damping factor on this <code>Particle</code>
	 */
	public Real getDamping() {
		return this.m_damping;
	}
	
	/**
	 * sets the damping factor on this <code>Particle</code> to a new force
	 * 
	 * @param newDamping			the new damping factor for this <code>Particle</code>
	 */
	public void setDamping( Real newDamping ) {
		this.m_damping = newDamping;
	}
	
	/**
	 * moves this <code>Particle</code> forward in time by the given duration. the position,
	 * velocity, acceleration, etc. are all modified
	 * 
	 * @param duration				duration for which this <code>Particle</code> will act
	 */
	@Override
	public void act( Real duration ) {
		
		//make sure the duration is positive
		if ( duration.compareTo( Real.ZERO ) < 0 ) {
			throw new IllegalArgumentException( ErrorMessages.Particle.INVALID_DURATION );
		}
		
		//update the position
		Vector3D dx = this.getVelocity().multiply( duration );
		this.setPosition( this.getPosition().add( dx ) );
		
		//update the acceleration
		this.setAcceleration( this.getNetForce().multiply( this.getInverseMass() ) );
		
		//after the force has been applied, set it back to zero for
		//so the force generators can add to it again
		this.resetNetForce();
		
		//update the velocity
		Vector3D dv = this.getAcceleration().multiply( duration );
		this.setVelocity( this.getVelocity().add( dv ) );
		
		//apply damping
		Real dampingFactor = this.m_damping.pow( duration );
		this.setVelocity( this.getVelocity().multiply( dampingFactor ) );
		
	}
	
	@Override
	public String toString() {
		String rtn = "";
		rtn += "position = " + this.getPosition().toString() + " ; velocity = " + this.getVelocity().toString() + " ; acceleration = " + this.getAcceleration().toString();
		return rtn;
	}
	
	@Override
	public Particle clone() {
		Particle rtn = new Particle( this.getMass() , this.getPosition() , this.getVelocity() , this.getAcceleration() , this.m_damping );
		rtn.setInverseMass( this.getInverseMass() );
		return rtn;
	}
}
