package rigidbody.force;

import rigidbody.RigidBody;
import _math.Real;
import _math.Vector3D;

/**
 * gravity generator for <code>RigidBodies</code>
 */
public class RigidBodyGravityGenerator extends RigidBodyForceGenerator {

	public static Vector3D g = new Vector3D( Real.ZERO , new Real( -0.98 ) , Real.ZERO );
	
	public RigidBodyGravityGenerator() {
		super();
	}
	
	@Override
	public void generateForce() {
		for ( RigidBody body : this.m_objects ) {
			
			//only apply forces to objects with finite mass
			if ( !body.getInverseMass().equals( Real.ZERO ) ) {
				body.addForceVector( body.getMass().multiply( g ) );
			}
		}
	}
}
