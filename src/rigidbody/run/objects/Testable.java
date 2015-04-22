package rigidbody.run.objects;

import javax.media.j3d.Group;

public interface Testable {
	
	/**
	 * sets up the drawing related properties of this <code>Testable</code>
	 */
	abstract public void setup();
	
	/**
	 * redraws this <code>Testable</code>. called every frame of an animation using
	 * this <code>Testable</code>
	 */
	abstract public void draw();
	
	/**
	 * @return			a <code>Group</code> that can be added to a <code>BranchGroup</code>
	 * 					to display this <code>Testable</code> in Java 3D
	 */
	abstract public Group getGroup();

}
