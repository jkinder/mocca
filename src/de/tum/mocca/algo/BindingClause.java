/* 
 * BindingClause.java - Copyright 2004 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.algo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * A BindingClause object holds a conjunction of BindingAtoms, thus
 * defining constraints on free variables for a formula to hold in 
 * the model. 
 * 
 * A BindingClause class that has no entries is the empty clause
 * and causes the whole BindingSet to become a tautology.
 * 
 * If a BindingClause object is null, i.e. an invalid binding, then
 * the operation returning this invalid binding has found 
 * that there is no binding that satisfies the requested 
 * merging or unification.  
 *
 * @author Johannes Kinder
 * @see de.tum.mocca.algo.BindingSet
 */
public class BindingClause {
	
	private Logger log = Logger.getLogger(BindingClause.class.getName());
	
	/**
	 * The empty BindingClause that represents a "match in all cases".
	 * Can be used for comparing Bindings to it.
	 */
	public static final BindingClause emptyClause = new BindingClause();
	
	/**
	 * Returns a copy of the given BindingClause with the BindingAtom
	 * containing the supplied variable removed. The BindingClause
	 * can become empty this way. If this BindingClause did not
	 * contain the variable, the copy of this BindingClause is
	 * returned unchanged.
	 * @param b The clause from which the variable should be removed.
	 * @param var The variable to be removed.
	 * @return A new BindingClause with the variable unbound.
	 */
	public static BindingClause purge(BindingClause b, String var) {
		BindingClause res = new BindingClause();
		for (Iterator iter = b.atoms.iterator(); iter.hasNext();) {
			BindingAtom e = (BindingAtom) iter.next();
			if (!(e.getVariable().equals(var)))
				res.add(e);
		}
		return res;
	}
	
	/**
	 * Negates a BindingClause, creating a BindingSet as result 
	 * (deMorgan).
	 * @return Returns the BindingSet that is the negation of this BindingClause.
	 */
	public static BindingSet negate(BindingClause b) {
		if (b.equals(BindingClause.emptyClause)) return null;
		BindingSet res = new BindingSet();
		for (Iterator iter = b.atoms.iterator(); iter.hasNext();) {
			BindingAtom e = (BindingAtom) iter.next();
			// create a new clause that will have only one atom.
			BindingClause t = new BindingClause();
			t.add(BindingAtom.negate(e));
			res.add(t);
		}
		return res;
	}
	
	/**
	 * Returns a new binding as the result of a boolean and of
	 * two BindingClauses. If the two Bindings contradict each other,
	 * null is returned.
	 * @param b1 The first BindingClause.
	 * @param b2 The other BindingClause.
	 * @return Returns the merged binding or null.
	 */
	public static BindingClause and(BindingClause b1, BindingClause b2) {
		BindingClause res = (BindingClause)b1.clone();
		
		for (Iterator iter = b2.atoms.iterator(); iter.hasNext();) {
			BindingAtom e = (BindingAtom) iter.next();
			if (!res.add((BindingAtom)e.clone())) return null;
		}
		return res;
	}
	
	private HashSet atoms;
	
	/**
	 * Initializes a new empty BindingClause object with a default 
	 * capacity.
	 */
	public BindingClause() {
		atoms = new HashSet(20);
	}
	
	protected BindingClause(HashSet atoms) {
		this.atoms = atoms;
	}
	
	/**
	 * Adds a new pair (variable x constant) to this binding. If the
	 * variable was already bound to another constant in this BindingClause,
	 * returns false without adding the pair.
	 * If the pair is forbidden in this BindingClause, returns false. 
	 * Any other not-equal-elements based on the variable are removed.
	 * If the variable was already bound to the same constant, returns
	 * true. Otherwise adds the pair and returns true.
	 * @param variable A string holding the name of the variable.
	 * @param constant The constant value the variable is to be bound to.
	 * @return True if the pair did not cause a contradiction.
	 */
	public boolean bind(String variable, String constant) {
		List removalList = new LinkedList();
		for (Iterator iter = atoms.iterator(); iter.hasNext();) {
			BindingAtom e = (BindingAtom) iter.next();
			if (e.getVariable().equals(variable))
			{
				if (e.getType() == BindingAtom.EQUAL) {
					if (!e.getConstant().equals(constant)) return false;
					else return true;
				} else /* NOT_EQUAL */ {
					if (e.getConstant().equals(constant)) return false;
					else removalList.add(e); // v != c2 is replaced by v = c1
				}
			}
		}
		
		// Remove the replaced elements.
		if (removalList.size() != 0) log.info("Purging " + removalList);
		for (Iterator iter = removalList.iterator(); iter.hasNext();)
			atoms.remove(iter.next());
		
		atoms.add(new BindingAtom(variable, constant, BindingAtom.EQUAL));
		return true;
	}
	
	/**
	 * Forbids a certain (variable x constant) pair in this binding. 
	 * If this pair was already forbidden in this BindingClause, returns 
	 * false without adding the pair.
	 * A variable may be part of several forbidden pairs.
	 * Otherwise adds the pair and returns true.
	 * @param variable A string holding the name of the variable.
	 * @param constant The constant value the variable is not allowed to be bound to.
	 * @return True if the pair is new, else false.
	 */
	public boolean forbid(String variable, String constant) {
		for (Iterator iter = atoms.iterator(); iter.hasNext();) {
			BindingAtom e = (BindingAtom) iter.next();
			if (e.getVariable().equals(variable))
			{
				if (e.getType() == BindingAtom.EQUAL) {
					if (e.getConstant().equals(constant)) return false;
					else return true; // this NOT_EQUAL binding is superfluous
				} 
			}
		}
		atoms.add(new BindingAtom(variable, constant, BindingAtom.NOT_EQUAL));
		return true;
	}
	
	public boolean add(BindingAtom e) {
		if (e.getType() == BindingAtom.EQUAL) 
			return this.bind(e.getVariable(), e.getConstant());
		else 
			return this.forbid(e.getVariable(), e.getConstant());
	}
	
	/**
	 * Retrieves the constant a variable is bound to in this BindingClause.
	 * If the variable is unbound, returns null. This method does
	 * not check whether the variable is forbidden and should be 
	 * used with caution.
	 * @param variable The variable
	 * @return The constant or null.
	 */
	public String lookup(String variable) {
		for (Iterator iter = atoms.iterator(); iter.hasNext();) {
			BindingAtom e = (BindingAtom) iter.next();
			if (e.getVariable().equals(variable)) return e.getConstant();
		}
		return null;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return atoms.toString(); 
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (arg0 instanceof BindingClause)
			return atoms.equals(((BindingClause)arg0).atoms);
		else return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return atoms.hashCode() + 1;
	}
	
	public Object clone() {
		return new BindingClause((HashSet)(atoms.clone()));
	}
}
