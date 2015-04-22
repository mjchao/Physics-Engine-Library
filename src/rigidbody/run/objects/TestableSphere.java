package rigidbody.run.objects;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import rigidbody.RigidBody;
import _math.Quaternion;
import _math.Vector3D;

import com.sun.j3d.utils.geometry.Sphere;

public class TestableSphere extends Sphere implements Testable {

	/**
	 * the transformation on this <code>TestableColorCube</code> from the original
	 * [ 0 , 0 , 0 ]
	 */
	private Transform3D m_transform = new Transform3D();
	
	/**
	 * a completely set-up group that provides what's needed to draw this
	 * <code>TestableColorCube</code> in Java 3D
	 */
	private TransformGroup m_transformGroup = new TransformGroup();
	
	/**
	 * the physical "model" of this graphical color cube
	 */
	private RigidBody m_body;
	
	public TestableSphere( RigidBody body ,  float radius ) {
		super( radius );
		this.m_body = body;
		
		//define sphere color
		Appearance ap = new Appearance();
		Color3f color = new Color3f( 1f , 0f , 0f );
		ColoringAttributes ca = new ColoringAttributes( color , ColoringAttributes.NICEST );
		ap.setColoringAttributes( ca );
		this.setAppearance( ap );
		
		setup();
	}
	
	/**
	 * creates a default sphere
	 * 
	 * @param body			the <code>RigidBody</code> this graphic represents
	 */
	public TestableSphere( RigidBody body ) {
		this( body , 0.3f );

	}
	
	protected void updatePositionAndOrientation() {
		
		//update position
		Vector3D position = this.m_body.getPosition();
		Vector3f graphicPosition = new Vector3f( position.getX().value() , position.getY().value() , position.getZ().value() );
		this.m_transform.setTranslation( graphicPosition );
		
		//update orientation
		Quaternion orientation = this.m_body.getOrientation();
		Quat4f graphicOrientation = new Quat4f( orientation.getX().value() , orientation.getY().value() , orientation.getZ().value() , orientation.getW().value() );
		this.m_transform.setRotation( graphicOrientation );
		
		//update the transform group
		this.m_transformGroup.setTransform( this.m_transform );
	}
	
	@Override
	public void setup() {
		
		//allow us to write to the transform group
		this.m_transformGroup.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		
		//update the position and orientation of the sphere
		updatePositionAndOrientation();
		
		this.m_transformGroup.setTransform( this.m_transform );
		this.m_transformGroup.addChild( this );
	}

	@Override
	public void draw() {

		//just update the position
		updatePositionAndOrientation();
	}

	@Override
	public Group getGroup() {
		return this.m_transformGroup;
	}
}
