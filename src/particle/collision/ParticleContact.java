package particle.collision;

import particle.Particle;
import util.ErrorMessages;
import _math.Real;
import _math.Vector3D;

/**
 * stores a pair of <code>Particle</code>s in contact with each other
 */
public class ParticleContact {

	/**
	 * the reference <code>Particle</code>. the collision is viewed from the
	 * perspective of the reference <code>Particle</code>
	 */
	private Particle m_reference;
	
	/**
	 * the other <code>Particle</code> involved in the collision. <code>null</code>
	 * if the collision is with scenery, e.g a wall.
	 */
	private Particle m_other;
	
	/**
	 * how much the objects stick together or bounce apart. 0 means they stick together
	 * completely, 1 means they collide elasitcally
	 */
	private Real m_elasticity;
	
	/**
	 * the unit <code>Vector3D</code> that is perpendicular to the surface of contact. it
	 * is the direction of contact
	 */
	private Vector3D m_contactNormal;
	
	/**
	 * magnitude of the penetration of the two <code>Particle</code>s involved
	 * in the collision
	 */
	private Real m_penetration;
	
	/**
	 * creates a <code>ParticleContact</code> which stores information about a collision
	 * between two <code>Particle</code>s.
	 * 
	 * @param reference						<code>Particle</code> serving as the reference for the collision
	 * @param other							the other <code>Particle</code> involved in the collision
	 * @param elasticity					how much the two <code>Particle</code>s stick or bounce apart after the collision
	 * @param contactNormal					the <code>Vector3D</code> that shows the direction of contact
	 * @param penetration					how deeply one <code>Particle</code> has entered the other <code>Particle</code>
	 * @throws IllegalArgumentException		if the reference does not exist
	 */
	public ParticleContact( Particle reference , Particle other , Real elasticity , Vector3D contactNormal , Real penetration ) {
		
		//make sure the reference exists
		if ( reference == null ) {
			throw new IllegalArgumentException( ErrorMessages.Particle.COLLISION.INVALID_REFERENCE );
		}
		this.m_reference = reference;
		this.m_other = other;
		this.m_elasticity = elasticity;
		this.m_contactNormal = contactNormal;
		this.m_penetration = penetration;
	}
	
	/**
	 * @return 		magnitude of the penetration of the two <code>Particle</code>s involved
	 * 				in the collision
	 */
	public Real getPenetration() {
		return this.m_penetration;
	}
	
	/**
	 * determines what happens to the two <code>Particle</code>s during the collision
	 * and updates them appropriately
	 * 
	 * @param duration			the relevant length of time for which to resolve collisions
	 */
	public void resolve( Real duration ) {
		resolveVelocity( duration );
		resolvePenetration();
	}
	
	/**
	 * determines the separating velocity of the two <code>Particle</code>s. it is 
	 * calculated by the formula
	 * <p>
	 * v_s = ( v_ref - v_other ) (dot) contact_normal
	 * <p>
	 * where v represents velocity. two particles with a positive separating velocity
	 * are getting farther away. two particles with zero separating velocity are not getting
	 * closer or farther. two particles with negative separating velocity
	 * are getting closer ( possibly going to collide )
	 */
	public Real calculateSeparatingVelocity() {
		Vector3D relativeVelocity;
		
		//if the other Particle in the collision is a scenery object,
		//then it has not velocity, so the relative velocity is just the reference
		//object's velocity
		if ( this.m_other == null ) {
			relativeVelocity = this.m_reference.getVelocity();
			
		//if the other Particle in the collision is a Particle,
		//then the relative velocity is the difference of
		//the reference Particle's velocity and the other Particle's velocity
		} else {
			relativeVelocity = this.m_reference.getVelocity().subtract( this.m_other.getVelocity() );
		}
		
		Real separatingVelocity = relativeVelocity.dot( this.m_contactNormal );
		return separatingVelocity;
	}
	
