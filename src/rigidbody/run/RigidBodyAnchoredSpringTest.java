package rigidbody.run;

import rigidbody.Matrix3;
import rigidbody.RigidBody;
import rigidbody.force.spring.RigidBodyAnchoredSpring;
import rigidbody.force.spring.RigidBodySpring;
import rigidbody.run.objects.Testable;
import rigidbody.run.objects.TestableAnchoredSpring;
import rigidbody.run.objects.TestableColorCube;
import _math.Quaternion;
import _math.Real;
import _math.Vector3D;

public class RigidBodyAnchoredSpringTest extends RigidBodyTester {

	final public static void main( String[] args ) {
		new RigidBodyAnchoredSpringTest().run();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Vector3D anchor = new Vector3D( new Real( 0 ) , new Real( 97 ) , new Real( 0 ) );
	private Vector3D otherConnectionPoint = new Vector3D( new Real( 0 ) , new Real( 0 ) , new Real( 0 ) );
	
	public RigidBodyAnchoredSpringTest() {
		super();
		
		Real cubeMass = new Real( 1000 );
		Real c = Real.ONE_TWELFTH.multiply( cubeMass );
		Real diagonal = Real.sqrt( Real.THREE.squared().multiply( Real.TWO ) );
		Real[][] cubeInertiaData = { { c.multiply( diagonal ) , Real.ZERO , Real.ZERO } ,
									 { Real.ZERO , c.multiply( diagonal ) , Real.ZERO } ,
									 { Real.ZERO , Real.ZERO , c.multiply( diagonal ) } };
		Matrix3 inverseInertia = new Matrix3( cubeInertiaData ).inverse();
		RigidBody cubeBody = new RigidBody( Real.THREE , inverseInertia , new Vector3D( Real.ZERO , new Real( 100 ) , Real.ZERO ) , Vector3D.ZERO , Vector3D.ZERO , Quaternion.ZERO , Vector3D.ZERO );
		Testable cubeGraphic = new TestableColorCube( cubeBody );
		this.addObject( cubeBody , cubeGraphic );
		
		RigidBodySpring spring = new RigidBodyAnchoredSpring( new Real( 1000 ) , new Real( 4 ) , this.anchor , this.otherConnectionPoint );
		spring.addObject( cubeBody );
		
		TestableAnchoredSpring springGraphic = new TestableAnchoredSpring( ( RigidBodyAnchoredSpring ) spring , cubeBody );
		this.addObject( springGraphic );
		
		this.m_world.addRigidBodyForceGenerator( spring );
	}

}
