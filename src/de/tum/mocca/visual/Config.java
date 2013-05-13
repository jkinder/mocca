/* 
 * Config.java - Copyright 2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.visual;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * TODO enter class comment
 *
 * @author Johannes Kinder
 */
public class Config {
	
	private String specificationDir;
	private String assemblerDir;
	private final String propHeader = "Mocca Properties";
	private final String specDirProp = "SpecificationDirectory";
	private final String asmDirProp = "AssemblerDirectory";
	private Properties prop;

	public Config() {
		prop = new Properties();
		try {
			File pFile = new File("mocca.properties");
			prop.load(new FileInputStream(pFile));
		} catch (Exception e) {
		}
		specificationDir = prop.getProperty(specDirProp, "");
		assemblerDir = prop.getProperty(asmDirProp, "");
	}
	
	public void save() {
		prop.setProperty(specDirProp, specificationDir);
		prop.setProperty(asmDirProp, assemblerDir);
		try {
			File pFile = new File("mocca.properties");
			prop.store(new FileOutputStream(pFile), propHeader);
		} catch (Exception e) {
		}
	}

	/**
	 * @return Returns the assemblerDir.
	 */
	public String getAssemblerDir() {
		return assemblerDir;
	}
	/**
	 * @param assemblerDir The assemblerDir to set.
	 */
	public void setAssemblerDir(String assemblerDir) {
		this.assemblerDir = assemblerDir;
	}
	/**
	 * @return Returns the specificationDir.
	 */
	public String getSpecificationDir() {
		return specificationDir;
	}
	/**
	 * @param specificationDir The specificationDir to set.
	 */
	public void setSpecificationDir(String specificationDir) {
		this.specificationDir = specificationDir;
	}
	
}
