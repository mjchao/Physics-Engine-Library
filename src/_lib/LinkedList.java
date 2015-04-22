package _lib;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * list with its elements connected together by "links," which are pointers. This is
 * singly linked. Each element knows the element following it and no more.
 * <p>
 * inserting does not change the current element pointer.
 * <p>
 * the first element added to the list defaults to the current element
 * <p>
 * removing changes the current element pointer to the element coming 
 * after the one that was removed
 * 
 * @param <E>			type of element in this list
 */
public class LinkedList < E > implements Iterable < E > {
	
	
	/**
	 * the head link that points to the first link
	 */
	private Link m_head = new Link( ( E ) null , null );
	
	/**
	 *  first element in list
	 */
	private Link m_first = null;
	
	/**
	 * last element in list
	 */
	private Link m_last = null;
	
	/**
	 * previous element before the current element
	 */
	private Link m_previous = null;
	
	/**
	 * element we are at in the list
	 */
	private Link m_current = null;
	
	/**
	 * size of the list
	 */
	private int m_size = 0;
	
	/**
	 * permits iterating through this linked list
	 */
	private LinkedListIterator m_iterator = new LinkedListIterator();
	
	public LinkedList() {
		
	}
	
	/**
	 * adds the given element to the end of list
	 * 
	 * @param element			element to add to list
	 */
	public void add( E element ) {
		Link toAdd = new Link ( element );
		
		//if there are no elements in the list, set the element to add
		//as the first and last element in the list
		if ( this.m_size == 0 ) {
			this.m_first = toAdd;
			this.m_last = toAdd;
			
			//update the head
			this.m_head.setNext( this.m_first );
			
			//update the current pointer
			this.m_current = this.m_first;
			
		//if elements have already been added to the list, add this element
		//after the last element in the list
		} else {
			this.m_last.setNext( toAdd );
			this.m_last = toAdd;
		}
		
		//update the size
		this.m_size ++;
	}
	
	
	/**
	 * inserts the given element between the current element and the next element
	 * example:  the current element is 3 in [1, 2, 3, 5, 6] and inserting 4 will
	 * yield [1, 2, 3, 4, 5, 6]
	 * <br>
	 * when inserting to an empty list, this method does the same thing as adding
	 * to an empty list
	 *
	 * @param element				
	 */
	public void insert( E element ) {
		
		//if no elements have been added, then inserting is the same
		//as adding
		if ( this.m_size == 0 ) {
			add( element );
			
		//if elements have already been added to the list
		} else {
			
			//insert the element between the current element and next element
			Link toAdd = new Link ( element );
			toAdd.setNext( this.m_current.next() );
			this.m_current.setNext( toAdd );
		}
		
		//update the size
		this.m_size ++;
	}
	
	/**
	 * inserts a value at the beginning of the list
	 * 
	 * @param element			value to add at the beginning of the list
	 */
	public void insertHead( E element ) {
		
		//if no elements have been added, this operation is 
		//the same as adding an element to the list
		if ( this.m_size == 0 ) {
			add( element );
			
		//if elements have already been added to the list
		} else {
		
			//modify the first element
			Link toAdd = new Link( element );
			toAdd.setNext( this.m_first );
			this.m_first = toAdd;
			
			//update the head
			this.m_head.setNext( this.m_first );
		}
	}
	
	/**
	 * replaces the current element with a new value
	 * 
	 * @param newValue				new value for the current element
	 */
	public void replace( E newValue ) {
		
		//make sure there is a current element
		if ( this.m_current == null ) {
			throw new NoSuchElementException();
		}
		this.m_current.setValue( newValue );
	}
	
	/**
	 * replaces the next element with a new value.
	 * 
	 * @param newValue					new value for the next element
	 * @throws NoSuchElementException	if there is no element following the current element
	 */
	public void replaceNext( E newValue ) {
		
		//make sure there there is an element following the current one
		if ( this.m_current == this.m_last ) {
			throw new NoSuchElementException();
		}
		
		Link nextElement = this.m_current.next();
		nextElement.setValue( newValue );
	}
	
	/**
	 * removes the current element from the list
	 * the current element still points to the link after the previous element
	 */
	public void remove() {
		
		//make sure we aren't removing from an empty list
		if ( this.m_size == 0 ) {
			throw new NoSuchElementException();
		}
		
		//if we are removing the first link, update the first link to
		//be the second link
		if ( this.m_first == this.m_current ) {
			this.m_first = this.m_first.next();
			
			//update the head
			this.m_head.setNext( this.m_first );
			
			//update the previous and current links
			this.m_previous = null;
			this.m_current = this.m_first;
			
		//if we are removing the last link, update the last link
		//in the list to be the previous link
		} else if ( this.m_last == this.m_current ) {
			this.m_previous.setNext( null );
			this.m_last = this.m_previous;
			
			//update the previous and current links
			this.m_current = null;
			
		//otherwise, remove this link from the list
		//by setting the link after the previous link to be
		//the link after this link
		} else {
			this.m_previous.setNext( this.m_current.next() );
			
			//update the previous and current links
			this.m_current = this.m_previous.next();
		}
		
		this.m_size --;
	}
	
	/**
	 * removes the given element from the list. this method checks by using
	 * <code>==</code> and not <code>equals</code>
	 * 
	 * @param toRemove			a pointer to the element to remove
	 */
	public void remove( E toRemove ) {
		
		for ( E element : this ) {
			if ( element == toRemove ) {
				this.remove();
				break;
			}
		}
	}
	
	/**
	 * removes all elements from this list
	 */
	public void removeAll() {
		
		//reset everything
		this.m_head.setNext( null );
		this.m_first = null;
		this.m_last = null;
		this.m_current = null;
		this.m_previous = null;
		this.m_size = 0;
	}
	
