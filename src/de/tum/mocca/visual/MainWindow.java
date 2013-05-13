/* 
 * MainWindow.java - Copyright 2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.visual;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.tum.mocca.Core;
import de.tum.mocca.algo.KripkeStructure;
import de.tum.mocca.parser.ParseException;
/**
 * TODO enter class comment
 *
 * @author Johannes Kinder
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = -1646910051429335952L;
	private javax.swing.JPanel jContentPane = null;
	private javax.swing.JMenuBar jJMenuBar = null;
	private javax.swing.JMenu fileMenu = null;
	private javax.swing.JMenu helpMenu = null;
	private javax.swing.JMenuItem exitMenuItem = null;  //  @jve:decl-index=0:
	private javax.swing.JMenuItem aboutMenuItem = null;
	private JMenuItem openAsmMenuItem = null;
	
	private Core core;
	private JMenu checkMenu = null;
	private JMenuItem checkAllMenuItem = null;
	private JMenuItem openSpecificationMenuItem = null;
	private JMenuItem checkSingleMenuItem = null;
	private JScrollPane outputScrollPane = null;
	private JTextArea outputArea = null;
	private JMenu viewMenu = null;
	private JMenuItem specificationMenuItem = null;
	private JMenuItem cfgMenuItem = null;
	
	private Config config;
	private KripkeStructure k;
	
	/**
	 * Constructor
	 */
	public MainWindow(Core core) {
		super();
		initialize();
		this.core = core;
		config = new Config();
	}
	
	private void print(String s) {
		outputArea.append(s);
		outputArea.setCaretPosition(outputArea.getText().length());
		outputArea.invalidate();
	}
	
	private void println(String s) {
		print(s + "\n");
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setBounds(0, 0, 500, 250);
		this.setLocation(100,100);
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.setJMenuBar(getJJMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle("Mocca");
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.setPreferredSize(new java.awt.Dimension(500,250));
			jContentPane.add(getOutputScrollPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */    
	private javax.swing.JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new javax.swing.JMenuBar();
			jJMenuBar.add(getFileMenu());
			jJMenuBar.add(getCheckMenu());
			jJMenuBar.add(getViewMenu());
			jJMenuBar.add(getHelpMenu());
		}
		return jJMenuBar;
	}
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private javax.swing.JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new javax.swing.JMenu();
			fileMenu.setText("File");
			fileMenu.add(getOpenAsmMenuItem());
			fileMenu.add(getOpenSpecificationMenuItem());
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private javax.swing.JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new javax.swing.JMenu();
			helpMenu.setText("?");
			helpMenu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			helpMenu.setHorizontalTextPosition(javax.swing.SwingConstants.TRAILING);
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}
	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private javax.swing.JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new javax.swing.JMenuItem();
			exitMenuItem.setText("Exit");
			exitMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					System.exit(0);
				}
			});
		}
		return exitMenuItem;
	}
	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private javax.swing.JMenuItem getAboutMenuItem() {
		if (aboutMenuItem == null) {
			aboutMenuItem = new javax.swing.JMenuItem();
			aboutMenuItem.setText("About / Copyright");
			aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {   
				public void actionPerformed(java.awt.event.ActionEvent e) {    
		    		JOptionPane.showMessageDialog((Component)(e.getSource()), 
		    				"<html><font size=+2>" + core.VERSION + 
							"</font>\nCopyright 2004-2005 by Johannes Kinder <jk@jakstab.org>\n\n" +
							"This software is provided \"as is\" and without warranty of any kind.\n" +
							"Reproduction and redistribution prohibited.", "About", JOptionPane.INFORMATION_MESSAGE);
				} 
			
			});
		}
		return aboutMenuItem;
	}
	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getOpenAsmMenuItem() {
		if (openAsmMenuItem == null) {
			openAsmMenuItem = new JMenuItem();
			openAsmMenuItem.setText("Open ASM File");
			openAsmMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFileChooser openAsmFileChooser = new JFileChooser();
					openAsmFileChooser.setCurrentDirectory(new File(config.getAssemblerDir()));
					openAsmFileChooser.setDialogTitle("Open Assembler Input File");
					openAsmFileChooser.addChoosableFileFilter(new SimpleFileFilter("asm", "Assembler Files"));
					if (openAsmFileChooser.showOpenDialog((Component)e.getSource()) 
							== JFileChooser.APPROVE_OPTION) {
						try {
							core.loadAssemblerInput(openAsmFileChooser.getSelectedFile().getAbsolutePath());
						} catch (FileNotFoundException ex) {
							println("Input file not found!");
							return;
						} catch (ParseException ex) {
							println("Parse exception in input file!");
							println(ex.getMessage());
							return;
						} finally {
							cfgMenuItem.setEnabled(false);
							config.setAssemblerDir(openAsmFileChooser.getSelectedFile().getParent());
							config.save();
						}
						println("Loaded " + core.getAllKripkeStructures().size() + " procedures from " + openAsmFileChooser.getSelectedFile().getName() + ".");
					}
				}
			});
		}
		return openAsmMenuItem;
	}
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private JMenu getCheckMenu() {
		if (checkMenu == null) {
			checkMenu = new JMenu();
			checkMenu.setText("Check");
			checkMenu.add(getCheckSingleMenuItem());
			checkMenu.add(getCheckAllMenuItem());
		}
		return checkMenu;
	}
	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getCheckAllMenuItem() {
		if (checkAllMenuItem == null) {
			checkAllMenuItem = new JMenuItem();
			checkAllMenuItem.setText("Check all");
			checkAllMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
					println("Checking all procedures.");
					long startTime = System.currentTimeMillis();
					k = core.checkAll();
					long stopTime = System.currentTimeMillis();

					if (k != null) {
						println("k, s0 |= F holds for procedure " + k.getName() 
								+ "! Constraints for remaining unbound variables follow:\n" 
								+ k.getEntryNode().getBindingSet(core.getCtplFormula())
								);
						cfgMenuItem.setEnabled(true);
					}
					else println("k, s0 |= F does not hold for any procedure!");
					println("Total time: " + (stopTime - startTime)/1000.0 + "s.");
				}
			});
		}
		return checkAllMenuItem;
	}
	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getOpenSpecificationMenuItem() {
		if (openSpecificationMenuItem == null) {
			openSpecificationMenuItem = new JMenuItem();
			openSpecificationMenuItem.setText("Open Specification");
			openSpecificationMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					JFileChooser openSpecFileChooser = new JFileChooser();
					openSpecFileChooser.setCurrentDirectory(new File(config.getSpecificationDir()));
					openSpecFileChooser.setDialogTitle("Open Specification");
					openSpecFileChooser.addChoosableFileFilter(new SimpleFileFilter("spec", "Specification Files"));
					if (openSpecFileChooser.showOpenDialog((Component)e.getSource()) 
							== JFileChooser.APPROVE_OPTION) {
						try {
							core.loadSpecification(openSpecFileChooser.getSelectedFile().getAbsolutePath());
						} catch (Exception ex)
						{
							println("Error parsing specification: " + ex.getMessage());
						}
						println("Loaded specification \"" + core.getSpecification().getName() + "\".");
						config.setSpecificationDir(openSpecFileChooser.getSelectedFile().getParent());
						config.save();
						specificationMenuItem.setEnabled(true);
					}
				}
			});
		}
		return openSpecificationMenuItem;
	}
	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getCheckSingleMenuItem() {
		if (checkSingleMenuItem == null) {
			checkSingleMenuItem = new JMenuItem();
			checkSingleMenuItem.setText("Check single procedure");
			checkSingleMenuItem.setEnabled(false);
		}
		return checkSingleMenuItem;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getOutputScrollPane() {
		if (outputScrollPane == null) {
			outputScrollPane = new JScrollPane();
			outputScrollPane.setViewportView(getOutputArea());
		}
		return outputScrollPane;
	}
	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */    
	private JTextArea getOutputArea() {
		if (outputArea == null) {
			outputArea = new JTextArea();
			outputArea.setEditable(false);
		}
		return outputArea;
	}
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private JMenu getViewMenu() {
		if (viewMenu == null) {
			viewMenu = new JMenu();
			viewMenu.setText("View");
			viewMenu.add(getSpecificationMenuItem());
			viewMenu.add(getCfgMenuItem());
		}
		return viewMenu;
	}
	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getSpecificationMenuItem() {
		if (specificationMenuItem == null) {
			specificationMenuItem = new JMenuItem();
			specificationMenuItem.setText("Specification");
			specificationMenuItem.setEnabled(false);
			specificationMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					JOptionPane.showMessageDialog((Component)e.getSource(), core.getSpecification().toString(), "Viewing " + core.getSpecification().getName(), JOptionPane.PLAIN_MESSAGE);
				}
			});
		}
		return specificationMenuItem;
	}
	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getCfgMenuItem() {
		if (cfgMenuItem == null) {
			cfgMenuItem = new JMenuItem();
			cfgMenuItem.setText("Control Flow Graph");
			cfgMenuItem.setEnabled(false);
			cfgMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					System.out.println("showing Window.");
					GraphWindow gWin = new GraphWindow(k);
					gWin.setVisible(true);
				}
			});
		}
		return cfgMenuItem;
	}
     }  //  @jve:decl-index=0:visual-constraint="10,10"
