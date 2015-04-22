package rigidbody.run2;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;

import rigidbody.Matrix3;
import rigidbody.RigidBody;
import rigidbody.force.RigidBodyGravityGenerator;
import rigidbody.force.spring.RigidBodyAnchoredSpring;
import rigidbody.force.spring.RigidBodySpring;
import rigidbody.run.RigidBodyWorld;
import _math.Quaternion;
import _math.Real;
import _math.Vector3D;

/**
 * @deprecated
 */
@Deprecated
public class RigidBodySpringTest extends RigidBodyTester implements Runnable {

	final public static void main( String[] args ) {
		new RigidBodySpringTest().run();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RigidBodyWorld m_world = new RigidBodyWorld();
	@SuppressWarnings("hiding")
	private RigidBody m_cube;
	
	private RigidBodyTesterSpring m_springGraphics;
	
	
	public RigidBodySpringTest() {
		Real cubeMass = new Real( 100 );
		Real c = Real.ONE_TWELFTH.multiply( cubeMass );
		Real diagonal = Real.sqrt( Real.THREE.squared().multiply( Real.TWO ) );
		Real[][] cubeInertiaData = { { c.multiply( diagonal ) , Real.ZERO , Real.ZERO } ,
									 { Real.ZERO , c.multiply( diagonal ) , Real.ZERO } ,
									 { Real.ZERO , Real.ZERO , c.multiply( diagonal ) } };
		Matrix3 inverseInertia = new Matrix3( cubeInertiaData ).inverse();
		this.m_cube = new RigidBody( Real.THREE , inverseInertia , new Vector3D( Real.ZERO , new Real( 100 ) , Real.ZERO ) , Vector3D.ZERO , Vector3D.ZERO , Quaternion.ZERO , Vector3D.ZERO );
		this.m_world.addRigidBody( this.m_cube );
		
		RigidBodyGravityGenerator gravity = new RigidBodyGravityGenerator();
		gravity.addObject( this.m_cube );
		this.m_world.addRigidBodyForceGenerator( gravity );
		
		RigidBodySpring spring = new RigidBodyAnchoredSpring( new Real( 5 ) , new Real( 5 ) , this.anchor , this.otherConnectionPoint );
		spring.addObject( this.m_cube );
		this.m_world.addRigidBodyForceGenerator( spring );
	}
	
	/**
	 * @return			a <code>BranchGroup</code> set up with all necessary component, e.g. cubes, spheres,
	 * 					that are to be part of the scene
	 */
	@Override
	public BranchGroup createScene() {
		super.m_cube = new RigidBodyTesterCube();
		this.m_group.addChild( super.m_cube.getTransformGroup() );
		
		this.m_springGraphics = new RigidBodyTesterSpring();
		TransformGroup tg = new TransformGroup();
		tg.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		tg.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );
		tg.addChild( this.m_springGraphics );
		this.m_group.addChild( tg );
		this.m_springGraphics.setupLine( new Vector3D( new Real( 0 ) , new Real( 96 ) , new Real( 0 ) ) , new Vector3D( new Real( 0 ) , new Real( 97 ) , new Real( 0 ) ) );
		
		return this.m_group;
	}
	
	private Vector3D anchor = new Vector3D( new Real( 0 ) , new Real( 97 ) , new Real( 0 ) );
	private Vector3D otherConnectionPoint = new Vector3D( new Real( -0.25 ) , new Real( -0.25 ) , new Real( -0.25 ) );
	
	@Override
	public void run() {
		while ( true ) {
			try {
				this.m_world.runPhysics( new Real( 0.001 ) );
				this.updateCube( this.m_cube.getPosition() , this.m_cube.getOrientation() );
				this.m_springGraphics.drawLine( this.anchor , this.m_cube.getPosition().add( this.otherConnectionPoint ) );
				Thread.sleep( 1 );
			} catch ( InterruptedException e ) {
				//ignore
			}
		}		
	}
}
