package rigidbody.force.spring;

import rigidbody.RigidBody;
import _math.Real;
import _math.Vector3D;

/**
 * a spring that is anchored at a specific point at one end
 */
public class RigidBodyAnchoredSpring extends RigidBodySpring {

	protected Vector3D m_anchor;
	
	/**
	 * creates a <code>RigidBodyAnchoredSpring</code> with the given spring constant and
	 * rest length. also specifies the anchor point at which this spring is fixed
	 * 
	 * @param springConstant
	 * @param restLength
	 * @param anchor
	 * @param otherConnectionPoint
	 * @throws IllegalArgumentException				if the spring constant or rest length is not positive
	 */
	public RigidBodyAnchoredSpring( Real springConstant , Real restLength , Vector3D anchor , Vector3D otherConnectionPoint ) throws IllegalArgumentException {
		super(springConstant, restLength, otherConnectionPoint);
		this.m_anchor = anchor;
	}

	@Override
	protected Vector3D getStretchVector(RigidBody body) {
		Vector3D referenceConnectionPoint = this.m_anchor;
		Vector3D bodyConnectionPoint = body.getPosition().add( this.m_otherConnectionPoint );
		return bodyConnectionPoint.subtract( referenceConnectionPoint );
	}

	@Override
	public Vector3D getReferenceConnectionPoint() {
		return this.m_anchor;
	}
	
}
