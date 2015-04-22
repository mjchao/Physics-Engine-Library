package particle.force;

import particle.Particle;
import _math.Real;
import _math.Vector3D;


public class ParticleGravityGenerator extends ParticleForceGenerator {

	final public static Vector3D g = new Vector3D( Real.ZERO , new Real( 9.8 ) , Real.ZERO );
	
	public ParticleGravityGenerator() {
		super();
	}

	@Override
	public void generateForce() {
		for ( Particle particle : this.m_objects ) {
			
			//only apply forces to particles with finite mass
			if ( particle.getInverseMass().compareTo( Real.ZERO ) > 0 ) {
				particle.addForceVector( g.multiply( particle.getMass() ) );
			}
		}
	}
}
