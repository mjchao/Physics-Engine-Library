package rigidbody.force;

import rigidbody.RigidBody;
import _math.Real;
import _math.Vector3D;

public class RigidBodyTestTorqueGenerator extends RigidBodyTorqueGenerator {

	public RigidBodyTestTorqueGenerator() {
		//super( new Vector3D( Real.ZERO , Real.ONE , Real.ZERO ) );
		//super( new Vector3D( Real.ONE , Real.THREE , new Real( 10 ) ) );
		super( new Vector3D( Real.ZERO , Real.ZERO , Real.ONE ) );
		//super( new Vector3D( Real.ONE , Real.ZERO , Real.ZERO ) );
		//super( new Vector3D( Real.ZERO , Real.ONE , Real.ZERO ) );
		//super( new Vector3D( Real.ONE , Real.ZERO , Real.THREE ) );
		//super( new Vector3D( Real.ONE , Real.ONE , Real.ONE ) );
		//super( Vector3D.ZERO );
		//super( new Vector3D( Real.NEGATIVE_ONE , Real.ZERO , Real.NEGATIVE_ONE ) );
	}
	
	int torquesGenerated = 0;
	@Override
	public void generateForce() {
		if ( this.torquesGenerated < 1000 ) {
			for ( RigidBody body : this.m_objects ) {
				body.addTorqueVector( this.getTorqueVector() );
			}
			this.torquesGenerated++;
		}
	}
}
