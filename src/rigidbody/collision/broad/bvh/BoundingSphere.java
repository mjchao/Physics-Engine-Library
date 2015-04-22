package rigidbody.collision.broad.bvh;

import util.ErrorMessages;
import _math.Real;
import _math.Vector3D;

/**
 * a sphere used in collision detection that bounds a <code>RigidBody</code>
 */
public class BoundingSphere extends BoundingShape {

	/**
	 * the center of this sphere
	 */
	private Vector3D m_center;
	
	/**
	 * the radius of this sphere
	 */
	private Real m_radius;
	
	/**
	 * creates a <code>BoundingSphere</code> with the given center and radius
	 * 
	 * @param center
	 * @param radius
	 * @throws IllegalArgumentException			if the radius is not positive
	 */
	public BoundingSphere( Vector3D center , Real radius ) {
		setCenter( center );
		setRadius( radius );
	}
	
	/**
	 * @return			the center of this sphere
	 */
	public Vector3D getCenter() {
		return this.m_center;
	}
	
	/**
	 * sets the center of this sphere to a new location
	 * 
	 * @param newCenter
	 */
	public void setCenter( Vector3D newCenter ) {
		this.m_center = newCenter;
	}
	
	/**
	 * @return			the radius of this sphere
	 */
	public Real getRadius() {
		return this.m_radius;
	}
	
	/**
	 * sets the radius of this sphere to a new value
	 * 
	 * @param newRadius
	 * @throws IllegalArgumentException		if the given radius is not positive
	 */
	public void setRadius( Real newRadius ) throws IllegalArgumentException {
		if ( newRadius.compareTo( Real.ZERO ) > 0 ) {
			this.m_radius = newRadius;
		} else {
			throw new IllegalArgumentException( ErrorMessages.RigidBody.Collision.BVH.BoundingShape.INVALID_DIMENSION( ErrorMessages.RigidBody.Collision.BVH.BoundingShape.RADIUS , newRadius ) );
		}
	}
	
	/**
	 * @throws IllegalArgumentException 		if attempting to compare this <code>BoundingSphere</code> with
	 * 											a <code>BoundingShape</code> that is not a <code>BoundingSphere</code>
	 */
	@Override
	public boolean touches( BoundingShape other ) throws IllegalArgumentException {
		if ( other instanceof BoundingSphere ) {
			BoundingSphere otherSphere = ( BoundingSphere ) other;
			Real centerToCenterDistance = this.getCenter().subtract( otherSphere.getCenter() ).magnitude();
			Real radiusSum = this.getRadius().add( otherSphere.getRadius() );
			return centerToCenterDistance.compareTo( radiusSum ) <= 0;
		} else {
			throw new IllegalArgumentException( ErrorMessages.RigidBody.Collision.BVH.BoundingShape.INVALID_SHAPE_PARAMETER( this.getClass().getName() , other.getClass().getName() ) );
		}
	}
	
	@Override
	public Real getGrowth( BoundingShape volumeToAdd ) {
		
		//only deal with bounding spheres
		if ( volumeToAdd instanceof BoundingSphere ) {
			BoundingSphere sphereToAdd = ( BoundingSphere ) volumeToAdd;
			
			//the radius of the bounding sphere that can encompass this sphere and
			//the sphere to add is
			//the center to center distance between this sphere and the sphere to add
			//plus the radii of this sphere and the sphere to add
			Real centerToCenterDistance = this.getCenter().subtract( sphereToAdd.getCenter() ).magnitude();
			Real newDiameter = centerToCenterDistance.add( this.getRadius() ).add( sphereToAdd.getRadius() );
			Real newRadius = newDiameter.divide( Real.TWO );
			return newRadius.subtract( this.getRadius() );
			
		//we cannot deal with a bounding sphere and some other shape
		} else {
			throw new IllegalArgumentException( ErrorMessages.RigidBody.Collision.BVH.BoundingShape.INVALID_SHAPE_PARAMETER( this.getClass().getName() , volumeToAdd.getClass().getName() ) );
		}
	}
	
	/**
	 * @throws IllegalArgumentException			if the given two <code>BoundingShape</code>s are not <code>BoundingSphere</code>s
	 */
	@Override
	public BoundingSphere calculateEnclosingShape( BoundingShape shape ) {
		
		if ( shape instanceof BoundingSphere ) {
			BoundingSphere sphere1 = this;
			BoundingSphere sphere2 = ( BoundingSphere ) shape;
			
			//check if one sphere encloses the other
			Real centerToCenterDistanceSquared = sphere1.getCenter().subtract( sphere2.getCenter() ).magnitudeSquared();
			Real radiusDifferenceSquared = sphere1.getRadius().subtract( sphere2.getRadius() ).squared();
			
			//if the difference in the radii of the spheres is less than the center to center distance,
			//then one sphere must enclose the other
			if ( radiusDifferenceSquared.compareTo( centerToCenterDistanceSquared ) >= 0 ) {
				
				//the radius of this bounding sphere will be the
				//radius of the bigger sphere
				Real biggerRadius = sphere1.getRadius();
				
				//and the center of this bounding sphere will be the center of
				//the bigger sphere
				Vector3D biggerSphereCenter = sphere1.getCenter();
				if ( sphere2.getRadius().compareTo( biggerRadius ) > 0 ) {
					biggerRadius = sphere2.getRadius();
					biggerSphereCenter = sphere2.getCenter();
				}
				
				return new BoundingSphere( biggerSphereCenter , biggerRadius );
				
			//if one sphere does not enclose the other
			} else {
				
				//the new diameter is the distance between the centers of the two spheres
				//plus the radius of each sphere
				Real diameter = Real.sqrt( centerToCenterDistanceSquared ).add( sphere1.getRadius() ).add( sphere2.getRadius() );
				Real radius = diameter.divide( Real.TWO );
				
				//the new center is found by taking a weighted average
				//proportional to the radii of the two spheres
				Vector3D center = sphere1.getCenter();
				
				//only take the weighted average if the two spheres are not identical
				if ( radius.compareTo( Real.ZERO ) > 0 ) {
					Vector3D centerToCenterVector = sphere1.getCenter().subtract( sphere2.getCenter() );
					Vector3D offsetScale = centerToCenterVector.divide( diameter );
					center = center.add( offsetScale.multiply( radius.subtract( sphere1.getRadius() ) ) );
				}
				
				return new BoundingSphere( center, radius );
			}
		} else {
			throw new IllegalArgumentException( ErrorMessages.RigidBody.Collision.BVH.BoundingShape.INVALID_SHAPE_PARAMETER( this.getClass().getName() , shape.getClass().getName() ) );
		}
	}

}