	/**
	 * moves the current element pointer back to the start of the list
	 */
	public void moveToStart() {
		this.m_previous = this.m_head;
		this.m_current = this.m_first;
	}
	
	public void moveToHead() {
		this.m_previous = null;
		this.m_current = this.m_head;
	}
	
	
	/**
	 * @return					if we have passed the end of the list or not
	 */
	public boolean hasCurrent() {
		return this.m_current != null;
	}
	
	/**
	 * @return					if we are at the end of the list or not
	 */
	public boolean hasNext() {
		
		//make sure there is a current element
		if ( hasCurrent() ) {
			return this.m_current.next() != null;
			
		//if there is no current element, then there clearly is no next element
		} else {
			return false;
		}
	}
	
	/**
	 * advances the current element forward by one
	 */
	public void advance() {
		
		//make sure there is an element following the current one
		if ( hasNext() ) {
		
			//advance the previous link pointer
			this.m_previous = this.m_current;
			
			//advance the current link pointer
			this.m_current = this.m_current.next();
		} else {
			throw new NoSuchElementException();
		}
	}
	
	
	/**
	 * @return								the value to which the current element pointer points
	 * @throws NoSuchElementException		if the linked list has already passed its last element
	 */
	public E get() {
		if ( this.m_current == null ) {
			throw new NoSuchElementException();
		} else {
			return this.m_current.value();
		}
	}
	
	/**
	 * gets the next element, but does not advance the linked list
	 * 
	 * @return								value of the next element in the list
	 * @throws NoSuchElementException 		if the current element is the last element in the list
	 */
	public E peek() {
		if ( this.m_current == null || this.m_current.next() == null ) {
			throw new NoSuchElementException();
		}
		return this.m_current.next().value();
	}
	
	/**
	 * @return			the number of elements in this list
	 */
	public int size() {
		return this.m_size;
	}
	
	/**
	 * checks if this list equals another list.
	 * the given object to compare should implement <code>equals(...)</code>
	 */
	@Override
	public boolean equals( Object anObject ) {
		
		//only deal with comparisons with linked lists
		if ( anObject instanceof LinkedList < ? > ) {
			
			LinkedList < ? > toCompare = ( LinkedList < ? > ) anObject;
			//first, make sure the sizes are the same
			if ( this.size() != toCompare.size() ) {
				return false;
			}
			
			//if the sizes of this list and the list to compare are the same,
			//go through and make sure the elements are equal
			Iterator< ? > compareListIterator = toCompare.iterator();
			for ( E elementInThisList : this ) {
				
				//compare the elements of each list
				Object elementToCompare = compareListIterator.next();
				
				//if any two elements at the same index are different,
				//then the lists are not equal
				if ( ! elementInThisList.equals( elementToCompare ) ) {
					return false;
				}
			}
			
			//if all above tests were passed, then the two lists are equal
			return true;
			
		//don't deal with objects that are not of type LinkedList
		} else {
			return super.equals( anObject );
		}
	}
	
	@Override
	public int hashCode() {
		// do not deal with this
		return super.hashCode();
	}
	
	/**
	 * returns a copy of the linked list. the links in the list are deep copies
	 * but the elements to which the links point are shallow.
	 */
	@Override
	public LinkedList < E > clone() {
		LinkedList < E > rtn = new LinkedList < E > ();
		for ( E element : this ) {
			rtn.add( element );
		}
		return rtn;
	}
	
	@Override
	public String toString() {
		moveToStart();
		String rtn = "[";
		
		//add the first element to the representation
		if ( hasCurrent() ) {
			rtn += this.m_current.toString();
			advance();
			
			while ( hasCurrent() ) {
				E nextValue = get();
				rtn += ", " + nextValue.toString();
				advance();
			}
		}
		rtn += "]";
		return rtn;
	}
	
	
	@Override
	public LinkedListIterator iterator() {
		moveToHead();
		return this.m_iterator;
	}


	/**
	 * permits iterating through a linked list. the next value is always the current
	 * element and advancing is done after getting the current value
	 */
	private class LinkedListIterator implements Iterator < E > {

		public LinkedListIterator() {
			
		}
		
		@Override
		public boolean hasNext() {
			return LinkedList.this.hasNext();
		}

		@Override
		public E next() {
			advance();
			E currentElement = get();
			return currentElement;
		}

		@Override
		public void remove() {
			remove();
		}
		
	}
	
	/**
	 * a link in this linked list 
	 * 
	 * @param <E>
	 */
	private class Link {
		
		/**
		 * value of this link
		 */
		private E m_value;
		
		
		/**
		 * next link in the list. the last link in the list has null for its next link
		 */
		private Link m_next;
		
		
		/**
		 * constructs a link with a specified value
		 * 
		 * @param value				value of this link
		 */
		public Link( E value ) {
			this.m_value = value;
			this.m_next = null;
		}
		
	
		/**
		 * constructs a link with the given value and following link
		 * 
		 * @param value				value of this link
		 * @param next				following link
		 */
		public Link( E value , Link next ) {
			this.m_value = value;
			this.m_next = next;
		}
		
		
		/**
		 * changes the value of this link to the specified value
		 * 
		 * @param newValue			new value for this link
		 */
		public void setValue( E newValue ) {
			this.m_value = newValue;
		}
		
		/**
		 * @return			value of this link
		 */
		public E value() {
			return this.m_value;
		}
		
		/**
		 * changes the following link to be the specified link
		 * 
		 * @param next			link to follow this link
		 */
		public void setNext( Link next ) {
			this.m_next = next;
		}
		
		
		/**
		 * @return		next element in the <code>LinkedList</code>
		 */
		public Link next() {
			return this.m_next;
		}
		
	
		@Override
		public String toString() {
			return this.m_value.toString();
		}
	}
}