package rigidbody.collision.generate;

import rigidbody.Matrix3;
import rigidbody.RigidBody;
import util.ErrorMessages;
import _lib.LinkedList;
import _math.Real;
import _math.Vector3D;

/**
 * stores data about a contact between two rigidbodies
 */
public class Contact {

	
	/**
	 * the reference body in this contact
	 */
	private RigidBody m_reference;
	
	/**
	 * the other body involved in this contact
	 */
	private RigidBody m_other;
	
	/**
	 * location of contact in world coordinates
	 */
	private Vector3D m_contactPoint;
	
	/**
	 * the direction of contact in world coordinates
	 */
	private Vector3D m_contactNormal;
	
	/**
	 * amount of penetration between the two rigidbodies that collided
	 */
	private Real m_penetration;
	
	/**
	 * the penetration offset used in pessimistic collision detection. detection
	 * uses slightly bigger shapes to detect collisions before they occur
	 */
	private Real m_penetrationOffset;
	
	/**
	 * amount of friction involved in this contact
	 */
	private Real m_friction;
	
	/**
	 * how elastic this contact, or collision, is
	 */
	private Real m_elasticity;
	
	/**
	 * the transformation from coordinates relative to the point of contact
	 * to world coordinates
	 */
	private Matrix3 m_contactBasis;
	
	/**
	 * constructs a contact with the given data
	 * 
	 * @param reference					the reference body in this contact, which may not be <code>null</code>
	 * @param other						the other body involved in this contact, which may be <code>null</code> if it is
	 * 									a immobile object with infinite mass
	 * @param contactPoint				location of contact in world coordinates
	 * @param contactNormal				direction of contact (vector perpendicular to the contact surface)
	 * @param penetration				amount of penetration between the two bodies that are in contact
	 * @param friction					amount of friction involved in this collision
	 * @param elasticity				how elastic this collision is
	 */
	public Contact( RigidBody reference , RigidBody other , Vector3D contactPoint , Vector3D contactNormal , Real penetration , Real penetrationOffset , Real friction , Real elasticity ) throws IllegalArgumentException {
		if ( reference == null ) {
			throw new IllegalArgumentException( ErrorMessages.RigidBody.Collision.CollisionGenerator.NULL_REFERENCE );
		}
		this.m_reference = reference;
		this.m_other = other;
		//swap bodies if the reference has infinite mass
		if ( this.m_reference.getInverseMass().equals( Real.ZERO ) ) {
			this.m_other = null;
			this.m_reference = other;
			if ( this.m_reference == null ) {
				throw new IllegalArgumentException();
			}
		}
		if ( other != null ) {
			if ( other.getInverseMass().equals( Real.ZERO ) ) {
				this.m_other = null;
			}
		}
		this.m_contactPoint = contactPoint;
		this.m_contactNormal = contactNormal;
		this.m_penetration = penetration.subtract( penetrationOffset );
		this.m_penetrationOffset = penetrationOffset;
		this.m_friction = friction;
		this.m_elasticity = elasticity;
		determineContactBasis();
	}

	/**
	 * generates an arbitrary orthonormal basis for this <code>Contact</code>.
	 * 
	 *  @see			#m_contactBasis
	 */
	public void determineContactBasis() {
		
		//the x axis is the contact normal
		Vector3D xAxis = this.m_contactNormal.normalize();
		
		//the y axis can be arbitrary, so we will let it be either [ 1 , 0 , 0] or
		//[ 0 , 1 , 0 ], depending on the x axis. we will use the axis that is
		//farther from the x axis
		Vector3D yAxis;
		if ( Real.abs( xAxis.getX() ).compareTo( Real.abs( xAxis.getY() ) ) > 0 ) {
			yAxis = Vector3D.WORLD_Y_AXIS;
		} else {
			yAxis = Vector3D.WORLD_X_AXIS;
		}
		
		Vector3D zAxis = Vector3D.makeOrthonormalBasis( xAxis , yAxis );
		this.m_contactBasis = new Matrix3( xAxis , yAxis , zAxis );
	}
	
