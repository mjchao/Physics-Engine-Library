package rigidbody.run;

import rigidbody.Matrix3;
import rigidbody.RigidBody;
import rigidbody.force.RigidBodyGravityGenerator;
import rigidbody.force.spring.RigidBodySpring;
import rigidbody.force.spring.RigidBodyUnanchoredSpring;
import rigidbody.run.objects.Testable;
import rigidbody.run.objects.TestableColorCube;
import rigidbody.run.objects.TestableUnanchoredSpring;
import _math.Quaternion;
import _math.Real;
import _math.Vector3D;

public class RigidBodyUnanchoredSpringTest extends RigidBodyTester {

	final public static void main( String[] args ) {
		new RigidBodyUnanchoredSpringTest().run();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Vector3D referenceConnectionPoint = new Vector3D( new Real( 0 ) , new Real( 0 ) , new Real( 0 ) );
	private Vector3D otherConnectionPoint = new Vector3D( new Real( 0 ) , new Real( 0 ) , new Real( 0 ) );
	
	public RigidBodyUnanchoredSpringTest() {
		super();
		
		Real cubeMass = new Real( 10 );
		Real c = Real.ONE_TWELFTH.multiply( cubeMass );
		Real diagonal = Real.sqrt( Real.THREE.squared().multiply( Real.TWO ) );
		Real[][] cubeInertiaData = { { c.multiply( diagonal ) , Real.ZERO , Real.ZERO } ,
									 { Real.ZERO , c.multiply( diagonal ) , Real.ZERO } ,
									 { Real.ZERO , Real.ZERO , c.multiply( diagonal ) } };
		Matrix3 inverseInertia = new Matrix3( cubeInertiaData ).inverse();
		RigidBody cubeBody = new RigidBody( Real.THREE , inverseInertia , new Vector3D( Real.ZERO , new Real( 100 ) , Real.ZERO ) , Vector3D.ZERO , Vector3D.ZERO , Quaternion.ZERO , Vector3D.ZERO );
		Testable cubeGraphic = new TestableColorCube( cubeBody );
		this.addObject( cubeBody , cubeGraphic );
		
		cubeMass = new Real( 500 );
		c = Real.ONE_TWELFTH.multiply( cubeMass );
		diagonal = Real.sqrt( Real.THREE.squared().multiply( Real.TWO ) );
		Real[][] cubeInertiaData2 = { { c.multiply( diagonal ) , Real.ZERO , Real.ZERO } ,
									 { Real.ZERO , c.multiply( diagonal ) , Real.ZERO } ,
									 { Real.ZERO , Real.ZERO , c.multiply( diagonal ) } };
		inverseInertia = new Matrix3( cubeInertiaData2 ).inverse();
		RigidBody cubeBody2 = new RigidBody( Real.THREE , inverseInertia , new Vector3D( Real.ZERO , new Real( 97 ) , Real.ZERO ) , Vector3D.ZERO , Vector3D.ZERO , Quaternion.ZERO , Vector3D.ZERO );
		Testable cubeGraphic2 = new TestableColorCube( cubeBody2 );
		this.addObject( cubeBody2 , cubeGraphic2 );
		
				
		
		RigidBodySpring spring = new RigidBodyUnanchoredSpring( new Real( 500 ) , new Real( 2.75 ) , cubeBody2 , this.referenceConnectionPoint , this.otherConnectionPoint );
		spring.addObject( cubeBody );
		
		TestableUnanchoredSpring springGraphic = new TestableUnanchoredSpring( ( RigidBodyUnanchoredSpring ) spring , cubeBody );
		this.addObject( springGraphic );
		
		this.m_world.addRigidBodyForceGenerator( spring );
		
		RigidBodyGravityGenerator gravity = new RigidBodyGravityGenerator();
		gravity.addObject( cubeBody );
		gravity.addObject( cubeBody2 );
		this.m_world.addRigidBodyForceGenerator( gravity );
	}
	
}
