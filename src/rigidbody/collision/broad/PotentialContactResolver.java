package rigidbody.collision.broad;

import rigidbody.collision.generate.Contact;
import rigidbody.collision.resolve.ContactResolver;
import _lib.LinkedList;
import _math.Real;

public class PotentialContactResolver {

	LinkedList < PotentialContact > m_contactsToResolve = new LinkedList < PotentialContact > ();
	//SphereAndPlaneContactGenerator m_collider = new SphereAndPlaneContactGenerator( Real.ZERO , Real.ONE , ContactGenerator.DEFAULT_PENETRATION_OFFSET );
	//BoxAndPlaneContactGenerator m_collider = new BoxAndPlaneContactGenerator( Real.ZERO , Real.ONE, ContactGenerator.DEFAULT_PENETRATION_OFFSET );
	//BoxAndSphereContactGenerator m_collider = new BoxAndSphereContactGenerator( Real.ZERO , Real.ONE , ContactGenerator.DEFAULT_PENETRATION_OFFSET );
	//BoxAndBoxCollisionGenerator m_collider = new BoxAndBoxCollisionGenerator( new Real( 0 ) , Real.ONE , ContactGenerator.DEFAULT_PENETRATION_OFFSET );
	//SphereAndSphereContactGenerator m_collider = new SphereAndSphereContactGenerator( Real.ZERO , Real.ONE , ContactGenerator.DEFAULT_PENETRATION_OFFSET );
	
	ContactResolver m_resolver = new ContactResolver( 1000 );
	
	public PotentialContactResolver() {
		
	}
	
	public void addContact( PotentialContact contact ) {
		this.m_contactsToResolve.add( contact );
	}
	
	/**
	 * adds a known contact immediately to the <code>ContactResolver</code>, skipping
	 * any preliminary potential contact checks
	 * 
	 * @param contact			a known contact to add
	 */
	public void addContact( Contact contact ) {
		this.m_resolver.addContact( contact );
	}
	
	
	//final private Real m_size = new Real( 0.3 ).add( ContactGenerator.DEFAULT_PENETRATION_OFFSET );
	
	//TODO
	public void resolve( Real duration ) {
		/*while ( this.m_contactsToResolve.size() > 0 ) {
			for ( PotentialContact contact : this.m_contactsToResolve ) {
				PrimitiveBox box1 = new PrimitiveBox( contact.getBody1() , contact.getBody1().getOrientation().toOrientationAndPositionMatrix( Vector3D.ZERO ) , new Vector3D( this.m_size , this.m_size , this.m_size ) );
				PrimitiveSphere sphere1 = new PrimitiveSphere( contact.getBody1() , contact.getBody1().getOrientation().toOrientationAndPositionMatrix( Vector3D.ZERO ) , this.m_size );
				PrimitivePlane plane1 = new PrimitivePlane( new Vector3D( new Real( 0 ) , new Real( 1 ) , new Real( 0 ) ), new Real( 97 ) );
				
				PrimitiveBox box2 = new PrimitiveBox( contact.getBody2() , contact.getBody2().getOrientation().toOrientationAndPositionMatrix( Vector3D.ZERO ) , new Vector3D( this.m_size , this.m_size , this.m_size ) );
				PrimitiveSphere sphere2 = new PrimitiveSphere( contact.getBody2() , contact.getBody2().getOrientation().toOrientationAndPositionMatrix( Vector3D.ZERO ) , this.m_size );
				LinkedList < Contact > contactsGenerated = this.m_collider.generateContacts( box1 , box2 );
				this.m_contactsToResolve.remove();
				if ( contactsGenerated.size() > 0 ) {
					for ( Contact contactToResolve : contactsGenerated ) {
						this.m_resolver.addContact( contactToResolve );
					}
				}
			}
		}*/
		this.m_resolver.resolve( duration );
	}
}
