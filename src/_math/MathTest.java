package _math;

import rigidbody.Matrix3;


public class MathTest {

	final public static void main( String[] args ) {
		
		/* //Vector crossproduct
		Vector3D v1 = new Vector3D( new Real( 1 ) , new Real( 2 ) , new Real( 3 ) );
		Vector3D v2 = new Vector3D( new Real ( 4 ) , new Real( 5 ) , new Real( 6 ) );
		System.out.println( v1.cross( v2 ) );//*/
		
		/* //Matrix multiply
		Real[][] matrix = { {Real.ONE , Real.TWO , new Real( 43 ) } ,
							{ Real.THREE , Real.FOUR, new Real( -82 ) } };
		Matrix test = new Matrix( matrix );
		System.out.println( test.getRows() + " x " + test.getColumns() );
		System.out.println( test );
		
		Real[][] matrix2 = { { Real.ZERO, Real.ONE }, 
							 { Real.TWO, Real.THREE },
							 { new Real( 12 ) , new Real( 79 )} };
		Matrix test2 = new Matrix( matrix2 );
		System.out.println( test2.getRows() + " x " + test2.getColumns() );
		System.out.println( test2 );
		
		System.out.println( test.multiply( test2 ) );//*/
		
		/*//Matrix3 inverse
		Real[][] matrix = { {Real.ONE , Real.ZERO , Real.THREE } ,
							{Real.TWO , new Real(1) , new Real(3)},
							{new Real(1), new Real(4) , new Real(0)} };
		Matrix3 test = new Matrix3( matrix );
		System.out.println( test.inverse() );//*/
		
		/*//Matrix4 inverse
		Real[][] matrix = { {Real.ONE , Real.TWO , Real.THREE , Real.TWO } ,
				{Real.TWO , new Real(1) , new Real(3) , Real.ONE },
				{new Real(1), new Real(4) , new Real(1) , Real.THREE },
				{Real.ONE , Real.FOUR , Real.THREE , Real.ONE } };
		Matrix4 test = new Matrix4( matrix );
		System.out.println( test.transpose() );//*/

		
		
		Real[][] matrix = { {Real.ONE , Real.ZERO , Real.THREE } ,
				{Real.TWO , new Real(1) , new Real(3)},
				{new Real(1), new Real(4) , new Real(0)} };
		Matrix3 test = new Matrix3( matrix );
		System.out.println( test.get( 7 ) );
		Vector3D transformVector = new Vector3D( Real.ONE , Real.TWO , Real.THREE );
		System.out.println( test.transform( transformVector ) );//*/
		
		/*
		Vector3D one = new Vector3D( new Real( 0.707 ) , new Real( 0.707 ) , Real.ZERO );
		Vector3D two = new Vector3D( new Real( -0.707 ) , Real.ZERO , new Real( -0.707 ) );
		System.out.println( Vector3D.makeOrthonormalBasis( one , two ) );//*/
		
	}
}
