package rigidbody.collision.generate;

import rigidbody.Matrix4;
import rigidbody.RigidBody;
import _math.Real;
import _math.Vector3D;

/**
 * a sphere that bounds a piece of a <code>RigidBody</code>
 */
public class PrimitiveSphere extends Primitive {
	
	private Real m_radius;
	
	public PrimitiveSphere( RigidBody body , Matrix4 offset , Real radius ) {
		super( body , offset );
		this.m_radius = radius;
	}

	/**
	 * @return			the radius of this sphere
	 */
	public Real getRadius() {
		return this.m_radius;
	}

	@Override
	public Vector3D getPosition() {
		return this.getBody().getPosition();
	}
}
