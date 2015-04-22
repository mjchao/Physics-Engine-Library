package rigidbody.collision.generate;

import java.util.Iterator;

import _lib.LinkedList;

/**
 * a set of <code>Primitive</code>s that form a whole body
 */
public class PrimitiveBody implements Iterable < Primitive > {

	/**
	 * the set of <code>Primitive</code>s that form this <code>PrimitiveBody</code>
	 */
	private LinkedList < Primitive > m_primitives = new LinkedList < Primitive > ();
	
	public PrimitiveBody() {
		
	}
	
	public PrimitiveBody( LinkedList < Primitive > primitives ) {
		this.m_primitives = primitives;
	}
	
	/**
	 * adds the given primitive to this body
	 * 
	 * @param toAdd
	 */
	public void addPrimitive( Primitive toAdd ) {
		this.m_primitives.add( toAdd );
	}
	
	/**
	 * removes the given primitive from this body
	 * @param toRemove
	 */
	public void removePrimitive( Primitive toRemove ) {
		this.m_primitives.remove( toRemove );
	}

	@Override
	public Iterator < Primitive > iterator() {
		return this.m_primitives.iterator();
	}
}
