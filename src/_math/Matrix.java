package _math;

import util.ErrorMessages;

/**
 * represents a Matrix in mathematics. it consists of <code>Real</code>s
 */
public class Matrix {

	/**
	 * number of rows in this <code>Matrix</code>
	 */
	final private int m_rows;
	
	/**
	 * number of columns in this <code>Matrix</code>
	 */
	final private int m_columns;
	
	/**
	 * the elements in this <code>Matrix</code>
	 */
	private Real[][] m_data;
	
	/**
	 * creates a <code>Matrix</code> with the given number of rows and columns.
	 * the resulting <code>Matrix</code> is a <code>rows</code> x <code>columns</code>
	 * <code>Matrix</code>. all its elements are <code>null</code> by default.
	 * 
	 * @param rows
	 * @param columns
	 * @throws IllegalArgumentException			if <code>rows</code> and/or <code>columns</code> is not positive
	 */
	public Matrix( int rows , int columns) {
		if ( rows <= 0 || columns <= 0 ) {
			throw new IllegalArgumentException( ErrorMessages.Math.Matrix.INVALID_MATRIX_DIMENSION );
		}
		this.m_rows = rows;
		this.m_columns = columns;
		this.m_data = new Real[ rows ][ columns ];
		for ( int rowIdx = 0 ; rowIdx < rows ; rowIdx ++ ) {
			for (int columnIdx = 0 ; columnIdx < columns ; columnIdx ++ ) {
				this.m_data[ rowIdx ][ columnIdx ] = Real.ZERO;
			}
		}
	}
	
	/**
	 * creates a <code>Matrix</code> with the given data
	 * 
	 * @param data
	 */
	public Matrix( Real[][] data ) {
		this( data.length , data[0].length );
		for ( int rowIdx = 0 ; rowIdx < data.length ; rowIdx ++ ) {
			for (int columnIdx = 0 ; columnIdx < data[ rowIdx ].length ; columnIdx ++ ) {
				this.m_data[ rowIdx ][ columnIdx ] = data[ rowIdx ][ columnIdx ];
			}
		}
	}
	
	/**
	 * @return			the number of rows in this <code>Matrix</code>
	 */
	public int getRows() {
		return this.m_rows;
	}
	
	/**
	 * @return			the number of columns in this <code>Matrix</code>
	 */
	public int getColumns() {
		return this.m_columns;
	}
	
	/**
	 * sets the element at (<code>row</code>, <code>column</code>) to <code>newValue</code>
	 * @param row
	 * @param column
	 * @param newValue
	 */
	public void set( int row , int column , Real newValue ) {
		this.m_data[ row ][ column ] = newValue;
	}
	
	/**
	 * @param row
	 * @param column
	 * @return				the element in the <code>Matrix</code> at (<code>row</code>, <code>column</code>)
	 */
	public Real get( int row , int column ) {
		return this.m_data[ row ][ column ];
	}
	
	/**
	 * gets the i-th element in this <code>Matrix</code>. for example, if this were
	 * a 4 by 4 <code>Matrix</code>, calling <code>get( 0 )</code> would be equivalent
	 * to calling <code>get( 0 , 0 )</code>. calling <code>get( 10 )</code> would be 
	 * equivalent to calling <code>get( 2 , 2 )</code> or calling <code>get( 10/4 , 10%4 )</code>
	 * 
	 * @param i
	 * @return				the i-th element in this <code>Matrix</code> 
	 */
	public Real get( int i ) {
		return get( i / this.m_rows , i % this.m_rows );
	}
	
	/**
	 * @return			the elements of this <code>Matrix</code> as a 2D array
	 */
	public Real[][] getData() {
		return this.m_data;
	}
	
