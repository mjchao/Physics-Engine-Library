package rigidbody.run.objects;

import rigidbody.RigidBody;
import rigidbody.force.spring.RigidBodyAnchoredSpring;

public class TestableAnchoredSpring extends TestableSpring {

	/**
	 * the spring that this graphic represents
	 */
	final private RigidBodyAnchoredSpring m_spring;
	
	/**
	 * the object attached to this spring
	 */
	final private RigidBody m_object;
	
	public TestableAnchoredSpring( RigidBodyAnchoredSpring spring , RigidBody object ) {
		super( spring.getReferenceConnectionPoint() , object.getPosition().add( spring.getConnectionPoint() ) );
		this.m_spring = spring;
		this.m_object = object;
		super.setup();
	}
	
	@Override
	protected void updateEndpoints() {
		this.setEndpoint1( this.m_spring.getReferenceConnectionPoint() );
		this.setEndpoint2( this.m_object.getPosition().add( this.m_spring.getConnectionPoint() ) );
		super.updateEndpoints();
	}
}
