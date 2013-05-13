/* 
 * GraphWindow.java - Copyright 2004 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.visual;

import java.awt.*;
import javax.swing.*;

import de.tum.mocca.algo.KripkeStructure;

/**
 * TODO enter class comment
 *
 * @author Johannes Kinder
 */
public class GraphWindow extends JFrame {
	
	private static final long serialVersionUID = 4520088748926670716L;
	protected JMenu menu;

	public GraphWindow() throws HeadlessException {
		super("Mocca");
		
		// Menu
		menu = new JMenu();
		
		
		
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JScrollPane scrollPane = new JScrollPane();
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        setSize(Math.min(800, scrollPane.getPreferredSize().width + 23), 500);
	    Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenDim.width - getWidth())/2, (screenDim.height - getHeight())/2);
		
	}
	
	/**
	 * @throws java.awt.HeadlessException
	 */
	public GraphWindow(KripkeStructure graph) throws HeadlessException {
		super("Graph Output");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        GraphVisualization gVis = new GraphVisualization(graph);
        JScrollPane scrollPane = new JScrollPane(gVis);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        setSize(Math.min(800, scrollPane.getPreferredSize().width + 23), 500);
	    Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenDim.width - getWidth())/2, (screenDim.height - getHeight())/2);
	}
}
