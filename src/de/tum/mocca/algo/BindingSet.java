/* 
 * BindingSet.java - Copyright 2004 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.algo;

import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * A set of BindingClauses, that define constraints on variables 
 * required for the CTL formula it is associated with to hold.
 * 
 * A BindingSet that contains an empty BindingClause object is a
 * tautology, i.e. the associated CTL Function holds in this state 
 * for every binding.
 * An empty BindingSet is unsatisfiable, i.e. the associated CTL 
 * formula does not hold for any binding.
 * A BindingSet with a value of null should never be created and is
 * a sign for an error.
 *
 * @author Johannes Kinder
 * @see de.tum.mocca.algo.BindingClause
 * @see de.tum.mocca.ctpl.CtplTreeNode
 * @see de.tum.mocca.algo.KripkeNode
 */
public class BindingSet {

	private Logger log = Logger.getLogger(BindingSet.class.getName());
	public static BindingSet emptySet = new BindingSet();
	
	/**
	 * Returns a copy of the given BindingSet with all BindingElements
	 * containing the supplied variable removed. Bindings can become 
	 * empty or equal to other Bindings in the BindingSet and may 
	 * annihilate others this way.
	 * @param var The variable to be removed.
	 * @return A new BindingSet with the variable unbound.
	 */
	public static BindingSet purge(BindingSet c, String var) {
		BindingSet res = new BindingSet();
		for (Iterator iter = c.bindings.iterator(); iter.hasNext();) {
			BindingClause purged = 
				BindingClause.purge((BindingClause)iter.next(), var);
			res.add(purged);
			if (purged.equals(BindingClause.emptyClause)) break;
		}
		return res;
	}
	
	/**
	 * Returns a new BindingSet with all Bindings in this BindingSet
	 * inverted. This is useful for the NOT operator. If this
	 * BindingSet contains the empty BindingClause, returns null. 
	 * @return Returns the inverted BindingSet or null.
	 */
	static public BindingSet negate(BindingSet c) {
		if (c.bindings.contains(BindingClause.emptyClause)) return emptySet;
		Iterator iter = c.bindings.iterator();
		BindingSet res = new BindingSet();
		res.addAll(BindingClause.negate((BindingClause)iter.next()));
		while (iter.hasNext()) {
			BindingClause b = (BindingClause) iter.next();
			res = BindingSet.and(res, BindingClause.negate(b));
		}
		return res;
	}

	/**
	 * Performs a boolean and on two BindingSets and returns the
	 * result as a new set. If the merging results in no satisfiable
	 * clauses, an empty BindingSet is returned.
	 * @param c1 First BindingSet.
	 * @param c2 Second BindingSet.
	 * @return the new BindingSet.
	 */
	public static BindingSet and(BindingSet c1, BindingSet c2) {
		BindingSet res = new BindingSet();
		for (Iterator iter = c1.bindings.iterator(); iter.hasNext();) {
			BindingClause curB1 = (BindingClause) iter.next();
			for (Iterator iter2 = c2.bindings.iterator(); iter2.hasNext();) {
				BindingClause curB2 = (BindingClause) iter2.next();
				BindingClause merged = BindingClause.and(curB1, curB2);
				if (merged != null) res.add(merged);
			}
		}
		return res;
	}
	
	/**
	 * Performs a boolean or on two BindingSets and returns the
	 * result in a new BindingSet. 
	 * @param c1 First BindingSet
	 * @param c2 Second BindingSet
	 * @return Returns (c1 | c2) as a new BindingSet.
	 */
	public static BindingSet or(BindingSet c1, BindingSet c2) {
		BindingSet res = (BindingSet)c1.clone();
		for (Iterator iter = c2.bindings.iterator(); iter.hasNext();) {
			BindingClause curB = (BindingClause) iter.next();
			res.add(curB);
		}
		return res;
	}

	
	private HashSet bindings;
	
	/**
	 * Initializes a new empty BindingSet with a default capacity.
	 */
	public BindingSet() {
		super();
		bindings = new HashSet(10);
	}
	
	protected BindingSet(HashSet bindings) {
		this.bindings = bindings;
	}
	
	/**
	 * Check whether the BindingSet is empty or not. 
	 * @return Returns true if the Set contains no clauses. 
	 */
	public boolean isEmpty() {
		return bindings.size() == 0;
	}

	/**
	 * Adds a BindingClause to this BindingSet. Returns true if the BindingClause
	 * was not yet present, and returns false if the BindingClause was
	 * already part of the set.
	 * The BindingClause object is added as itself, and not as a copy.
	 * @param b The BindingClause object to be added. 
	 * @return Returns if this set has been changed.
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public boolean add(BindingClause b) {
		if (b == null) {
			// this should never happen
			log.warn("Tried to add nullbinding to BindingSet!");
			return false;
		}
		// if this Set contains the empty binding, nothing more can be added 
		// as it's already a tautology
		if (bindings.contains(BindingClause.emptyClause)) return false;
		// adding the empty binding removes all others (BindingSet becomes tautology).
		if (b.equals(BindingClause.emptyClause)) {
			bindings.clear();
			bindings.add(BindingClause.emptyClause);
			return true;
		}
		return bindings.add(b);
	}
	
	/**
	 * Adds all members of the BindingSet to this set. Returns true
	 * if there were any new bindings introduced this way, and false
	 * if this Object remains unchanged. 
	 * @param bindingSet
	 * @return Returns if this set has been changed.
	 * @see java.util.Set#addAll(java.util.Collection)
	 */
	public boolean addAll(BindingSet bindingSet) {
		if (bindingSet == null) {
			// this should never happen
			log.warn("Tried to add nullBindingSet to BindingSet!");
			return false;
		}
		
		boolean changed = false;
		for (Iterator iter = bindingSet.bindings.iterator(); iter.hasNext();) {
			BindingClause curB = (BindingClause) iter.next();
			changed |= this.add(curB);
		}
		return changed;
	}
	
	/**
	 * Returns whether this BindingSet contains a given BindingClause clause.
	 * @param b The BindingClause
	 * @return Returns true or false. 
	 */
	public boolean contains(BindingClause b) {
		return bindings.contains(b);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return bindings.toString();
	}
	
	/**
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		return new BindingSet((HashSet)this.bindings.clone());
	}
	
	/**
	 * Two BindingSets are equal if they contain only 
	 * equal bindings.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		return bindings.equals(((BindingSet)arg0).bindings);
	}
	
	/**
	 * The hashCode is calculated from the set of bindings.
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return bindings.hashCode() + 1;
	}
}
