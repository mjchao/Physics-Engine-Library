package rigidbody.run;

import java.util.ArrayList;

import rigidbody.RigidBody;
import rigidbody.collision.broad.PotentialContactGenerator;
import rigidbody.force.RigidBodyForceGenerator;
import _lib.LinkedList;
import _math.Real;

public class RigidBodyWorld {
	
	/**
	 * the list of <code>RigidBody</code> objects in this <code>RigidBodyWorld</code>
	 */
	final private ArrayList < RigidBody > m_rigidBodies = new ArrayList < RigidBody > ();
	
	/**
	 * the list of <code>RigidBodyForceGenerator</code>s in this <code>RigidBodyWorld</code>
	 */
	final private LinkedList < RigidBodyForceGenerator > m_forceGenerators = new LinkedList < RigidBodyForceGenerator > ();
	
	/**
	 * the list of <code>PotentialContactGenerator</code>s in this <code>RigidBodyWorld</code>
	 */
	final private LinkedList < PotentialContactGenerator > m_potentialContactGenerators = new LinkedList < PotentialContactGenerator > ();
	
	public RigidBodyWorld() {
		
	}
	
	public void addRigidBody( RigidBody toAdd ) {
		this.m_rigidBodies.add( toAdd );
	}
	
	public void addRigidBodyForceGenerator( RigidBodyForceGenerator toAdd ) {
		this.m_forceGenerators.add( toAdd );
	}
	
	public void addRigidBodyCollisionGenerator( PotentialContactGenerator toAdd ) {
		this.m_potentialContactGenerators.add( toAdd );
	}
	
	public void startFrame() {
		for ( RigidBody body : this.m_rigidBodies ) {
			body.clearAccumulators();
		}
	}
	
	public void integrate( Real duration ) {
		for ( RigidBody body : this.m_rigidBodies ) {
			body.act( duration );
		}
	}
	
	public void generateContacts( Real duration ) {
		/*//DEBUG
		PotentialContactResolver pcr = new PotentialContactResolver();
		pcr.addContact( new PotentialContact( this.m_rigidBodies.get( 0 ) , this.m_rigidBodies.get( 1 ) ) );
		pcr.resolve( duration );//*/
		
		for ( PotentialContactGenerator contactGenerator : this.m_potentialContactGenerators ) {
			contactGenerator.generatePotentialContacts();
		}
		for ( PotentialContactGenerator contactGenerator : this.m_potentialContactGenerators ) {
			contactGenerator.resolve( duration );
		}
	}
	
	public void runPhysics( Real duration ) {
		
		//apply force generators
		for ( RigidBodyForceGenerator generator : this.m_forceGenerators ) {
			generator.generateForce();
		}
		
		//have all rigidbodies act
		integrate( duration );
		
		//generate contacts
		generateContacts( duration );
	}
}
