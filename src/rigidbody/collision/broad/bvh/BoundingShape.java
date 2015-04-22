package rigidbody.collision.broad.bvh;

import _math.Real;

/**
 * a shape that surrounds a <code>RigidBody</code> object. this is used during
 * broad collision detection
 */
abstract public class BoundingShape {


	/**
	 * 
	 * @param other		the other <code>BoundingShape</code> involved in this broad collision
	 * 					detection
	 * @return	 		if this <code>BoundingShape</code> touches or overlaps with the other
	 * 					<code>BoundingShape</code>
	 */
	abstract public boolean touches( BoundingShape other );
	
	/**
	 * calculates the size of the <code>BoundingShape</code> that can encompass
	 * this <code>BoundingShape</code> and the shape to be added. then determines
	 * how much growth is necessary to encompass this shape and the shape to add
	 * 
	 * @param volumeToAdd
	 * @return					the growth ( of the radius ) necessary to encompass this <code>BoundingSphere</code>
	 * 							and the <code>volumeToAdd</code>
	 */
	abstract public Real getGrowth( BoundingShape volumeToAdd );
	
	/**
	 * determines the shape required to enclose this <code>BoundingShape</code>
	 * and the given <code>BoundingShape</code>
	 * 
	 * @param shape
	 * @return				a <code>BoundingShape</code> that encompasses <code>this</code> and <code>shape2</code>
	 */
	abstract public BoundingShape calculateEnclosingShape( BoundingShape shape2 );
}
