package rigidbody.run.objects;

import rigidbody.RigidBody;
import rigidbody.force.spring.RigidBodyUnanchoredSpring;

public class TestableUnanchoredSpring extends TestableSpring {

	/**
	 * the spring that this graphic represents
	 */
	private RigidBodyUnanchoredSpring m_spring;
	
	/**
	 * the object connected to this spring
	 */
	private RigidBody m_object;
	
	public TestableUnanchoredSpring( RigidBodyUnanchoredSpring spring , RigidBody object ) {
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
