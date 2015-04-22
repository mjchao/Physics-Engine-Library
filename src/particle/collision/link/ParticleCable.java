package particle.collision.link;

import particle.Particle;
import particle.collision.ParticleContact;
import _lib.LinkedList;
import _math.Real;
import _math.Vector3D;

/**
 * links together a pair of <code>Particle</code>s and generates a contact if they
 * go too far away from each other
 */
public class ParticleCable extends ParticleLink {

	public ParticleCable( Particle head , Particle tail , Real restLength , Real elasticity) {
		super( head , tail , restLength , elasticity);
	}
	
	@Override
	public Real calculateCurrentLength() {
		return this.m_head.getPosition().subtract( this.m_tail.getPosition() ).magnitude();
	}

	/**
	 * generates contacts on the head and tail of this <code>ParticleCable</code>.
	 * the head is the reference and the tail is the other object in the collision
	 */
	@Override
	public LinkedList < ParticleContact > generateContact() {
		
		//calculate the length of the cable
		Real length = this.calculateCurrentLength();
		
		//determine if we are stretched
		//if we are not stretched, no collisions are necessary
		if ( length.compareTo( this.m_restLength ) < 0 ) {
			return null;
			
		//otherwise, we are at rest or over-stretched and contact forces should be generated
		} else {
			
			//determine the two particles in the collision
			Particle contactReference = this.m_head;
			Particle contactOther = this.m_tail;
			
			//find the collision normal
			Vector3D normal = this.m_tail.getPosition().subtract( this.m_head.getPosition() ).normalize();
			
			//calculate the penetration
			//by subtracting the maximum length from the current length
			Real penetration = length.subtract( this.m_restLength );
			
			//determine the elasticity
			Real elasticity = this.m_elasticity;
			
			//return the ParticleContact
			LinkedList < ParticleContact > rtn = new LinkedList < ParticleContact > ();
			ParticleContact contact = new ParticleContact( contactReference , contactOther , elasticity , normal , penetration );
			rtn.add( contact );
			return rtn;
		}
	}

}
