package rigidbody.collision.generate;

import _lib.LinkedList;
import _math.Real;
import _math.Vector3D;

/**
 * takes sets of two primitives and determines if they are in contact with each other
 */
abstract public class ContactGenerator {
	
	final public static Real DEFAULT_PENETRATION_OFFSET = new Real( 0.005 );
	/**
	 * friction involved in collisions
	 */
	private Real m_friction;

	/**
	 * elasticity of collisions
	 */
	private Real m_elasticity;
	
	/**
	 * the amount of penetration offset in pessimistic collision detection
	 */
	private Real m_penetrationOffset;
	
	/**
	 * creates a collision generator with the given friction and elasticity
	 * involved in collisions
	 * 
	 * @param friction
	 * @param elasticity
	 */
	public ContactGenerator( Real friction , Real elasticity , Real penetrationOffset ) {
		this.m_friction = friction;
		this.m_elasticity = elasticity;
		this.m_penetrationOffset = penetrationOffset;
	}
	
	/**
	 * @return			friction involved in collisions
	 * @see				#m_friction
	 */
	public Real getFriction() {
		return this.m_friction;
	}
	
	/**
	 * @return			elasticity of collisions
	 * @see				#m_elasticity
	 */
	public Real getElasticity() {
		return this.m_elasticity;
	}
	
	/**
	 * @return			the penetration offset in collisions
	 */
	public Real getPenetrationOffset() {
		return this.m_penetrationOffset;
	}
	
	/**
	 * generates all contacts between <code>p1</code> and <code>p2</code>
	 *  
	 * @param p1		a primitive
	 * @param p2		a primitive
	 * @return			all contacts between the two given primitivies
	 */
	abstract public LinkedList < Contact > generateContacts( Primitive p1 , Primitive p2 );
	
	/**
	 * determines the distance of a point to a plane. applies the formula
	 * <p>
	 * distance = point (dot) plane_normal - plane_distance_from_origin
	 * <p>
	 * 
	 * @param point		a point
	 * @param plane		a plane
	 * @return			the distance of <code>point</code> from <code>plane</code>
	 */
	public static Real pointToPlaneDistance( Vector3D point , PrimitivePlane plane ) {
		return point.dot( plane.getNormal() ).subtract( plane.getDistanceFromOrigin() );
	}
}
