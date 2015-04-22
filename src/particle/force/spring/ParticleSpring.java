package particle.force.spring;

import particle.Particle;
import particle.force.ParticleForceGenerator;
import util.ErrorMessages;
import _math.Real;
import _math.Vector3D;

/**
 * a spring
 */
abstract public class ParticleSpring extends ParticleForceGenerator {

	/**
	 * the spring constant
	 */
	protected Real m_springConstant;
	
	/**
	 * the length of the spring when it is uncompressed/unstretched
	 */
	protected Real m_restLength;
	
	/**
	 * creates a <code>ParticleSpring</code> with the given spring constant and
	 * length at rest
	 * 
	 * @param springConstant			the spring constant for the spring
	 * @param restLength				the length of the spring when it is uncompressed or unstretched
	 * @throws IllegalArgumentException	if the spring constant and/or rest length is not positive
	 */
	public ParticleSpring( Real springConstant , Real restLength ) {
		super();
		setSpringConstant( springConstant );
		setRestLength( restLength );
	}
	
	@Override
	public void generateForce() {
		
		//go through the particles attached to this spring
		for ( Particle aParticle : this.m_objects ) {

			//calculate the spring force on each particle
			Vector3D springForce = calculateSpringForce( aParticle );
			
			//and add the spring force to the particle
			aParticle.addForceVector( springForce );
			
			//generate a reaction force on the reference if there is a reference part
			if ( this instanceof ParticleUnanchoredSpring ) {
				( ( ParticleUnanchoredSpring ) this ).m_reference.addForceVector( springForce.multiply( Real.NEGATIVE_ONE ) );
			}
		}
	}

	/**
	 * sets the spring constant of this spring to a new value
	 * 
	 * @param newSpringConstant				the new spring constant for this spring
	 * @throws IllegalArgumentException		if the new spring constant is not positive
	 */
	public void setSpringConstant( Real newSpringConstant ) {
		
		//make sure the spring constant is not negative
		if ( newSpringConstant.compareTo( Real.ZERO ) <= 0 ) {
			throw new IllegalArgumentException( ErrorMessages.Particle.Spring.INVALID_SPRING_CONSTANT );
		}
		this.m_springConstant = newSpringConstant;
	}
	
	/**
	 * @return			the spring constant of this spring
	 */
	public Real getSpringConstant() {
		return this.m_springConstant;
	}
	
	/**
	 * sets the uncompressed/unstretched length of this spring to a new value
	 * 
	 * @param newRestLength					the new uncompressed/unstretched length
	 * @throws IllegalArgumentException		if the new rest length is not positive
	 */
	public void setRestLength( Real newRestLength ) {
		
		//make sure the rest length is positive
		if ( newRestLength.compareTo( Real.ZERO ) < 0 ) {
			throw new IllegalArgumentException( ErrorMessages.Particle.Spring.INVALID_REST_LENGTH );
		}
		this.m_restLength = newRestLength;
	}
	
	/**
	 * @return			length of this spring when uncompressed and unstretched
	 */
	public Real getRestLength() {
		return this.m_restLength;
	}
	
	/**
	 * calculates the spring force on a <code>Particle</code> using the formula
	 * <p>
	 * F = -k * x   
	 * <p>
	 * where k is the spring constant and x is the stretch distance
	 * 
	 * @param particle				a particle
	 * @return						the force this spring exerts on the <code>Particle</code>
	 * 								based on its distance from rest
	 */
	protected Vector3D calculateSpringForce( Particle particle ) {
		
		//determine the vector connecting the particle to the reference
		Vector3D vectorFromParticleToReference = getStretchVector( particle );
		
		//get the magnitude of the stretch distance
		Real particleToReferenceDistance = vectorFromParticleToReference.magnitude();
		Real stretchDistance = particleToReferenceDistance.subtract( this.m_restLength );
		
		//calculate the magnitude of the force
		Real forceMagnitude = this.m_springConstant.multiply( stretchDistance );
		
		//get the unit direction vector, which is the negative of the normalized stretch vector
		Vector3D unitDirectionVector = vectorFromParticleToReference.normalize().invert();
		
		//scale the unit direction vector by the spring constant times the stretch distance
		Vector3D forceVector = unitDirectionVector.multiply( forceMagnitude );
		
		return forceVector;
	}
	
	
	/**
	 * @return 				the distance from the particle to which to apply a force to
	 * 						a reference point
	 */
	abstract protected Vector3D getStretchVector( Particle target );
	
	abstract protected Vector3D getReferencePosition();
	
}
