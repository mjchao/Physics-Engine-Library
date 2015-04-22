package rigidbody.collision.generate;

import util.ErrorMessages;
import _lib.LinkedList;
import _math.Real;
import _math.Vector3D;

/**
 * a collision generator for spheres colliding with spheres
 */
public class SphereAndSphereCollisionGenerator extends ContactGenerator {
	
	/**
	 * constructs a sphere to sphere collision generator using the given amount
	 * of friction and elasticity in every collision
	 * 
	 * @param friction
	 * @param elasticity
	 */
	public SphereAndSphereCollisionGenerator( Real friction , Real elasticity , Real penetrationOffset ) {
		super( friction , elasticity , penetrationOffset );
	}
	
	/**
	 * returns all contacts between a sphere and another sphere
	 * 
	 * @throws IllegalArgumentException			if either <code>p1</code> or <code>p2</code> is not a <code>PrimitiveSphere</code>
	 * @see										PrimitiveSphere
	 */
	@Override
	public LinkedList < Contact > generateContacts( Primitive p1 , Primitive p2 ) throws IllegalArgumentException {
		
		//make sure we are given two primitive spheres
		if ( p1 instanceof PrimitiveSphere ) {
			if ( p2 instanceof PrimitiveSphere ) {
				PrimitiveSphere sphere1 = ( PrimitiveSphere ) p1;
				PrimitiveSphere sphere2 = ( PrimitiveSphere ) p2;
			
				//determine the sphere positions
				Vector3D position1 = sphere1.getPosition();
				Vector3D position2 = sphere2.getPosition();
				
				//determine the distance between the two spheres
				Vector3D vectorFromTwoToOne = position1.subtract( position2 );
				Real distanceFromTwoToOne = vectorFromTwoToOne.magnitude();
				
				//determine the sum of the radii of both spheres
				Real radiiSum = sphere1.getRadius().add( sphere2.getRadius() );
				
				//if the distance between the two spheres is greater than
				//the sum of their radii
				if ( distanceFromTwoToOne.compareTo( radiiSum ) > 0 ) {
					
					//then there are no contacts, so return an empty list
					return new LinkedList < Contact > ();
					
				//if the distance between the two spheres is less than or equal to
				//the sum of their radii
				} else {
					
					//then there are contacts
					LinkedList < Contact > contactsList = new LinkedList < Contact > ();
					
					//determine the contact normal, which is just the vector from
					//one sphere to the other, scaled to unit length
					Vector3D contactNormal = vectorFromTwoToOne.divide( distanceFromTwoToOne );
					
					//determine the contact point
					//which is the halfway point on the vector from sphere 1 to sphere 2
					Vector3D contactPoint = position1.add( vectorFromTwoToOne.multiply( Real.ONE_HALF ) );
					
					//determine the penetration, which is the radius overlap
					Real penetration = sphere1.getRadius().add( sphere2.getRadius() ).subtract( distanceFromTwoToOne );
					
					Contact sphereContact = new Contact( sphere1.getBody() , sphere2.getBody() , contactPoint , contactNormal , penetration , this.getPenetrationOffset() , this.getFriction() , this.getElasticity() );
					contactsList.add( sphereContact );
					return contactsList;
				}
			} else {
				throw new IllegalArgumentException( ErrorMessages.RigidBody.Collision.CollisionGenerator.INVALID_PRIMITIVE_PARAMETER( PrimitiveSphere.class.getName() , p2.getClass().getName() ) );
			}
		} else {
			throw new IllegalArgumentException( ErrorMessages.RigidBody.Collision.CollisionGenerator.INVALID_PRIMITIVE_PARAMETER( PrimitiveSphere.class.getName() , p1.getClass().getName() ) );
		}
	}

}