	/**
	 * determines the total linear velocity of a <code>RigidBody</code> due to
	 * both linear velocity and angular rotation
	 * 
	 * @param body
	 * @return				the total linear velocity of the <code>RigidBody</code> relative
	 * 						to the point of contact (so, not in world coordinates)
	 */
	protected Vector3D calculateLinearVelocity( RigidBody body ) {
		
		//calculate linear velocity due to angular rotation
		Vector3D relativeContactPosition = this.getContactPoint().subtract( body.getPosition() );
		Vector3D linearVelocityFromRotation = body.getAngularVelocity().cross( relativeContactPosition );
		
		//calculate the total linear velocity
		Vector3D linearVelocity = linearVelocityFromRotation.add( body.getVelocity() );
		
		return linearVelocity;
	}
	
	/**
	 * @return			the closing velocity of the two objects in this collision
	 */
	protected Vector3D calculateClosingVelocity() {
		Vector3D referenceVelocity = calculateLinearVelocity( this.m_reference );
		Vector3D closingVelocity;
		if ( this.m_other == null ) {
			closingVelocity = this.m_contactBasis.transform( referenceVelocity );
		} else {
			Vector3D otherVelocity = calculateLinearVelocity( this.m_other );
			closingVelocity = this.m_contactBasis.transform( referenceVelocity.subtract( otherVelocity ) );
		}
		
		//remove velocity in the direction of the contact normal, to avoid penetration
		Vector3D closingVelocityWithoutX = new Vector3D( Real.ZERO , closingVelocity.getY() , closingVelocity.getZ() );
		return closingVelocityWithoutX;
	}
	
	/**
	 * determines the separating velocity of the two object's involved in this collision
	 * 
	 * @return			the closing velocity, in contact coordinates, relative to the point of contact,
	 * 					which is defined by v_1 - v_2, but v_2 is 0 if
	 * 					the other object in this collision has infinite mass (i.e. is a scenery object
	 * 					such as a wall). the velocity will be negative if the objects are getting closer
	 * 					(i.e. about to collide) and be positive if the objects are moving apart
	 */
	protected Vector3D calculateSeparatingVelocity() {
		Vector3D referenceVelocity = calculateLinearVelocity( this.m_reference );
		if ( this.m_other == null ) {
			return referenceVelocity;
		} else {
			return referenceVelocity.subtract( calculateLinearVelocity( this.m_other ) );
		}
	}
	
	final public static Real DEFAULT_MINIMUM_CONTACT_VELOCITY = new Real( 0.01 );
	
