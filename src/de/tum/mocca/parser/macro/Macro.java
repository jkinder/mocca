/* 
 * Macro.java - Copyright 2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.parser.macro;

import de.tum.mocca.parser.ParseException;

/**
 * This is the abstract parent class to all macros. These are created by a
 * MacroFactory depending on the parsed name. Contains functions needed for
 * macro evaluation.
 * Every macro to be expanded is created as an own object, that returns its
 * expansion by the two functions getExpansion and getAppendix.
 *
 * @author Johannes Kinder
 */
public abstract class Macro {

	protected String[] parameters;
	protected int id;
	
	/**
	 * Expands the macro with respect to the given parameters.
	 * If the parameters are fewer than required, throws a ParseException.
	 * @param id A unique id that should precede any local variables.
	 * @param parameters The paramters as a string array.
	 * @throws ParseException
	 */
	public Macro(int id, String[] parameters) throws ParseException {
		if (parameters.length < getParameterCount()) 
			throw new ParseException("Too few parameters to macro " + getName() + "!");
		this.parameters = parameters;
		this.id = id;
	}
	
	/**
	 * @return Returns the name of the macro. 
	 */
	public abstract String getName();
	
	/**
	 * @return Returns the number of parameters required.
	 */
	public abstract int getParameterCount();
	
	/**
	 * Returns the text that shall replaces the macro call in the formula.
	 * @return Returns the expanded text.
	 */
	public abstract String getExpansion();
	
	/**
	 * Returns text that should be added at the end of the formula as a
	 * sideeffect of the expansion. 
	 * @return Returns text expanded to the appendix.
	 */
	public abstract String getAppendix();
}
