package rigidbody.force.spring;

import rigidbody.RigidBody;
import util.ErrorMessages;
import _math.Real;
import _math.Vector3D;

/**
 * a spring with one end attached to a reference <code>RigidBody</code>
 * and the other end attached to some other <code>RigidBody</code> objects
 */
public class RigidBodyUnanchoredSpring extends RigidBodySpring {

	/**
	 * the reference <code>RigidBody</code>
	 */
	protected RigidBody m_reference;
	
	/**
	 * the point at which this spring is connected to the reference
	 */
	protected Vector3D m_referenceConnectionPoint;
	
	/**
	 * creates a <code>RigidBodyUnanchoredSpring</code>
	 * 
	 * @param springConstant					spring constant for this spring
	 * @param restLength						uncompressed and unstretched length of this spring
	 * @param reference							the reference
	 * @param referenceConnectionPoint			the point at which this spring is connected to the referece
	 * @param otherConnectionPoint				the point at which this spring is connected to other <code>RigidBody</code> objects
	 * @throws IllegalArgumentException			if the spring constant or rest length is not positive or if the reference is <code>null</code>
	 */
	public RigidBodyUnanchoredSpring( Real springConstant , Real restLength , RigidBody reference , Vector3D referenceConnectionPoint , Vector3D otherConnectionPoint ) throws IllegalArgumentException {
		super(springConstant, restLength, otherConnectionPoint);
		if ( reference == null ) {
			throw new IllegalArgumentException( ErrorMessages.RigidBody.Spring.INVALID_REFERENCE );
		}
		this.m_reference = reference;
		this.m_referenceConnectionPoint = referenceConnectionPoint;
	}

	/**
	 * @return			the reference of this spring
	 */
	public RigidBody getReference() {
		return this.m_reference;
	}
	
	@Override
	protected Vector3D getStretchVector( RigidBody body ) {
		Vector3D referenceConnectionPoint = getReferenceConnectionPoint();
		Vector3D bodyConnectionPoint = body.getPosition().add( this.m_otherConnectionPoint );
		return bodyConnectionPoint.subtract( referenceConnectionPoint );
	}

	@Override
	public Vector3D getReferenceConnectionPoint() {
		return this.m_reference.getPosition().add( this.m_referenceConnectionPoint );
	}

}