	/**
	 * deals with the velocities and impulses of two <code>Particles</code>
	 * and any collisions that may occur between them
	 * 
	 * @param duration				the relevant length of time for which collision processing
	 * 								should occur
	 */
	private void resolveVelocity( Real duration ) {
		
		//get the separating velocity before the collision
		Real separatingVelocity = calculateSeparatingVelocity();
		
		//if the two particles are getting farther apart,
		if ( separatingVelocity.compareTo( Real.ZERO ) > 0 ) {
			
			//then there is no need to deal with them anymore
			return;
			
		//if the two particles are getting closer together or
		} else {
			
			//calculate a separating velocity after the collision
			//it is the negative of the separating velocity before colliding
			//multiplied by the elasticity
			Real afterCollisionSeparatingVelocity = separatingVelocity.multiply( Real.NEGATIVE_ONE ).multiply( this.m_elasticity );
			
			//check velocity increase due to acceleration
			Vector3D velocityFromAcceleration;
			if ( this.m_other == null ) {
				velocityFromAcceleration = this.m_reference.getAcceleration();
			} else {
				velocityFromAcceleration = this.m_reference.getAcceleration().subtract( this.m_other.getAcceleration() );
			}
			
			//calculate the separating velocity increase due to acceleration
			//by taking the dot product and multiplying by the duration
			Real separatingVelocityFromAcceleration = velocityFromAcceleration.dot( this.m_contactNormal ).multiply( duration );
		
			//if the acceleration caused a closing velocity to occur, it needs to
			//be removed
			if ( separatingVelocityFromAcceleration.compareTo( Real.ZERO ) < 0 ) {
				afterCollisionSeparatingVelocity = afterCollisionSeparatingVelocity.add( this.m_elasticity.multiply( separatingVelocityFromAcceleration ) );
				
				//but make sure we don't remove more than there is to remove
				if ( afterCollisionSeparatingVelocity.compareTo( Real.ZERO ) < 0 ) {
					afterCollisionSeparatingVelocity = Real.ZERO;
				}
			}
			
			//calculate a change in velocity
			Real deltaVelocity = afterCollisionSeparatingVelocity.subtract( separatingVelocity );
			
			//momentum is conserved in a collision. p = mv so
			//v = p/m so the after collision velocity is proportional to the
			//inverse mass
			
			//distribute the velocity to the objects in the collision proportionally
			//to inverse mass
			Real totalInverseMass;
			if ( this.m_other == null ) {
				
				//if the other object in the collision has infinite mass,
				//its inverse mass is zero, so do not add it. only add
				//the inverse mass of the reference particle
				totalInverseMass = this.m_reference.getInverseMass();
				
			//if both objects exist, add their masses
			} else {
				
				//add their inverse masses together to find the
				//total inverse mass
				totalInverseMass = this.m_reference.getInverseMass().add( this.m_other.getInverseMass() );
			}
			
			//if the inverse mass is infinite, nothing moves, so we don't
			//have to do anything else
			if ( totalInverseMass.compareTo( Real.ZERO ) <= 0 ) {
				return;
			}
			
			//determine the net impulse that would be applied to the two
			//particles in the collision
			//J = m*dv or dv/( inverse mass )
			Real netImpulse = deltaVelocity.divide( totalInverseMass );
			
			//convert the impulse magnitude to a vector by scaling
			//the contact normal
			Vector3D netImpulseVector = netImpulse.multiply( this.m_contactNormal );
			
			//apply impulse to the reference particle
			Vector3D newReferenceParticleVelocity = this.m_reference.getVelocity().add( netImpulseVector.multiply( this.m_reference.getInverseMass() ) );
			this.m_reference.setVelocity( newReferenceParticleVelocity );
			
			//apply impulse to the other particle if it is not a scenery object
			//however, the direction of the impulse must be in
			//the opposite direction of the impulse applied to the reference object
			if ( this.m_other != null ) {
				Vector3D newOtherParticleVelocity = this.m_other.getVelocity().add( netImpulseVector.multiply( this.m_other.getInverseMass() ).multiply( Real.NEGATIVE_ONE ) );
				this.m_other.setVelocity( newOtherParticleVelocity );
			}
		}
	}

	/**
	 * deals with objects entering other objects, which is not avoidable in
	 * this type of collision detector. penetration is resolved by moving two
	 * penetrating objects apart proportionally to their inverse mass.
	 */
	private void resolvePenetration() {
		
		//if there is no penetration, we are done
		if ( this.m_penetration.compareTo( Real.ZERO ) <= 0 ) {
			return;
		}
		
		//penetration is resolved by moving two objects apart proportionally
		//to inverse mass
		
		//calculate the total mass
		Real totalInverseMass;
		if ( this.m_other == null ) {
			totalInverseMass = this.m_reference.getInverseMass();
		} else {
			totalInverseMass = this.m_reference.getInverseMass().add( this.m_other.getInverseMass() );
		}
		
		//if both particles have infinite mass, we cannot do anything about the
		//penetration
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
	}
}
