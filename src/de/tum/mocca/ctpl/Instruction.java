/* 
 * Instruction.java - Copyright 2004,2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */

package de.tum.mocca.ctpl;

import de.tum.mocca.algo.ModelChecker;

/**
 * Represents one line of assemblercode, i.e. the instruction
 * and its parameters. This is also a CtplPredicate, so it can
 * be unified with abstract predicates.
 *
 * @author Johannes Kinder
 */
public class Instruction extends CtplPredicate {

	private CtplPredicate location;
	private int offset;
		
	/**
	 * Initializes a new instruction with the given parameters.
	 * 
	 * @param opcode The mnemonic of this instruction.
	 * @param parameters A string array with all parameters in correct order.
	 */
	public Instruction(String opcode, String[] parameters) {
		super(opcode.toLowerCase(), parameters);
	}
	
	/**
	 * @return Returns the opcode.
	 */
	public String getOpcode() {
		return body;
	}

	/**
	 * Gets the offset that this instruction has relatively to
	 * the procedure start.
	 * @return Returns the offset.
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Sets the (pseudo)offset of this instruction within its 
	 * procedure. 
	 * The offset is used as identification of individual nodes
	 * and for laying out the graph in the visualization routines. 
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
		this.location = 
			new CtplPredicate(ModelChecker.P_LOCATION, 
					new String[]{Integer.toString(offset)});
	}

	/**
	 * @return Returns the location.
	 */
	public CtplPredicate getLocation() {
		return location;
	}
	
	/**
	 * Returns a string of the format 'mnemonic([param1[, param2]]');
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String r = body + "(";
		if (parameters.length >= 1) r+= parameters[0];
		for (int i=1; i<parameters.length; i++) r+= "," + parameters[i];
		return r + ")"; 
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof Instruction)) return false;
		if (!super.equals(arg0)) return false;
		return ((Instruction)arg0).offset == this.offset;
	}
	
	/**
	 * Needed because equals is overridden.
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return super.hashCode() + offset;
	}
}
