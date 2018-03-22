package exunion.config;

import org.apache.logging.log4j.util.PropertiesUtil;

public class Configuration {
	
	private static PropertiesUtil properties = null;
	
	private static String fileName = "application.properties";
	
	private static void load(String fileName){
		properties = new PropertiesUtil(fileName);
	}

	public static PropertiesUtil getProperties(){
		if (null == properties){
			load(fileName);
		}
		return properties;
	}
	
	public static void setPropertyFile(String fileName){
		load(fileName);
	}
	
}
