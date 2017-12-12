package com.pg3;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class Utils {

	public static String[] getFileNameParts(final String src) {
	    return getFileName(src).split("\\.");
	}
	
	public static String getFileName(final String src) {
	    assert StringUtils.isNotEmpty(src);
	
	    final File file = new File(src);
	    if (file.isFile()) {
	        return file.getName();
	    } else {
	        throw new RuntimeException("source is not a valid file");
	    }
	}
	
	public static String getDirectoryName(final String src) {
	    assert StringUtils.isNotEmpty(src);
	
		Path path = Paths.get(src);
	    if (path != null) {
	        return path.getFileName().toString();
	    } else {
	        throw new RuntimeException("source is not a valid file");
	    }
	}
	
	public static String escapeEspecialCharacters(String text) {
		Pattern p = Pattern.compile("([ ()])"); //regex setup
		Matcher m = p.matcher(text); //sample input
		StringBuffer s = new StringBuffer();
		while (m.find())
		    m.appendReplacement(s, "\\\\\\\\" + m.group(1));
		m.appendTail(s); //add the last tail of code
		return s.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(escapeEspecialCharacters("jar:file:/C:/Users/Administrador/Google Drive/Neo4j/Backup/solarbraz.graphdb.zip"));
	}
	
}
