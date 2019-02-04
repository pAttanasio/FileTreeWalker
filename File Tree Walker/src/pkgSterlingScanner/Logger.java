package pkgSterlingScanner;

import java.util.*;
import java.util.regex.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import java.io.*;
import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardOpenOption.*;

public class Logger {

	private ArrayList<String> filePaths;
	private ArrayList<String> noAccessFiles;
	private String LogName;
	private String LogDestination;
	private long totalRuntime;
	
	Logger(String rootFolder){
		
		String[] rootName = rootFolder.split("\\\\");
		rootFolder = rootName[1];
		
		DateTimeFormatter dtf= DateTimeFormatter.ofPattern("MM-dd-yyyy HH_mm_ss");
		LocalDateTime now = LocalDateTime.now();
		setLogName((String) dtf.format(now) + " " + rootFolder);
		setLogDestination("S:\\8 - SOLIDWORKS MODELS\\SOLIDWORKS TEMPLATES\\Macros\\Duplicates Logs\\");
		filePaths = new ArrayList<String>();
		noAccessFiles = new ArrayList<String>();
	}
	
	protected String getLogName(){
		return this.LogName;
	}
	
	protected String getLogDestination(){
		return this.LogDestination;
	}
	
	protected void setLogName(String newLogName){
		this.LogName = newLogName;
	}
	
	protected void setLogDestination(String newLogDestination){
		this.LogDestination = newLogDestination;
	}
	
	protected void setTotalRuntime(long runtime){
		this.totalRuntime = runtime;
	}
	
	protected void logDuplicates(String filePath1, String filePath2){
		this.filePaths.add(filePath1);
		this.filePaths.add(filePath2);
	}
	
	protected void logNoAccess(String filePath1){
		this.noAccessFiles.add(filePath1);
	}
	
	protected void endLog(){
		
		Path outputFilePath = Paths.get(this.getLogDestination() + this.getLogName() + ".txt");
		String dataString = "Auto Generated Log from the Sterling File Tree Walk executed on " + this.getLogName();
		byte[] data = dataString.getBytes();
		
		try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(outputFilePath, CREATE, APPEND))){
			out.write(data, 0, data.length); // Write the log title
			
			dataString = "\nTotal Runtime: " + (this.totalRuntime / 1000) + "s";
			data = dataString.getBytes();
			out.write(data, 0, data.length);
			
			dataString = "\n\nDuplicate Files: ";
			data = dataString.getBytes();
			out.write(data, 0, data.length); 
			
			dataString = "\nTotal Duplicates: " + filePaths.size();
			data = dataString.getBytes();
			out.write(data, 0, data.length);
			
			for (int i = 0; i < this.filePaths.size(); i++){
				dataString = "\n" + this.filePaths.get(i);
				data = dataString.getBytes();
				out.write(data, 0, data.length);
				if (Math.floorMod(i+1, 2) == 0){
					dataString = "\n";
					data = dataString.getBytes();
					out.write(data, 0, data.length);
				}
			}
			
			dataString = "\n\nFiles and/or directories that the executing user does not have access to:";
			data = dataString.getBytes();
			out.write(data, 0, data.length);
			
			for (int i = 0; i < this.noAccessFiles.size(); i++){
				dataString = "\n" + this.noAccessFiles.get(i);
				data = dataString.getBytes();
				out.write(data, 0, data.length);
				data = "\n".getBytes();
				out.write(data, 0, data.length);
			}
			
			dataString = "\n\nEnd of log.";
			data = dataString.getBytes();
			out.write(data, 0, data.length);
		} catch (IOException x) {
			System.err.println(x);
			System.out.println();
			x.printStackTrace();
		}
		
	}
	
}
