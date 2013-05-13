/* 
 * NoAssign.java - Copyright 2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.parser.macro;

import de.tum.mocca.parser.ParseException;

/**
 * Specifies the %noassign macro. The resulting formula prohibits the given variable
 * from being assigned any value in the current state.
 *
 * @author Johannes Kinder
 */
public class NoAssign extends Macro {

	public final static String name = "noassign";
	
	/**
	 * @param id
	 * @param parameters
	 * @throws ParseException
	 */
	public NoAssign(int id, String[] parameters) throws ParseException {
		super(id, parameters);
	}

	/**
	 * @see de.tum.mocca.parser.macro.Macro#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see de.tum.mocca.parser.macro.Macro#getParameterCount()
	 */
	public int getParameterCount() {
		return 1;
	}

	/**
	 * @see de.tum.mocca.parser.macro.Macro#getExpansion()
	 */
	public String getExpansion() {
		return "(-mov(" + parameters[0] + ", $*) & -lea(" + parameters[0] + ", $*))"; 
	}

	/**
	 * @see de.tum.mocca.parser.macro.Macro#getAppendix()
	 */
	public String getAppendix() {
		return "";
	}

}
