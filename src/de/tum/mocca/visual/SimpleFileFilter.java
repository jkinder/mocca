/* 
 * SimpleFileFilter.java - Copyright 2005 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.visual;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * TODO enter class comment
 *
 * @author Johannes Kinder
 */
public class SimpleFileFilter extends FileFilter {
	
	private String[] extensions;
	private String description;

	public SimpleFileFilter(String extension, String description) {
		this(new String[]{extension}, description);
	}

	public SimpleFileFilter(String[] extension, String description) {
		super();
		this.extensions = extension;
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {
		if (f.isDirectory()) return true;
		String ext = getExtension(f);
		for (int i = 0; i < extensions.length; i++)
			if (extensions[i].equals(ext)) return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Return the file extension.
	 */
	private String getExtension(File f) {
		if(f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if((i > 0) && (i < filename.length() - 1)) {
				return filename.substring(i + 1).toLowerCase();
			};
		}
		return null;
	}
}
