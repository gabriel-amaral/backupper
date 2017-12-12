package com.pg3.commands;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.pg3.renamer.RenameStrategyFactory;

public class RenameCommand implements Command {
	
	private String newPartPosition = null;
	private String newPart = null;
	private String separator = null;

	public RenameCommand(String newPartPosition, String newPart, String separator) {
		this.newPartPosition = newPartPosition;
		this.newPart = newPart;
		this.separator = separator;
		
	}

	@Override
	public String process(String source) throws Exception {
		Path current = Paths.get(source);
		String newRawPath = RenameStrategyFactory.getInstance().get(this.newPartPosition).createNewPath(current, newPart, separator);
		File newFile = new File(newRawPath);
		current.toFile().renameTo(newFile);
		return newRawPath;
	}

	@Override
	public String getRawTrashPath() {
		return null;
	}

}
