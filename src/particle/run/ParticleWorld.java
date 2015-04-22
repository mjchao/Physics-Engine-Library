package particle.run;

import particle.Particle;
import particle.collision.ParticleContact;
import particle.collision.ParticleContactGenerator;
import particle.collision.ParticleContactResolver;
import particle.force.ParticleForceGenerator;
import _lib.LinkedList;
import _math.Real;

public class ParticleWorld {

	/**
	 * the <code>Particle</code>s in this <code>ParticleWorld</code>
	 */
	private LinkedList < Particle > m_particles = new LinkedList < Particle > ();
	
	/**
	 * the <code>ForceGenerator</code>s in this <code>ParticleWorld</code>
	 */
	private LinkedList < ParticleForceGenerator > m_forceGenerators = new LinkedList < ParticleForceGenerator > ();
	
	/**
	 * the <code>ParticleContactGenerator</code>s in this <code>ParticleWorld</code>
	 */
	private LinkedList < ParticleContactGenerator > m_contactGenerators = new LinkedList < ParticleContactGenerator > ();
	
	/**
	 * resolves <code>ParticleContact</code>s
	 */
	private ParticleContactResolver m_contactResolver;
	
	/**
	 * maximum number of <code>ParticleContacts</code> that can be stored in this
	 * <code>ParticleWorld</code>
	 */
	private int m_maxContacts;
	
	/**
	 * maximum number of <code>ParticleContact</code>s with which to deal in each frame
	 */
	private int m_maxContactsPerFrame;
	
	
	/**
	 * creates a <code>ParticleWorld</code> that runs physics indefinitely
	 */
	public ParticleWorld() {
		this.m_maxContacts = Integer.MAX_VALUE;
		this.m_maxContactsPerFrame = Integer.MAX_VALUE;
		this.m_contactResolver = new ParticleContactResolver( this.m_maxContactsPerFrame );
	}
	
	/**
	 * creates a <code>ParticleWorld</code> that will handle <code>maxContacts</code>
	 * <code>ParticleContact</code>s at a time. the maximum number of
	 * <code>ParticleContact</code>s dealt with in each frame is by default twice
	 * <code>maxContacts</code>
	 * 
	 * @param maxContactsPerFrame			the maximum number of contacts to deal with
	 */
	public ParticleWorld( int maxContacts ) {
		this.m_maxContacts = maxContacts;
		this.m_maxContactsPerFrame = maxContacts * 2;
	}
	
	/**
	 * creates a <code>ParticleWorld</code> that will handle at most <code>maxContact</code>
	 * <code>ParticleContact</code>s at any time and at most <code>maxContactsPerFrame</code>
	 * <code>ParticleContact</code>s in a frame
	 * 
	 * @param maxContacts				maximum <code>ParticleContact</code>s to be stored
	 * @param maxContactsPerFrame		maximum <code>ParticleContact</code>s to be dealt with in a frame
	 */
	public ParticleWorld( int maxContacts , int maxContactsPerFrame ) {
		this.m_maxContacts = maxContacts;
		this.m_maxContactsPerFrame = maxContactsPerFrame;
	}
	
	/**
	 * adds the given <code>Particle</code> to this <code>ParticleWorld</code>
	 * 
	 * @param toAdd
	 */
	public void addParticle( Particle toAdd ) {
		this.m_particles.add( toAdd );
	}
	
	/**
	 * adds the given <code>ForceGenerator</code> to this <code>ParticleWorld</code>
	 * 
	 * @param toAdd
	 */
	public void addForceGenerator( ParticleForceGenerator toAdd ) {
		this.m_forceGenerators.add( toAdd );
	}
	
	/**
	 * adds the given <code>ParticleContactGenerator</code> to this <code>ParticleWorld</code>
	 * 
	 * @param toAdd
	 */
	public void addContactGenerator( ParticleContactGenerator toAdd ) {
		this.m_contactGenerators.add( toAdd );
	}
	
	/**
	 * generates <code>ParticleContact</code>s between <code>Particle</code>s
	 */
	public void generateContacts() {
		int contactsGenerated = this.m_contactResolver.getNumContacts();
		
		//go through all the ParticleContactGenerators
		for ( ParticleContactGenerator contactGenerator : this.m_contactGenerators ) {
			
			//do not go over the limit of maximum contacts with which to deal
			if ( contactsGenerated > this.m_maxContacts ) {
				break;
			}
			
			LinkedList < ParticleContact > contacts = contactGenerator.generateContact();
			
			//if no contact was created, then
			if ( contacts == null ) {
				
				//continue
				
			//if contacts were created, add it to the list
			} else {
				for ( ParticleContact contact : contacts ) {
					this.m_contactResolver.addContact( contact );
					contactsGenerated ++;
				}
			}
		}
	}
	
	/**
	 * integrates particle data over the given duration
	 * 
	 * @param duration
	 */
	public void integrate( Real duration ) {
		for ( Particle particle : this.m_particles ) {
			particle.act( duration );
		}
	}
	
	public void runPhysics( Real duration ) {
		
		//apply force generators
		for ( ParticleForceGenerator forceGenerator : this.m_forceGenerators ) {
			forceGenerator.generateForce();
		}
		
		//integrate and update particle data
		integrate( duration );
		
		//generate contacts
		generateContacts();
		
		//process contacts
		this.m_contactResolver.resolve( duration );
	}
	
}
