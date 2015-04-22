package particle.force.spring;

import particle.Particle;
import _math.Real;
import _math.Vector3D;

public class ParticleAnchoredSpring extends ParticleSpring {

	/**
	 * the reference point
	 */
	private Vector3D m_reference;
	
	/**
	 * creates a <code>ParticleSpring</code> with the given spring constant and rest length
	 * and also specifies the point at which this <code>ParticleAnchoredSpring</code> is
	 * anchored
	 * 
	 * @param anchor
	 * @param springConstant
	 * @param restLength
	 */
	public ParticleAnchoredSpring( Vector3D anchor , Real springConstant , Real restLength ) {
		super( springConstant , restLength );
		this.m_reference = anchor;
	}

	@Override
	protected Vector3D getStretchVector(Particle target) {
		return target.getPosition().subtract( this.m_reference );
	}
	
	@Override
	public Vector3D getReferencePosition() {
		return this.m_reference;
	}
}
