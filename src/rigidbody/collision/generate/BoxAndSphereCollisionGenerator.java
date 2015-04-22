package rigidbody.collision.generate;

import util.ErrorMessages;
import _lib.LinkedList;
import _math.Real;
import _math.Vector3D;

public class BoxAndSphereCollisionGenerator extends ContactGenerator {

	public BoxAndSphereCollisionGenerator( Real friction, Real elasticity , Real penetrationOffset ) {
		super( friction , elasticity , penetrationOffset );
	}

	@Override
	public LinkedList<Contact> generateContacts(Primitive p1, Primitive p2) {
		if ( p1 instanceof PrimitiveBox ) {
			if ( p2 instanceof PrimitiveSphere ) {
				PrimitiveBox box = ( PrimitiveBox ) p1;
				box.determineVertices();
				PrimitiveSphere sphere = ( PrimitiveSphere ) p2;
				LinkedList < Contact > contacts = new LinkedList < Contact > ();
				
				//determine the closest point of contact
				Vector3D contactPoint = determineClosestContactPoint( box , sphere );
				if ( contactPoint != null ) {
					Vector3D contactPointToSphereCenterVector = contactPoint.subtract( sphere.getPosition() );
					Vector3D contactNormal = contactPointToSphereCenterVector.normalize();
					Real penetration = sphere.getRadius().subtract( contactPointToSphereCenterVector.magnitude() );
					Contact sphereBoxContact = new Contact( box.getBody() , sphere.getBody() , contactPoint , contactNormal , penetration , this.getPenetrationOffset() , this.getFriction() , this.getElasticity() );
					contacts.add( sphereBoxContact );
				}
				return contacts;
			} else {
				throw new IllegalArgumentException( ErrorMessages.RigidBody.Collision.CollisionGenerator.INVALID_PRIMITIVE_PARAMETER( PrimitiveSphere.class.getName() , p2.getClass().getName() ) );
			}
		} else {
			throw new IllegalArgumentException( ErrorMessages.RigidBody.Collision.CollisionGenerator.INVALID_PRIMITIVE_PARAMETER( PrimitiveBox.class.getName() , p1.getClass().getName() ) );
		}
	}

	/**
	 * determines the point on a box that is closest to a sphere and in contact with the sphere
	 * 
	 * @param box			a box
	 * @param sphere		a sphere
	 * @return				the point on the box, in world coordinates, that is closest to and in contact with the sphere, or
	 * 						<code>null</code> if the sphere is not in contact with the box
	 */
	protected static Vector3D determineClosestContactPoint( PrimitiveBox box , PrimitiveSphere sphere ) {
		
		//convert the sphere's center's coordinates into local coordinates of the box
		Vector3D spherePosition = box.getBody().convertWorldToLocal( sphere.getPosition() );
		
		//make sure the sphere could be in contact with the box
		if ( Real.abs( spherePosition.getX() ).subtract( sphere.getRadius() ).compareTo( box.getHalfSize().getX() ) > 0 ||
			 Real.abs( spherePosition.getY() ).subtract( sphere.getRadius() ).compareTo( box.getHalfSize().getY() ) > 0 ||
			 Real.abs( spherePosition.getZ() ).subtract( sphere.getRadius() ).compareTo( box.getHalfSize().getZ() ) > 0 ) {
			return null;
		}
		
		//check if the x of the center of the sphere is within the boundaries of the box
		Real closestX = spherePosition.getX();
		
		//if the x coordinate of the center of the sphere is too big,
		//then set the closest x coordinate as the largest x coordinate on the box
		if ( closestX.compareTo( box.getHalfSize().getX() ) > 0 ) {
			closestX = box.getHalfSize().getX();
			
		//if the x coordinate of the center of the sphere is too small,
		//then the closest point has the smallest possible x coordinate on the box
		} else if ( closestX.compareTo( box.getHalfSize().getX().multiply( Real.NEGATIVE_ONE ) ) < 0 ) {
			closestX = box.getHalfSize().getX().multiply( Real.NEGATIVE_ONE );
		}
		
		//check if the y of the center of the sphere is within the boundaries of the box
		Real closestY = spherePosition.getY();
		
		//if the y coordinate is too big,
		//then set the closest y coordinate as the biggest possible y coordinate on the box
		if ( closestY.compareTo( box.getHalfSize().getY() ) > 0 ) {
			closestY = box.getHalfSize().getY();
			
		//if the y coordinate is too small,
		//then set the closest y coordinate as the smallest possible y coordinate on the box
		} else if ( closestY.compareTo( box.getHalfSize().getY().multiply( Real.NEGATIVE_ONE ) ) < 0 ) {
			closestY = box.getHalfSize().getY().multiply( Real.NEGATIVE_ONE );
		}
		
		//check if the z of the center of the sphere is within the boundaries of the box
		Real closestZ = spherePosition.getZ();
		
		//if the z coordinate is too big,
		//then set the closest z coordinate as the biggest possible z on the box
		if ( closestZ.compareTo( box.getHalfSize().getZ() ) > 0 ) {
			closestZ = box.getHalfSize().getZ();
			
		//if the z coordinate is too small,
		//then set the closest z coordinate as the smallest possible z coordinate on the box
		} else if ( closestY.compareTo( box.getHalfSize().getZ().multiply( Real.NEGATIVE_ONE ) ) < 0 ) {
			closestZ = box.getHalfSize().getZ().multiply( Real.NEGATIVE_ONE );
		}
		
		Vector3D closestPointInLocalCoordinates = new Vector3D( closestX , closestY , closestZ );
		
		//convert to world coordinates
		Vector3D closestPointInWorldCoordinates = box.getBody().convertLocalToWorld( closestPointInLocalCoordinates );
		
		//make sure the point is on the sphere
		Vector3D distanceFromCenterOfSphere = closestPointInWorldCoordinates.subtract( sphere.getPosition() );
		if ( distanceFromCenterOfSphere.magnitudeSquared().compareTo( sphere.getRadius().squared() ) < 0 ) {
			return closestPointInWorldCoordinates;
		} else {
			return null;
		}
	}

}
