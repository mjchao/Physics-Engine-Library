package rigidbody.collision.generate;

import util.ErrorMessages;
import _lib.LinkedList;
import _math.Real;
import _math.Vector3D;

public class BoxAndBoxCollisionGenerator extends ContactGenerator {

	public BoxAndBoxCollisionGenerator( Real friction , Real elasticity , Real penetrationOffset ) {
		super( friction , elasticity , penetrationOffset );
	}
	
	/**
	 * @throws IllegalArgumentException			if <code>p1</code> or <code>p2</code> is not a <code>PrimitiveBox</code>
	 * @see										PrimitiveBox
	 */
	@Override
	public LinkedList < Contact > generateContacts( Primitive p1 , Primitive p2 ) throws IllegalArgumentException {

		//make sure we are dealing with the correct type of primitive
		if ( p1 instanceof PrimitiveBox ) {
			if ( p2 instanceof PrimitiveBox ) {
				PrimitiveBox box1 = ( PrimitiveBox ) p1;
				box1.determineVertices();
				PrimitiveBox box2 = ( PrimitiveBox ) p2;
				box2.determineVertices();
				
				LinkedList < Contact > contacts = new LinkedList < Contact > ();
				Contact boxToBoxContact = determineContact( box1 , box2 );
				if ( boxToBoxContact != null ) {
					contacts.add( boxToBoxContact );
				}
				return contacts;
			} else {
				throw new IllegalArgumentException( ErrorMessages.RigidBody.Collision.CollisionGenerator.INVALID_PRIMITIVE_PARAMETER( PrimitiveBox.class.getName() , p2.getClass().getName() ) );
			}
		} else {
			throw new IllegalArgumentException( ErrorMessages.RigidBody.Collision.CollisionGenerator.INVALID_PRIMITIVE_PARAMETER( PrimitiveBox.class.getName() , p1.getClass().getName() ) );
		}
	}

	/**
	 * Uses the separating axis test to determine the axis of the box on which
	 * a collision between two boxes occurred (or determines that no collision occurred). then
	 * determines the contact data between the two boxes
	 * 
	 * @param box1		the first box involved in a possible collision
	 * @param box2		another box involved in a possible collision
	 * @return			the contact data for the collision that occurred, or <code>null</code> if no collision occurred
	 */
	public Contact determineContact( PrimitiveBox box1 , PrimitiveBox box2 ) {
	
		//use a greedy algorithm to determine which axis has the least overlap
		Real bestOverlap = Real.MAX_VALUE;
		Vector3D bestAxis = null;
		
		//determines if the contact is vertex-face (true) or edge-edge (false)
		boolean isVertexFaceContact = false;
		
		//if the contact is vertex-face, this determines if the face belongs to box 1 (true)
		//or box2 (false)
		boolean isFaceFromBox1 = false;
		
		//check the face axes for the first box
		Vector3D[] firstBoxEdges = { box1.getXAxis() , box1.getYAxis() , box1.getZAxis() };
		Vector3D centerToCenterVector = box1.getPosition().subtract( box2.getPosition() );
		for ( Vector3D axis : firstBoxEdges ) {
			Real axisOverlap = determineOverlapOnAxis( box1 , box2 , axis , centerToCenterVector );
			
			//if overlap is negative, then there can be no collision
			if ( axisOverlap.compareTo( Real.ZERO ) < 0 ) {
				return null;
			} else {
				
				//save the axis with the smallest overlap
				if ( axisOverlap.compareTo( bestOverlap ) < 0 ) {
					bestOverlap = axisOverlap;
					bestAxis = axis;
					isVertexFaceContact = true;
				}
			}
		}

		//check the face axes for the second box
		Vector3D[] secondBoxEdges = { box2.getXAxis() , box2.getYAxis() , box2.getZAxis() };
		for ( Vector3D axis : secondBoxEdges ) {
			Real axisOverlap = determineOverlapOnAxis( box1 , box2 , axis , centerToCenterVector );
			
			//if overlap is negative, then there can be no collision
			if ( axisOverlap.compareTo( Real.ZERO ) < 0 ) {
				return null;
			} else {
				
				//save the axis with the smallest overlap
				if ( axisOverlap.compareTo( bestOverlap ) < 0 ) {
					bestOverlap = axisOverlap;
					bestAxis = axis;
					isVertexFaceContact = true;
					isFaceFromBox1 = true;
				}
			}
		}
		
		//check edge-edge axes
		Vector3D bestFirstBoxEdge = null;
		Vector3D bestSecondBoxEdge = null;
		for ( Vector3D firstBoxEdge : firstBoxEdges ) {
			for ( Vector3D secondBoxEdge : secondBoxEdges ) {
				
				//determine the axis by using the vector perpendicular to both axes
				Vector3D axis = firstBoxEdge.cross( secondBoxEdge );
				
				//if the two edges were approximately parallel, then ignore them
				//because we've already checked them in the face axes calculations
				if ( axis.magnitudeSquared().compareTo( Real.ONE_THOUSANDTH ) < 0 ) {
					
					//continue
					
				//otherwise, determine the overlap
				} else {
					Real axisOverlap = determineOverlapOnAxis( box1 , box2 , axis.normalize() , centerToCenterVector );
					//if overlap is negative, then the boxes cannot be in contact
					if ( axisOverlap.compareTo( Real.ZERO ) < 0 ) {
						return null;
					} else {
						
						//save the axis with the smallest overlap
						if ( axisOverlap.compareTo( bestOverlap ) < 0 ) {
							bestOverlap = axisOverlap;
							bestAxis = axis;
							isVertexFaceContact = false;
							isFaceFromBox1 = false;
							bestFirstBoxEdge = firstBoxEdge;
							bestSecondBoxEdge = secondBoxEdge;
						}
					}
				}
			}
		}
	
		//determine if the axis of contact was on a face or on an edge
		//if it was on a face, then the contact is between a vertex and a face
		if ( isVertexFaceContact ) {
			
			//determine the face in the contact
			if ( isFaceFromBox1 ) {
				return createVertexFaceContactData( box1 , box2 , bestAxis , centerToCenterVector , bestOverlap );
			} else {
				return createVertexFaceContactData( box2 , box1 , bestAxis , centerToCenterVector , bestOverlap );
			}
			
		//if the axis of contact was on an edge, then the contact is between
		//an edge and an edge
		} else {
			return createEdgeEdgeContactData( box1 , bestFirstBoxEdge , box2 , bestSecondBoxEdge , bestAxis , bestOverlap );
		}
	}
	
