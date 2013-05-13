/* 
 * Syscall.java - Copyright 2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.parser.macro;

import java.util.Iterator;
import java.util.LinkedList;

import de.tum.mocca.parser.ParseException;

/**
 * The %syscall macro specifies a system call. The first parameter specifies the
 * function name, 2nd and following are parameters in STDCALL convention.
 * Anchor point of the macro is the call instruction to the function passed in
 * param 1.
 *
 * @author Johannes Kinder
 */
public class Syscall extends Macro {
	
	public final static String name = "syscall";

	/**
	 * @param parameters
	 * @throws ParseException
	 */
	public Syscall(int id, String[] parameters) throws ParseException {
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
	 * @see de.tum.mocca.parser.macro.Macro#getAppendix()
	 */
	public String getAppendix() {
		// If the function has no parameters, the appendix is empty
		if (parameters.length <= 1) return "";
		/* this list is used to determine which variables are only used locally
		 * and can be existentially quantified around the call definition */
		LinkedList localVars = new LinkedList();
		// In paramInit we build the formula that binds parameters to a call
		String paramInit = "EF";
		// In varInit we build the formula that initializes the parameters
		String varInit = "";
		for (int i=1; i<parameters.length; i++) {
			/* immediate parameters are pushed directly onto the stack
			   and wildcards don't need initialization */
			if (parameters[i].equals("$*") || parameters[i].startsWith("$i")) {
				paramInit += 
					"(push(" + parameters[i] + ") & EX(E %nostack U ";
			}
			/* Else assume the value is being put in a register before pushing */
			else {
				/* This precedes the call */
				/*
				paramInit += 
					"(push($rp" + i + ") & #loc($Lc" + id + "p" + i + 
					") & EX(E %nostack U ";
					*/
				/* We now allow both indirect and direct passing, w00t! */
				paramInit += 
					"(push($*) & #loc($Lc" + id + "p" + i +	") & EX(E %nostack U ";
				
				localVars.add("$rp" + i);
				localVars.add("$Lc" + id + "p" + i);
				/* The following creates the subformula for variable initialization
				 * which is put in the appendix */

				/* Variables with $p at start are treated as pointers, 
				 * their address is loaded with lea */
				if (parameters[i].startsWith("$p"))
					varInit += "& (EF(lea($rp" + i + "," + parameters[i] + ")";

				// First possibility is a direct push
				//varInit += ;
				/* Everything else uses direct push or mov */
				else varInit += "& (EF(push(" + parameters[i] + ") & #loc($Lc" + id + "p" + i +	")) | " +
				                   "EF(mov($rp" + i + "," + parameters[i] + ")";
				varInit += "& EX(E %noassign($rp" + i + ") U (push($rp" + i +
				") & #loc($Lc" + id + "p" + i + ")))))";
			}
		}
		/* Add the same call and location predicates as in the expansion */
		paramInit += getExpansion();
		// Close 2*paramcount parentheses in the parameter to call binding
		for (int i=1; i<parameters.length; i++) paramInit += "))";

		// generate the exists quantifiers
		String appendix = "& (";
		for (Iterator iter = localVars.iterator(); iter.hasNext();) {
			String var = (String) iter.next();
			appendix += "exists " + var + " ";
		}
		// The second closing parenthesis corresponds to the one before the exists
		appendix += "(" + paramInit + varInit + "))";
		return appendix;
	}
	
	/**
	 * @see de.tum.mocca.parser.macro.Macro#getExpansion()
	 */
	public String getExpansion() {
		return "(call(" + parameters[0] + ") & #loc($Lc" + id + "))";
	}
}
