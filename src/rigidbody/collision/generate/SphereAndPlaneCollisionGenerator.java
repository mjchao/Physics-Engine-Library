package rigidbody.collision.generate;

import util.ErrorMessages;
import _lib.LinkedList;
import _math.Real;
import _math.Vector3D;

public class SphereAndPlaneCollisionGenerator extends ContactGenerator {

	public SphereAndPlaneCollisionGenerator( Real friction , Real elasticity , Real penetrationOffset ) {
		super( friction , elasticity , penetrationOffset );
	}

	/**
	 * returns all contacts between a sphere and a plane
	 * 
	 * @throws IllegalArgumentException			if <code>p1</code> is not a <code>PrimitiveSphere</code> or <code>p2</code> is not a <code>PrimitivePlane</code>
	 * @see										PrimitiveSphere
	 * @see										PrimitivePlane
	 */
	@Override
	public LinkedList < Contact > generateContacts( Primitive p1 , Primitive p2 ) throws IllegalArgumentException {
		if ( p1 instanceof PrimitiveSphere ) {
			if ( p2 instanceof PrimitivePlane ) {
				PrimitiveSphere sphere = ( PrimitiveSphere ) p1;
				PrimitivePlane plane = ( PrimitivePlane ) p2;
				LinkedList < Contact > contacts = new LinkedList < Contact > ();
				
				//determine the sphere's position
				Vector3D spherePosition = sphere.getPosition();
				
				//determine the distance of the sphere from the plane
				//by a projection 
				Real sphereDistanceFromPlane = ContactGenerator.pointToPlaneDistance( spherePosition , plane );
				
				//determine if the distance from the plane is less than a radius of the sphere
				
				//if the sphere is within a radius length of the plane,
				//then there is a contact
				if ( Real.abs( sphereDistanceFromPlane ).compareTo( Real.abs( sphere.getRadius() ) ) <= 0 ) {
					
					//determine the properties of the collision
					//based on the side of the plane on which the collision occurred
					Vector3D planeNormal;
					Real penetration;
					
					//if the contact was on the side facing lower coordinate values
					if ( sphereDistanceFromPlane.compareTo( Real.ZERO ) < 0 ) {
						planeNormal = plane.getNormal().multiply( Real.NEGATIVE_ONE );
						penetration = sphereDistanceFromPlane.add( sphere.getRadius() );
						
					//if the contact was on the side facing higher coordinate values
					} else {
						planeNormal = plane.getNormal();
						penetration = sphereDistanceFromPlane.multiply( Real.NEGATIVE_ONE ).add( sphere.getRadius() );
					}
					
					//determine the contact point on the plane
					Vector3D contactPoint = spherePosition.subtract( plane.getNormal() ).multiply( sphereDistanceFromPlane );
					
					Contact spherePlaneContact = new Contact( sphere.getBody() , null , contactPoint , planeNormal , penetration , this.getPenetrationOffset() , this.getFriction() , this.getElasticity() );
					contacts.add( spherePlaneContact );
				}
				return contacts;
			} else {
				throw new IllegalArgumentException( ErrorMessages.RigidBody.Collision.CollisionGenerator.INVALID_PRIMITIVE_PARAMETER( PrimitivePlane.class.getName() , p2.getClass().getName() ) );
			}
		} else {
			throw new IllegalArgumentException( ErrorMessages.RigidBody.Collision.CollisionGenerator.INVALID_PRIMITIVE_PARAMETER( PrimitiveSphere.class.getName() , p1.getClass().getName() ) );
		}
	}

}
