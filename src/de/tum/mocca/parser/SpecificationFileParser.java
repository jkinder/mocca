/* 
 * SpecificationFileParser.java - Copyright 2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import de.tum.mocca.Specification;

/**
 * TODO enter class comment
 *
 * @author Johannes Kinder
 */
public class SpecificationFileParser {
	
	private Logger log = Logger.getLogger(SpecificationFileParser.class.getName());

	private BufferedReader in;
	
	private static final int STATE_NONE = 0;
	private static final int STATE_VERSION = 1;
	private static final int STATE_NAME = 2;
	private static final int STATE_DESCRIPTION = 3;
	private static final int STATE_CLUES = 4;
	private static final int STATE_FORMULA = 5;
	
	public static final char COMMENT_CHAR = ';';

	/**
	 * Creates a new SpecificationFileParser bound to the
	 * given BufferedReader.
	 * @param in The input reader. 
	 */
	public SpecificationFileParser(BufferedReader in) {
		this.in = in;
	}
	
	public Specification parseSpecification() throws IOException, ParseException {
		
		Specification spec = new Specification();
		String description = "";
		String formula = "";
		List clues = new LinkedList();
		int lineNumber = 0;
		int state = STATE_NONE;
		for (;;) {
			String line = in.readLine();
			if (line == null) break;
			lineNumber++;
			int commentIndex = line.indexOf(COMMENT_CHAR);
			if (commentIndex >= 0) line = line.substring(0, commentIndex);
			line = line.trim();
			if (line.equals("")) continue;
			else if (line.equals("[version]")) state = STATE_VERSION;
			else if (line.equals("[name]")) state = STATE_NAME;
			else if (line.equals("[description]")) state = STATE_DESCRIPTION;
			else if (line.equals("[clues]")) state = STATE_CLUES;
			else if (line.equals("[formula]")) state = STATE_FORMULA;
			else switch(state) {
				case STATE_VERSION:
					spec.setVersion(line);
					state = STATE_NONE;
					break;
				case STATE_NAME:
					spec.setName(line);
					state = STATE_NONE;
					break;
				case STATE_DESCRIPTION:
					description += line + "\n";
					break;
				case STATE_CLUES:
					clues.add(line);
					break;
				case STATE_FORMULA:
					formula += line;
					break;
				default:
					log.error("Parse Error in line " + lineNumber + "!");
					throw new ParseException("Parse Error in line " + lineNumber + ".");
			}
			
		}
		spec.setDescription(description);
		String prepCtpl = MacroCtplParser.parse(formula);
		log.info("Preprocessed formula to: " + prepCtpl);
		spec.parseCtlFormula(prepCtpl);
		spec.setClues(clues);
		return spec;
	}
}
