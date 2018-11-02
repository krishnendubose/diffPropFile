package test.util;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.apache.log4j.Logger;

public class ReadFileThread extends Thread {
	File propFile;
	HashSet<String> propFileSet;
	final static Logger logger = Logger.getLogger(ReadFileThread.class);

	public HashSet<String> getPropFileSet() {
		return propFileSet;
	}	

	public ReadFileThread(String name, File propFile) {
		super(name);
		this.propFile = propFile;
	}

	@Override
	public void run() {
		logger.info("Within run() of " +this.getName());
		try {
			propFileSet = ReadFile.readPropFile(propFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		logger.info("Exiting run() of " +this.getName());
	}
}
