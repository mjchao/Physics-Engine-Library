package particle.force.buoyancy;

import particle.Particle;
import particle.force.ParticleForceGenerator;
import _math.Real;
import _math.Vector3D;

/**
 * <code>ForceGenerator</code> that deals with buoyancy forces, assuming that
 * the plane of the relevant liquid is parallel to the XZ plane
 */
public class ParticleBuoyancy extends ParticleForceGenerator {

	
	/**
	 * the maximum depth a <code>Particle</code> can reach in a liquid using this
	 * <code>ParticleBuoyancy</code> before the maximum buoyancy force occurs
	 */
	private Real m_maxDepth;
	
	/**
	 * volume of the <code>Particle</code> to which this <code>ParticleForceGenerator</code>
	 * applies
	 */
	private Real m_particleVolume;
	
	/**
	 * the height of the surface of the water
	 */
	private Real m_waterLevel;
	
	/**
	 * density of the liquid in which the <code>Particle</code> is submerged
	 */
	private Real m_liquidDensity;
	
	/**
	 * 
	 * @param maxDepth					maximum depth a <code>Particle</code> can reach in this liquid
	 * @param particleVolume			volume of the <code>Particle</code>
	 * @param waterLevel				height of the liquid
	 * @param liquidDensity				density of the liquid
	 */
	public ParticleBuoyancy( Real maxDepth , Real particleVolume , Real waterLevel , Real liquidDensity ) {
		this.m_maxDepth = maxDepth;
		this.m_particleVolume = particleVolume;
		this.m_waterLevel = waterLevel;
		this.m_liquidDensity = liquidDensity;
	}
	
	@Override
	public void generateForce() {
		
		//go through all the particles associated with this ForceGenerator
		for ( Particle aParticle : this.m_objects ) {
			
			//calculate a buoyant force
			Vector3D buoyantForce = calculateBuoyantForce( aParticle );
			aParticle.addForceVector( buoyantForce );
		}
	}
	
	public Vector3D calculateBuoyantForce( Particle particle ) {
		Real particleY = particle.getPosition().getY();
		Real particleDepth = this.m_waterLevel.subtract( particleY );
		
		//if the particle is not submerged, 
		//then there is no buoyant force on it
		if ( particleDepth.compareTo( Real.ZERO ) <= 0 ) {
			particle.setDamping( new Real( 0.999 ) );
			return Vector3D.ZERO;
			
		//if the particle has reached or exceeded max depth, i.e. completely submerged,
		//then calculate the maximum buoyant force
		} else if ( particleDepth.compareTo( this.m_maxDepth ) >= 0 ) {
			//particle.setDamping( new Real( 0.8 ) );
			Real buoyantForceY = this.m_liquidDensity.multiply( this.m_particleVolume );
			return new Vector3D( Real.ZERO , buoyantForceY , Real.ZERO );
			
		//if the particle is partially submerged, 
		//then calculate the buoyant force on it
		} else {
			//particle.setDamping( new Real(0.8 ) ) ;
			Real proportionSubmerged = particleY.subtract( this.m_waterLevel ).subtract( this.m_maxDepth ).divide( Real.TWO.multiply( this.m_maxDepth ) );
			Real buoyantForceY = this.m_liquidDensity.multiply( this.m_particleVolume ).multiply( proportionSubmerged );
			return new Vector3D( Real.ZERO , buoyantForceY , Real.ZERO );
		}
	}
}
