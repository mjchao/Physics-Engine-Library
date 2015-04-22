package particle.collision.link;

import particle.Particle;
import particle.collision.ParticleContact;
import _lib.LinkedList;
import _math.Real;
import _math.Vector3D;

/**
 * simulates a rod, which keeps <code>Particle</code>s from going too far and 
 * too close to each other
 */
public class ParticleRod extends ParticleLink {

	final public static Real ROD_ELASTICITY = Real.ZERO;
	
	public ParticleRod(Particle head, Particle tail, Real restLength ) {
		super( head , tail , restLength , ROD_ELASTICITY );
	}

	@Override
	public Real calculateCurrentLength() {
		return this.m_head.getPosition().subtract( this.m_tail.getPosition() ).magnitude();
	}

	@Override
	public LinkedList < ParticleContact > generateContact() {
	
		//check if we are stretched or compressed
		Real currentLength = calculateCurrentLength();
		
		//if we're not stretched or compressed, the no contact is necessary
		if ( currentLength.compareTo( this.m_restLength ) == 0 ) {
			return null;
			
		//otherwise, we are stretched, so
		//generate the appropriate ParticleContact
		} else {
			
			//determine the reference and the other
			Particle reference = this.m_head;
			Particle other = this.m_tail;
			
			//calculate the normal
			Vector3D normal;
			if ( currentLength.compareTo( this.m_restLength ) > 0 ) {
				normal = this.m_tail.getPosition().subtract( this.m_head.getPosition() ).normalize();
			} else {
				normal = this.m_head.getPosition().subtract( this.m_tail.getPosition() ).normalize();
			}
			
			//determine the penetration - it must always be positive
			Real penetration = Real.abs( currentLength.subtract( this.m_restLength ) );
			
			//determine the elasticity
			Real elasticity = this.m_elasticity;
			
			//return the contact
			LinkedList < ParticleContact > rtn = new LinkedList < ParticleContact > ();
			ParticleContact contact = new ParticleContact( reference , other , elasticity , normal , penetration );
			rtn.add( contact );
			return rtn;
		}
	}

}
