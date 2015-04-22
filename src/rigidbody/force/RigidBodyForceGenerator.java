package rigidbody.force;

import rigidbody.RigidBody;
import force.Force;
import force.ForceGenerator;

/**
 * template for <code>ForceGenerator</code>s that apply forces to <code>RigidBody</code> objects
 */
abstract public class RigidBodyForceGenerator extends ForceGenerator < RigidBody > {

	/**
	 * creates a <code>RigidBodyForceGenerator</code> with no defined force
	 */
	public RigidBodyForceGenerator() {
		super();
	}
	
	/**
	 * creates a <code>RigidBodyForceGenerator</code> that applies a constant force
	 * 
	 * @param aForce			the constant force to apply to <code>RigidBodies</code>
	 */
	public RigidBodyForceGenerator( Force aForce ) {
		super( aForce );
	}
}
