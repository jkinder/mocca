/* 
 * NoStack.java - Copyright 2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.parser.macro;

import de.tum.mocca.parser.ParseException;

/**
 * Specifies the %nostack macro. The resulting formula prohibits the stack
 * from being changed in the current state.
 *
 * @author Johannes Kinder
 */
public class NoStack extends Macro {

	public static String name = "nostack";
	
	/**
	 * @param id
	 * @param parameters
	 * @throws ParseException
	 */
	public NoStack(int id, String[] parameters) throws ParseException {
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
		return 0;
	}

	/**
	 * @see de.tum.mocca.parser.macro.Macro#getExpansion()
	 */
	public String getExpansion() {
		return "(-push($*) & -pop($*))"; 
	}

	/**
	 * @see de.tum.mocca.parser.macro.Macro#getAppendix()
	 */
	public String getAppendix() {
		return "";
	}

}
