package com.pg3.renamer;

import java.nio.file.Path;

public interface RenameStrategy {

	String createNewPath(Path current, String newPart, String separator);
	
}
