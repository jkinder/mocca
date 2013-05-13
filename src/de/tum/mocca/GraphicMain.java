/* 
 * GraphicMain.java - Copyright 2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca;

import de.tum.mocca.visual.MainWindow;

/**
 * TODO enter class comment
 *
 * @author Johannes Kinder
 */
public class GraphicMain {

	public static void main(String[] args) {
		MainWindow mainWin = new MainWindow(new Core());
		mainWin.setVisible(true);
	}
}
