package rigidbody.force;

import rigidbody.RigidBody;
import _math.Vector3D;
import force.Force;

/**
 * template for torque generators that apply torques to <code>RigidBody</code> objects.
 * these are torque generators that do not apply a force, but rather just cause
 * rotation, as if the center of mass of the object were pinned
 */
public class RigidBodyTorqueGenerator extends RigidBodyForceGenerator {

	/**
	 * the torque to apply to <code>RigidBody</code> objects
	 */
	private Vector3D m_torque;
	
	/**
	 * creates a <code>RigidBodyTorqueGenerator</code> with an undefined torque
	 */
	public RigidBodyTorqueGenerator() {
		super( new Force( Vector3D.ZERO ) );
	}
	
	/**
	 * creates a <code>RigidBodyTorqueGenerator</code> that applies a constant
	 * torque
	 */
	public RigidBodyTorqueGenerator( Vector3D torque ) {
		super( new Force( Vector3D.ZERO ) );
		this.m_torque = torque;
	}
	
	/**
	 * @return		the torque this <code>RigidBodyTorqueGenerator</code> applies
	 */
	public Vector3D getTorqueVector() {
		return this.m_torque;
	}
	
	@Override
	public void generateForce() {
		for ( RigidBody body : this.m_objects ) {
			body.addTorqueVector( this.m_torque );
		}
	}
}
