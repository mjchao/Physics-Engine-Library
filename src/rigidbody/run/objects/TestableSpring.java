package rigidbody.run.objects;

import javax.media.j3d.Appearance;
import javax.media.j3d.Group;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

import _math.Vector3D;

/**
 * a line segment that represents a spring
 */
abstract public class TestableSpring extends LineArray implements Testable {

	private Group m_group = new Group();
	
	/**
	 * the first endpoint of this line segment
	 */
	protected Vector3D m_point1;
	
	/**
	 * the second endpoint of this line segment
	 */
	protected Vector3D m_point2;
	
	/**
	 * a line segment going from <code>endpoint1</code> to <code>endpoint2</code>
	 * @param endpoint1
	 * @param endpoint2
	 */
	public TestableSpring( Vector3D endpoint1 , Vector3D endpoint2 ) {
		super( 2 , COORDINATES );
		setEndpoint1( endpoint1 );
		setEndpoint2( endpoint2 );
	}
	
	/**
	 * sets the first endpoint of this line
	 * 
	 * @param position
	 */
	public void setEndpoint1( Vector3D position ) {
		this.m_point1 = position;
	}
	
	/**
	 * sets the second endpoint of this line
	 * 
	 * @param position
	 */
	public void setEndpoint2( Vector3D position ) {
		this.m_point2 = position;
	}
	
	/**
	 * updates the endpoints of this line based on the information stored in
	 * <code>m_point1</code> and <code>m_point2</code>
	 * 
	 * @see				#m_point1
	 * @see				#m_point2 
	 */
	protected void updateEndpoints() {
		Point3f endpoint1 = new Point3f( this.m_point1.getX().value() , this.m_point1.getY().value() , this.m_point1.getZ().value() );
		Point3f endpoint2 = new Point3f( this.m_point2.getX().value() , this.m_point2.getY().value() , this.m_point2.getZ().value() );
		Point3f[] points = { endpoint1 , endpoint2 };
		this.setCoordinates( 0 , points );
	}
	
	@Override
	public void setup() {
		
		//allow the endpoints of this line to be modified
		this.setCapability( ALLOW_COORDINATE_WRITE );
		this.setCapability( ALLOW_COORDINATE_READ );
	
		//set the endpoints of this line
		updateEndpoints();
		
		//create the shape for this line
		Shape3D line = new Shape3D( this , new Appearance() );
		
		//add the line to the group
		this.m_group.addChild( line );
	}

	@Override
	public void draw() {
		updateEndpoints();
	}

	@Override
	public Group getGroup() {
		return this.m_group;
	}

}
