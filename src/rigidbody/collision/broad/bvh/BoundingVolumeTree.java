package rigidbody.collision.broad.bvh;

import rigidbody.RigidBody;
import rigidbody.collision.broad.PotentialContact;
import _lib.LinkedList;

/**
 * a binary tree representing a hierarchy of <code>BoundingShape</code>s. The parent
 * of one or two children is the smallest <code>BoundingShape</code> that encompasses
 * both children <code>BoundingShape</code>s
 * 
 * @param <T>		the type of <code>BoundingShape</code> used in this tree
 */
public class BoundingVolumeTree < T extends BoundingShape > {
	
	/**
	 * the root of this tree
	 */
	private Node m_root;
	
	/**
	 * constructs an empty <code>BoundingVolumeTree</code>
	 */
	public BoundingVolumeTree() {
		
	}
	
	/**
	 * constructs a <code>BoundingVolumeTree</code> with the given body and 
	 * bounding volume as the root
	 *  
	 * @param body
	 * @param boundingVolume
	 */
	public BoundingVolumeTree( RigidBody body , T boundingVolume ) {
		insert( body , boundingVolume );
	}
	
	public void insert( RigidBody bodyToAdd , T volumeToAdd ) {
		
		//if this is an empty tree, then set the root as the given node
		if ( this.m_root == null ) {
			this.m_root = new Node( bodyToAdd , volumeToAdd );
			
		//if this is not an empty tree, then go through the tree
		} else {
			Node currentNode = this.m_root;
			while ( !currentNode.isLeaf() ) {
				Node leftChild = currentNode.getLeftChild();
				Node rightChild = currentNode.getRightChild();
				
				//add the bounding volume to whichever child's volume growth is least
				//increased
				if ( leftChild.getBoundingVolume().getGrowth( volumeToAdd ).compareTo( 
						rightChild.getBoundingVolume().getGrowth( volumeToAdd ) ) > 0 ) {
					currentNode = rightChild;
				} else {
					currentNode = leftChild;
				}
			}
			
			//if we reach a leaf
			//we must add in the new bounding volume
			Node currentLeaf = currentNode;
			
			//determine the new children
			Node leafLeftChild = new Node( currentLeaf.getBody() , currentLeaf.getBoundingVolume() );
			Node leafRightChild = new Node( bodyToAdd , volumeToAdd );
			
			//add the children
			currentLeaf.setLeftChild( leafLeftChild );
			currentLeaf.setRightChild( leafRightChild );
			
			//the leaf no longer contains one body once it receives two children
			//it now contains two bodies in that its children nodes take care of
			currentLeaf.clearBody();
		
			//lastly, the leaf's bounding volume must be recalculated on the addition
			//of children
			currentLeaf.recalculateBoundingVolume();
		}
	}
	
	/**
	 * removes the given <code>RigidBody</code> from the <code>BoundingVolumeTree</code>.
	 * comparison is done by pointer reference and NOT by <code>equals()</code>. no
	 * effect occurs if the given <code>RigidBody</code> is not in this tree
	 * 
	 * @param body		the <code>RigidBody</code> to remove
	 */
	public void delete( RigidBody body ) {
		
		//check if this tree is empty
		if ( this.m_root == null ) {
			
			//because there is nothing to do if the tree is empty
			return;
		} else {
			
			//check the root of this tree
			//if the root of this tree contains the body we want to remove
			//then this tree has only one element
			//so empty the tree
			if ( this.m_root.getBody() == body ) {
				this.m_root = null;
				return;
				
			//otherwise, search the tree for the body to remove
			} else {
				delete( this.m_root , body );
			}
		}
	}
	
	/**
	 * removes the given <code>RigidBody</code> from the <code>BoundingVolumeTree</code>.
	 * comparison is done by pointer reference and NOT by <code>equals()</code>
	 * 
	 * @param currentNode		the node at which we are in the tree
	 * @param body				the <code>RigidBody</code> to remove
	 */
	protected void delete( Node currentNode , RigidBody body ) {
		
		//check the children of this node for the body to remove, if possible
		//if the current node is a leaf, then the body to remove is not here
		if ( currentNode.isLeaf() ) {
			return;
			
		//otherwise, check the children
		} else {
			
			//determine the children
			Node leftChild = currentNode.getLeftChild();
			Node rightChild = currentNode.getRightChild();
			
			//check the children for the body to remove
			//if either child has the body to remove,
			if ( leftChild.getBody() == body || rightChild.getBody() == body ) {
				
				Node sibling;
				if ( leftChild.getBody() == body ) {
					sibling = rightChild;
				} else {
					sibling = leftChild;
				}
				
				//delete it by replacing its parent with the child's sibling
				Node parent = currentNode;
				parent.setBody( sibling.getBody() );
				parent.setBoundingVolume( sibling.getBoundingVolume() );
				parent.setLeftChild( sibling.getLeftChild() );
				parent.setRightChild( sibling.getRightChild() );
				
			} else {
				delete( leftChild , body );
				delete( rightChild , body );
			}
			
		}
	}
	
