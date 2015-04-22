package rigidbody.force.spring;

import rigidbody.RigidBody;
import rigidbody.force.RigidBodyForceGenerator;
import util.ErrorMessages;
import _math.Real;
import _math.Vector3D;

abstract public class RigidBodySpring extends RigidBodyForceGenerator {

	/**
	 * spring constant for this <code>RigidBodySpring</code>
	 */
	protected Real m_springConstant;
	
	/**
	 * the unstretched, uncompressed length of this <code>RigidBodySpring</code>
	 */
	protected Real m_restLength;
	
	/**
	 * the point at which this spring is connected to the <code>RigidBody</code>
	 * objects receiving a spring force in local coordinates relative to the object
	 */
	protected Vector3D m_otherConnectionPoint;
	
	/**
	 * creates a <code>RigidBodySpring</code> with the given spring constant and
	 * uncompressed/unstretched length
	 * 
	 * @param springConstant
	 * @param restLength
	 * @throws IllegalArgumentException			if the spring constant or rest length is not positive
	 */
	public RigidBodySpring( Real springConstant , Real restLength , Vector3D otherConnectionPoint ) throws IllegalArgumentException {
		setSpringConstant( springConstant );
		setRestLength( restLength );
		this.m_otherConnectionPoint = otherConnectionPoint;
	}
	
	/**
	 * sets the spring constant for this <code>RigidBodySpring</code>
	 * 
	 * @param newSpringConstant				a spring constant
	 * @throws IllegalArgumentException		if the given spring constant is not positive
	 */
	public void setSpringConstant( Real newSpringConstant ) throws IllegalArgumentException {
		if ( newSpringConstant.compareTo( Real.ZERO ) > 0 ) {
			this.m_springConstant = newSpringConstant;
		} else {
			throw new IllegalArgumentException( ErrorMessages.RigidBody.Spring.INVALID_SPRING_CONSTANT );
		}
	}
	
	/**
	 * @return			the spring constant of this <code>RigidBodySpring</code>
	 */
	public Real getSpringConstant() {
		return this.m_springConstant;
	}
	
	public void setRestLength( Real newRestLength ) throws IllegalArgumentException {
		if ( newRestLength.compareTo( Real.ZERO ) > 0 ) {
			this.m_restLength = newRestLength;
		} else {
			throw new IllegalArgumentException( ErrorMessages.RigidBody.Spring.INVALID_REST_LENGTH );
		}
	}
	
	/**
	 * @return		the unstretched and uncompressed length of this <code>RigidBodySpring</code>
	 */
	public Real getRestLength() {
		return this.m_restLength;
	}
	
	/**
	 * @return		the vector in local coordinates that describes where objects
	 * 				attached to this spring are connected to the spring
	 */
	public Vector3D getConnectionPoint() {
		return this.m_otherConnectionPoint;
	}
	
	@Override
	public void generateForce() {
		
		//go through all the rigidbodies
		for ( RigidBody body : this.m_objects ) {
			
			//calculate spring force on each body
			Vector3D springForce = calculateSpringForce( body );
			
			//apply the force
			body.addForceAtPoint( springForce , body.getPosition().add( this.m_otherConnectionPoint ) );

			//generate a reaction force if the reference is a body
			if ( this instanceof RigidBodyUnanchoredSpring ) {
				RigidBody reference = ( ( RigidBodyUnanchoredSpring ) this ).getReference();
				reference.addForceAtPoint( springForce.multiply( Real.NEGATIVE_ONE ) , reference.getPosition().add( ( ( RigidBodyUnanchoredSpring ) this ).getReferenceConnectionPoint() ) );
			}
		}
	}
	
	/**
	 * calculates the spring force on a particle using the formula
	 * <p>
	 * F = -k * x
	 * </p>
	 * where k is the spring constant and x is the stretch/compress distance
	 * 
	 * @param body			a <code>RigidBody</code>
	 * @return				the force this spring exerts on the given <code>RigidBody</code>
	 */
	protected Vector3D calculateSpringForce( RigidBody body ) {
		
		//determine the vector connecting particle to reference
		Vector3D vectorFromParticleToReference = getStretchVector( body );
		
		//calculate the stretch distance
		Real particleToReferenceDistance = vectorFromParticleToReference.magnitude();
		Real stretchDistance = particleToReferenceDistance.subtract( this.m_restLength );
		
		//calculate magnitude of the force
		Real forceMagnitude = this.m_springConstant.multiply( stretchDistance );
		
		//determine the force vector's direction
		Vector3D forceUnitVector = vectorFromParticleToReference.normalize().invert();
		
		//scale the unit direction vector
		Vector3D forceVector = forceUnitVector.multiply( forceMagnitude );
		
		//return result
		return forceVector;
	}
	
	abstract protected Vector3D getStretchVector( RigidBody body );
	
	abstract public Vector3D getReferenceConnectionPoint();
}