	/**
	 * sets the data in this <code>Matrix</code>
	 * 
	 * @param data							the new elements for this <code>Matrix</code>
	 * @throws IllegalArgumentException		if the dimensions of the new data are incorrect
	 */
	protected void setData( Real[][] data ) throws IllegalArgumentException {
		if ( data.length == this.m_rows && data[ 0 ].length == this.m_columns ) {
			this.m_data = data;
		} else {
			throw new IllegalArgumentException( ErrorMessages.Math.Matrix.INVALID_MATRIX_N_DIMENSION( this.m_rows , this.m_columns ) );
		}
	}
	
	
	/**
	 * adds two <code>Matrix</code> objects using Matrix addition and returns the result.
	 * 
	 * @param augend
	 * @return								the elements in the sum of <code>this</code> + <code>augend</code>
	 * @throws IllegalArgumentException		if the addend and augend have different dimensions
	 */
	protected Real[][] calculateAddData( Matrix augend ) {
		
		//only add if the dimensions are the same
		if ( this.getRows() != augend.getRows() || this.getColumns() != augend.getColumns() ) {
			throw new IllegalArgumentException();
		}
		
		//add
		Real[][] sum = new Real[ this.getRows() ][ this.getColumns() ];
		for ( int rowIdx = 0 ; rowIdx < this.m_rows ; rowIdx ++ ) {
			for ( int columnIdx = 0 ; columnIdx < this.m_columns ; columnIdx ++ ) {
				sum[ rowIdx ][ columnIdx ] = this.get( rowIdx , columnIdx ).add( augend.get( rowIdx ,  columnIdx ) );
			}
		}
		
		//return the result
		return sum;
	}
	
	/**
	 * @param augend
	 * @return				the <code>Matrix</code> <code>this</code> + <code>augend</code>
	 */
	public Matrix add( Matrix augend ) {
		return new Matrix( calculateAddData( augend ) );
	}
	
	/**
	 * subtracts two <code>Matrix</code> objects using Matrix subtraction and returns the result.
	 * 
	 * @param subtrahend
	 * @return								the elements in the difference <code>this</code> - <code>subtrahend</code>
	 * @throws IllegalArgumentException		if the minuend and subtrahend have different dimensions
	 */
	protected Real[][] calculateSubtractData( Matrix subtrahend ) {
		
		//only subtract if dimensions are the same
		if ( this.getRows() != subtrahend.getRows() || this.getColumns() != subtrahend.getColumns() ) {
			throw new IllegalArgumentException( ErrorMessages.Math.Matrix.INVALID_SUBTRAHEND_DIMENSION );
		}
		
		//subtract
		Real[][] difference = new Real[ this.getRows() ][ this.getColumns() ];
		for ( int rowIdx = 0 ; rowIdx < this.m_rows ; rowIdx ++ ) {
			for ( int columnIdx = 0 ; columnIdx < this.m_columns ; columnIdx ++ ) {
				difference[ rowIdx ][ columnIdx ] = this.get( rowIdx , columnIdx ).subtract( subtrahend.get( rowIdx , columnIdx ) );
			}
		}
		
		//return the difference
		return difference;
	}
	
	/**
	 * @param subtrahend
	 * @return					the <code>Matrix</code> <code>this</code> - <code>subtrahend</code>
	 */
	public Matrix subtract( Matrix subtrahend ) {
		return new Matrix( calculateSubtractData( subtrahend ) );
	}
	
	/**
	 * multiplies this <code>Matrix</code> by a scalar
	 * 
	 * @param scalar			the value by which to scale
	 * @return					the elements in the <code>Matrix</code> <code>this</code> * <code>scalar</code>
	 */
	protected Real[][] calculateMultiplyData( Real scalar ) {
		Real[][] product = new Real[ this.getRows() ][ this.getColumns() ];
		for ( int rowIdx = 0 ; rowIdx < this.getRows() ; rowIdx ++ ) {
			for ( int columnIdx = 0 ; columnIdx < this.getColumns() ; columnIdx ++ ) {
				product[ rowIdx ][ columnIdx ] = this.get( rowIdx , columnIdx ).multiply( scalar );
			}
		}
		return product;
	}
	
	/**
	 * @param scalar		value by which to scale
	 * @return				the <code>Matrix</code> <code>this</code> * <code>scalar</code>
	 */
	public Matrix multiply( Real scalar ) {
		return new Matrix( calculateMultiplyData( scalar ) );
	}
	
