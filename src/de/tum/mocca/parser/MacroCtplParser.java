/* 
 * MacroCtplParser.java - Copyright 2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import de.tum.mocca.parser.macro.Macro;
import de.tum.mocca.parser.macro.MacroFactory;

/**
 * Preprocesses a specification formula to expand macros. Currently
 * accepts the %syscall(c, p1, ..., pn) macro.
 *
 * @author Johannes Kinder
 */
public class MacroCtplParser {
	
	private static Logger log = Logger.getLogger(MacroCtplParser.class.getName());
	private static char macroPrefix = '%'; 
	
	public static String parse(String s) throws ParseException {
		MacroFactory macroFactory = new MacroFactory();
		int locCounter = 1;	
		
		/* expand macros until none are left
		 this loop is needed for nested macros */
		int pass = 1;
		while (s.indexOf(macroPrefix) >= 0) {
			String appendix = "";
			String out = "";
			int pos = 0;
			
			for (;;) {
				/* Find macro prefix */
				int pos2 = s.indexOf(macroPrefix, pos);
				if (pos2 < 0) {
					out += s.substring(pos, s.length());
					break;
				}
				/* Copy everything before the prefix into output */
				out += s.substring(pos, pos2);
				for (pos = pos2; pos < s.length(); pos++)
					if (s.charAt(pos) == '(' 
						|| s.charAt(pos) == ' ' 
							|| s.charAt(pos) == '\n'
								|| s.charAt(pos) == ')') break;
				if (pos > s.length()) throw new ParseException("Unfinished macro.");
				String macroName = s.substring(pos2 + 1, pos);
				String[] params;
				// If we have parameters
				if (s.charAt(pos) == '(') {
					pos++;
					pos2 = s.indexOf(')', pos);
					params = s.substring(pos, pos2).split(",");
					for (int i = 0; i < params.length; i++) 
						params[i] = params[i].trim();
					pos = pos2 + 1;
				} else params = new String[0];
				
				Macro macro = macroFactory.create(locCounter++, macroName, params);
				out += macro.getExpansion();
				appendix += macro.getAppendix();
			}
			s = (out + appendix);
			log.debug("Preprocessing pass " + pass + ":\n" + s);
			pass++;
		}
		return s;
	}
	
	public static void main(String[] args) throws IOException {
		String s = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter formula:");
		for(;;) {
			s = in.readLine();
			if (s.equals("quit")) return;
			try {
				System.out.println(parse(s));
			} catch (ParseException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
