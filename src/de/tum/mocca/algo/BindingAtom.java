/* 
 * BindingAtom.java - Copyright 2004 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.algo;

/**
 * A statement of (variable = constant) or (variable != constant).
 *
 * @see de.tum.mocca.algo.BindingClause
 * @author Johannes Kinder
 */
public class BindingAtom {
	public static final int EQUAL = 0;
	public static final int NOT_EQUAL = 1;
	
	/**
	 * Creates a new BindingAtom as the negation of the given
	 * atom, i.e. changes the type of the copy to the opposite.
	 * @param a The BindingAtom to be negated.
	 * @return Returns the new negated BindingAtom.
	 */
	public static BindingAtom negate(BindingAtom a) {
		return new BindingAtom(a.variable, 
				a.constant, 
				(a.type == EQUAL) ? NOT_EQUAL : EQUAL);
	}
	
	private String variable;
	private String constant;
	private int type;
	
	/**
	 * @param variable
	 * @param constant
	 * @param type
	 */
	public BindingAtom(String variable, String constant, int type) {
		this.variable = variable;
		this.constant = constant;
		this.type = type;
	}
	
	/**
	 * @return Returns the constant.
	 */
	
	public String getConstant() {
		return constant;
	}
	
	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * @return Returns the variable.
	 */
	public String getVariable() {
		return variable;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	protected Object clone() {
		return new BindingAtom(this.variable, this.constant, this.type);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (arg0 instanceof BindingAtom)
			return (((BindingAtom)arg0).constant.equals(this.constant))
			&& (((BindingAtom)arg0).variable.equals(this.variable))
			&& (((BindingAtom)arg0).type == this.type);
		else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return constant.hashCode() + variable.hashCode() + type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return variable + ((type == EQUAL) ? "=" : "!=") 
				+ constant;
	}
}
