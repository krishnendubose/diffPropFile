package test.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class WriteFile {
	final static Logger logger = Logger.getLogger(WriteFile.class);


	public static void writePropFile(String propFileName, String outputDirPath, HashSet<String> propDataSet, String propFileExt) throws IOException {
		logger.info("Within writePropFile()");
		File outputDirectory = new File(outputDirPath);
		if (! outputDirectory.exists()){
			outputDirectory.mkdir();
			// If you require it to make the entire directory path including parents,
			// use directory.mkdirs(); here instead.
		}

		FileWriter fileWriter = new FileWriter(outputDirPath+"/"+propFileName+propFileExt);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		for(String propData: propDataSet) {
			if(propFileExt.equals(Common_Constant.PROP_FILE_EXTENSION_YML)) {
				String key = propData.split("=")[0].trim();
				String value = propData.split("=")[1].trim();
				printWriter.println(key+Common_Constant.PROP_KEY_VALUE_SEPARATOR_COLON+value);
			}else {
				printWriter.println(propData);
			}
		}

		printWriter.close();
		logger.info("Exiting writePropFile()");
	}

	public static void writePropFileInThread(HashSet<String> unionOfAllPropSet, ArrayList<ReadFileThread> readFileThreadList, String outputDirPath) throws InterruptedException {
		logger.info("Within writePropFileInThread()");

		HashSet<String> intersectionDataSet = new HashSet<String>(unionOfAllPropSet);
		for(ReadFileThread readFileThread : readFileThreadList) {
			intersectionDataSet.retainAll(readFileThread.getPropFileSet());
		}
		/**
		 * 
		 */
		ExecutorService executor = Executors.newFixedThreadPool(readFileThreadList.size());

		for(ReadFileThread readFileThread : readFileThreadList) {
			HashSet<String> uniqDataSet = new HashSet<String>(readFileThread.getPropFileSet());
			uniqDataSet.removeAll(intersectionDataSet);
			WriteFileThread writeFileThread = new WriteFileThread(readFileThread.getName(), outputDirPath, 
					uniqDataSet, Common_Constant.PROP_FILE_EXTENSION_YML);
			executor.execute(writeFileThread);

		}
		WriteFileThread writeFileThread = new WriteFileThread(Common_Constant.COMMN_FILE_NAME, outputDirPath, 
				intersectionDataSet, Common_Constant.PROP_FILE_EXTENSION_PROPERTIES);
		executor.execute(writeFileThread);


		executor.shutdown();
		boolean allThreadsCompleted = executor.awaitTermination(Common_Constant.FILE_WRITE_TIMEOUT, TimeUnit.MINUTES);
		if (!allThreadsCompleted) {
			logger.error("File writing in-progress, increase read timeout.");
			System.exit(-1);
		}else {
			logger.info("All file writing Completed.");
		}

		logger.info("Exiting writePropFileInThread()");

	}
}
