package com.assignment.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;

/**
 * Utility class that contains:<br/><br/>
 * 1) HTTP result codes;<br/>
 * 2) methods to encode and decode binary data in Base64;<br/
 * 3) methods to create, load and save properties files.<br/>
 * 
 * @author lucas.reginato@gmail.com
 */
public class Utils {

	// possible return codes of the HTTP access.
	public static final int OK = 200;
	public static final int OK_WITH_NO_CONTENT = 204;
	public static final int INTERNAL_SERVER_ERROR = 500;
	
	// this should be a properties file, where
	// we can define output strings for the user
	// and have different languages (localization).
	public static final String RESULT = "Result";
	public static final String RESULT_ARE_EQUAL = "The right and left data are equals.";
	public  static final String RESULT_NOT_SAME_LENGTH = "The right and left data does not have the same length.";
	public  static final String RESULT_SAME_LENGTH_WITH_DIFFS = "The right and left data have the same length, but differences were found.";
	public  static final String DIFFERENCES = "Differences";
	public  static final String OFFSET = "Offset";
	public  static final String LENGTH = "Length";
	
	// logger of the class.
	private Logger log = Logger.getLogger("com.assignment.utils.Utils");

	/** 
	 * Transforms a byte array to String using printBase64Binary method from DatatypeConverter. 
	 * 
	 * @param bite byte array to be converted.
	 * @return String String in Base64 that represents the byte array.
	 * 
	 * */
	public String encodeBase64(byte[] bite){
		return DatatypeConverter.printBase64Binary(bite);
	}
	
	/** 
	 * Transforms a String in Base64 into a byte array.
	 * 
	 * @param str String in Base64 to be converted to byte array.
	 * @return byte[] byte array
	 * 
	 * */
	public byte[] decodeBase64(String str){
		return DatatypeConverter.parseBase64Binary(str);
	}

	/**
	 * Get data (right or level) from the file system, transforms to byte array, and returns it.
	 * 
	 * @param id The id of the operation. it also represents the file with the right/left data.
	 * @param data The value that is been searched (DataEnum.RIGHT or DataEnum.LEFT).
	 * @return The byte array of this data.
	 */
	public String getData(String id, DataEnum data) {
		try {
			Properties properties = new Properties();
			InputStream input = new FileInputStream(id);
			properties.load(input);
			input.close();
			return properties.getProperty(data.name());
		} catch (IOException exc) {
			log.debug("Exception loading data from file: " + exc.getMessage(), exc);
			return null;
		}		
	}

	/**
	 * Saves the properties file in the file system.
	 * 
	 * @param id The id of the request, which is also the filename that holds RIGHT/LEFT data.
	 * @param properties The Properties object that is used to access RIGHT/LEFT data.
	 * @return The Properties object that was saved.
	 */
	public Properties savePropertiesFile(String id, Properties properties) {
		try {
			OutputStream output = new FileOutputStream(id);
			properties.store(output, null);
			output.close();
			return properties;
		} catch (Exception exc) {
			log.debug("Exception saving properties file: " + exc.getMessage());
			return null;
		}
	}

	/**
	 * Loads the properties file from the file system.
	 * 
	 * @param id The id of the request, which is also the filename that holds RIGHT/LEFT data.
	 * @return The Properties object that is used to access the RIGHT/LEFT data.
	 */
	public Properties loadPropertiesFile(String id) {
		try {
			Properties properties = new Properties();
			InputStream input = new FileInputStream(id);
			properties.load(input);
			input.close();
			return properties;
		} catch (IOException exc) {
			log.debug("Exception loading properties file: " + exc.getMessage());
			return null;
		}
	}

	/**
	 * Creates the properties file in the file system with the RIGHT and LEFT properties.
	 * 
	 * @param id The id of the operation,  which is also the filename that holds RIGHT/LEFT data.
	 * @return The Properties object that is used to access the RIGHT/LEFT data. 
	 */
	public Properties createPropertiesFile(String id) {
		try {
			OutputStream output = new FileOutputStream(id);
			Properties properties = new Properties();
			properties.setProperty(DataEnum.RIGHT.name(), "");
			properties.setProperty(DataEnum.LEFT.name(), "");
			properties.store(output, null);
			output.close();
			return properties;
		} catch (Exception exc) {
			log.debug("Exception creating properties file: " + exc.getMessage());
			return null;
		}
	}	
	
	/**
	 * Checks if the filename, which name is the id value, exists in the file system.
	 * 
	 * @param id The name of the file
	 * @return True is the filename exists, otherwise false.
	 */
	public boolean fileExists(String id) {
		File file = new File(id);
		return file.exists();
	}
}
