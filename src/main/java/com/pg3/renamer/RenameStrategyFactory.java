package com.pg3.renamer;

import com.pg3.Constants;

public class RenameStrategyFactory {

	private static RenameStrategyFactory instance = new RenameStrategyFactory();
	
	private RenameStrategyFactory() {
	}
	
	public static RenameStrategyFactory getInstance() {
		return instance;
	}
	
	public RenameStrategy get(String newPartPosition) {
		switch(newPartPosition){
		case Constants.PRE:
			return new AddPreffix();
		case Constants.SUFFIX:
			return new AddSUffix();
		default:
			throw new RuntimeException("Rename strategy not supported");
		}
	}
	
}
