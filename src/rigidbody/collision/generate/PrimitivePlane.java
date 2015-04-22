package rigidbody.collision.generate;

import rigidbody.Matrix4;
import rigidbody.RigidBody;
import _math.Real;
import _math.Vector3D;

public class PrimitivePlane extends Primitive {

	/**
	 * the unit normal vector to this plane
	 */
	private Vector3D m_normal;
	
	/**
	 * the signed distance of this plane from the origin. in the equation for a plane
	 * <p>
	 * Ax + By + Cz + D = 0
	 * </p>
	 * where the vector [ A , B , C ] is of unit length, D is the distance from the origin.
	 */
	private Real m_distanceFromOrigin;
	
	public PrimitivePlane( Vector3D normal , Real distanceFromOrigin ) {
		super( null , null );
		this.m_normal = normal;
		this.m_distanceFromOrigin = distanceFromOrigin;
	}

	/**
	 * @return			the unit normal vector to this plane
	 */
	public Vector3D getNormal() {
		return this.m_normal;
	}
	
	/**
	 * @return			the signed distance of this plane from the origin ( 0 , 0 , 0 )
	 * @see				#m_distanceFromOrigin
	 */
	public Real getDistanceFromOrigin() {
		return this.m_distanceFromOrigin;
	}
	
	/**
	 * @return 			always returns <code>null</code> because an infinite plane does not
	 * 					represent a finite body
	 */
	@Override
	public RigidBody getBody() {
		return null;
	}
	
	/**
	 * @return			always returns <code>null</code> because an infinite plane does
	 * 					not represent a finite body, so there is no rotational or translational
	 * 					offset from the body
	 */
	@Override
	public Matrix4 getOffset() {
		return null;
	}
	
	/**
	 * @return 			always returns <code>null</code>
	 */
	@Override
	public Vector3D getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

}
