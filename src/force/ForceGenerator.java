package force;

import _lib.LinkedList;
import _math.Real;

/**
 * generates forces on objects
 *
 * @param <T>		the type of object on which to generator forces
 */
abstract public class ForceGenerator < T extends MassedObject > {

	/**
	 * the force this <code>ForceGenerator</code> generates on other objects
	 */
	private Force m_force;
	
	/**
	 * the objects to which this <code>ForceGenerator</code>'s force is applied
	 */
	protected LinkedList < T > m_objects = new LinkedList < T > ();
	
	/**
	 * creates a <code>ForceGenerator</code> that applies a variable <code>Force</code>
	 */
	public ForceGenerator() {
		this.m_force = Force.ZERO;
	}
	/**
	 * creates a <code>ForceGenerator</code> that applies the given force to 
	 * objects
	 * 
	 * @param aForce			the force to apply to objects
	 */
	public ForceGenerator( Force aForce ) {
		this.m_force = aForce;
	}
	
	/**
	 * @return		the force this <code>ForceGenerator</code> applies to objects
	 */
	public Force getForce() {
		return this.m_force;
	}
	
	/**
	 * sets the force of this <code>ForceGenerator</code>
	 * 
	 * @param aForce		the new force for this <code>ForceGenerator</code>
	 */
	public void setForce( Force aForce ) {
		this.m_force = aForce;
	}
	
	/**
	 * adds a <code>MassedObject</code> that experiences the force from this 
	 * <code>ForceGenerator</code>
	 * 
	 * @param toAdd			the <code>MassedObject</code> to add
	 */
	public void addObject( T toAdd ) {
		this.m_objects.add( toAdd );
	}
	
	/**
	 * removes the given <code>MassedObject</code> from this <code>ForceGenerator</code>.
	 * 
	 * @param toRemove			the <code>MassedObject</code> to remove
	 * @see						LinkedList#remove(Object)
	 */
	public void removeObject( T toRemove ) {
		this.m_objects.remove( toRemove );
	}
	
	/**
	 * adds the <code>Force</code> this <code>ForceGenerator</code> applies to the
	 * objects on which this <code>ForceGenerator</code> acts
	 */
	public void generateForce() {
		for ( T object : this.m_objects ) {
			if ( !object.getInverseMass().equals( Real.ZERO ) ) {
				object.addForceVector( this.getForce().getVector() );
			}
		}
	}
}
