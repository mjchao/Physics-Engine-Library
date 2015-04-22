package rigidbody.collision.generate;

import rigidbody.Matrix4;
import rigidbody.RigidBody;
import _math.Vector3D;

/**
 * a basic 3D shape, e.g. box or sphere, that can bound a piece of a rigidbody.
 * not all rigidbodies might be perfect shapes, such as boxes or spheres, and
 * sometimes, those bodies need to be bounded by several boxes or spheres that
 * each contain a part of the body
 */
abstract public class Primitive {

	/**
	 * the <code>RigidBody</code> that is bounded by this <code>Primitive</code>
	 */
	private RigidBody m_body;
	
	/**
	 * the distance of this <code>Primitive</code> from the center of mass of
	 * the <code>RigidBody</code>
	 */
	private Matrix4 m_offset;
	
	public Primitive( RigidBody body , Matrix4 offset ) {
		this.m_body = body;
		this.m_offset = offset;
	}
	
	/**
	 * @return			the <code>RigidBody</code> bounded by this <code>Primitive</code>
	 */
	public RigidBody getBody() {
		return this.m_body;
	}
	
	/**
	 * @return			the distance of this <code>Primitive</code> from the center of the
	 * 					<code>RigidBody</code>
	 * @see				#m_body
	 * @see				#getBody()
	 */
	public Matrix4 getOffset() {	
		return this.m_offset;
	}
	
	/**
	 * @return			the center of mass of this primitive shape
	 */
	abstract public Vector3D getPosition();
}