	/**
	 * multiplies two <code>Matrix</code> objects and returns the result
	 * 
	 * @param multiplier
	 * @return								the elements in the product <code>this</code> * <code>multiplier</code>
	 * @throws IllegalArgumentException		if the columns in <code>this</code> is not equal to the rows in <code>multiplier</code>
	 */
	protected Real[][] calculateMultiplyData( Matrix multiplier ) {
		
		//only multiply if this has the same number of columns as the
		//multiplier has rows
		if ( this.getColumns() != multiplier.getRows() ) {
			throw new IllegalArgumentException( ErrorMessages.Math.Matrix.INVALID_MULTIPLIER_DIMENSION );
		}
		
		//multiply
		Real[][] product = new Real[ this.getRows() ][ multiplier.getColumns() ];
		for ( int rowIdx = 0 ; rowIdx < this.m_rows ; rowIdx ++ ) {
			for ( int columnIdx = 0 ; columnIdx < multiplier.getColumns() ; columnIdx ++ ) {
				Real element = Real.ZERO;
				for ( int commonIdx = 0 ; commonIdx < this.getColumns() ; commonIdx ++ ) {
					element = element.add( this.get( rowIdx , commonIdx ).multiply( multiplier.get( commonIdx , columnIdx ) ) );
				}
				product[ rowIdx ][ columnIdx ] = element;
			}
		}
		
		//return product
		return product;
	}
	
	/**
	 * @param multiplier
	 * @return								the <code>Matrix</code> <code>this * multiplier</code>
	 * @throws IllegalArgumentException		if the columns in <code>this</code> does not equal the rows in <code>multiplier</code>
	 */
	public Matrix multiply( Matrix multiplier ) {
		return new Matrix( calculateMultiplyData( multiplier ) );
	}
	
	/**
	 * divides a <code>Matrix</code> by a scalar
	 * 
	 * @param scalar			value by which to scale
	 * @return					the elements in the <code>Matrix</code> <code>this</code> / <code>scalar</code>
	 */
	protected Real[][] calculateDivideData( Real scalar ) {
		return this.calculateMultiplyData( Real.ONE.divide( scalar ) );
	}
	
	/**
	 * @param scalar
	 * @return				the <code>Matrix</code> <code>this / scalar</code>
	 */
	public Matrix divide( Real scalar ) {
		return new Matrix( calculateDivideData( scalar ) );
	}
	
	/**
	 * the <code>Matrix</code> such that <code>this</code> * <code>this.inverse()</code> yields
	 * an identity matrix
	 * 
	 * @return			the data of the inverse of this <code>Matrix</code>
	 */
	protected Real[][] calculateInverseData() {
		if ( this.m_rows != this.m_columns ) {
			throw new IllegalStateException( ErrorMessages.Math.Matrix.INVALID_INVERSE_DIMENSION );
		}
		return null;
	}
	
	/**
	 * @return			the inverse of this <code>Matrix</code>
	 */
	public Matrix inverse() {
		return new Matrix( calculateInverseData() );
	}
	

	/**
	 * transposes this <code>Matrix</code> by swapping rows with columns.
	 * 
	 * @return			the elements of the transpose of <code>this</code>
	 */
	protected Real[][] calculateTransposeData() {
		Real[][] transposeData = new Real[ this.getColumns() ][ this.getRows() ];
		for ( int rowIdx = 0 ; rowIdx < this.getRows() ; rowIdx ++ ) {
			for ( int columnIdx = 0 ; columnIdx < this.getColumns() ; columnIdx ++ ) {
				transposeData[ columnIdx ][ rowIdx ] = this.get( rowIdx , columnIdx );
			}
		}
		return transposeData;
	}
	
	/**
	 * @return			the transpose of this <code>Matrix</code>
	 */
	public Matrix transpose() {
		return new Matrix( calculateTransposeData() );
	}
	
	@Override
	public String toString() {
		String rtn = "";
		for ( int rowIdx = 0 ; rowIdx < this.m_rows ; rowIdx ++ ) {
			String rowRepresentation = "[" + this.get( rowIdx , 0 ).toString();
			for ( int columnIdx = 1 ; columnIdx < this.m_columns ; columnIdx ++ ) {
				Real value = this.get( rowIdx , columnIdx );
				if ( value == null ) {
					rowRepresentation += ", null";
				} else {
					rowRepresentation += ", " + this.get( rowIdx , columnIdx ).toString();
				}
			}
			rowRepresentation += "]\n";
			rtn += rowRepresentation;
		}
		return rtn;
	}
	
	/**
	 * @return 		a shallow copy of this <code>Matrix</code>. however, <code>Real</code>s are
	 * 				immutable, so that should not be a big problem
	 */
	@Override
	public Matrix clone() {
		return new Matrix( this.m_data.clone() );
	}
}
