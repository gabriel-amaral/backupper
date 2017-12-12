package com.pg3.visitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

public class DirectoryCopyVisitor extends SimpleFileVisitor<Path> {

	private Path sourceDir;
    private Path targetDir;
	private List<String> filesToSkip = new LinkedList<>();

	public DirectoryCopyVisitor(Path sourceDir, Path targetDir) {
		this.sourceDir = sourceDir;
		this.targetDir = targetDir;
	}
	
    public DirectoryCopyVisitor(Path sourceDir, Path targetDir, List<String> filesToSkip) {
        this.sourceDir = sourceDir;
        this.targetDir = targetDir;
        this.filesToSkip = filesToSkip;
    }
    
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
        try {
            Path targetFile = targetDir.resolve(sourceDir.relativize(file).toString());
            for (String skipFile : this.filesToSkip) {
            	if(targetFile.getFileName().toString().startsWith(skipFile)){
            		return FileVisitResult.CONTINUE; 
            	}
			}
            Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (Exception e) {
        	System.out.println(file.toString());
        }
        return FileVisitResult.CONTINUE;
    }
 
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) {
        try {
            Path newDir = targetDir.resolve(sourceDir.relativize(dir).toString());
            if(Files.notExists(newDir)){
				Files.createDirectories(newDir);
			}
        } catch (Exception e) {
            System.err.println(e);
        }
        return FileVisitResult.CONTINUE;
    }
 
    public String copy() throws IOException {
        Files.walkFileTree(sourceDir, this);
        return this.targetDir.toAbsolutePath().toString();
    }

}

