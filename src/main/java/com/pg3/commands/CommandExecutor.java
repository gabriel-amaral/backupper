package com.pg3.commands;

import java.util.LinkedList;
import java.util.List;

public class CommandExecutor {

	private List<Command> commands = new LinkedList<>();

	public void addCommand(Command operation){
		this.commands.add(operation);
	}
	
	public String execute(String source) throws Exception {
		String finalPath = source;
		for (Command command : commands) {
			finalPath = command.process(finalPath);
			cleanTrash(command);
		}
		return finalPath;
	}
	
	private void cleanTrash(Command processedCommand) throws Exception {
		String rawTrashPath = processedCommand.getRawTrashPath();
		if(rawTrashPath != null)
			new DeleteCommand(null).process(rawTrashPath);
	}
	
}
