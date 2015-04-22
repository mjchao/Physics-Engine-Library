package particle.collision;

import util.ErrorMessages;
import _lib.LinkedList;
import _math.Real;

/**
 * resolves collisions occurring between <code>Particles</code>
 */
public class ParticleContactResolver {

	/**
	 * a limit on the number of times to process <code>ParticleContact</code>s
	 */
	private int m_maximumIterations;
	
	/**
	 * the <code>ParticleContact</code>s to be resolved
	 */
	private LinkedList < ParticleContact > m_contacts = new LinkedList < ParticleContact > ();
	
	/**
	 * creates a <code>ParticleContactResolver</code>
	 * 
	 * @param maximumIterations				the maximum number of times to process <code>ParticleContact</code>s
	 * @param contacts						the <code>ParticleContact</code>s to be resolved
	 * @throws IllegalArgumentException		if <code>maximumIterations</code> is negative
	 */
	public ParticleContactResolver( int maximumIterations ) {
		setMaximumIterations( maximumIterations );
	}
	
	/**
	 * adds a <code>ParticleContact</code> to this <code>ParticleContactGenerator</code>
	 * 
	 * @param aContact		the <code>ParticleContact</code> to add
	 */
	public void addContact( ParticleContact aContact ) {
		this.m_contacts.add( aContact );
	}
	
	/**
	 * @return			the number of <code>ParticleContact</code>s with which this 
	 * 					<code>ParticleContactResolver</code> deals
	 */
	public int getNumContacts() {
		return this.m_contacts.size();
	}
	
	/**
	 * sets the maximum number of times to process <code>ParticleContact</code>s
	 * 
	 * @param maximumIterations				maximum number of times to process <code>ParticleContact</code>s
	 * @throws IllegalArgumentException		if <code>maximumIterations</code> is negative		
	 */
	private void setMaximumIterations( int maximumIterations ) {
		
		//make sure we are not given a negative number of maximum iterations
		if ( maximumIterations < 0 ) {
			throw new IllegalArgumentException( ErrorMessages.Particle.COLLISION.INVALID_ITERATION_QUANTITY );
		}
		this.m_maximumIterations = maximumIterations;
	}
	
	/**
	 * resolves the <code>ParticleContact</code>s assigned to this <code>ParticleContactResolver</code>.
	 * the order used is in order of lowest ( most negative ) separation velocity
	 * 
	 * @param duration			time length of a frame of the physics simulation
	 */
	public void resolve( Real duration ) {
		int numContacts = this.m_contacts.size();
		
		//if there are no contacts, then there is nothing to resolve
		if ( numContacts == 0 ) {
			return;
		}
		
		for ( int iterations = 0 ; iterations < this.m_maximumIterations ; iterations ++ ) {
			
			//find the contact with the largest closing velocity, i.e. the smallest separation velocity
			
			Real max = Real.MAX_VALUE;
			ParticleContact highestPriorityContact = null;
			
			//then go through all the contacts and find the one with the largest closing velocity
			for ( ParticleContact contact : this.m_contacts ) {
				Real separationVelocity = contact.calculateSeparatingVelocity();
				if ( ( separationVelocity.compareTo( max ) < 0 && separationVelocity.compareTo( Real.ZERO ) < 0 ) || 
						contact.getPenetration().compareTo( Real.ZERO ) > 0 )  {
					max = separationVelocity;
					highestPriorityContact = contact;
				}
			}
			
			//if no contact was found that needed to be resolved,
			//then we are done
			if ( highestPriorityContact == null ) {
				return;
				
			//otherwise, resolve the contact
			} else {
				highestPriorityContact.resolve( duration );
				
				//and then remove it from the list of contacts to resolve
				this.m_contacts.remove( highestPriorityContact );
			}
		}
	}
}
