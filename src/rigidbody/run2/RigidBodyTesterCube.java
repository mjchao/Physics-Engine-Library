package rigidbody.run2;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import _math.Quaternion;
import _math.Real;
import _math.Vector3D;

import com.sun.j3d.utils.geometry.ColorCube;

public class RigidBodyTesterCube extends ColorCube {

	private Transform3D transform = new Transform3D();
	private TransformGroup tg = new TransformGroup();
	
	public RigidBodyTesterCube() {
		super( 0.3f );
		setupCube( new Vector3D( Real.ZERO , new Real( 100 ) , Real.ZERO ) , Quaternion.ZERO );
	}
	
	public void setupCube( Vector3D position , Quaternion orientation ) {
		
		this.tg.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		
		//update position
		Vector3f vector = new Vector3f( position.getX().value() , position.getY().value() , position.getZ().value() );
		this.transform.setTranslation( vector );
		
		//update orientation
		Quat4f quat = new Quat4f( orientation.getW().value() , orientation.getX().value() , orientation.getY().value() , orientation.getZ().value() );
		this.transform.setRotation( quat );
		
		this.tg.setTransform( this.transform );
		this.tg.addChild( this );
	}
	
	public TransformGroup getTransformGroup() {
		return this.tg;
	}
	
	public void drawCube( Vector3D position, Quaternion orientation ) {
		
		//update position
		Vector3f vector = new Vector3f( position.getX().value() , position.getY().value() , position.getZ().value() );
		this.transform.setTranslation( vector );
		
		//update orientation
		Quat4f quat = new Quat4f( orientation.getZ().value() , orientation.getY().value() , orientation.getX().value() , orientation.getW().value() );
		this.transform.setRotation( quat );
		
		this.tg.setTransform( this.transform );
	}
}
