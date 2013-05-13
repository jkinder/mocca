/* 
 * KripkeStructure.java - Copyright 2004 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.algo;

import java.util.Iterator;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.graph.DefaultDirectedGraph;

/**
 * A Kripke Structure that consists of a graph holding the 
 * individual nodes. Supports explicit entry and exit nodes
 * and provides some helper functions. 
 *
 * @author Johannes Kinder
 * @see de.tum.mocca.algo.KripkeNode
 * @see <a href="http://jgrapht.sourceforge.net/javadoc/org/_3pq/jgrapht/DirectedGraph.html">org._3pq.jgrapht.DirectedGraph</a> 
 */
public class KripkeStructure {

	private DirectedGraph graph;
	private KripkeNode entryNode;
	private KripkeNode exitNode;
	private String name;

	/**
	 * Initializes a new Kripkestructures with the given values.
	 * @param graph The underlying DirectedGraph object.
	 * @param entryNode The entry point of the procedure
	 * @param exitNode The node containing the (last) return statement.  
	 */
	public KripkeStructure(DirectedGraph graph, KripkeNode entryNode,
			KripkeNode exitNode, String name) {
		this.graph = graph;
		this.entryNode = entryNode;
		this.exitNode = exitNode;
		this.name = name;
	}
	
	public KripkeStructure() {
		this(new DefaultDirectedGraph(), null, null, "default");
	}
	
	/**
	 * Clears all marks from the nodes in this Kripke structure.
	 * Useful for algorithms.
	 * @see KripkeNode#mark()
	 */
	public void clearMarks() {
		for (Iterator iter = graph.vertexSet().iterator(); iter.hasNext();)
			((KripkeNode)iter.next()).clearMark();
	}
	
	/**
	 * Removes all labels from all nodes in this Kripke Structure
	 * and resets it to the initial state.
	 */
	public void reset() {
		for (Iterator iter = graph.vertexSet().iterator(); iter.hasNext();)
			((KripkeNode)iter.next()).reset();
	}
	
	/**
	 * @return Returns the entry node.
	 */
	public KripkeNode getEntryNode() {
		return entryNode;
	}
	/**
	 * @return Returns the exit node.
	 */
	public KripkeNode getExitNode() {
		return exitNode;
	}
	/**
	 * @return Returns the graph.
	 */
	public DirectedGraph getGraph() {
		return graph;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
}
