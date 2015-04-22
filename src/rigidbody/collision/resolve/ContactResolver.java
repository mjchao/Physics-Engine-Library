package rigidbody.collision.resolve;

import rigidbody.collision.generate.Contact;
import _lib.LinkedList;
import _math.Real;

/**
 * resolves contacts between <code>RigidBody</code> objects
 */
public class ContactResolver {
	
	final public static int DEFAULT_MAXIMUM_ITERATIONS = 1000;
	
	/**
	 * the list of penetrations to be resolved
	 */
	private LinkedList < Contact > m_penetrationsToResolve = new LinkedList < Contact > ();

	/**
	 * the list of velocities to be resolved
	 */
	private LinkedList < Contact > m_velocitiesToResolve = new LinkedList < Contact > ();
	
	/**
	 * the maximum iterations to be used in any resolution step
	 */
	private int m_maximumIterations;
	
	/**
	 * creates a <code>ContactResolver</code> that resolves collisions between objects 
	 * 
	 * @param maximumResolveIterations
	 */
	public ContactResolver( int maximumResolveIterations ) {
		this.m_maximumIterations = maximumResolveIterations;
	}
	
	public ContactResolver() {
		this( DEFAULT_MAXIMUM_ITERATIONS );
	}
	
	/**
	 * adds the given <code>Contact</code> to the list of <code>Contact</code>s that need
	 * to be resolved
	 * 
	 * @param contact			the <code>Contact</code> to add
	 */
	public void addContact( Contact contact ) {
		this.m_penetrationsToResolve.add( contact );
		this.m_velocitiesToResolve.add( contact );
	}
	
	/**
	 * resolves the <code>Contact</code>s assigned to this <code>ContactResolver</code>
	 * 
	 * @param duration		duration over which these <code>Contact</code>s occur - i.e.
	 * 						the duration passed to the <code>RigidBody</code> <code>act()</code>
	 * 						method
	 * @see					force.MassedObject#act(Real)
	 */
	public void resolve( Real duration ) {
		resolvePenetrations();
		resolveVelocities( duration );
	}
	
	/**
	 * resolves the penetrations in the <code>Contact</code>s assigned to this
	 * <code>ContactResolver</code>
	 */
	protected void resolvePenetrations() {
		int iterationsUsed = 0;
		while ( iterationsUsed < this.m_maximumIterations ) {
			Real maximumPenetration = Real.ZERO;
			Contact contactToResolve = null;
			
			//go through each contact
			for ( Contact aContact : this.m_penetrationsToResolve ) {
				
				//find the worst contact - i.e. the one with the most penetration
				if ( aContact.getPenetration().compareTo( maximumPenetration ) >= 0 ) {
					maximumPenetration = aContact.getPenetration();
					contactToResolve = aContact;
				}
			}
			
			//resolve the worst contact if it exists
			if ( contactToResolve == null ) {
				return;
			} else {
				
				//first, wake up any objects in the contact, if necessary
				contactToResolve.wakeUpObjects();
				
				//then, resolve the penetration
				contactToResolve.resolvePenetration( Contact.DEFAULT_ANGULAR_LIMIT_CONSTANT , this.m_penetrationsToResolve );
				
				//and remove the contact from the list
				this.m_penetrationsToResolve.remove( contactToResolve );
			}
			
			iterationsUsed++;
		}
	}
	
	/**
	 * resolves velocities of <code>RigidBody</code> objects involved in a <code>Contact</code>
	 * 
	 * @param duration			the duration for which this <code>Contact</code> occurs - i.e. the
	 * 							duration of the <code>act()</code> method in the <code>RigidBody</code> class
	 * @see 					force.MassedObject#act(Real)
	 */
	protected void resolveVelocities( Real duration ) {
		
		//go through each contact
		for ( Contact aContact : this.m_velocitiesToResolve ) {
			
			//resolve the impulses associated with the contact
			aContact.resolveVelocity( duration );
			
			//remove the contact from the list of contacts for which to resolve
			//velocity
			this.m_velocitiesToResolve.remove();
		}
	}
}
