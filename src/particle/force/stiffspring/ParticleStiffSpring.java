package particle.force.stiffspring;

import particle.Particle;
import particle.force.spring.ParticleAnchoredSpring;
import util.ErrorMessages;
import _math.Real;
import _math.Vector3D;

/**
 * a faked spring with a very high spring constant that is indended to be
 * very stiff.
 */
public class ParticleStiffSpring extends ParticleAnchoredSpring {

	/**
	 * the damping on the simple harmonic motion of this spring
	 */
	private Real m_damping;
	
	/**
	 * the dt to use when integrating to update particle data
	 */
	private Real m_integrationDuration;
	
	/**
	 * constructs a "faked" <code>ParticleStiffSpring</code> that fakes simple harmonic
	 * motion with a stiff spring. the spring rest length is always limited to zero.
	 * 
	 * @param anchor					anchor point
	 * @param springConstant			spring constant
	 * @param damping					damping for simple harmonic motion
	 * @param deltaT					the dt used in integrating to update
	 * 									particle data
	 */
	public ParticleStiffSpring( Vector3D anchor , Real springConstant , Real damping , Real deltaT ) {
		
		//a stiff spring would have a rest length of 0
		super( anchor , springConstant , Real.ZERO );
		this.m_damping = damping;
		this.m_integrationDuration = deltaT;
	}
	
	@Override
	public void generateForce() {
		for ( Particle aParticle : this.m_objects ) {
			
			//calculate the spring force on each particle
			Vector3D springForce = calculateSpringForce( aParticle , this.m_integrationDuration );
			
			//apply the force
			aParticle.addForceVector( springForce );
		}
	}
	
	/**
	 * assumes the particle attached to this spring is undergoing damped simple harmonic 
	 * motion and calculates an average force to apply to get the particle to get it
	 * to an approximate predicted position. this uses the equation
	 * <p>
	 * p(t) = [ p_i * cos( gamma * t ) + c * sin( gamma * t) ] * e^(-1/2*d*t)
	 * <p>
	 * where p(t) is the position as a function of time, p_i is the initial position,
	 * t is the time, d is damping , gamma is given by
	 * <p>
	 * gamma = 1/2 * sqrt( 4*k - d^2 ) ,
	 * <p>
	 * and c is given by 
	 * <p>
	 * c = d/(2 * gamma ) * p_i + 1/gamma * v_i
	 * <p>
	 * and v_i is the velocity of the particle when it is at p_i
	 * 
	 */
	protected Vector3D calculateSpringForce( Particle particle , Real duration ) {
		
		//make sure the particle has finite mass or else we cannot
		//fake this spring
		Real inverseMass = particle.getInverseMass();
		if ( inverseMass.equals( Real.ZERO ) ) {
			throw new IllegalArgumentException( ErrorMessages.Particle.Spring.INVALID_MASS );
		}
		
		//calculate the distance of this particle from the anchor
		Vector3D particlePosition = particle.getPosition().subtract( this.getReferencePosition() );
		Vector3D particleVelocity = particle.getVelocity();
		
		//calculate the constants in the position formula
		
		//calculate gamma
		Real gammaRadicand = Real.FOUR.multiply( this.getSpringConstant() ).subtract( this.m_damping.squared() );
		Real gamma = Real.ONE_HALF.multiply( Real.sqrt( gammaRadicand ) );
		
		//calculate c
		Vector3D cFirstAugend = this.m_damping.divide( Real.TWO.multiply( gamma ) ).multiply( particlePosition );
		Vector3D cSecondAugend = Real.ONE.divide( gamma ).multiply( particleVelocity );
		Vector3D c = cFirstAugend.add( cSecondAugend );
		
		//calculate the position as a function of time
		Real positionScalar = Real.E.pow( Real.NEGATIVE_ONE.multiply( Real.ONE_HALF ).multiply( this.m_damping ).multiply( duration ) );
		Vector3D positionVector = particlePosition.multiply( Real.sin( gamma.multiply( duration ) ) ).add( c.multiply( Real.sin( gamma.multiply( duration ) ) ) );
		Vector3D targetPosition = positionVector.multiply( positionScalar );
		
		//calculate the acceleration needed
		//we use the kinematic equation delta(x) = v_i*t + 1/2*a*t^2
		//so rearranging gives a = 2*( dx - v_i*t )/t^2
		Vector3D deltaX = targetPosition.subtract( particlePosition );
		Vector3D acceleration = Real.TWO.multiply( deltaX.subtract( particleVelocity ).multiply( duration ) ).divide( duration.squared() );
		
		//get the force
		Vector3D force = particle.getMass().multiply( acceleration );
		return force;
	}
}
