package rigidbody.run2;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import rigidbody.Matrix3;
import rigidbody.RigidBody;
import rigidbody.force.RigidBodyTestTorqueGenerator;
import rigidbody.run.RigidBodyWorld;
import _math.Quaternion;
import _math.Real;
import _math.Vector3D;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * @deprecated
 */
@Deprecated
public class RigidBodyTorqueTest extends SimpleUniverse implements Runnable {

	final public static void main( String[] args ) {
		new RigidBodyTorqueTest().run();
	}
	
	private RigidBodyWorld m_world = new RigidBodyWorld();
	private RigidBody m_cube;
	private RigidBodyTestTorqueGenerator m_torque = new RigidBodyTestTorqueGenerator();
	
	public RigidBodyTorqueTest() {

		Real cubeMass = new Real( 8 );
		Real c = Real.ONE_TWELFTH.multiply( cubeMass );
		Real diagonal = Real.sqrt( Real.THREE.squared().multiply( Real.TWO ) );
		Real[][] cubeInertiaData = { { c.multiply( diagonal ) , Real.ZERO , Real.ZERO } ,
									 { Real.ZERO , c.multiply( diagonal ) , Real.ZERO } ,
									 { Real.ZERO , Real.ZERO , c.multiply( diagonal ) } };
		Matrix3 inverseInertia = new Matrix3( cubeInertiaData ).inverse();
		this.m_cube = new RigidBody( Real.THREE , inverseInertia , Vector3D.ZERO , Vector3D.ZERO , Vector3D.ZERO , Quaternion.ZERO , Vector3D.ZERO );
		this.m_world.addRigidBody( this.m_cube );
		this.m_torque.addObject( this.m_cube );
		this.m_world.addRigidBodyForceGenerator( this.m_torque );
	}
	
	@Override
	public void run() {
		setupCube();
		while ( true ) {
			try {
				this.m_world.runPhysics( new Real( 0.001 ) );
				drawCube();
				//System.out.println( this.m_cube );
				Thread.sleep( 1 );
			} catch ( InterruptedException e ) {
				//ignore
			}
		}
	}
	
	private BranchGroup group = new BranchGroup();
	private ColorCube cube = new ColorCube( 0.3f );
	private Transform3D transform = new Transform3D();
	private TransformGroup tg = new TransformGroup();
	
	public void setupCube() {
		
		this.tg.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		
		//update position
		Vector3D position = this.m_cube.getPosition();
		Vector3f vector = new Vector3f( position.getX().value() , position.getY().value() , position.getZ().value() );
		this.transform.setTranslation( vector );
		
		//update orientation
		Quaternion orientation = this.m_cube.getOrientation();
		Quat4f quat = new Quat4f( orientation.getW().value() , orientation.getX().value() , orientation.getY().value() , orientation.getZ().value() );
		this.transform.setRotation( quat );
		
		this.tg.setTransform( this.transform );
		this.tg.addChild( this.cube );
		this.group.addChild( this.tg );
		
		//set light
		Color3f light1Color = new Color3f(.1f, 1.4f, .1f);
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
		Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
		DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
		light1.setInfluencingBounds(bounds);
		this.group.addChild(light1);
		
		this.getViewingPlatform().setNominalViewingTransform();
		this.addBranchGraph( this.group );
	}
	
	public void drawCube() {
		
		//update position
		Vector3D position = this.m_cube.getPosition();
		Vector3f vector = new Vector3f( position.getX().value() , position.getY().value() , position.getZ().value() );
		this.transform.setTranslation( vector );
		
		//update orientation
		Quaternion orientation = this.m_cube.getOrientation();
		Quat4f quat = new Quat4f( orientation.getZ().value() , orientation.getY().value() , orientation.getX().value() , orientation.getW().value() );
		this.transform.setRotation( quat );
		
		this.tg.setTransform( this.transform );
		this.getCanvas().repaint();
		

	}
}
