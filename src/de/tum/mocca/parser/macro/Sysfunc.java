/* 
 * Sysfunc.java - Copyright 2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.parser.macro;

import de.tum.mocca.parser.ParseException;

/**
 * The %sysfunc macro specifies a system call that returns a value. The first
 * parameter specifies the variable which the return value is assigned to. 
 * The second parameter is the function name, 3rd and following are parameters
 * in STDCALL convention.
 * Anchor point of the macro is the assignment of the return value to the 
 * variable in param 1.
 *
 * @author Johannes Kinder
 */
public class Sysfunc extends Macro {
	
	public final static String name = "sysfunc";
	// private Macro syscall;

	/**
	 * @param id
	 * @param parameters
	 * @throws ParseException
	 */
	public Sysfunc(int id, String[] parameters) throws ParseException {
		super(id, parameters);
/*		String[] syscallParams = new String[parameters.length - 1];
		for (int i=1; i<parameters.length; i++)
			syscallParams[i-1] = parameters[i];
		syscall = new Syscall(id, syscallParams);
*/
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
		return 2;
	}

	/**
	 * @see de.tum.mocca.parser.macro.Macro#getExpansion()
	 */
	public String getExpansion() {
		return "(mov(" + parameters[0] + ", eax) & #loc($Lc" + id + "))";
	}

	/**
	 * @see de.tum.mocca.parser.macro.Macro#getAppendix()
	 */
	public String getAppendix() {
		String syscallParams = parameters[1];
		for (int i=2; i<parameters.length; i++)
			syscallParams += ", " + parameters[i];

		//return syscall.getAppendix() + "& EF(" + syscall.getExpansion() + " & EX(E (%noassign(eax) & -call($*)) U (mov(" + parameters[0] + ", eax))))";
		return "& EF(%syscall(" + syscallParams + ") & EX(E (%noassign(eax) & -call($*)) U (mov(" + parameters[0] + ", eax) & #loc($Lc" + id + "))))";
	}

}
