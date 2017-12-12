package com.pg3.commands;

public interface Command {

	String process(String source) throws Exception;

	String getRawTrashPath();
	
}
