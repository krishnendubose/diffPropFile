package test.util;

import java.io.IOException;
import java.util.HashSet;

import org.apache.log4j.Logger;

public class WriteFileThread extends Thread {
	
	final static Logger logger = Logger.getLogger(WriteFileThread.class);
	String propFileName;
	String outputDirPath;
	HashSet<String> propDataSet;
	String propFileExt;
	
	public WriteFileThread(String propFileName, String outputDirPath, HashSet<String> propDataSet,String propFileExt) {
		super(propFileName);
		this.propFileName = propFileName;
		this.propDataSet = propDataSet;
		this.outputDirPath = outputDirPath;
		this.propFileExt = propFileExt;
	}
	
	public void run() {
		logger.info("Within run() of " +this.getName());
		try {
			WriteFile.writePropFile(propFileName, outputDirPath, propDataSet, propFileExt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		logger.info("Exiting run() of " +this.getName());
	}
}
