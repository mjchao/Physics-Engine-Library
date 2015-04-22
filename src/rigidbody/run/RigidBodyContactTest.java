package rigidbody.run;

import java.awt.event.KeyEvent;

import rigidbody.Matrix3;
import rigidbody.RigidBody;
import rigidbody.force.RigidBodyGravityGenerator;
import rigidbody.force.RigidBodyTestTorqueGenerator;
import rigidbody.run.objects.Testable;
import rigidbody.run.objects.TestableColorCube;
import _math.Quaternion;
import _math.Real;
import _math.Vector3D;

public class RigidBodyContactTest extends RigidBodyTester {

	final public static void main( String[] args ) {
		new RigidBodyContactTest().run();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RigidBody cubeBody;
	private RigidBody cubeBody2;
	private RigidBody cubeBody3;
	
	public RigidBodyContactTest() {
		super();
		
		Real cubeMass = new Real( 10 );
		Real c = Real.ONE_TWELFTH.multiply( cubeMass );
		Real diagonal = Real.sqrt( Real.THREE.squared().multiply( Real.TWO ) );
		Real[][] cubeInertiaData = { { c.multiply( diagonal ) , Real.ZERO , Real.ZERO } ,
									 { Real.ZERO , c.multiply( diagonal ) , Real.ZERO } ,
									 { Real.ZERO , Real.ZERO , c.multiply( diagonal ) } };
		Matrix3 inverseInertia = new Matrix3( cubeInertiaData ).inverse();
		
		//Quaternion orientation = new Quaternion( new Real( 0.44585603 ) , new Real( 0.6866251 ) , new Real( -0.041437604 ) , new Real( 0.57274896 ) );
		//Vector3D position = new Vector3D( new Real( 0.017344702 ) , new Real( 90.704636 ) , new Real(  -0.014280056 ) );
		//this.cubeBody = new RigidBody( Real.THREE , inverseInertia , position , Vector3D.ZERO , Vector3D.ZERO , orientation , Vector3D.ZERO );
		this.cubeBody = new RigidBody( Real.THREE , inverseInertia , new Vector3D( new Real( 0 ) , new Real( 100 ) , new Real( 0 ) ) , new Vector3D( new Real( 0 ) , new Real( -3 ) , new Real( 0 ) ) , Vector3D.ZERO , Quaternion.ZERO , Vector3D.ZERO );
		Testable cubeGraphic = new TestableColorCube( this.cubeBody );
		this.addObject( this.cubeBody , cubeGraphic );
		
		cubeMass = new Real( 10 );
		c = Real.ONE_TWELFTH.multiply( cubeMass );
		diagonal = Real.sqrt( Real.THREE.squared().multiply( Real.TWO ) );
		Real[][] cubeInertiaData2 = { { c.multiply( diagonal ) , Real.ZERO , Real.ZERO } ,
									 { Real.ZERO , c.multiply( diagonal ) , Real.ZERO } ,
									 { Real.ZERO , Real.ZERO , c.multiply( diagonal ) } };
		inverseInertia = new Matrix3( cubeInertiaData2 ).inverse();
		this.cubeBody2 = new RigidBody( Real.THREE , inverseInertia , new Vector3D( new Real( 0 ) , new Real( 97 ) , Real.ZERO ) , new Vector3D( new Real( 0 ) , new Real( 0 ) , new Real( 0 ) ) , Vector3D.ZERO , Quaternion.ZERO , Vector3D.ZERO );
		//this.cubeBody2.setInverseMass( Real.ZERO , inverseInertia );
		Testable cubeGraphic2 = new TestableColorCube( this.cubeBody2 );
		this.addObject( this.cubeBody2 , cubeGraphic2 );
		
		cubeMass = new Real( 10 );
		c = Real.ONE_TWELFTH.multiply( cubeMass );
		diagonal = Real.sqrt( Real.THREE.squared().multiply( Real.TWO ) );
		Real[][] cubeInertiaData3 = { { c.multiply( diagonal ) , Real.ZERO , Real.ZERO } ,
									 { Real.ZERO , c.multiply( diagonal ) , Real.ZERO } ,
									 { Real.ZERO , Real.ZERO , c.multiply( diagonal ) } };
		Matrix3 inverseInertia3 = new Matrix3( cubeInertiaData3 ).inverse();
		
		this.cubeBody3 = new RigidBody( Real.FOUR , inverseInertia3 , new Vector3D( new Real( 0 ) , new Real( 98.5 ) , new Real( 0 ) ) , new Vector3D( new Real( 0 ) , new Real( -3 ) , new Real( 0 ) ) , Vector3D.ZERO , Quaternion.ZERO , Vector3D.ZERO );
		Testable cubeGraphic3 = new TestableColorCube( this.cubeBody3 );
		this.addObject( this.cubeBody3 , cubeGraphic3 );
		
		RigidBodyGravityGenerator gravity = new RigidBodyGravityGenerator();
		gravity.addObject( this.cubeBody );
		//gravity.addObject( this.cubeBody2 );
		
		this.m_world.addRigidBodyForceGenerator( gravity );
		
		
		RigidBodyTestTorqueGenerator torque = new RigidBodyTestTorqueGenerator();
		torque.addObject( this.cubeBody );
		//torque.addObject( this.cubeBody2 );
		
		this.m_world.addRigidBodyForceGenerator( torque );
	}

	@Override
	public void handleKeyPressed( int keyCode ) {
		super.handleKeyPressed( keyCode );
		if ( keyCode == KeyEvent.VK_U ) {
			System.out.println("up");
			//this.cubeBody.addForceVector( Vector3D.WORLD_Y_AXIS.multiply( new Real( 1000 ) ) );
			this.cubeBody2.addForceVector( Vector3D.WORLD_Y_AXIS.multiply( new Real( 1000 ) ) );
		}
	}
}
