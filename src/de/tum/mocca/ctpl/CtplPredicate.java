/* 
 * CtplPredicate.java - Copyright 2004 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.ctpl;

import de.tum.mocca.algo.BindingClause;
import de.tum.mocca.parser.CtplParserTreeConstants;


/**
 * A general CTL Predicate
 *
 * @author Johannes Kinder
 */
public class CtplPredicate extends CtplAtom {

	protected String body;
	protected String[] parameters;
	public static final char VARIABLE_PREFIX = '$';
	public static final char WILDCARD_ALL = '*';
	
	/**
	 * Default constructor
	 */
	protected CtplPredicate() {
		type = CtplParserTreeConstants.JJTPREDICATE;
	}

	/**
	 * Initializes a new CtplPredicate with a body, i.e. the name
	 * of the predicate, and an array of strings holding the 
	 * parameters in correct order. 
	 * @param body The predicate body
	 * @param parameters The parameters of the predicate 
	 */
	public CtplPredicate(String body, String[] parameters) {
		this();
		this.body = body;
		this.parameters = parameters;
		if (parameters == null) parameters = new String[0];
	}
	
	/**
	 * @return Returns the parameters.
	 */
	public String[] getParameters() {
		return parameters;
	}

	/**
	 * Returns the parameter at index i 
	 * @param i The index
	 * @return The parameter at index i.
	 */
	public String getParameter(int i) {
		if (i >= parameters.length) throw new IndexOutOfBoundsException("No such parameter");
		return parameters[i];
	}

	/**
	 * Tries to unify the predicate with another. If successful,
	 * creates and returns the corresponding BindingClause object. If
	 * unsuccessful, returns null.
	 * If no substitution is needed for the unification (i.e. the
	 * predicates are identical), returns the empty binding.
	 * In order to be eligible for substitution, all variables 
	 * must start with a dollar sign '$'. 
	 * @param p Other predicate
	 * @return Returns successful binding or null.
	 */
	public BindingClause unify(CtplPredicate p) {
		//System.out.println(this + " " + p + "(" + this.parameters.length +","+ p.parameters.length + ")");
		if (!(this.body.equals(p.body)) 
				|| (this.parameters.length != p.parameters.length))
			return null;
		BindingClause binding = new BindingClause();
		for (int i=0; i<this.parameters.length; i++)
		{
			if (this.parameters[i].equals(p.parameters[i])) continue;
			// TODO: different types of variables needed?
			if (this.parameters[i].charAt(0) == VARIABLE_PREFIX) {
				// if the variable is $*, don't bind a variable. 
				if (this.parameters[i].charAt(1) == WILDCARD_ALL) continue;
				if (!binding.bind(this.parameters[i], p.parameters[i]))
					return null;
			}
			else if (p.parameters[i].charAt(0) == VARIABLE_PREFIX) {
				if (p.parameters[i].charAt(1) == WILDCARD_ALL) continue;
				if (!binding.bind(p.parameters[i], this.parameters[i]))
					return null;
			}
			else return null;
		}
		return binding;
	}

	/**
	 * Returns a string displaying the predicate in prefix notation.
	 * @return the predicate as string.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String r = body + "(";
		if (parameters != null) { 
			if (parameters.length >= 1) r+= parameters[0];
			for (int i=1; i<parameters.length; i++) r+= "," + parameters[i];
		}
		return r + ")"; 
	}
	
	/**
	 * Two predicates are equal if body and parameters are equal.
	 * @param arg0 Other predicate
	 * @return Returns true if equal.
	 */
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof CtplPredicate)
				|| !((CtplPredicate)arg0).body.equals(body) 
				|| (this.parameters.length != ((CtplPredicate)arg0).parameters.length)) 
			return false;
		for (int i = 0; i < parameters.length; i++)
			if (!(parameters[i].equals(((CtplPredicate)arg0).parameters[i]))) 
				return false;
		return true;
	}
	
	/**
	 * Needed because equals is overridden.
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return toString().hashCode() + 1;
	}
	
	/**
	 * @return Returns the body.
	 */
	public String getBody() {
		return body;
	}
}
