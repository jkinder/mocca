/* 
 * Main.java - Copyright 2004,2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca;

import java.io.FileNotFoundException;
import java.io.IOException;

import de.tum.mocca.algo.KripkeStructure;
import de.tum.mocca.parser.ParseException;

/**
 * @author Johannes Kinder
 *
 */
public class Main {
	
	private static Core core; 

	public static void main(String[] args) {
				
		String inputFilename = "";
		int procNumber = -1;
		String specFilename = "";
		core = new Core();
		String help = "Parameters: \n" +
		"-S <filename>  Load specification from <filename>\n" + 
		"-I <filename>  Use <filename> as asm-input\n" +
		"-P <procNum>   Number of procedure to check, 'all' checks all\n";
		
		System.out.println(core.VERSION + " - Copyright 2004-2005 by Johannes Kinder <jk@jakstab.org>");
		
		/* Process arguments */
		if (args.length < 1) {
			System.out.println(help);
			System.exit(1);
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-I")) {
				inputFilename = args[++i];
			} else if (args[i].equals("-P")) {
				String numString = args[++i];
				if (numString.equals("all")) procNumber = -1;
				else try {
					procNumber = Integer.parseInt(numString);
				} catch (NumberFormatException e) {
					System.out.println("Illegal procedure number.");
					System.exit(0);
				}
			} else if (args[i].equals("-S")) {
				specFilename = args[++i];
			} else {
				System.out.println(help);
				System.exit(1);
			}
		}
				
		try {
			core.loadAssemblerInput(inputFilename);
		} catch (FileNotFoundException e) {
			System.out.println("Input file not found!");
			System.exit(1);
		} catch (ParseException e) {
			System.out.println("Parse exception in input file!");
			e.printStackTrace();
			System.exit(1);
		}

		try {
			core.loadSpecification(specFilename);
		} catch (FileNotFoundException e) {
			System.out.println("Specification file not found!");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("IO Error reading specification file!");
			System.exit(1);
		} catch (ParseException e) {
			System.out.println("Error parsing specification file!");
			System.out.println(e.getMessage());
			System.exit(1);
		}
		  
		if (procNumber == -1) {
			System.out.println("Checking all procedures.");
			long startTime = System.currentTimeMillis();
			KripkeStructure resProc = core.checkAll();
			long stopTime = System.currentTimeMillis();

			if (resProc != null) 
				System.out.println(
						"k, s0 |= F holds for procedure " + resProc.getName() 
						+ "! Constraints for remaining unbound variables follow:\n" 
						+ resProc.getEntryNode().getBindingSet(core.getCtplFormula())
						);
			else System.out.println("k, s0 |= F does not hold for any procedure!");
			System.out.println("Total time: " + (stopTime - startTime)/1000.0 + "s.");
		}
		else {
			System.out.println("Checking procedure " + core.getKripkeStructure(procNumber).getName());
			if (core.checkProcedure(procNumber))
				System.out.println("k, s0 |= F holds! Constraints for remaining unbound variables follow:\n" 
						+ core.getKripkeStructure(procNumber).getEntryNode().getBindingSet(core.getCtplFormula()));
			else System.out.println("k, s0 |= F does not hold!");
		}
	}
}
