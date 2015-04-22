package rigidbody.run2;

import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Group;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

import _math.Vector3D;

public class RigidBodyTesterSpring extends Group {
	
	private LineArray m_line = new LineArray( 2 , GeometryArray.COORDINATES ) ;
	
	public RigidBodyTesterSpring() {
		super();
	}
	
	public void setupLine( Vector3D p1 , Vector3D p2 ) {
		
		//update position
		Point3f point1 = new Point3f( p1.getX().value() , p1.getY().value() , p1.getZ().value() );
		Point3f point2 = new Point3f( p2.getX().value() , p2.getY().value() , p2.getZ().value() );
		Point3f[] points = { point1 , point2 };
		this.m_line.setCoordinates( 0 , points );
		this.m_line.setCapability( GeometryArray.ALLOW_COORDINATE_WRITE );
		this.m_line.setCapability( GeometryArray.ALLOW_COORDINATE_READ );
		Shape3D line = new Shape3D( this.m_line , new Appearance() );
		this.addChild( line );
	}
	
	public void drawLine( Vector3D p1 , Vector3D p2  ) {
		Point3f point1 = new Point3f( p1.getX().value() , p1.getY().value() , p1.getZ().value() );
		Point3f point2 = new Point3f( p2.getX().value() , p2.getY().value() , p2.getZ().value() );
		Point3f[] points = { point1 , point2 };
		this.m_line.setCoordinates( 0 , points );
		//Shape3D line = new Shape3D( this.m_line , new Appearance() );
	}
	
}
