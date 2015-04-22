package particle.force;

import particle.Particle;
import force.Force;
import force.ForceGenerator;

/**
 * template for <code>ForceGenerator</code>s that apply to <code>Particle</code>s
 */
abstract public class ParticleForceGenerator extends ForceGenerator < Particle > {

	/**
	 * creates a <code>ParticleForceGenerator</code> with no defined force
	 */
	public ParticleForceGenerator() {
		super();
	}
	
	/**
	 * creates a <code>ParticleForceGenerator</code> that applies a constant force
	 * 
	 * @param aForce		the constant force to apply
	 */
	public ParticleForceGenerator( Force aForce ) {
		super( aForce );
	}
}
