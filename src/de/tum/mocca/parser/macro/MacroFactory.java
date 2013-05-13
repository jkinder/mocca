/* 
 * MacroFactory.java - Copyright 2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.parser.macro;

import de.tum.mocca.parser.ParseException;

/**
 * Instantiates the correct subclass of the Macro class, depending on the
 * name parsed from the specification file.
 *
 * @author Johannes Kinder
 */
public class MacroFactory {
	
	/**
	 * 
	 */
	public MacroFactory() {
	}
	
	/**
	 * Instantiates a new Macro object.
	 * @param id A unique id that should precede any local variables.
	 * @param name The name of the Macro, needed for determining the correct class.
	 * @param parameters The paramters as a string array.
	 * @return Returns the newly created Macro object.
	 * @throws ParseException
	 */
	public Macro create(int id, String name, String[] parameters) throws ParseException {
		if (name.equals(Syscall.name)) return new Syscall(id, parameters);
		else if (name.equals(NoAssign.name)) return new NoAssign(id, parameters);
		else if (name.equals(NoStack.name)) return new NoStack(id, parameters);
		else if (name.equals(Sysfunc.name)) return new Sysfunc(id, parameters);
		throw new ParseException("Macro " + name + " is not defined.");
	}

}
