package rigidbody.run;

import rigidbody.Matrix3;
import rigidbody.RigidBody;
import rigidbody.force.RigidBodyTestTorqueGenerator;
import rigidbody.run.objects.Testable;
import rigidbody.run.objects.TestableColorCube;
import _math.Quaternion;
import _math.Real;
import _math.Vector3D;

public class RigidBodyTorqueTest extends RigidBodyTester {

	final public static void main( String[] args ) {
		new RigidBodyTorqueTest().run();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RigidBodyTorqueTest() {
		super();
		Real cubeMass = new Real( 5 );
		Real c = Real.ONE_TWELFTH.multiply( cubeMass );
		Real diagonal = Real.sqrt( Real.THREE.squared().multiply( Real.TWO ) );
		Real[][] cubeInertiaData = { { c.multiply( diagonal ) , Real.ZERO , Real.ZERO } ,
									 { Real.ZERO , c.multiply( diagonal ) , Real.ZERO } ,
									 { Real.ZERO , Real.ZERO , c.multiply( diagonal ) } };
		Matrix3 inverseInertia = new Matrix3( cubeInertiaData ).inverse();
		RigidBody cubeBody = new RigidBody( Real.THREE , inverseInertia , new Vector3D( new Real( 0 ) , new Real( 100 ) , new Real( 0 ) ) , Vector3D.ZERO , Vector3D.ZERO , Quaternion.ZERO , Vector3D.ZERO );
		Testable cubeGraphic = new TestableColorCube( cubeBody );
		this.addObject( cubeBody , cubeGraphic );
		
		RigidBodyTestTorqueGenerator torque = new RigidBodyTestTorqueGenerator();
		torque.addObject( cubeBody );
		this.m_world.addRigidBodyForceGenerator( torque );
	}

}
