/**
 * Philip M. Attanasio
 * 1/28/2019
 * The SterlingScanner is a program that scans through the file tree looking for 
 * duplicate files.
 * 
 * @author wfhq_pattanasio
 * @version 1.0
 */


package pkgSterlingScanner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class SterlingScanner {
	
	public static void main(String[] args) {
		
		ArrayList<Path> dynamicListOfFiles = new ArrayList<Path>();
		String rootFolder = "S:\\COSMOGAS";
		FileTreeWalker sterlingFTW = new FileTreeWalker(rootFolder);
		sterlingFTW.setStartPath(rootFolder);
		
		long startTime = System.currentTimeMillis();
		try {
			sterlingFTW.startWalking();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		sterlingFTW.setTotalRuntime(endTime - startTime);
		sterlingFTW.stopWalking();		
	}
}
