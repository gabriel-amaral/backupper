package com.pg3.commands;

import java.io.File;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import com.pg3.Constants;

public class CleanDestinationCommand implements Command {

	private String separator;
	private String datePosition;
	private String dateFormat;
	private Date today;
	private long daysToKeep;
	
	public CleanDestinationCommand(Date today, long daysToKeep, String dateFormat, String datePosition, String separator) {
		this.separator = separator;
		this.datePosition = datePosition;
		this.dateFormat = dateFormat;
		this.today = today;
		this.daysToKeep = daysToKeep;
	}

	@Override
	public String process(String source) throws Exception {
		Path parent = Paths.get(source).getParent();
		File[] fileList = parent.toFile().listFiles();
		for (File file : fileList) {
			try {
				LinkedList<String> datePossibilities = new LinkedList<>();
				String localPath = file.getName();
				if(file.isFile() && localPath.lastIndexOf(".") != -1) {
					localPath = localPath.substring(0, localPath.lastIndexOf("."));
				}
				String[] localFileParts = localPath.split("\\" + this.separator);
				if(localFileParts.length < 2)
					continue;
				datePossibilities.add(localFileParts[0]);
				datePossibilities.add(localFileParts[localFileParts.length-1]);
	
				String dateAsString = datePossibilities.getFirst();
				if( Constants.SUFFIX.equals(this.datePosition) ) {
					dateAsString = datePossibilities.getLast();
				}
				Date fileDate = new SimpleDateFormat(dateFormat).parse(dateAsString);
				long diff = this.today.getTime() - fileDate.getTime();
				long diffInDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				if(diffInDays >= this.daysToKeep) {
					if(file.isFile()) {
						file.delete();
					} else {
						Files.walk(file.toPath(), FileVisitOption.FOLLOW_LINKS)
					    .sorted(Comparator.reverseOrder())
					    .map(Path::toFile)
					    .forEach(File::delete);
					}
				}
			} catch(Exception e) {
				System.out.println(file.getName() + " error:" + e.getMessage());
			}
		}
		return source;
	}

	@Override
	public String getRawTrashPath() {
		return null;
	}

}
