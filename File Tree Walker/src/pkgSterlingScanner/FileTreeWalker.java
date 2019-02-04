package pkgSterlingScanner;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class FileTreeWalker {

	private Path startPath;
	private Logger logger;
	private Path retPathFile;
	private ArrayList<Path> dynamicListOfFiles;
	private long totalRuntime;
	private ArrayList<String> excludeFiles;
	
	FileTreeWalker(String rootFolder){
		this.logger = new Logger(rootFolder);
		this.dynamicListOfFiles = new ArrayList<Path>();
		this.excludeFiles = new ArrayList<String>();
		this.initializeExclusions();
	}
	
	protected void initializeExclusions(){
		this.excludeFiles.add("Thumbs.db");
		this.excludeFiles.add("plot.log");
		this.excludeFiles.add("PLOT.log");
	}
	
	protected Path getStartPath(){
		return this.startPath;
	}
	
	protected void setStartPath(String newStartPath){
		this.startPath = Paths.get(newStartPath);
	}
	
	protected void addToList(Path fileName){
		this.dynamicListOfFiles.add(fileName);
	}
	
	protected void addToNoAccessList(Path fileName){
		this.logger.logNoAccess(fileName.toString());;
	}
	
	protected ArrayList<Path> getDynamicListOfFiles(){
		return this.dynamicListOfFiles;
	}
	
	protected long getTotalRuntime(){
		return this.totalRuntime;
	}
	
	protected void setTotalRuntime(long runtime){
		this.totalRuntime = runtime;
		logger.setTotalRuntime(runtime);
	}
	
	protected ArrayList<String> getExcludeFiles(){
		return this.excludeFiles;
	}
	
	protected void stopWalking(){
		this.logger.endLog();
	}
	
	protected void findDuplicates(){
		
		System.out.println("dynamicListOfFiles.size() = " + dynamicListOfFiles.size());
		System.out.println("Finding duplicates...");
		Path lCompare;
		Path rCompare;
		for (int i = 0; i < dynamicListOfFiles.size(); i++){
			lCompare = dynamicListOfFiles.get(i).getFileName();
			for (int j = i+1; j < dynamicListOfFiles.size(); j++){
				rCompare = dynamicListOfFiles.get(j).getFileName();
				//System.out.println("lCompare = " + lCompare + "\nrCompare = " + rCompare);
				if (lCompare.equals(rCompare)){
					logger.logDuplicates(dynamicListOfFiles.get(i).toString(), dynamicListOfFiles.get(j).toString());
					i++;
					break;
				}
			}
		}
		System.out.println("Finished finding duplicates...");
	}

	
	protected void startWalking() throws IOException{
		
		ArrayList<String> exclusions = this.getExcludeFiles();
		System.out.println("Walking File Tree...");
		retPathFile = java.nio.file.Files.walkFileTree(startPath, new SimpleFileVisitor<Path>(){
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
				for (int i = 0; i < exclusions.size(); i++){
					if (file.getFileName().toString().equals(exclusions.get(i))){
						return FileVisitResult.CONTINUE;
					}
				}
				addToList(file);
				return FileVisitResult.CONTINUE;
			}
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException{
				if (e == null){
					return FileVisitResult.CONTINUE;
				}
				else {
					// directory iteration failed
					throw e;
				}
			}
			@Override
			public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException{				
				if (file.getFileName().toString().equals("Thumbs.db")){
					return FileVisitResult.CONTINUE;
				}
				else{
					addToList(file);
					addToNoAccessList(file);
					return FileVisitResult.CONTINUE;
				}
			}
		});
		System.out.println("Finished walking file tree...");
		this.findDuplicates();
	}
	
}
