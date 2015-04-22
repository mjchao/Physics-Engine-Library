package rigidbody.run2;

import java.awt.event.KeyEvent;

import rigidbody.Matrix3;
import rigidbody.RigidBody;
import rigidbody.force.RigidBodyForceGenerator;
import rigidbody.force.RigidBodyGravityGenerator;
import rigidbody.force.RigidBodyTestTorqueGenerator;
import rigidbody.run.RigidBodyWorld;
import _math.Quaternion;
import _math.Real;
import _math.Vector3D;

/**
 * @deprecated
 */
@Deprecated
public class RigidBodyGravityTest extends RigidBodyTester implements Runnable {


	final public static void main( String[] args ) {
		new RigidBodyGravityTest().run();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RigidBodyWorld m_world = new RigidBodyWorld();
	@SuppressWarnings("hiding")
	private RigidBody m_cube;
	private RigidBodyForceGenerator m_gravity = new RigidBodyGravityGenerator();
	private RigidBodyForceGenerator m_torque = new RigidBodyTestTorqueGenerator();

	
	public RigidBodyGravityTest() {
		Real cubeMass = new Real( 8 );
		Real c = Real.ONE_TWELFTH.multiply( cubeMass );
		Real diagonal = Real.sqrt( Real.THREE.squared().multiply( Real.TWO ) );
		Real[][] cubeInertiaData = { { c.multiply( diagonal ) , Real.ZERO , Real.ZERO } ,
									 { Real.ZERO , c.multiply( diagonal ) , Real.ZERO } ,
									 { Real.ZERO , Real.ZERO , c.multiply( diagonal ) } };
		Matrix3 inverseInertia = new Matrix3( cubeInertiaData ).inverse();
		this.m_cube = new RigidBody( Real.THREE , inverseInertia , new Vector3D( Real.ZERO , new Real( 100 ) , Real.ZERO ) , Vector3D.ZERO , Vector3D.ZERO , Quaternion.ZERO , Vector3D.ZERO );
		this.m_world.addRigidBody( this.m_cube );
		this.m_gravity.addObject( this.m_cube );
		this.m_world.addRigidBodyForceGenerator( this.m_gravity );
		this.m_torque.addObject( this.m_cube );
		this.m_world.addRigidBodyForceGenerator( this.m_torque );
		
	}
	
	@Override
	public void handleKeyPressed( int keyCode ) {
		super.handleKeyPressed( keyCode );
		if ( keyCode == KeyEvent.VK_U ) {
			this.m_cube.addForceAtPoint( new Vector3D( Real.ZERO , new Real( 980 ) ,  Real.ZERO ) , this.m_cube.getPosition().add( new Vector3D( Real.ZERO , Real.ZERO , Real.ZERO ) ) ); 
		} else if ( keyCode == KeyEvent.VK_L ) {
			this.m_cube.addForceAtPoint( new Vector3D( Real.ZERO , new Real( 980 ) ,  Real.ZERO ) , this.m_cube.getPosition().add( new Vector3D( Real.ZERO , Real.ZERO , new Real( -0.5 ) ) ) );
		} else if ( keyCode == KeyEvent.VK_R ) {
			this.m_cube.addForceAtPoint( new Vector3D( Real.ZERO , new Real( 980 ) ,  Real.ZERO ) , this.m_cube.getPosition().add( new Vector3D( Real.ZERO , Real.ZERO , new Real( 0.5 ) ) ) ); 
		} else if ( keyCode == KeyEvent.VK_SPACE ) {
			double x = Math.random() * 2 - 1;
			double y = Math.random() * 2 - 1;
			double z = Math.random() * 2 - 1;
			this.m_cube.addForceAtPoint( new Vector3D( Real.ZERO , new Real( 980 ) ,  Real.ZERO ) , this.m_cube.getPosition().add( new Vector3D( new Real( x ) , new Real( y ) , new Real( z ) ) ) ); 
		}
	}
	
	@Override
	public void run() {
		while ( true ) {
			try {
				this.m_world.runPhysics( new Real( 0.001 ) );
				this.updateCube( this.m_cube.getPosition() , this.m_cube.getOrientation() );
				//System.out.println( this.m_cube );
				Thread.sleep( 1 );
			} catch ( InterruptedException e ) {
				//ignore
			}
		}		
	}
}
