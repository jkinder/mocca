/* 
 * GraphVisualization.java - Copyright 2004 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Diplomarbeit project.
 */
package de.tum.mocca.visual;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org._3pq.jgrapht.edge.DirectedEdge;
import org._3pq.jgrapht.ext.JGraphModelAdapter;
import org.apache.log4j.Logger;
import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import de.tum.mocca.algo.KripkeNode;
import de.tum.mocca.algo.KripkeStructure;

/**
 * Gives a graphic representation of a given directed graph.
 * Performs a simple tree-like layout algorithm.
 *
 * @author Johannes Kinder
 */
public class GraphVisualization extends JPanel {

	private static final long serialVersionUID = -3558370949966408408L;
	public final int DEFAULT_CELL_WIDTH = 280;
	public final int DEFAULT_CELL_HEIGHT = 16;
	public final int DEFAULT_XSTEP = DEFAULT_CELL_WIDTH + 15;
	public final int DEFAULT_YSTEP = DEFAULT_CELL_HEIGHT + 6;
	
	private JGraphModelAdapter modelAdapter;
	private JGraph jgraph;
	private Logger log = Logger.getLogger(GraphVisualization.class.getName());

	/**
	 * Creates a new visual representation of a given Kripke Structure
	 * 
	 * @param k The KripkeStructure to show. 
	 */
	public GraphVisualization(KripkeStructure k) {
		super();
    	jgraph = createJGraph(k);
    	add(jgraph);
    }
    
	public GraphVisualization() {
		super();
		add(new JGraph());
    }

	private JGraph createJGraph(KripkeStructure k) {

    	AttributeMap vertMap = new AttributeMap();

        GraphConstants.setBounds(vertMap, new Rectangle2D.Double(30, 30, DEFAULT_CELL_WIDTH, DEFAULT_CELL_HEIGHT));
	    GraphConstants.setBorder(vertMap, BorderFactory.createRaisedBevelBorder());
	    GraphConstants.setBackground(vertMap, Color.decode("#009900"));
	    GraphConstants.setForeground(vertMap, Color.white);
	    GraphConstants.setFont(vertMap,
	    		GraphConstants.DEFAULTFONT.deriveFont(Font.PLAIN, 10));
	    GraphConstants.setOpaque(vertMap, true);
	     
        AttributeMap edgeMap = new AttributeMap();

        GraphConstants.setLineEnd(edgeMap, GraphConstants.ARROW_CLASSIC);
        GraphConstants.setEndFill(edgeMap, true);
        GraphConstants.setEndSize(edgeMap, 5);

        GraphConstants.setForeground(edgeMap, Color.decode("#F00011"));
        GraphConstants.setFont(edgeMap,
        		GraphConstants.DEFAULTFONT.deriveFont(Font.PLAIN, 0));
        GraphConstants.setLineColor(edgeMap, Color.decode("#7AA1E6"));
        GraphConstants.setLabelAlongEdge(edgeMap, false);
	    
	    modelAdapter = new JGraphModelAdapter(k.getGraph(), vertMap, edgeMap);
        JGraph jgraph = new JGraph(modelAdapter);

        jgraph.setBackground(Color.white);
        jgraph.setConnectable(false);
        jgraph.setDisconnectable(false);
        jgraph.setDisconnectOnMove(false);
        jgraph.setEditable(false);
        jgraph.setPortsVisible(false);
        jgraph.setGridVisible(false);
        
        Object rootNode = k.getEntryNode();
        if (rootNode == null) {
        	log.fatal("Procedure graph has no root");
        	System.exit(1);
        }
        //System.out.println("Graph root: " + rootNode);
        
        // Do a breadth first search and layout nodes
        LinkedList nodeList = new LinkedList();
        LinkedList posList = new LinkedList();
        nodeList.add(rootNode);
        posList.add(new Point(0, 0));
        Map markedNodes = new HashMap();
        int xstep = DEFAULT_XSTEP;
        
        while (!nodeList.isEmpty()) {
        	Object curNode = nodeList.removeFirst();
        	Point curPos = (Point)posList.removeFirst();
        	
        	positionVertexAt(curNode, curPos.x, curPos.y);

        	curPos.y += DEFAULT_YSTEP;
        	
        	if (k.getGraph().outDegreeOf(curNode) > 0)
        	for(Iterator iter = k.getGraph().outgoingEdgesOf(curNode).iterator(); iter.hasNext();) {
        		Object newNode = ((DirectedEdge)(iter.next())).getTarget(); 
        		if (markedNodes.get(newNode) == null) {
        			if (((KripkeNode)newNode).getInstruction().getOffset() == ((KripkeNode)curNode).getInstruction().getOffset() + 1) {
        				nodeList.addFirst(newNode);
        				posList.addFirst(curPos.clone());
        			} else {
                		curPos.x += xstep;
                		curPos.y -= DEFAULT_YSTEP;
        				nodeList.addLast(newNode);
        				posList.addLast(curPos.clone());
        			}
        			markedNodes.put(newNode, new Boolean(false));
        		}
        	}
        }
        
        return jgraph;
    }

    private void positionVertexAt( Object vertex, int x, int y ) {
        DefaultGraphCell cell = modelAdapter.getVertexCell( vertex );
        Map              attr = cell.getAttributes();

        GraphConstants.setBounds( attr, new Rectangle( x, y, DEFAULT_CELL_WIDTH, DEFAULT_CELL_HEIGHT));

        Map cellAttr = new HashMap(  );
        cellAttr.put( cell, attr );
        modelAdapter.edit( cellAttr );
    }
}
