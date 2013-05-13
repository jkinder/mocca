/* 
 * Specification.java - Copyright 2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import de.tum.mocca.ctpl.CtplTreeNode;
import de.tum.mocca.ctpl.CtplTreeNodeComparator;
import de.tum.mocca.parser.CtplParser;
import de.tum.mocca.parser.ParseException;

/**
 * TODO enter class comment
 *
 * @author Johannes Kinder
 */
public class Specification {
	private Logger log = Logger.getLogger(Specification.class.getName());
	private CtplTreeNode ctplFormula;
	private String description;
	private String name;
	private String version;
	private List clues;
	private List subformulaList;
	
	/**
	 * Default Constructor 
	 */
	public Specification() {
		super();
	}
	
	/**
	 * @param ctplFormula
	 * @param description
	 * @param name
	 * @param version
	 * @param clues
	 */
	public Specification(CtplTreeNode ctplFormula, String description,
			String name, String version, List clues) {
		this.ctplFormula = ctplFormula;
		subformulaList = breakUpFormula(ctplFormula);
		this.description = description;
		this.name = name;
		this.version = version;
		this.clues = clues;
	}
	
	/**
	 * @return Returns the clues.
	 */
	public List getClues() {
		return clues;
	}
	/**
	 * @param clues The clues to set.
	 */
	public void setClues(List clues) {
		this.clues = clues;
	}
	/**
	 * @return Returns the ctplFormula.
	 */
	public CtplTreeNode getCtplFormula() {
		return ctplFormula;
	}

	/**
	 * Parse the CTPL formula in the given string. The result is added
	 * to the internal model.
	 * @param ctplString
	 * @throws ParseException
	 */
	public void parseCtlFormula(String ctplString) throws ParseException {
		CtplParser ctlParser = new CtplParser(ctplString);
		ctplFormula = ctlParser.parseIntoTree();
		log.debug("CTL formula to check: F=" + ctplFormula);
		subformulaList = breakUpFormula(ctplFormula);
		//log.debug("List of broken up formulas: " + subformulaList);
		log.info("Got " + subformulaList.size() + " subformulas.");
	}
	
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * @return Returns the subformulaList.
	 */
	public List getSubformulaList() {
		return subformulaList;
	}
	
	/**
	 * Expands a CTL Formula to a list of all subformulas 
	 * (including the formula itself)
	 * @param f CTL Formula
	 * @return List of all subformulas
	 */
	private static List breakUpFormula(CtplTreeNode f) {
		Set all = new HashSet();
		LinkedList queue = new LinkedList();
		
		queue.add(f);
		while (!queue.isEmpty()) {
			CtplTreeNode cur = (CtplTreeNode)queue.removeFirst();
			for (int i = 0; i < cur.getDegree(); i++)
				queue.add(cur.getChild(i));	
			all.add(cur);
		}
		
		List res = new ArrayList(all);
		Collections.sort(res, new CtplTreeNodeComparator());
		return res;
	}
	

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName() + "\n" + getDescription() + "\n"; 
	}
}