	protected Vector3D calculateDesiredVelocityChange( Real duration , Real velocityMinimum ) {
		
		//calculate the velocity due to the acceleration from the last frame
		Vector3D scaledContactNormal = this.m_contactNormal.multiply( duration );
		Vector3D velocityFromLastFrameAcceleration = this.m_reference.getLastFrameAcceleration().multiply( scaledContactNormal );
		if ( this.m_other != null ) {
			velocityFromLastFrameAcceleration = velocityFromLastFrameAcceleration.subtract( this.m_other.getLastFrameAcceleration().multiply( scaledContactNormal ) );
		}
		
		//calculate the closing velocity (i.e. the separating velocity before
		//the collision)
		Vector3D closingVelocity = calculateSeparatingVelocity();
		
		//if the closing velocity is very small, then the two objects should not
		//bounce that much
		Real elasticityToUse = this.m_elasticity;
		if ( closingVelocity.getX().compareTo( velocityMinimum ) < 0 ) {
			elasticityToUse = Real.ZERO;
		}
		
		//apply the formula
		//v_separating = -c * v_closing
		//which is rearranged ( by subtracting v_closing from both sides ) to
		// delta velocity = -v_closing - c * v_closing = -(1+c)*v_closing
		//Vector3D deltaVelocity = closingVelocity.multiply( Real.NEGATIVE_ONE ).multiply( Real.ONE.add( elasticityToUse ) );
		Vector3D deltaVelocity = closingVelocity.multiply( Real.NEGATIVE_ONE ).subtract( elasticityToUse.multiply( closingVelocity.subtract( velocityFromLastFrameAcceleration ) ) );
		return deltaVelocity;
	}

	
	/**
	 * determines the change in velocity each unit of impulse will cause on this
	 * <code>RigidBody</code>
	 * 
	 * @param body
	 * @return				the change in velocity on this <code>RigidBody</code> caused per unit of impulse
	 */
	protected Real calculateDeltaVelocityPerUnitOfImpulse( RigidBody body ) {
		
		//determine the location of contact relative to the center of mass of this body
		Vector3D relativeContactPosition = this.m_contactPoint.subtract( body.getPosition() );
		
		//determine the impulsive torque generated per unit of impulse using the
		//formula torque per unit impulse = r (cross) dp, where r is the vector from the point of
		//contact to the body and dp is the change in momentum vector. in this case, dp has
		//magnitude 1 because the impulse is 1 unit, so dp can be replaced by
		//the direction of the contact normal
		Vector3D impulsiveTorquePerUnitImpulse = relativeContactPosition.cross( this.m_contactNormal );
		
		//determine the change in angular velocity per unit of impulse using the formula
		//delta omega = torque/I, where
		//torque is the impulsive torque calculated in the previous step and
		//I is the moment of inertia. in this case, we transform the torque
		//by the inverse moment of inertia
		Vector3D changeInAngularVelocityPerUnitImpulse = body.getInverseMomentOfInertiaWorld().transform( impulsiveTorquePerUnitImpulse );
		
		//calculate the change in the linear velocity per unit of impulse due to the rotation
		//of the body. the linear velocity is calculated by v = delta omega (dot) contact normal,
		//or v = delta u (cross) r (dot) omega, where u is the angular velocity per unit of impulse ( last step)
		//r is relative contact position (see first step), omega is the change in
		//angular velocity per unit impulse calculated in the last step
		Real changeInLinearVelocityPerUnitImpulseDueToRotation = changeInAngularVelocityPerUnitImpulse.cross( relativeContactPosition ).dot( this.m_contactNormal );
		
		//calculate the change in linear velocity per unit of impulse due to the
		//linear motion of the body. examining J = delta p, where J is impulse and dp
		//p is momentum, we see that we can rewrite as J = m * (delta v), where m is mass,
		//and v is velocity. now, if J = 1 unit, then delta v = 1/m, or the inverse mass
		Real changeInLinearVelocityPerUnitImpulseDueToLinearMotion = body.getInverseMass();
		
		//combine the angular part and linear part to calculate the
		//change in linear velocity per unit impulse
		Real changeInLinearVelocityPerUnitImpulse = changeInLinearVelocityPerUnitImpulseDueToRotation.add( changeInLinearVelocityPerUnitImpulseDueToLinearMotion );
		
		return changeInLinearVelocityPerUnitImpulse;
	}
	
	/**
	 * calculates the impulse required ( in local coordinates ) necessary to achieve
	 * a certain change in velocity
	 * 
	 * @param desiredDeltaVelocity			the desired change in velocity
	 * @return								the impulse required to achieve the desired change in velocity
	 */
	protected Vector3D calculateFrictionlessImpulse( Vector3D desiredDeltaVelocityWorld ) {
		Real deltaVelocityPerUnitImpulse = calculateDeltaVelocityPerUnitOfImpulse( this.m_reference );
		if ( this.m_other != null ) {
			deltaVelocityPerUnitImpulse = deltaVelocityPerUnitImpulse.add( calculateDeltaVelocityPerUnitOfImpulse( this.m_other ) );
		}
		Real desiredDeltaVelocityLocal = desiredDeltaVelocityWorld.dot( this.m_contactNormal );
		
		Real requiredImpulse = desiredDeltaVelocityLocal.divide( deltaVelocityPerUnitImpulse );
		
		//the impulse should be applied along the contact normal, which is the x axis in contact-coordinates
		Vector3D impulse = new Vector3D( requiredImpulse , Real.ZERO , Real.ZERO );
		
		//the impulse to apply should be converted to world coordinates and
		//then returned
		return this.m_contactBasis.transform( impulse );
	}
	
