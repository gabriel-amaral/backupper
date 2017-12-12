package com.pg3.commands;

import java.io.File;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class DeleteCommand implements Command {

	private String continuePath;
	
	public DeleteCommand(String continuePath) {
		this.continuePath = continuePath;
	}

	@Override
	public String process(String source) throws Exception {
		Path current = Paths.get(source);
		if(current.toFile().isFile()) {
			current.toFile().delete();
		} else {
			Files.walk(current, FileVisitOption.FOLLOW_LINKS)
		    .sorted(Comparator.reverseOrder())
		    .map(Path::toFile)
		    .forEach(File::delete);
		}
		return this.continuePath;
	}

	@Override
	public String getRawTrashPath() {
		return null;
	}
	
}
