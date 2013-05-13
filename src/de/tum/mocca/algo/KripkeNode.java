/* 
 * KripkeNode.java - Copyright 2004 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.algo;

import java.util.HashMap;

import org.apache.log4j.Logger;

import de.tum.mocca.ctpl.CtplTreeNode;
import de.tum.mocca.ctpl.Instruction;

/**
 * One state in a Kripke structure. Contains the instruction that
 * is represented by the node and can be tagged with multiple
 * labels that are CtlFormulas with their corresponding BindingSets.
 *
 * @see de.tum.mocca.algo.KripkeStructure
 * @see de.tum.mocca.ctpl.Instruction
 * @see de.tum.mocca.ctpl.CtplTreeNode
 * @see de.tum.mocca.algo.BindingSet
 * @author Johannes Kinder
 */
public class KripkeNode {
	
	private Logger log = Logger.getLogger(KripkeNode.class.getName());
	private Instruction instruction;
	private boolean mark;
	private HashMap labels;

	/**
	 * Initializes a new, unmarked KripkeNode with an empty
	 * set of labels and the given Instruction.
	 */
	public KripkeNode(Instruction instruction) {
		mark = false;
		labels = new HashMap(100);
		this.instruction = instruction;
	}

	/**
	 * @return Returns the instruction.
	 */
	public Instruction getInstruction() {
		return instruction;
	}

	/**
	 * Adds a label (a CTL formula) with a set of associated 
	 * bindings. 
	 * If the label is already present, adds the whole new BindingSet
	 * to the existing BindingSet (boolean OR) and returns true if 
	 * the BindingSet contained any new bindings or false respectively.
	 * If the label is not yet present, adds it together with the 
	 * BindingSet and returns true. 
	 * @param label The label to be added.
	 * @param bindingSet the BindingSet associated with the label.
	 * @return True if the labelling changed, false otherwise.
	 * @see KripkeNode#setLabel(CtplTreeNode, BindingSet) 
	 */
	public boolean addLabel(CtplTreeNode label, BindingSet bindingSet) {
		if (bindingSet == null) {
			log.warn("Trying to add a null BindingSet to " + label);
		}
		if (labels.containsKey(label))
			return ((BindingSet)labels.get(label)).addAll(bindingSet);
		
		labels.put(label, bindingSet);
		return true;
	}
	
	/**
	 * Labels the node with a CTL formula and a set of associated 
	 * bindings without checking if the label was already present. 
	 * @param label The label to be added.
	 * @param bindingSet the BindingSet associated with the label.
	 * @see KripkeNode#addLabel(CtplTreeNode, BindingSet)
	 */
	public void setLabel(CtplTreeNode label, BindingSet bindingSet) {
		labels.put(label, bindingSet);
	}
	
	/**
	 * Returns whether this node is labelled with the given
	 * label.
	 * @param label The label to check.
	 */
	public boolean hasLabel(CtplTreeNode label) {
		return this.labels.containsKey(label);
	}
	
	/**
	 * Returns the BindingSet associated with the given label.
	 * If the label does not exist on this node, returns null. 
	 * @param label The label
	 * @return The BindingSet of the label
	 */
	public BindingSet getBindingSet(CtplTreeNode label) {
		if (label == null) {
			log.warn("Trying to get bindings for null label!");
			return null;
		}
		return (BindingSet)(this.labels.get(label));
	}
	
	/**
	 * Checks whether this node has been marked (by an algorithm).
	 * @return True if this node is marked, false otherwise.
	 */
	public boolean isMarked() {
		return mark;
	}

	/**
	 * Marks the node without affecting the label set. 
	 * This is particularly useful for algorithms. 
	 */
	public void mark() {
		this.mark = true;
	}

	/**
	 * Clears the mark without affecting the label set. 
	 * This is particularly useful for algorithms.
	 * @see KripkeStructure#clearMarks()
	 */
	public void clearMark() {
		this.mark = false;
	}
	
	/**
	 * Removes all labels and resets this node to the initial
	 * state.
	 */
	public void reset() {
		clearMark();
		labels.clear();
	}
	
	/**
	 * Returns a string consisting of the number of labels
	 * followed by all labels and finally the instruction.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return instruction.getLocation()/*instruction.getOffset()*/ + ": (" + labels.size() + ") | " + 
		instruction.toString()/* + " | " +
		labels.entrySet().toString()*/;
	}
}
