/* 
 * CtplQuantifier.java - Copyright 2004 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.ctpl;

import de.tum.mocca.parser.CtplParserTreeConstants;

/**
 * A class for CTL Quantifiers. A quantifier may be of the type
 * EXISTS or ALL. It adds implicit parameters to the CtplOperator
 * class (support for the quantified variables).
 *
 * @author Johannes Kinder
 */
public class CtplQuantifier extends CtplOperator {
	
	public static final int EXISTS = CtplParserTreeConstants.JJTEXISTS;
	//public static final int ALL = CtlParserTreeConstants.JJTALL;
	
	protected String parameter = "";

	/**
	 * @param type
	 */
	public CtplQuantifier(int type, String parameter) {
		super(type);
		this.parameter = parameter;
	}

	/**
	 * @return Returns a string representation of this operator. 
	 */
	public String toString() {
		String r;
		switch (type) {
		case EXISTS: 
			r = "exists";
			break;
/*		case ALL: 
			r = "all";
			break;
*/		default: 
			r = "Invalid type";
		}
		r += " " + parameter + " ";
		return r;
	}
	
	/**
	 * @return Returns the parameter.
	 */
	public String getParameter() {
		return parameter;
	}
	
	/**
	 * Checks for equality with a type of quantifier. Note that
	 * this is not checking the equality of the quantified
	 * variable, merely the type of quantification!
	 * @return True if this quantifier is of the specified type.
	 * @see CtplAtom#equals(int)
	 */
	public boolean equals(int arg0) {
		return this.type == arg0;
	}

	/**
	 * Two operators are equal if and only if their types are equal.
	 * @see Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (arg0 instanceof CtplQuantifier)
			return (((CtplQuantifier)arg0).type == this.type) &&
			(((CtplQuantifier)arg0).parameter.equals(this.parameter));
		else return false;
	}

}
