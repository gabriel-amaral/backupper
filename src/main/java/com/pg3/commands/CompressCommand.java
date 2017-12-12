package com.pg3.commands;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pg3.Constants;
import com.pg3.visitor.DirectoryCopyVisitor;

public class CompressCommand implements Command {

	private String extension;
	private List<String> filesToSkip;
	private boolean isToDeleteSource;
	private String trash;

	public CompressCommand(String extension, List<String> filesToSkip, boolean isToDeleteSource){
		this.extension = extension;
		this.filesToSkip = filesToSkip;
		this.isToDeleteSource = isToDeleteSource;
	}
	
	@Override
	public String process(String source) throws Exception {
		if(isToDeleteSource) {
			this.trash = source;
		}
		String zipFilename = source + Constants.DEFAULT_EXTENSION_SEPARATOR + this.extension;
		try (FileSystem compressFileSystem = createComrpessFileSystem(zipFilename, true)) {
			Path root = compressFileSystem.getPath("/");
			
			final Path src = Paths.get(source);

			if(Files.isDirectory(src)){
				root = compressFileSystem.getPath("/" + src.getFileName());
				Files.createDirectories(root);
				new DirectoryCopyVisitor(src, root, filesToSkip).copy();
			} else {
				final Path dest = compressFileSystem.getPath(root.toString(), src.getFileName().toString());
				Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
			}
		}
		return zipFilename;
	}
	
	private FileSystem createComrpessFileSystem(String zipFilename, boolean create) throws IOException {
		final Path path = Paths.get(zipFilename);
		String uriPath = "jar:file://" + path.toUri().getPath();
		final URI uri = URI.create(uriPath.replaceAll(" ", "%20"));
		
		final Map<String, String> env = new HashMap<>();
		if (create) {
			env.put("create", "true");
		}
		return FileSystems.newFileSystem(uri, env);
	}

	@Override
	public String getRawTrashPath() {
		return this.trash;
	}
	
	public static void main(String[] args) throws Exception {
		new CompressCommand("zip", new LinkedList<String>(), false).process("/Users/gamaral/dev-env/neo4j-community-3.1.3/data/databases/solarbraz.graphdb");
	}
	
}
