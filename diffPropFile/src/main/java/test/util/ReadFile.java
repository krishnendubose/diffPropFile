package test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class ReadFile {

	final static Logger logger = Logger.getLogger(ReadFile.class);

	public static ArrayList<File> getFileList(String dirPath){

		logger.info("Within getFileList()");
		ArrayList<File> fileList = new ArrayList<File>();
		File folder = new File(dirPath);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile() && file.getName().trim().contains(Common_Constant.PROP_FILE_EXTENSION_PROPERTIES)) {
				fileList.add(file);
			}
		}

		logger.info("Exiting getFileList()");
		return fileList;

	}

	public static HashSet<String> readPropFile(File propFile) throws IOException{
		logger.info("Within readPropFile()");

		HashSet<String> propFileSet = new HashSet<String>();
		BufferedReader br = null;

		String line; 
		try {

			br = new BufferedReader(new FileReader(propFile));	
			while ((line = br.readLine()) != null && line.indexOf(Common_Constant.COMMENT_CHAR) != 0 ) {
				logger.trace("Line in file:- " + propFile.getName() + " is :- " + line);
				propFileSet.add(line.trim());
			}

		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();				

			} catch (IOException ex) {
				logger.error(ex.getMessage());
				ex.printStackTrace();

			}
		}

		logger.info("Exiting readPropFile()");
		return propFileSet;
	}
	
	public static ArrayList<ReadFileThread> readPropFileInThread(ArrayList<File> propFileList) throws InterruptedException {
		
		logger.info("Within readPropFileInThread()");
		
		ArrayList<ReadFileThread> readFileThreadList = new ArrayList<ReadFileThread>();
		ExecutorService executor = Executors.newFixedThreadPool(propFileList.size());
		
		for(File propFile: propFileList) {
			ReadFileThread readFileThread = new ReadFileThread(propFile.getName().replaceAll(".properties",""), propFile);
			readFileThreadList.add(readFileThread);
			executor.execute(readFileThread);
			
		}
		executor.shutdown();
		boolean allThreadsCompleted = executor.awaitTermination(Common_Constant.FILE_READ_TIMEOUT, TimeUnit.MINUTES);
		if (!allThreadsCompleted) {
			logger.error("File reading in-progress, increase read timeout.");
			System.exit(-1);
		}else {
			logger.info("All file reading Completed.");
		}
		
		logger.info("Exiting readPropFileInThread()");
		return readFileThreadList;
	}
	
	
}