	/**
	 * determines the overlap on the given axis between the two given boxes.
	 * 
	 * @param box1						a <code>PrimitiveBox</code>
	 * @param box2						another <code>PrimitiveBox</code>
	 * @param axis						the axis for which to determine the overlap
	 * @param centerToCenterVector		the precomputed vector from the first box's center to the second box's center
	 * @return							the amount of overlap on the given axis between the two boxes. positive overlap means the boxes are overlapping 
	 * 									and negative overlap means the boxes are not overlapping on the given axis
	 */
	public static Real determineOverlapOnAxis( PrimitiveBox box1 , PrimitiveBox box2 , Vector3D axis , Vector3D centerToCenterVector ) {
		Real box1ProjectionLength = box1.transformToAxis( axis );
		Real box2ProjectionLength = box2.transformToAxis( axis );
		Real box1AndBox2ProjectionLength = Real.abs( centerToCenterVector.dot( axis ) );
		
		//the overlap is the sum of the individual projections minus the
		//sum of the projections of both boxes together
		return box1ProjectionLength.add( box2ProjectionLength ).subtract( box1AndBox2ProjectionLength );
	}
	
	/**
	 * creates a <code>Contact</code> object with the data describing the contact
	 * between box1 and box2
	 * 
	 * @param box1					the box with a face in contact
	 * @param box2					the box with a vertex in contact
	 * @param axisOfContact			the axis along which the contact occurrs
	 * @param centerToCenterVector	the precomputed vector from one <code>box1</code>'s center to <code>box2</code>'s center
	 * @param penetration			the amount of penetration
	 * @return						the contact data describing the collision between <code>box1</code> and <code>box2</code>
	 */
	protected Contact createVertexFaceContactData( PrimitiveBox box1 , PrimitiveBox box2 , Vector3D axisOfContact , Vector3D centerToCenterVector , Real penetration ) {
		
		//determine which face is in contact: the one facing in the same direction
		//or the one facing in the opposite direction of the axis of contact 
		Vector3D contactNormal;
		if ( axisOfContact.dot( centerToCenterVector ).compareTo( Real.ZERO ) > 0 ) {
			contactNormal = axisOfContact.multiply( Real.NEGATIVE_ONE );
		} else {
			contactNormal = axisOfContact;
		}
		
		//determine the vertex in contact in the box's local coordinates
		Real vertexX;
		if ( box2.getXAxis().dot( contactNormal ).compareTo( Real.ZERO ) < 0 ) {
			vertexX = box2.getHalfSize().getX().multiply( Real.NEGATIVE_ONE );
		} else {
			vertexX = box2.getHalfSize().getX();
		}
		
		Real vertexY;
		if ( box2.getYAxis().dot( contactNormal ).compareTo( Real.ZERO ) < 0 ) {
			vertexY = box2.getHalfSize().getY().multiply( Real.NEGATIVE_ONE );
		} else {
			vertexY = box2.getHalfSize().getY();
		}
		
		Real vertexZ;
		if ( box2.getZAxis().dot( contactNormal ).compareTo( Real.ZERO ) < 0 ) {
			vertexZ = box2.getHalfSize().getZ().multiply( Real.NEGATIVE_ONE );
		} else {
			vertexZ = box2.getHalfSize().getZ();
		}
		
		Vector3D vertex = new Vector3D( vertexX , vertexY , vertexZ );
		Vector3D vertexInWorldCoordinates = box2.getBody().getOrientation().toOrientationAndPositionMatrix( box2.getPosition() ).convertLocalToWorld( vertex );
		
		//create the contact data
		Contact contact = new Contact( box1.getBody() , box2.getBody() , contactNormal , vertexInWorldCoordinates , penetration , this.getPenetrationOffset() , this.getFriction() , this.getElasticity() );
		return contact;
	}
	
