/* 
 * GraphBuilder.java - Copyright 2004,2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.parser;

import org._3pq.jgrapht.graph.DefaultDirectedGraph;
import org.apache.log4j.Logger;

import de.tum.mocca.algo.KripkeNode;
import de.tum.mocca.algo.KripkeStructure;
import de.tum.mocca.ctpl.Instruction;

import java.util.*;

/**
 * Used to build a new graph during parsing of the assembler-input
 * by subsequent calls to addLine 
 *
 * @author Johannes Kinder
 */
public class GraphBuilder {
	
	private Logger log = Logger.getLogger(GraphBuilder.class.getName());
	private DefaultDirectedGraph graph;
	private KripkeStructure kripkeStructure;
	private KripkeNode lastNode;
	private KripkeNode entry;
	private String lastLabel;
	private HashMap labels;
	private Vector controlInstructions;
	private String procedureName;
	private int curOffset;
	public final int START_OFFSET = 0; 
	
	/**
	 * Inititalizes a new GraphBuilder object 
	 */
	public GraphBuilder() {
		 graph = new DefaultDirectedGraph();
		 lastLabel = "";
		 lastNode = null;
		 labels = new HashMap();
		 controlInstructions = new Vector(50);
		 curOffset = START_OFFSET;
		 entry = null;
		 procedureName = "unnamed";
	}		
	
	/**
	 * @return Returns the graph.
	 */
	public KripkeStructure getKripkeStructure() {
		return kripkeStructure;
	}
	
	/**
	 * Adds one instruction to the end of the current branch
	 * in the graph 
	 * This is used by the graph builder during parsing of the
	 * disassembly.
	 * @param opcode The mnemonic of this instruction.
	 * @param paraS The parameters as a stack in reverse order.
	 * @return The newly created node carrying the instruction. 
	 */
	public KripkeNode addInstruction(String opcode, Stack paraS) {
		String[] p;
		if (paraS == null) p = new String[0];
		else {
			p = new String[paraS.size()];
			for (int i=0; !paraS.empty(); i++)
				p[i] = (String)paraS.pop();
			}
		
		/* some simple replacements */
		if (opcode.equals("xor") && p[0].equals(p[1])) {
			opcode = "mov";
			p[1] = "0";
		} /*else if (opcode.equals("lea")) {
			opcode = "mov";
			p[1] = "offset " + p[1];
		}  */
		
		Instruction instr = new Instruction(opcode, p);
		KripkeNode curNode = new KripkeNode(instr);
		addInstruction(curNode);
		return curNode;
	}
	
	/**
	 * Adds one instruction to the end of the current branch
	 * in the graph 
	 * This is used by the graph builder during parsing of the
	 * disassembly.
	 * @param opcode The mnemonic of this instruction.
	 * @param paraS The parameters as a stack in reverse order.
	 * @return The newly created node carrying the instruction. 
	 */
	public KripkeNode addInstruction(String[] instrAndParams) {

		int iLength;
		for (iLength = 0; iLength < instrAndParams.length; iLength++)
			if (instrAndParams[iLength] == null) break;
		
		if (iLength < 1) throw new RuntimeException("Cannot add instruction without body.");
		
		String opcode = instrAndParams[0];
		String[] p;
		p = new String[iLength - 1];
		for (int i = 0; i < iLength - 1; i++)
			p[i] = instrAndParams[i+1];
		
		/* some simple replacements */
		if (opcode.equals("xor") && p[0].equals(p[1])) {
			opcode = "mov";
			p[1] = "0";
		} /*else if (opcode.equals("lea")) {
			opcode = "mov";
			p[1] = "offset " + p[1];
		}  */
		
		Instruction instr = new Instruction(opcode, p);
		KripkeNode curNode = new KripkeNode(instr);
		addInstruction(curNode);
		// all j-instructions are jumps, see intel manual.
		if ((opcode.charAt(0) == 'j') || (opcode.startsWith("loop"))) 
			controlInstructions.add(curNode);
		return curNode;
	}
	
	/**
	 * Adds one instruction to the end of the current branch
	 * in the graph 
	 *
	 * @param curNode the KripkeNode to add
	 */
	public void addInstruction(KripkeNode curNode) {
		if (curOffset == START_OFFSET) entry = curNode; 
		curNode.getInstruction().setOffset(curOffset++);
		graph.addVertex(curNode);
		if ((lastNode != null) && (!lastNode.getInstruction().getOpcode().equals("jmp")))
		{	
			graph.addEdge(lastNode, curNode);
		}
		if (lastLabel != "") {
			// This instruction is labelled
			labels.put(lastLabel, curNode);
			lastLabel = "";
		}
		
		lastNode = curNode;
	}
	
	/**
	 * Adds one instruction that influences the flow of control
	 * to the end of the current branch in the graph and adds
	 * the new edges
	 * @param instr Instruction Object 
	 * @return The newly created node carrying the instruction. 
	 */
	public KripkeNode addControlInstruction(String opcode, Stack paraS) {
		KripkeNode curNode = addInstruction(opcode, paraS);
		controlInstructions.add(curNode);
		return curNode;
	}
	
	/**
	 * Signals that the line of the next instruction is to be
	 * labelled with the given string.
	 *
	 * @param label The Label
	 */
	public void addLabel(String label) {
		lastLabel = label;
	}
	
	/**
	 * @param procedureName The procedureName to set.
	 */
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	
	/**
	 * Finishes building the graph and inserts all edges 
	 * added by control flow changing instructions
	 */
	public void finish() {
		for (Iterator i = controlInstructions.iterator(); i.hasNext();) {
			KripkeNode source = (KripkeNode)i.next();
			KripkeNode target = (KripkeNode)labels.get(source.getInstruction().getParameters()[0]);
			if (target == null) {
				log.warn("Unresolved jump to " + source.getInstruction().getParameters()[0] + " in " + procedureName + "!");
			} else {
				graph.addEdge(source, target);
			}
		}
		kripkeStructure = new KripkeStructure(graph, entry, lastNode, procedureName);
		log.debug("Graph for procedure " + kripkeStructure.getName() + " built.");
	}
}