	/**
	 * applies the given impulse to both objects involved in the collision, or
	 * just the reference object if the other object involved in the collision
	 * has infinite mass (i.e. is a scenery object, such as a wall). the formula
	 * applied is
	 * <p>
	 * J = delta p = m * (delta v)
	 * <p>
	 * where J is impulse, p is momentum, m is mass, and v is velocity. rearranged,
	 * we get
	 * <p>
	 * J/m = delta v
	 * 
	 * @param impulse						the impulse to apply
	 * @param possiblyAffectedContacts		the list of contact data that may need to be modified
	 * 										after the given impulse is applied
	 */
	protected void applyImpulse( Vector3D impulse ) {
		applyImpulse( impulse, this.m_reference );
		
		if ( this.m_other != null ) {
			applyImpulse( impulse , this.m_other );
		}
	}
	
	/**
	 * applies the given impulse to the given body
	 * 
	 * @param impulse						impulse to apply
	 * @param body							body to which to apply the impulse
	 * @param possiblyAffectedContacts		list of contact data that may need to be modified
	 * 										after the given impulse is applied
	 */
	protected void applyImpulse( Vector3D impulse , RigidBody body ) {

		Vector3D deltaVelocity = impulse.multiply( body.getInverseMass() );
		if ( body == this.m_reference ) {
			body.setVelocity( body.getVelocity().add( deltaVelocity ) );
		} else if ( body == this.m_other ) {
			body.setVelocity( body.getVelocity().subtract( deltaVelocity ) );
		}
		
		Vector3D relativeContactPosition = this.m_contactPoint.subtract( body.getPosition() );
		Vector3D impulsiveTorque = relativeContactPosition.cross( impulse );
		Vector3D deltaOmega = body.getInverseMomentOfInertiaWorld().transform( impulsiveTorque );
		if ( body == this.m_reference ) {
			body.setAngularVelocity( body.getAngularVelocity().add( deltaOmega ) );
		} else if ( body == this.m_other ) {
			body.setAngularVelocity( body.getAngularVelocity().subtract( deltaOmega ) );
		}
		
		//TODO necessary?
		/*for ( Contact aContact : possiblyAffectedContacts ) {
			if ( aContact != this ) {
				if ( aContact.getReferenceBody() == body || aContact.getOtherBody() == body ) {
					aContact.resolveVelocity( possiblyAffectedContacts );
				}
			}
		}*/
	}
	
	public void resolveVelocity( Real duration ) {
		
		//resolve the velocity
		Vector3D referenceDesiredVelocity = calculateDesiredVelocityChange( duration , DEFAULT_MINIMUM_CONTACT_VELOCITY );
		applyImpulse( calculateFrictionlessImpulse( referenceDesiredVelocity ) );
	}
	
	/**
	 * the default constant that defines how much rotation can be used in nonlinear
	 * projection when resolving penetration
	 */
	final public static Real DEFAULT_ANGULAR_LIMIT_CONSTANT = new Real( 0.5 );
	
	/*/**
	 * resolves penetration between the objects in this contact using
	 * linear projection
	 */
	/*public void resolvePenetrationUsingLinearProjection() {
		
		//if there's no penetration, then we're done
		if ( this.m_penetration.compareTo( Real.ZERO ) >= 0 ) {
			return;
		}
		
		//calculate total inverse mass
		Real totalInverseMass;
		if ( this.m_other == null ) {
			totalInverseMass = this.m_reference.getInverseMass();
		} else {
			totalInverseMass = this.m_reference.getInverseMass().add( this.m_other.getInverseMass() );
		}
		
		//we can't do anything about two objects with infinite mass
		if ( totalInverseMass.equals( Real.ZERO ) ) {
			return;
		}
		
		//calculate how much to move a particle per unit of its inverse mass
		Real movementPerUnitInvMass = this.m_penetration.divide( totalInverseMass );
		
		//convert the magnitude of the movement to a vector by using the contact normal
		Vector3D vectorToMovePerUnitInvMass = this.m_contactNormal.multiply( movementPerUnitInvMass );
		
		//move the reference
		Vector3D referenceMovement = vectorToMovePerUnitInvMass.multiply( this.m_reference.getInverseMass() );
		this.m_reference.setPosition( this.m_reference.getPosition().add( referenceMovement ) );
		
		//move the other particle if it exists - but in the opposite of
		//the direction in which we moved the reference particle
		if ( this.m_other != null ) {
			Vector3D otherMovement = vectorToMovePerUnitInvMass.multiply( this.m_other.getInverseMass() ).multiply( Real.NEGATIVE_ONE );
			this.m_other.setPosition( this.m_other.getPosition().add( otherMovement ) );
		}
	}*/
	
