package rigidbody.collision.broad;

import rigidbody.collision.generate.Contact;
import _math.Real;

abstract public class PotentialContactGenerator {

	protected static PotentialContactResolver DEFAULT_CONTACT_RESOLVER = new PotentialContactResolver();
	
	private PotentialContactResolver m_potentialContactResolver = DEFAULT_CONTACT_RESOLVER;
	
	/**
	 * creates a default <code>PotentialContactGenerator</code> that uses the default
	 * <code>PotentialContactResolver</code> for all <code>PotentialContact</code>s generated
	 */
	public PotentialContactGenerator() {
		
	}
	
	/**
	 * adds the given <code>PotentialContact</code> to the list of <code>PotentialContact</code>s
	 * 
	 * @param contact		the <code>PotentialContact</code> to add
	 */
	protected void addPotentialContact( PotentialContact contact ) {
		this.m_potentialContactResolver.addContact( contact );
	}
	
	/**
	 * adds a known contact immediately to the <code>ContactResolver</code>, skipping
	 * any preliminary potential contact checks
	 * 
	 * @param contact			a known contact to add
	 */
	protected void addContact( Contact contact ) {
		this.m_potentialContactResolver.addContact( contact );
	}
	
	/**
	 * sets the <code>PotentialContactResolver</code> associated with this <code>PotentialContactGenerator</code>
	 * 
	 * @param newResolver			the new <code>PotentialContactResolver</code>
	 */
	protected void setContactResolver( PotentialContactResolver newResolver ) {
		this.m_potentialContactResolver = newResolver;
	}
	
	/**
	 * generates all <code>PotentialContact<code>s associated with this <code>PotentialContactGenerator</code>
	 */
	abstract public void generatePotentialContacts();
	
	/**
	 * resolves <code>PotentialContact</code>s that have been generated
	 * 
	 * @param duration			duration over which contacts should be resolved
	 */
	public void resolve( Real duration ) {
		this.m_potentialContactResolver.resolve( duration );
	}
}
