/* 
 * CtplOperator.java - Copyright 2004 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.ctpl;

import de.tum.mocca.parser.CtplParserTreeConstants;

/**
 * A class for CTPL Operators. Every operator must be of a type
 * listed as one of the constants in this class.
 *
 * @author Johannes Kinder
 */
public class CtplOperator extends CtplAtom {
	public static final int AF = CtplParserTreeConstants.JJTAF;
	public static final int AX = CtplParserTreeConstants.JJTAX;
	public static final int AG = CtplParserTreeConstants.JJTAG;
	public static final int AU = CtplParserTreeConstants.JJTAU;
	public static final int EF = CtplParserTreeConstants.JJTEF;
	public static final int EX = CtplParserTreeConstants.JJTEX;
	public static final int EG = CtplParserTreeConstants.JJTEG;
	public static final int EU = CtplParserTreeConstants.JJTEU;
	public static final int AND = CtplParserTreeConstants.JJTAND;
	public static final int OR = CtplParserTreeConstants.JJTOR;
	public static final int NOT = CtplParserTreeConstants.JJTNOT;
	
	/**
	 * Create a new Operator using one of the constants in this 
	 * class as type.
	 * @param type The type of the operator. 
	 */
	public CtplOperator(int type) {
		this.type = type;
	}
	
	/**
	 * @return Returns a string representation of this operator. 
	 */
	public String toString() {
		switch (type) {
		case AF: return "AF";
		case AX: return "AX";
		case AG: return "AG";
		case AU: return "AU";
		case EF: return "EF";
		case EX: return "EX";
		case EG: return "EG";
		case EU: return "EU";
		case AND: return "&";
		case OR: return "|";
		case NOT: return "-";
		default: return "Invalid type";
		}
	}

	/**
	 * Checks for equality with a type of operator
	 * @return True if this operator is of the specified type.
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
		if (arg0 instanceof CtplOperator)
			return ((CtplOperator)arg0).type == this.type;
		else return false;
	}
}
