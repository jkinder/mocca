/* 
 * CtplTreeNode.java - Copyright 2004 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.ctpl;




/**
 * Tree class to represent CTL-Formulas.
 *
 * @author Johannes Kinder
 */
public class CtplTreeNode {

	protected CtplTreeNode[] children;
	protected CtplTreeNode parent;
	protected CtplAtom value;
	protected int numChildren;
	
	private final int MAX_CHILDREN = 2;  //2 is enough, CTL only has binary operators
	
	/**
	 * Initializes a new treenode with no parents nor children
	 * and the given CTL Atom as value.
	 * @param value The atom to be contained in this node.
	 */
	public CtplTreeNode(CtplAtom value) {
		this.children = new CtplTreeNode[MAX_CHILDREN];
		this.value = value; 
		this.parent = null;
		this.numChildren = 0;
	}
	
	/**
	 * Adds a child to the node and sets this node as parent
	 * at the child node.
	 * @param child The node to add.
	 * @throws IllegalArgumentException If the node is already at its maximum capacity.
	 */
	public void addChild(CtplTreeNode child) {
		if (numChildren >= MAX_CHILDREN) 
			throw new IllegalArgumentException("Node does not support more than " + MAX_CHILDREN + "children"); 
		children[numChildren++] = child;
		child.parent = this;
	}
	
	/**
	 * Returns the value contained in this node.
	 * @return The value.
	 */
	public CtplAtom getValue() {
		return value;
	}
	
	/**
	 * Returns the degree of this node, i.e. the number of children.
	 * @return The degree.
	 */
	public int getDegree() {
		return numChildren;
	}
	
	/**
	 * Returns the child at the specified index.
	 * @param index
	 * @return the child node
	 * @throws IllegalArgumentException If there is no child with that index.
	 */
	public CtplTreeNode getChild(int index) {
		if (index >= numChildren) 
			throw new IllegalArgumentException("No child with index " + index); 
		return children[index];
	}
	
	/**
	 * Returns the parent of this node.
	 * @return the parent node.
	 * @throws UnsupportedOperationException If this node has no parent.
	 */
	public CtplTreeNode getParent() {
		if (parent == null) 
			throw new UnsupportedOperationException("Root node has no parent.");
		return parent;
	}
	
	/**
	 * Returns the total number of objects contained in the
	 * subtree starting with this node (NOT the height).
	 * @return Number of objects.
	 */
	public int getSize() {
		int size = 1;
		for (int i = 0; i < numChildren; i++) {
			size += children[i].getSize();
		}
		return size;
	}

	/**
	 * Checks whether this is a leafnode.
	 * @return Returns (degree == 0)
	 */
	public boolean isLeaf() {
		return (numChildren == 0);
	}
	
	/**
	 * @see CtplPredicate#toString()
	 */
	public String toString() {
		String r;
		if (isLeaf()) r = value.toString();
		else {
			r = value + "(";
			r += children[0].toString();
			for  (int i = 1; i < numChildren; i++) 
				r += "," + children[i].toString();
			r += ")";
		}
		return r;
	}
	
	/**
	 * Two CtlTreeNodes are equal if they and all their children
	 * are equal. This operation is linear to the treesize.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		CtplTreeNode t = (CtplTreeNode)arg0;
		if (!t.value.equals(this.value)
			|| (t.numChildren != numChildren))
			return false;
		for (int i = 0; i < numChildren; i++)
			if (!children[i].equals(t.children[i])) return false;
		return true;
	}
	
	/** 
	 * Very cheap hashCode implementation by calling toString()
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return toString().hashCode() + 1;
	}
}
