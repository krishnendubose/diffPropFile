package test.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.log4j.Logger;

public class Main {
	
	public static final String DIR_PATH = "/home/krishnendu/Desktop/Development/projects/diffPropFile/Prop_files";
	public static final String OUTPUT_DIR_PATH = DIR_PATH+"/output"; 
	final static Logger logger = Logger.getLogger(Main.class);
	static HashSet<String> unionOfAllPropSet;
	/*
	public static void main(String[] args) {
		String propName = "prop.properties";
		System.out.println(propName.replaceAll(".properties",""));
	}*/
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		logger.info("Within main()");
		ArrayList<File> propFileList = ReadFile.getFileList(DIR_PATH);
		if( propFileList.size() == 0 ) {
			logger.error("Dir "+DIR_PATH+" is empty.");
			System.exit(0);
		}
		ArrayList<ReadFileThread> readFileThreadList = ReadFile.readPropFileInThread(propFileList);
		unionOfAllPropSet = new HashSet<String>();
		for(ReadFileThread readFileThread: readFileThreadList) {
			unionOfAllPropSet.addAll(readFileThread.getPropFileSet());
		}
		System.out.println("Union of all props:- \n" + unionOfAllPropSet);
		
		
		WriteFile.writePropFileInThread(unionOfAllPropSet, readFileThreadList, OUTPUT_DIR_PATH);
		logger.info("Exiting main()");
	}

}
