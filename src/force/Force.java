package force;

import _math.Vector3D;

/**
 * represents a constant force in physics
 */
public class Force {

	final public static Force ZERO = new Force( Vector3D.ZERO );
	
	final private Vector3D m_force;
	
	/**
	 * creates a <code>Force</code> with magnitude and direction as a <code>Vector3D</code>
	 * 
	 * @param force			the magnitude and direction of this force
	 */
	public Force( Vector3D force ) {
		this.m_force = force;
	}
	
	/**
	 * @return			the magnitude and direction of this <code>Force</code> as a <code>Vector3D</code>
	 */
	public Vector3D getVector() {
		return this.m_force;
	}
}
