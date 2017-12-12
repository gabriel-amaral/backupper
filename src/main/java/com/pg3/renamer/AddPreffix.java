package com.pg3.renamer;

import java.nio.file.Path;

import com.pg3.Utils;

public class AddPreffix implements RenameStrategy {

	@Override
	public String createNewPath(Path current, String newPart, String separator) {
		Path parent = current.getParent();
		String basePath = (parent != null ? parent.toString() : "") + "/";
		if(current.toFile().isFile()) {
			String fileName = Utils.getFileName(current.toString());
			return basePath + newPart + separator + fileName;
		}
		String directoryName = Utils.getDirectoryName(current.toString());
		return basePath + newPart + separator + directoryName;		
	}

	
}
