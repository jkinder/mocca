/* 
 * CtplTreeNodeComparator.java - Copyright 2004 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.ctpl;

import java.util.Comparator;

/**
 * Compares two treenodes regarding their size, needed for sorting subformulas.
 * 
 * @see de.tum.mocca.ctpl.CtplTreeNode
 * @author Johannes Kinder
 */
public class CtplTreeNodeComparator implements Comparator {

	/**
	 * The compare function.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {
		int size0 = ((CtplTreeNode)arg0).getSize(); 
		int size1 = ((CtplTreeNode)arg1).getSize();
		if (size0 < size1) return -1;
		if (size0 == size1) return 0;
		return 1;
	}

}
