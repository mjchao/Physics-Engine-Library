package particle.force.bungee;

import particle.Particle;
import particle.force.spring.ParticleUnanchoredSpring;
import _math.Real;
import _math.Vector3D;

public class ParticleUnanchoredBungee extends ParticleUnanchoredSpring {
	
	/**
	 * creates a <code>ParticleBungee</code> with the given spring constant and rest length
	 * and also specifies the particular <code>Particle</code> that serves as a reference
	 * for this <code>ParticleUnanchoredBungee</code>
	 * 
	 * @param reference
	 * @param springConstant
	 * @param restLength
	 */
	public ParticleUnanchoredBungee( Particle reference , Real springConstant , Real restLength ) {
		super( reference , springConstant , restLength );
	}

	@Override
	protected Vector3D getStretchVector(Particle target) {
		return target.getPosition().subtract( this.m_reference.getPosition() );
	}
	
	/**
	 * calculates the bungee spring force on a <code>Particle</code> using the formula
	 * <p>
	 * F = 0, for x <= rest length
	 * <p>
	 * F = -k * x, for x >= rest length
	 * <p>
	 * where x is the stretch distance
	 */
	@Override
	protected Vector3D calculateSpringForce( Particle particle ) {
		
		//determine the vector connecting this particle to the reference
		Vector3D vectorFromParticleToReference = getStretchVector( particle );
		
		//get the magnitude of the stretch distance
		Real particleToReferenceDistance = vectorFromParticleToReference.magnitude();
		Real stretchDistance = particleToReferenceDistance.subtract( this.m_restLength );
		
		//if the stretch distance is negative
		//then the spring force is 0 because bungees cannot push
		if ( stretchDistance.compareTo( Real.ZERO ) <= 0 ) {
			return Vector3D.ZERO;
			
		//otherwise, calculate the spring force
		} else {
			
			//calculate the magnitude of the force
			Real forceMagnitude = this.m_springConstant.multiply( stretchDistance );
			
			//get the unit direction vector, which is the negative of the normalized stretch vector
			Vector3D unitDirectionVector = vectorFromParticleToReference.normalize().invert();
			
			//scale the unit direction vector by the spring constant times the stretch distance
			Vector3D forceVector = unitDirectionVector.multiply( forceMagnitude );
			
			return forceVector;
		}
	}
}
