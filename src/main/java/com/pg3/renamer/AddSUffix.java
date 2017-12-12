package com.pg3.renamer;

import java.nio.file.Path;

import com.pg3.Utils;

public class AddSUffix implements RenameStrategy {

	@Override
	public String createNewPath(Path current, String newPart, String separator) {
		Path parent = current.getParent();
		String basePath = (parent != null ? parent.toString() : "") + "/";
		if(current.toFile().isFile()) {
			String[] fileNameParts = Utils.getFileNameParts(current.toString());
			String fileName = "";
			int lastPosition = fileNameParts.length - 1;
			for (int i = 0; i < lastPosition ; i++) {
				fileName += "." + fileNameParts[i];
			}
			return basePath + fileName.substring(1) + separator + newPart + "." +  fileNameParts[lastPosition];
		}
		String directoryName = Utils.getDirectoryName(current.toString());
		return basePath + directoryName + separator + newPart;
	}

}