	/**
	 * resolves penetration between two objects in this collision by using nonlinear
	 * projection
	 * 
	 * @param angularRotationLimitConstant			defines the maximum rotation that is allowed
	 * @param possiblyAffectedContacts				a list of other contacts with data that may be affected by resolving
	 * 												the penetration in this <code>Contact</code>
	 * 												in nonlinear projection 
	 */
	public void resolvePenetration( Real angularRotationLimitConstant , LinkedList < Contact > possiblyAffectedContacts ) {
		
		//calculate the total inertia involved in the collision
		Real totalInertiaInCollision = this.m_reference.getMomentOfInertia( this.m_contactNormal , this.m_contactPoint ).add( this.m_reference.getInverseMass() );
		if ( this.m_other != null ) {
			totalInertiaInCollision = totalInertiaInCollision.add( this.m_other.getMomentOfInertia( this.m_contactNormal , this.m_contactPoint ).add( this.m_other.getInverseMass() ) );
		}
		
		//first deal with the reference object in the collision
		resolvePenetration( totalInertiaInCollision , this.m_reference , angularRotationLimitConstant , possiblyAffectedContacts );
		
		//then deal with the other object in the collision if it has finite mass
		//in the same manner
		if ( this.m_other != null ) {
			resolvePenetration( totalInertiaInCollision, this.m_other , angularRotationLimitConstant , possiblyAffectedContacts );
		}
	}
	
