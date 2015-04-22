package particle.collision.link;

import particle.Particle;
import particle.collision.ParticleContact;
import particle.collision.ParticleContactGenerator;
import _lib.LinkedList;
import _math.Real;

/**
 * simulates a link, as in a link in a chain, where if particles get too far apart,
 * contact forces are generated
 */
abstract public class ParticleLink extends ParticleContactGenerator {

	/**
	 * the head of the link. this is used just for identification purposes
	 * and is completely arbitrary
	 */
	protected Particle m_head;

	/**
	 * the tail of the link. this is used just for identification purposes and
	 * is completely arbitrary
	 */
	protected Particle m_tail;
	
	/**
	 * the normal length of the <code>ParticleLink</code> when it is not
	 * stretched or compressed
	 */
	protected Real m_restLength;

	/**
	 * "bounciness" of the <code>ParticleLink</code>, i.e. how elastic the collisions
	 * between this link and the head and tail will be if stretched or compressed
	 */
	protected Real m_elasticity;
	
	public ParticleLink( Particle head , Particle tail , Real restLength , Real elasticity ) {
		this.m_head = head;
		this.m_tail = tail;
		this.m_restLength = restLength;
		this.m_elasticity = elasticity;
	}
	
	/**
	 * @return			the current length of this <code>ParticleLink</code> based
	 * 					on the position of its head and tail
	 */
	abstract public Real calculateCurrentLength();
	
	/**
	 * examines the properties of this <code>ParticleLink</code> and then
	 * returns an appropriate <code>ParticleContact</code> for simulating
	 * the reaction of the head and tail of this link.
	 * 
	 * @return				a <code>ParticleContact</code> object if a collision
	 * 						between the head and/or tail with this link should occur
	 * 						or <code>null</code> if no collision should occur
	 */
	@Override
	abstract public LinkedList < ParticleContact > generateContact();
}
