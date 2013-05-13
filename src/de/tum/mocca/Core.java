/* 
 * Core.java - Copyright 2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import de.tum.mocca.algo.KripkeNode;
import de.tum.mocca.algo.KripkeStructure;
import de.tum.mocca.algo.ModelChecker;
import de.tum.mocca.ctpl.CtplTreeNode;
import de.tum.mocca.parser.AsmParser;
import de.tum.mocca.parser.ParseException;
import de.tum.mocca.parser.SpecificationFileParser;

/**
 * The main application logic class. This is used by all frontends, i.e. the
 * command line tool and the GUI.
 *
 * @author Johannes Kinder
 */
public class Core {
	
	public final String VERSION = "Mocca 0.1"; 
	private List kripkeStructures;
	private Specification spec;
	private Logger log = Logger.getLogger(Core.class.getName());

	/**
	 * 
	 */
	public Core() {
		super();
	}
	
	/**
	 * Parse disassembler output and create graph in the internal model.
	 * @param filename
	 * @throws FileNotFoundException
	 * @throws ParseException
	 */ 
	public void loadAssemblerInput(String filename) throws FileNotFoundException, ParseException {
		log.info("Begin parsing disassembly...");
		AsmParser parser = new AsmParser(new FileReader(filename));
		kripkeStructures = parser.parseIntoKripkeStructures();
		log.info("End parsing disassembly.");
	}
	
	public void loadSpecification(String filename) throws FileNotFoundException, IOException, ParseException {
		SpecificationFileParser parser = new SpecificationFileParser(
				new BufferedReader(new FileReader(filename)));
		setSpecification(parser.parseSpecification());
	}
	
	/**
	 * Sets the specification.
	 * @param spec the specification
	 */
	public void setSpecification(Specification spec) {
		log.info("Loaded specification " + spec.getName() + ".");
		this.spec = spec;
	}
	
	/**
	 * @return Returns the specification.
	 */
	public Specification getSpecification() {
		return spec;
	}
	
	public KripkeStructure checkAll() {
		if (kripkeStructures == null || spec == null) return null;
		int i = 0;
		//while (kripkeStructures.size() > 0) {
			/* We need to remove the structure from the list to have it deposed 
			   after the check else we need too much memory */
			//KripkeStructure k = (KripkeStructure)kripkeStructures.remove(0);
		int skipped = 0;
		for (Iterator kIter = kripkeStructures.iterator(); kIter.hasNext();) {
			KripkeStructure k = (KripkeStructure)kIter.next();
			List tempClues = new LinkedList();
			// copy the clue list b/c it is destroyed during the cluecheck
			for (Iterator iter = spec.getClues().iterator(); iter.hasNext();)
				tempClues.add(iter.next());
			
			/* Check for clues */
			for (Iterator iter = k.getGraph().vertexSet().iterator();
			iter.hasNext();) {
				KripkeNode s = (KripkeNode)iter.next();
				for (Iterator jter = tempClues.iterator(); jter.hasNext();) {
					if (s.getInstruction().toString().equals((String)jter.next()))
						jter.remove();											
				}
				if (tempClues.size() == 0) break;
			}
			
			/* If there are still unfound clues left, skip */
			if (tempClues.size() > 0) {
				skipped++;
				//log.info("Clue not found, skipping " + k.getName() + " (" + i + ").");
			} else {
				if (skipped > 0) {
					log.info("Skipped " + skipped + " procedure" + ((skipped == 1) ? "" : "s") + " not containing the clues.");
					skipped = 0;
				}
				log.info("Checking " + k.getName() + " (" + i + ").");
				if (ModelChecker.check(k, spec)) return k;
				// Reset labels to save memory
				k.reset();
			}
			i++;
		}
		return null;
	}

	public boolean checkProcedure(int procNum) {
		if (kripkeStructures.size() - 1 < procNum) throw new IndexOutOfBoundsException("No such procedure");
		return ModelChecker.check(
				(KripkeStructure)kripkeStructures.get(procNum), spec);
	}

	/**
	 * @return Returns the spec.getCtlFormula().
	 */
	public CtplTreeNode getCtplFormula() {
		return spec.getCtplFormula();
	}
	
	public KripkeStructure getKripkeStructure(int index) {
		if (kripkeStructures.size() - 1 < index) throw new IndexOutOfBoundsException("No such procedure");
		return (KripkeStructure)kripkeStructures.get(index);
	}
	
	/**
	 * @return Returns the kripkeStructures.
	 */
	public List getAllKripkeStructures() {
		return kripkeStructures;
	}
	
}
