package particle.collision;

import _lib.LinkedList;

abstract public class ParticleContactGenerator {

	/**
	 * @return			an appropriate <code>ParticleContact</code> based on the current
	 * 					situation. <code>null</code> if no contact should be generated
	 */
	abstract public LinkedList < ParticleContact > generateContact();
}
