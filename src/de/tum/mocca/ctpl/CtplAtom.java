/* 
 * CtplAtom.java - Copyright 2004 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.ctpl;


/**
 * The superclass of all CTPL syntax elements. 
 *
 * @author Johannes Kinder
 */
public abstract class CtplAtom {
	
	protected int type = -1;
	
	/**
	 * Default constructor
	 */
	protected CtplAtom() {
		super();
	}
	
	/**
	 * Equals with integer argument is always false for all 
	 * atoms that are no operators.
	 * 
	 * @param id a CtplOperator constant
	 * @see CtplOperator#equals(int)
	 */
	public boolean equals(int id) {
		return false;
	}
}
