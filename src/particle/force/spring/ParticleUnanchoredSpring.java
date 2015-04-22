package particle.force.spring;

import particle.Particle;
import util.ErrorMessages;
import _math.Real;
import _math.Vector3D;

/**
 * generates spring-like forces on a <code>Particle</code>. the stretch distance of
 * the spring is based on the location of another moveable <code>Particle</code>
 */
public class ParticleUnanchoredSpring extends ParticleSpring {

	/**
	 * the particle attached to one end of the spring that is referenced
	 * in calculations involving stretch distance
	 */
	protected Particle m_reference;
	
	/**
	 * creates a <code>ParticleSpring</code> with the given spring constant and rest length
	 * and also specifies the <code>Particle</code> that serves as a reference for this
	 * <code>ParticleUnanchoredSpring</code>
	 * 
	 * @param reference
	 * @param springConstant
	 * @param restLength
	 * @throws IllegalArgumentException				if the reference is <code>null</code>
	 */
	public ParticleUnanchoredSpring( Particle reference , Real springConstant , Real restLength ) {
		super( springConstant , restLength );
		
		//make sure the reference exists
		if ( reference == null ) {
			throw new IllegalArgumentException( ErrorMessages.Particle.Spring.INVALID_REFERENCE );
		}
		this.m_reference = reference;
	}

	@Override
	protected Vector3D getStretchVector(Particle target) {
		return target.getPosition().subtract( this.m_reference.getPosition() );
	}
	
	@Override
	public Vector3D getReferencePosition() {
		return this.m_reference.getPosition();
	}
}
