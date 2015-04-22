package rigidbody.run2;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;

import rigidbody.Matrix3;
import rigidbody.RigidBody;
import rigidbody.force.RigidBodyGravityGenerator;
import rigidbody.force.spring.RigidBodySpring;
import rigidbody.force.spring.RigidBodyUnanchoredSpring;
import rigidbody.run.RigidBodyWorld;
import _math.Quaternion;
import _math.Real;
import _math.Vector3D;

/**
 * @deprecated
 */
@Deprecated
public class RigidBodySpringTest2 extends RigidBodyTester implements Runnable {

	final public static void main( String[] args ) {
		new RigidBodySpringTest2().run();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RigidBodyWorld m_world = new RigidBodyWorld();
	@SuppressWarnings("hiding")
	private RigidBody m_cube;
	private RigidBody m_cube2;
	
	private RigidBodyTesterSpring m_springGraphics;
	
	
	public RigidBodySpringTest2() {
		Real cubeMass = new Real( 18 );
		Real c = Real.ONE_TWELFTH.multiply( cubeMass );
		Real diagonal = Real.sqrt( Real.THREE.squared().multiply( Real.TWO ) );
		Real[][] cubeInertiaData = { { c.multiply( diagonal ) , Real.ZERO , Real.ZERO } ,
									 { Real.ZERO , c.multiply( diagonal ) , Real.ZERO } ,
									 { Real.ZERO , Real.ZERO , c.multiply( diagonal ) } };
		Matrix3 inverseInertia = new Matrix3( cubeInertiaData ).inverse();
		this.m_cube = new RigidBody( Real.THREE , inverseInertia , new Vector3D( Real.ZERO , new Real( 100 ) , Real.ZERO ) , Vector3D.ZERO , Vector3D.ZERO , Quaternion.ZERO , Vector3D.ZERO );
		

		cubeMass = new Real( 8 );
		c = Real.ONE_TWELFTH.multiply( cubeMass );
		diagonal = Real.sqrt( Real.THREE.squared().multiply( Real.TWO ) );
		Real[][] cubeInertiaData2 = { { c.multiply( diagonal ) , Real.ZERO , Real.ZERO } ,
									 { Real.ZERO , c.multiply( diagonal ) , Real.ZERO } ,
									 { Real.ZERO , Real.ZERO , c.multiply( diagonal ) } };
		Matrix3 inverseInertia2 = new Matrix3( cubeInertiaData2 ).inverse();
		this.m_cube2 = new RigidBody( Real.THREE , inverseInertia2 , new Vector3D( Real.ZERO , new Real( 95 ) , Real.ZERO ) , Vector3D.ZERO , Vector3D.ZERO , Quaternion.ZERO , Vector3D.ZERO );
		
		
		this.m_world.addRigidBody( this.m_cube );
		this.m_world.addRigidBody( this.m_cube2 );
		
		RigidBodyGravityGenerator gravity = new RigidBodyGravityGenerator();
		gravity.addObject( this.m_cube );
		gravity.addObject( this.m_cube2 );
		this.m_world.addRigidBodyForceGenerator( gravity );
		
		RigidBodySpring spring = new RigidBodyUnanchoredSpring( new Real( 5 ) , new Real( 7 ) , this.m_cube2 , this.referenceConnectionPoint , this.otherConnectionPoint );
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
		this.m_cubeGraphic2 = new RigidBodyTesterCube();
		this.m_group.addChild( this.m_cubeGraphic2.getTransformGroup() );
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
	
	private Vector3D referenceConnectionPoint = new Vector3D( new Real( 0 ) , new Real( 0 ) , new Real( 0 ) );
	private Vector3D otherConnectionPoint = new Vector3D( new Real( -0.25 ) , new Real( -0.25 ) , new Real( -0.25 ) );
	
	@Override
	public void run() {
		while ( true ) {
			try {
				this.m_world.runPhysics( new Real( 0.001 ) );
				this.updateCube( this.m_cube.getPosition() , this.m_cube.getOrientation() );
				this.m_cubeGraphic2.drawCube( this.m_cube2.getPosition() , this.m_cube2.getOrientation() );
				this.m_springGraphics.drawLine( this.m_cube2.getPosition().add( this.referenceConnectionPoint ) , this.m_cube.getPosition().add( this.otherConnectionPoint ) );
				Thread.sleep( 1 );
			} catch ( InterruptedException e ) {
				//ignore
			}
		}		
	}
	
	private RigidBodyTesterCube m_cubeGraphic2;
	
	@Override
	public void updateCube( Vector3D newPosition, Quaternion newOrientation ) {
		super.m_cube.drawCube( newPosition , newOrientation );
		this.m_cubeGraphic2.drawCube( this.m_cube2.getPosition() , this.m_cube2.getOrientation() );
	}
}
