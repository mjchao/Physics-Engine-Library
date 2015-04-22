package util;

import _math.Real;

public class ErrorMessages {
	
	final public static class Math {
		
		final public static class Matrix {
			final public static String INVALID_MATRIX_DIMENSION = "Nonpositive matrix dimensions";
			final public static String INVALID_MATRIX3_DIMENSION = "Data provided is not 3 by 3";
			final public static String INVALID_MATRIX4_DIMENSION = "Data provided is not 4 by 4";
			final public static String INVALID_MATRIX_N_DIMENSION( int rows , int columns ) {
				return "Data provided is not " + rows + " by " + columns;
			}
			
			final public static String INVALID_AUGEND_DIMENSION = "Addend and augend must have same dimensions";
			final public static String INVALID_SUBTRAHEND_DIMENSION = "Minuend and subtrahend must have same dimensions";
			final public static String INVALID_MULTIPLIER_DIMENSION = "Multiplier must have the same number of rows as multiplicand has columns.";
			final public static String INVALID_INVERSE_DIMENSION = "Not a square matrix.";
			final public static String NO_EXISTING_INVERSE = "The given matrix does not have an inverse";

		}
	}

	final public static class Particle {
		final public static String INVALID_DURATION = "Zero or negative duration";
		
		final public static class COLLISION {
			final public static String INVALID_REFERENCE = "Null reference object";
			final public static String INVALID_ITERATION_QUANTITY = "Negative number of iterations";
		}
		
		final public static class Spring {
			final public static String INVALID_SPRING_CONSTANT = "Zero or negative spring constant";
			final public static String INVALID_REST_LENGTH = "Zero or negative rest uncompressed/unstretched length";
			final public static String INVALID_REFERENCE = "Null reference object";
			final public static String INVALID_MASS = "Infinite, zero, or negative mass.";
		}
	}
	
	final public static class RigidBody {
		
		final public static String CANNOT_SLEEP = "The specified RigidBody cannot be put to sleep";
		
		final public static class Spring {
			final public static String INVALID_SPRING_CONSTANT = Particle.Spring.INVALID_SPRING_CONSTANT;
			final public static String INVALID_REST_LENGTH = Particle.Spring.INVALID_REST_LENGTH;
			final public static String INVALID_REFERENCE = Particle.Spring.INVALID_REFERENCE;
			final public static String INVALID_MASS = Particle.Spring.INVALID_MASS;
		}
		
		final public static class Collision {
			
			final public static class BVH {
				
				final public static class BoundingShape {
					
					final public static String RADIUS = "radius";
					
					final public static String INVALID_SHAPE_PARAMETER( String expectedShape , String detectedShape ) {
						return "Expected parameter of type " + expectedShape + ", but found parameter of type " + detectedShape;
					}
					
					final public static String INVALID_DIMENSION( String dimensionName , Real dimensionValue ) {
						return dimensionValue + " is not a valid value for a " + dimensionName;
					}
					
				}
			}
			
			final public static class CollisionGenerator {
				final public static String NULL_REFERENCE = "Null reference in collision";
				final public static String INVALID_PRIMITIVE_PARAMETER( String expectedPrimitiveType , String detectedPrimitiveType ) {
					return "Expected parameter(s) of type " + expectedPrimitiveType + ", but found parameter(s) of type " + detectedPrimitiveType;
				}
			}

		}
	}
}
