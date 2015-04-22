package rigidbody.collision.broad;

import rigidbody.RigidBody;

/**
 * represents a possible contact between two <code>RigidBody</code> objects
 */
public class PotentialContact {

	/**
	 * one <code>RigidBody</code> that may be in contact with another object
	 */
	private RigidBody m_body1;
	
	/**
	 * another <code>RigidBody</code> that may be in contact with the first
	 */
	private RigidBody m_body2;
	
	public PotentialContact( RigidBody body1 , RigidBody body2 ) {
		this.m_body1 = body1;
		this.m_body2 = body2;
	}
	
	public RigidBody getBody1() {
		return this.m_body1;
	}
	
	public RigidBody getBody2() {
		return this.m_body2;
	}
}
