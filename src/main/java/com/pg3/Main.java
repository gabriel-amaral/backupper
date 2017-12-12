package com.pg3;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.pg3.commands.CleanDestinationCommand;
import com.pg3.commands.CommandExecutor;
import com.pg3.commands.CompressCommand;
import com.pg3.commands.CopyCommand;
import com.pg3.commands.RenameCommand;

public class Main {

	private static final String DESTINATION_OPTION = "d";
	private static final String COMPRESS_OPTION = "c";
	private static final String SOURCE_OPTION = "s";
	private static final String HELP_OPTION = "h";
	private static final String DATE_FORMAT_OPTION = "df";
	private static final String DATE_POSITION_OPTION = "dp";
	private static final String DAYS_KEEP_PERIOD_OPTION = "dk";
	private static final String SKIP_FILES_OPTION = "sf";
	private static final String DELETE_SOURCE_OPTION = "ds";
	
	private static CommandExecutor commandExecutor = new CommandExecutor();

	public static void main(String[] args) throws Exception {
		// create the parser
		try {
			// parse the command line arguments
			Options options = createOptions();
			CommandLine line = new DefaultParser().parse( options, args );
			if( line.hasOption( HELP_OPTION ) ) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "backupper", options );
			} else {
				String[] optionValues = line.hasOption(SKIP_FILES_OPTION) ? line.getOptionValues(SKIP_FILES_OPTION) : new String[0];
				LinkedList<String> skipFiles = new LinkedList<>(Arrays.asList(optionValues));
				String source = line.getOptionValue(SOURCE_OPTION);
				boolean isToDeleteSource = line.hasOption(DELETE_SOURCE_OPTION);
				if(line.hasOption( COMPRESS_OPTION )) {
					String compressExtension = line.getOptionValue( COMPRESS_OPTION ).toLowerCase();
					commandExecutor.addCommand(new CompressCommand(compressExtension, skipFiles, isToDeleteSource));
					isToDeleteSource = true;
				}				
				String destination = line.getOptionValue(DESTINATION_OPTION);
				commandExecutor.addCommand(new CopyCommand(destination, skipFiles, isToDeleteSource));
				if(line.hasOption( DATE_POSITION_OPTION )) {
					String datePosition = line.getOptionValue(DATE_POSITION_OPTION);
					String dateFormat = line.hasOption( DATE_FORMAT_OPTION ) ? line.getOptionValue(DATE_FORMAT_OPTION) : "yyyyMMdd";
					Date today = new Date();
					String dateFormatted = new SimpleDateFormat(dateFormat).format(today);
					String separator = Constants.DEFAULT_SEPARATOR;
					commandExecutor.addCommand(new RenameCommand(datePosition, dateFormatted, separator));
					if(line.hasOption( DAYS_KEEP_PERIOD_OPTION )) {
						long daysToKeep = Long.parseLong(line.getOptionValue(DAYS_KEEP_PERIOD_OPTION));
						commandExecutor.addCommand(new CleanDestinationCommand(today,daysToKeep, dateFormat, datePosition, separator));
					}
				}
				commandExecutor.execute(source);
			}
		} catch( ParseException exp ) {
			// oops, something went wrong
			System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
			exp.printStackTrace();
			throw exp;
		} catch( Exception exp ) {
			System.err.println( "Command failed.  Reason: " + exp.getMessage() );
			exp.printStackTrace();
			throw exp;
		}
	}

	
	private static Options createOptions() {
		Option help = new Option( HELP_OPTION, "print the usage and the options" );
		Option compressOption = Option.builder(COMPRESS_OPTION)
                .hasArg()
                .optionalArg(true)
                .desc("it uses the file system compression, cosidering that your file system supports the extension you are trying to use you can "
                	+ "use any extension you want. For example you can use \"-compress tar.gz\", but your file system needs support this kind of compression. "
                	+ "If it isn't defined the source won't be compressed")
                .build();
		Option datePosition = Option.builder(DATE_POSITION_OPTION)
                .hasArg()
                .optionalArg(true)
                .desc("position of the date in the name: \"PRE\" to be added as a prefix or \"SUF\" to be added as a suffix. If it isn't defined the date won't be added")
                .build();  
		Option dateFormat = Option.builder(DATE_FORMAT_OPTION)
                .hasArg()
                .optionalArg(true)
                .desc("the date format to be applied in the name. If it isn't definide the format applied will be yyyyMMdd (java.text.SimpleDateFormat)")
                .build();
		Option daysKeepPeriod = Option.builder(DAYS_KEEP_PERIOD_OPTION)
                .hasArg()
                .optionalArg(true)
                .desc("Once date position is setted you can define the period to this day to keep the files. The process will delete de files with the same name structure older than today.")
                .build();  
		Option srcPath = Option.builder(SOURCE_OPTION)
				                .hasArg()
				                .desc("the source path")
				                .build();
		Option deleteSource = Option.builder(DELETE_SOURCE_OPTION)
                .desc("delete the source.")
                .build();
		Option skipFiles = Option.builder(SKIP_FILES_OPTION)
                .hasArgs()
                .desc("type of the source: \"D\" - Directory or \"F\" - File")
                .build();
		Option destPath = Option.builder(DESTINATION_OPTION)
                .hasArg()
                .desc("the destination path")
                .build();
		Options options = new Options();

		options.addOption( help );
		options.addOption( compressOption );
		options.addOption( dateFormat );
		options.addOption( datePosition );
		options.addOption( daysKeepPeriod );
		
		options.addOption( srcPath );
		options.addOption( skipFiles );
		options.addOption( deleteSource );
		options.addOption( destPath );
		
		return options;
	}
	

}
