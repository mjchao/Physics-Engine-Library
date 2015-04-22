package particle.force;

import particle.Particle;
import _math.Real;
import _math.Vector3D;

public class ParticleDragGenerator extends ParticleForceGenerator {

	/**
	 * the velocity drag constant
	 */
	private Real m_k1;
	
	/**
	 * the velocity-squared drag constant
	 */
	private Real m_k2;
	
	public ParticleDragGenerator( Real k1 , Real k2) {
		
		//drag force has variable forces, so use the appropriate constructor
		super();
		this.m_k1 = k1;
		this.m_k2 = k2;
	}
	
	@Override
	public void generateForce() {
		
		//go through all the objects
		for ( Particle aParticle : this.m_objects ) {
			
			//calculate the drag force on the particle
			Vector3D dragForce = calculateDragForce( aParticle );
			
			//apply the drag force to the particle
			aParticle.addForceVector( dragForce );
		}
	}
	
	/**
	 * calculates the drag force on a particle using the equation
	 * <p>
	 * f_drag = -v( k1*|v| + k2*|v|^2 )
	 * 
	 * @param particle			any particle with mass
	 * @return					the drag force that shoud be applied to the <code>Particle</code>
	 */
	public Vector3D calculateDragForce( Particle particle ) {
		Vector3D velocity = particle.getVelocity();
		
		//calculate the first power velocity part of the drag coefficient
		Real dragVelocityPart = this.m_k1.multiply( velocity.magnitude() );
		
		//calculate the second power velocity part of the drag coefficient
		Real dragVelocitySquaredPart = this.m_k2.multiply( velocity.magnitude().multiply( velocity.magnitude() ) );
		
		//add to get a drag coefficient
		Real dragCoefficient = dragVelocityPart.add( dragVelocitySquaredPart );
		
		//scale the unit velocity vector by the drag coefficient
		Vector3D dragVector = velocity.normalize().multiply( dragCoefficient ).invert();
		return dragVector;
	}
}
