package com.pg3.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.pg3.Utils;
import com.pg3.visitor.DirectoryCopyVisitor;

public class CopyCommand implements Command {

	private String target;
	private List<String> filesToSkip;
	private boolean isToDeleteSource;
	private String trash;
	
	public CopyCommand(String target, List<String> filesToSkip, boolean isToDeleteSource){
		this.target = target;
		this.filesToSkip = filesToSkip;
		this.isToDeleteSource = isToDeleteSource;
	}
	
	@Override
	public String process(String source) throws Exception{
		if (StringUtils.isEmpty(source) || StringUtils.isEmpty(this.target)) {
	        throw new IllegalArgumentException("source and target required");
	    }
		if(this.isToDeleteSource) {
			this.trash = source;
		}
		Path sourcePath = Paths.get(source);
		if(Files.isDirectory(sourcePath)){
			String directoryName = Utils.getDirectoryName(source);
			Path targetPath = Paths.get(target + "/" + directoryName);
			if(this.target.endsWith("\\")) {
				targetPath = Paths.get(target + directoryName);
			}
			return new DirectoryCopyVisitor(sourcePath, targetPath, filesToSkip).copy();
		}
		String fileName = Utils.getFileName(source);
		Path targetPath = Paths.get(this.target + "/" + fileName);
		Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
		return targetPath.toAbsolutePath().toString();
	}

	@Override
	public String getRawTrashPath() {
		return this.trash;
	}

	public static void main(String[] args) throws Exception {
//		new CompressCommand("zip", new LinkedList<String>()).process("/Users/gamaral/dev-env/neo4j-community-3.1.3/data/databases/solarbraz.graphdb");
		new CopyCommand("/Users/gamaral/Desktop", new LinkedList<>(), false).process("/Users/gamaral/dev-env/neo4j-community-3.1.3/data/databases/solarbraz.graphdb");
	}
	
}