	/**
	 * resolves penetration for a single <code>RigidBody</code> in this collision
	 * 
	 * @param totalInertia					the total inertia of both <code>RigidBody</code> objects involved in this collision
	 * @param body							the <code>RigidBody</code> for which to resolve penetration
	 * @param angularRotationLimitConstant	defines the maximum rotation that is allowed
	 * 										in nonlinear projection 
	 */
	protected void resolvePenetration( Real totalInertia , RigidBody body , Real angularRotationLimitConstant , LinkedList < Contact > possiblyAffectedContacts ) {
		Real linearInertia = body.getInverseMass();
		Real linearMovementFactor = this.m_penetration.multiply( linearInertia ).divide( totalInertia );

		//then deal with the orientation and angular inertia
		Vector3D relativeContactPosition = this.m_contactPoint.subtract( body.getPosition() );
		Real angularInertia = body.getInverseMomentOfInertiaWorld().transform( relativeContactPosition.cross( this.m_contactNormal ) ).cross( relativeContactPosition ).dot( this.m_contactNormal );
		Vector3D linearPositionChange = this.m_contactNormal.multiply( linearMovementFactor );
		
		//determine by how many units of motion to rotate
		Real angularMovementFactor = this.m_penetration.multiply( angularInertia ).divide( totalInertia );
		
		//check that the angular rotation factor is not too great
		Real rotationLimit = angularRotationLimitConstant.multiply( relativeContactPosition.magnitude() );
		if ( Real.abs( angularMovementFactor ).compareTo( rotationLimit ) > 0 ) {
			Real linearMovementToAdd = angularMovementFactor.subtract( rotationLimit );
			angularMovementFactor = rotationLimit.multiply( Real.sgn( angularMovementFactor ) );
			linearMovementFactor = linearMovementFactor.add( linearMovementToAdd );
		}
		
		//determine the direction of rotation
		Vector3D relativeRotationDirection = relativeContactPosition.cross( this.m_contactNormal );
		Vector3D worldRotationDirection = body.getInverseMomentOfInertiaWorld().transform( relativeRotationDirection );
		
		//determine rotation per unit of motion
		Vector3D rotationPerUnitMotion = worldRotationDirection.divide( angularInertia );
		
		//finally, obtain the amount by which to rotate the reference object
		Vector3D rotation = rotationPerUnitMotion.multiply( angularMovementFactor );
		
		//move the object in the collision
		if ( body == this.m_reference ) {
			body.setPosition( body.getPosition().add( linearPositionChange ) );
			
			//don't rotate if no rotation should be done
			if ( !angularMovementFactor.equals( Real.ZERO ) ) {
				body.setOrientation( body.getOrientation().add( rotation ) );
			}
		} else if ( body == this.m_other ) {
			body.setPosition( body.getPosition().subtract( linearPositionChange ) );
			
			//don't rotate if no rotation is necessary
			if ( !angularMovementFactor.equals( Real.ZERO ) ) {
				body.setOrientation( body.getOrientation().subtract( rotation ) );
			}
		}
		
		//check the other contacts and see if any other contacts have changed due to this penetration resolution
		Vector3D velocityFactor = linearPositionChange;
		for ( Contact aContact : possiblyAffectedContacts ) {
			if ( aContact != this ) {
				Vector3D otherContactRelativeContactPosition = aContact.getContactPoint().subtract( body.getPosition() );
				Vector3D rotationFactor = rotation.cross( otherContactRelativeContactPosition );
				Vector3D changeInPosition = rotationFactor.add( velocityFactor );
				Real changeInPenetration = changeInPosition.dot( aContact.getContactNormal() );
					
				if ( aContact.getReferenceBody() == body ) {
					aContact.setPenetration( aContact.getPenetration().add( changeInPenetration ) );
				} else if ( aContact.getOtherBody() == body ){
					aContact.setPenetration( aContact.getPenetration().subtract( changeInPenetration ) );
				}
			}
		}
	}//*/
	
	/**
	 * wakes up any sleeping <code>RigidBody</code> objects in this
	 * <code>Contact</code>, if necessary
	 */
	public void wakeUpObjects() {
		
		//collisions with scenery never wake up objects
		if ( this.m_other == null ) {
			return;
		}
		
		boolean isReferenceAwake = this.m_reference.isAwake();
		boolean isOtherAwake = this.m_other.isAwake();
		if ( isReferenceAwake ^ isOtherAwake ) {
			if ( isReferenceAwake ) {
				this.m_other.setAwake();
			} else {
				this.m_reference.setAwake();
			}
		}
	}
	
	/**
	 * @return			the reference body for this collision
	 */
	public RigidBody getReferenceBody() {
		return this.m_reference;
	}
	
	/**
	 * @return			the other body involved in this collision
	 */
	public RigidBody getOtherBody() {
		return this.m_other;
	}
	
	/**
	 * @return			the location of contact in world coordinates
	 */
	public Vector3D getContactPoint() {
		return this.m_contactPoint;
	}
	
	/**
	 * @return			the direction of contact (the vector perpendicular to the contact surface)
	 */
	public Vector3D getContactNormal() {
		return this.m_contactNormal;
	}
	
	/**
	 * @return			the amount of penetration between the two bodies involved in this contact
	 */
	public Real getPenetration() {
		return this.m_penetration;
	}
	
	/**
	 * modifies the penetration of the two objects in this <code>Contact</code>. used
	 * during penetration resolution
	 * 
	 * @param modifiedPenetration		the new penetration between the two <code>RigidBody</code> objects
	 * 									in the collision
	 */
	protected void setPenetration( Real modifiedPenetration ) {
		this.m_penetration = modifiedPenetration.subtract( this.m_penetrationOffset );
	}
	
	/**
	 * @return			amount of friction in this collision
	 */
	public Real getFriction() {
		return this.m_friction;
	}
	
	/**
	 * @return			how elastic this collision is
	 */
	public Real getElasticity() {
		return this.m_elasticity;
	}

}
