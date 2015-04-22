package rigidbody.collision.generate;

import rigidbody.RigidBody;
import util.ErrorMessages;
import _lib.LinkedList;
import _math.Real;
import _math.Vector3D;

public class BoxAndPlaneCollisionGenerator extends ContactGenerator {

	public BoxAndPlaneCollisionGenerator( Real friction , Real elasticity , Real penetrationOffset ) {
		super( friction , elasticity , penetrationOffset );
	}

	/**
	 * returns all the contacts between the vertices of a box and a plane
	 * there will be a minimum of zero and a maximum of four vertices in contact
	 * with the plane
	 * 
	 * @throws IllegalArgumentException			if <code>p1</code> is not a <code>PrimtiveBox</code> or <code>p2</code> is not a <code>PrimitivePlane</code>
	 * @see										PrimtiveBox
	 * @see										PrimitvePlane
	 */
	@Override
	public LinkedList < Contact > generateContacts( Primitive p1 , Primitive p2 ) throws IllegalArgumentException {
		
		//make sure we are given the proper parameters
		if ( p1 instanceof PrimitiveBox ) {
			if ( p2 instanceof PrimitivePlane ) {
				PrimitiveBox box = ( PrimitiveBox ) p1;
				box.determineVertices();
				PrimitivePlane plane = ( PrimitivePlane ) p2;
				LinkedList < Contact > contacts = new LinkedList < Contact > ();
				
				//go through each vertex of the box
				for ( Vector3D vertex : box.getVertices() ) {
					
					//check if it is in contact with the plane
					Contact vertexPlaneContact = generateVertexContact( box.getBody() , vertex , plane );
					
					//and if it is, add it to the list of contacts
					if ( vertexPlaneContact != null ) {
						contacts.add( vertexPlaneContact );
					}
				}
				return contacts;
			} else {
				throw new IllegalArgumentException( ErrorMessages.RigidBody.Collision.CollisionGenerator.INVALID_PRIMITIVE_PARAMETER( PrimitivePlane.class.getName() , p2.getClass().getName() ) );
			}
		} else {
			throw new IllegalArgumentException( ErrorMessages.RigidBody.Collision.CollisionGenerator.INVALID_PRIMITIVE_PARAMETER( PrimitiveBox.class.getName() , p1.getClass().getName() ) );
		}
	}
	
	/**
	 * generates a contact between a vertex of a box and a plane.
	 * 
	 * @param box				the box
	 * @param vertexPosition	the position of the vertex of a box
	 * @param plane				the plane
	 * @return					the contact between the vertex of a box and a plane, or <code>null</code> if
	 * 							the vertex is not in contact with the plane
	 */
	protected Contact generateVertexContact( RigidBody box , Vector3D vertexPosition , PrimitivePlane plane ) {
		
		//determine the distance of the vertex to the plane
		Real vertexToPlaneDistance = ContactGenerator.pointToPlaneDistance( vertexPosition , plane );
		
		//if the distance of the vertex to the plane is negative, then there is a contact
		if ( vertexToPlaneDistance.compareTo( Real.ZERO ) <= 0 ) {
			
			//determine the location of the contact 
			Vector3D contactPoint = plane.getNormal().multiply( vertexToPlaneDistance.subtract( plane.getDistanceFromOrigin() ) ).add( vertexPosition );
			
			Vector3D contactNormal = plane.getNormal();
			Real penetration = vertexToPlaneDistance.multiply( Real.NEGATIVE_ONE );
			
			return new Contact( box , null , contactPoint , contactNormal , penetration , this.getPenetrationOffset() , this.getFriction() , this.getElasticity() );
			
		//otherwise, if the distance of vertex to plane is positive, there is no contact
		} else {
			return null;
		}
	}

}