	//TODO test edge edge collision
	private Contact createEdgeEdgeContactData( PrimitiveBox box1 , Vector3D axis1 , PrimitiveBox box2 , Vector3D axis2 , Vector3D axisOfContact , Real penetration ) {
		Vector3D pointOnEdge1 = box1.getHalfSize();
		Vector3D pointOnEdge2 = box2.getHalfSize();
		
		//TODO find better way to do this ( pg 325 in book )
		if ( !axisOfContact.getX().equals( Real.ZERO ) ) {
			if ( axisOfContact.getX().compareTo( Real.ZERO ) > 0 ) {
				pointOnEdge1 = new Vector3D( Real.ZERO , pointOnEdge1.getY() , Real.ZERO );
				pointOnEdge2 = new Vector3D( Real.ZERO , Real.ZERO , pointOnEdge2.getZ() );
			} else {
				pointOnEdge1 = new Vector3D( Real.ZERO , Real.ZERO , pointOnEdge1.getZ() );
				pointOnEdge2 = new Vector3D( Real.ZERO , pointOnEdge2.getY() , Real.ZERO );
			}
		} else if ( !axisOfContact.getY().equals( Real.ZERO ) ) {
			if ( axisOfContact.getY().compareTo( Real.ZERO ) > 0 ) {
				pointOnEdge1 = new Vector3D( Real.ZERO , Real.ZERO , pointOnEdge1.getZ() );
				pointOnEdge2 = new Vector3D( pointOnEdge2.getX() , Real.ZERO , Real.ZERO );
			} else {
				pointOnEdge1 = new Vector3D( pointOnEdge1.getX() , Real.ZERO , Real.ZERO );
				pointOnEdge2 = new Vector3D( Real.ZERO , Real.ZERO , pointOnEdge2.getZ() );
			}
		} else if ( !axisOfContact.getZ().equals( Real.ZERO ) ) {
			if ( axisOfContact.getZ().compareTo( Real.ZERO ) > 0 ) {
				pointOnEdge1 = new Vector3D( pointOnEdge1.getX() , Real.ZERO , Real.ZERO );
				pointOnEdge2 = new Vector3D( Real.ZERO , pointOnEdge2.getY() , Real.ZERO );
			} else {
				pointOnEdge1 = new Vector3D( Real.ZERO , pointOnEdge1.getY() , Real.ZERO );
				pointOnEdge2 = new Vector3D( pointOnEdge2.getX() , Real.ZERO , Real.ZERO );
			}
		}
		
		pointOnEdge1 = box1.getBody().getOrientation().toOrientationAndPositionMatrix( box1.getPosition() ).convertLocalToWorld( pointOnEdge1 );
		pointOnEdge2 = box2.getBody().getOrientation().toOrientationAndPositionMatrix( box2.getPosition() ).convertLocalToWorld( pointOnEdge2 );
		
		Vector3D contactPoint = determineContactPoint( axis1 , pointOnEdge1 , axis2 , pointOnEdge2 );
		
		//create the contact
		Contact contact = new Contact( box1.getBody() , box2.getBody() , axisOfContact , contactPoint , penetration , this.getPenetrationOffset() , this.getFriction() , this.getElasticity() );
		return contact;
	}
	
	//TODO figure out what this does ( pg 326 )
	/**
	 * determines the edge-edge contact point between two boxes given directions
	 * of both edges and points on both edges
	 * 
	 * @param axis1			direction of the edge on box 1
	 * @param point1		point on the edge of box 1
	 * @param axis2			direction of the edge on box 2
	 * @param point2		point on the edge of box 2
	 */
	private static Vector3D determineContactPoint( Vector3D axis1 , Vector3D point1 , Vector3D axis2 , Vector3D point2 ) {
		Vector3D pointToPointVector = point1.subtract( point2 );
		Real axisOneLength = axis1.dot( pointToPointVector );
		Real axisTwoLength = axis2.dot( pointToPointVector );
		
		Real axisOneSquareMag = axis1.magnitudeSquared();
		Real axisTwoSquareMag = axis2.magnitudeSquared();
		Real axisDotProduct = axis1.dot( axis2 );
		
		Real denom = axisOneSquareMag.multiply( axisTwoSquareMag ).subtract( axisDotProduct.multiply( axisDotProduct ) );
		Real axis1Scale = axisDotProduct.multiply( axisTwoLength ).subtract( axisTwoSquareMag.multiply( axisTwoLength ) ).divide( denom );
		Real axis2Scale = axisOneSquareMag.multiply( axisTwoLength ).subtract( axisDotProduct.multiply( axisOneLength ) );
		
		Vector3D nearestPoint1 = point1.add( axis1.multiply( axis1Scale ) );
		Vector3D nearestPoint2 = point2.add( axis2.multiply( axis2Scale ) );
		return nearestPoint1.multiply( Real.ONE_HALF ).add( nearestPoint2.multiply( Real.ONE_HALF ) );
	}
}