	/**
	 * generates possible contacts between <code>RigidBody</code> objects
	 * 
	 * @param limit			the maximum number of contacts to generate.
	 * @return				a list of possible contacts between <code>RigidBody</code> objects
	 */
	public LinkedList < PotentialContact > getPotentialContacts( int limit ) {
		return getPotentialContactAt( this.m_root , limit );
	}
	
	
	//TODO test this
	/**
	 * determines possible contacts between <code>RigidBody</code> objects beginning
	 * at the given node
	 * 
	 * @param currentNode			the starting node
	 * @param limit					maximum number of contacts to generate
	 * @return						a list of possible contacts between <code>RigidBody</code> objects
	 */
	protected LinkedList < PotentialContact > getPotentialContactAt( Node currentNode , int limit ) {
		
		//if we're at a leaf, no potential contacts are possible
		//and if the limit is zero, we cannot generate any contacts
		if ( currentNode.isLeaf() || limit <= 0 ) {
			return new LinkedList < PotentialContact > ();
		} else {
			
			if ( currentNode.getLeftChild().getBoundingVolume().touches( currentNode.getRightChild().getBoundingVolume() ) ) {
				LinkedList < PotentialContact > rtn = new LinkedList < PotentialContact > ();
				int updatingLimit = limit;
				
				//if both children are leaves, then there is one contact
				if ( currentNode.getLeftChild().isLeaf() && currentNode.getRightChild().isLeaf() ) {
						PotentialContact contact = new PotentialContact( currentNode.getLeftChild().getBody() , currentNode.getRightChild().getBody() );
						rtn.add( contact );
						
						//subtract 1 from the limit
						updatingLimit -= 1;
				} else {
					
					//if the left child is not a leaf, search the left child's children
					if ( !currentNode.getLeftChild().isLeaf() ) {
						LinkedList < PotentialContact > leftChildrenContacts = getPotentialContactAt( currentNode.getLeftChild() , updatingLimit );
						
						//update the limit
						updatingLimit -= leftChildrenContacts.size();
						
						//add the left children contacts to the master list
						for ( PotentialContact contact : leftChildrenContacts ) {
							rtn.add( contact );
						}
					}
					
					//if we have room for more contacts and
					//the right child is not a leaf, search the right child's children
					if ( updatingLimit > 0 && !currentNode.getRightChild().isLeaf() ) {
						LinkedList < PotentialContact > rightChildrenContacts = getPotentialContactAt( currentNode.getRightChild() , updatingLimit );
						
						//update the limit
						updatingLimit -= rightChildrenContacts.size();
						
						//add the right children contacts to the master list
						for ( PotentialContact contact : rightChildrenContacts ) {
							rtn.add( contact );
						}
					}
				}
				
				//return the contacts
				return rtn;
				
			//if the current node's children's bounding volumes do not touch, then none of their
			//children can possibly touch either
			} else {
				return new LinkedList < PotentialContact > ();
			}
		}
	}
	
	/**
	 * stores a <code>RigidBody</code> and a <code>BoundingShape</code>. the bounding volume
	 * encompasses the rigidbody
	 */
	private class Node {
		
		/**
		 * the <code>RigidBody</code> encompassed by the <code>BoundingShape</code>
		 * stored in this node
		 */
		private RigidBody m_body;
		
		/**
		 * the <code>BoundingShape</code> surrounding the <code>RigidBody</code> stored
		 * in this node
		 */
		private T m_boundingVolume;
		
		/**
		 * the left child of this node
		 */
		private Node m_leftChild;
		
		/**
		 * the right child of this node
		 */
		private Node m_rightChild;
		
		
		/**
		 * creates a <code>Node</code> in a <code>BoundingVolumeTree</code>
		 * representing the given body with the given bounding volume. the node
		 * has the specified parent in the tree
		 * 
		 * @param body
		 * @param boundingShape
		 * @param parent					the parent of this node, or null if this node is the root of the tree
		 */
		public Node( RigidBody body , T boundingShape ) {
			this.m_body = body;
			this.m_boundingVolume = boundingShape;
		}
		
		/**
		 * @return		the <code>RigidBody</code> encompassed by the <code>BoundingShape</code>
		 * 				stored in this node
		 */
		final protected RigidBody getBody() {
			return this.m_body;
		}
		
		/**
		 * sets the <code>RigidBody</code> of this node
		 * 
		 * @param body
		 */
		final protected void setBody( RigidBody body ) {
			this.m_body = body;
		}
		
		/**
		 * sets the <code>BoundingShape</code> surrounding the <code>RigidBody</code> stored
		 * in this node
		 * 
		 * @param shape
		 */
		final protected void setBoundingVolume( T shape ) {
			this.m_boundingVolume = shape;
		}
		
		/**
		 * @return		the <code>BoundingShape</code> surrounding the <code>RigidBody</code> stored
		 * 				in this node
		 */
		final protected T getBoundingVolume() {
			return this.m_boundingVolume;
		}
		
		/**
		 * removes the <code>RigidBody</code> associated with this node
		 * when this node receives children
		 */
		final protected void clearBody() {
			this.m_body = null;
		}
		
		/**
		 * sets the left child of this node
		 * 
		 * @param node
		 */
		final protected void setLeftChild( Node node ) {
			this.m_leftChild = node;
		}
		
		/**
		 * @return			the left child of this node
		 */
		final public Node getLeftChild() {
			return this.m_rightChild;
		}
		
		/**
		 * sets the right child of this node
		 * 
		 * @param node
		 */
		final protected void setRightChild( Node node ) {
			this.m_rightChild = node;
		}
		
		/**
		 * @return		the right child of this node
		 */
		final protected Node getRightChild() {
			return this.m_rightChild;
		}
		
		/**
		 * recalculates the <code>BoundingVolume</code> of this node
		 * once it has received two children
		 */
		final protected void recalculateBoundingVolume() {
			this.m_boundingVolume = ( T ) this.getLeftChild().getBoundingVolume().calculateEnclosingShape( this.getRightChild().getBoundingVolume() );
		}
		
		/**
		 * @return			if this node is a leaf, i.e. if this node has no children
		 */
		final protected boolean isLeaf() {
			return this.m_leftChild == null && this.m_rightChild == null;
		}
	}

}
