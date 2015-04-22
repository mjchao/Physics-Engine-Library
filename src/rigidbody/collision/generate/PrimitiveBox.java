package rigidbody.collision.generate;

import rigidbody.Matrix4;
import rigidbody.RigidBody;
import _math.Real;
import _math.Vector3D;

/**
 * a box that bounds a <code>RigidBody</code> or a part of a <code>RigidBody</code>
 */
public class PrimitiveBox extends Primitive {

	
	/**
	 * halfsize of this box. the x component is half the box's x-axis length, the y
	 * component is half the box's y-axis length, and the z component is half the box's
	 * z axis length
	 */
	final private Vector3D m_halfSize;
	
	/**
	 * the 8 vertices of a box
	 */
	private Vector3D[] m_vertices = new Vector3D[ 8 ];
	
	/**
	 * creates a box with the given properties
	 * 
	 * @param body				the <code>RigidBody</code> this box bounds
	 * @param offset			the offset of this box from the center of mass of the <code>RigidBody</code>
	 * @param halfSize			the x, y, and z half-lengths of the box (i.e. x is half the x-axis length of the box)
	 */
	public PrimitiveBox( RigidBody body, Matrix4 offset , Vector3D halfSize ) {
		super(body, offset);
		this.m_halfSize = halfSize;
		
		//determine the vertices
		determineVertices();
	}
	
	public void determineVertices() {
		Vector3D halfSize = this.m_halfSize;
		
		Real x = halfSize.getX();
		Real negativeX = x.multiply( Real.NEGATIVE_ONE );
		Real y = halfSize.getY();
		Real negativeY = y.multiply( Real.NEGATIVE_ONE );
		Real z = halfSize.getZ();
		Real negativeZ = z.multiply( Real.NEGATIVE_ONE );
		
		Vector3D[] vertices = {
			new Vector3D( negativeX , negativeY , negativeZ ),
			new Vector3D( negativeX , negativeY , z 		) ,
			new Vector3D( negativeX , y 		, negativeZ ) ,
			new Vector3D( x			, negativeY , negativeZ ) ,
			new Vector3D( x 		, y			, negativeZ ) ,
			new Vector3D( x 		, negativeY , z ) ,
			new Vector3D( negativeX , y 		, z ) ,
			new Vector3D( x			, y			, z	)
		};
		
		//convert each vertex into world coordinates
		for ( int vertexIdx = 0 ; vertexIdx < vertices.length ; vertexIdx ++ ) {
			vertices[ vertexIdx ] = this.getBody().convertLocalToWorld( vertices[ vertexIdx ] );
			//vertices[ vertexIdx ] = this.getBody().convertWorldToLocal( vertices[ vertexIdx ] );
			//vertices[ vertexIdx ] = this.getBody().convertLocalToWorld( vertices[ vertexIdx ] );
		}
		
		this.m_vertices = vertices;
	}
	
	public Vector3D[] getVertices() {
		return this.m_vertices;
	}
	
	/**
	 * @return			the half size of this box
	 * @see				#m_halfSize
	 */
	public Vector3D getHalfSize() {
		return this.m_halfSize;
	}
	
	@Override
	public Vector3D getPosition() {
		return this.getBody().getPosition();
	}

	/**
	 * @return			the unit vector x-axis of the box, relative to the faces of the box ( which may have been rotated )
	 */
	public Vector3D getXAxis() {
		Vector3D unrotatedXAxis = new Vector3D( this.getHalfSize().getX() , Real.ZERO , Real.ZERO );
		Vector3D rotatedXAxis = this.getBody().getOrientation().toOrientationMatrix().transform( unrotatedXAxis );
		return rotatedXAxis.normalize();
	}
	
	/**
	 * @return			the unit vector y-axis of the box, relative to the faces of the box ( which may have been rotated )
	 */
	public Vector3D getYAxis() {
		Vector3D unrotatedYAxis = new Vector3D( Real.ZERO , this.getHalfSize().getY() , Real.ZERO );
		Vector3D rotatedYAxis = this.getBody().getOrientation().toOrientationMatrix().transform( unrotatedYAxis );
		return rotatedYAxis.normalize();
	}
	
	/**
	 * @return			the unit vector z-axis of the box, relative to the faces of the box ( which may have been rotated )
	 */
	public Vector3D getZAxis() {
		Vector3D unrotatedZAxis = new Vector3D( Real.ZERO , Real.ZERO , this.getHalfSize().getZ() );
		Vector3D rotatedZAxis = this.getBody().getOrientation().toOrientationMatrix().transform( unrotatedZAxis );
		return rotatedZAxis.normalize();
	}
	
	/**
	 * @param axis		the axis on which to project the axes of this box
	 * @return			the sum of the lengths of the x, y, and z axes of this box projected onto the given axis
	 */
	public Real transformToAxis( Vector3D axis ) {
		return this.getHalfSize().getX().multiply( Real.abs( axis.dot( this.getXAxis() ) ) ).add( 
				this.getHalfSize().getY().multiply( Real.abs( axis.dot( this.getYAxis() ) ) ) ).add( 
				this.getHalfSize().getZ().multiply( Real.abs( axis.dot( this.getZAxis() ) ) ) );
	}
}
