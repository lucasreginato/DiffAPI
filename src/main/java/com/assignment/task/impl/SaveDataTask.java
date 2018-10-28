package com.assignment.task.impl;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import com.assignment.task.Task;
import com.assignment.utils.Utils;

/**
 * Represents the task that saves the data in the file system.<br/>
 * It saves the "right" and also the "left" data on the file system in a filename 
 * named with the value of the id variable.<br/>
 * 
 * @author lucas.reginato@gmail.com
 */
public class SaveDataTask implements Task {

	// internal variables (used in constructor).
	private String id;
	private String key;
	private String value;
	
	// logger of the class.
	private Logger log = Logger.getLogger("com.assignment.task.impl.SaveDataTask");
	
	// other variables
	private Properties properties = null;
	private Utils utils = new Utils();

	/**
	 * Constructor of the SaveDiskTask class.
	 * 
	 * @param id The id of the request. It is also used as a filename to hold the right/left data.
	 * @param key The name of the key, which can be RIGHT or LEFT (from DataEnum enum).
	 * @param value The value to be saved, a JSON base64 encoded binary data.
	 */
	public SaveDataTask(String id, String key, String value) {
		this.id = id;
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Saves the data in the file system.<br/>
	 * This class is executed in a synchronized way (no threads).<br/>
	 * 
	 * @return JSONObject JSONObject with the result of the save data operation.
	 */
	@Override
	public JSONObject executeTask() {
		try {
			// check if file exists, if does not, create one
			if (!utils.fileExists(id)) {
				log.info("Creating file (" + id + ") at " + new File(".").getAbsolutePath());
				properties = utils.createPropertiesFile(id);
			} else {
				// if it already exists, loads properties file from disk 
				log.info("Loading file (" + id + ") at " + new File(".").getAbsolutePath());
				properties = utils.loadPropertiesFile(id);
			}
			
			// set the new property value
			log.info("Setting property (" + key + " = " + value + ") to property file (" + id + ") at "
					+ new File(".").getAbsolutePath());
			properties.setProperty(key, value);

			// save properties file to root folder
			properties = utils.savePropertiesFile(id, properties);

			// return a result to the user, it could have an UUID that represents the operation.
			// today the result is an empty JSONObejct(), because it will not be checked/returned to the user.
			return new JSONObject();
		} catch (Exception exc) {
			log.debug("Exception in the execution of SaveDataTask: " + exc.getMessage(), exc);
			return null;
		}
	}

	/**
	 * Set the Utils instance to be used in this class.
	 * 
	 * @param utils An instance of the Utils class.
	 */
	public void setUtils(Utils utils) {
		this.utils = utils;	
	}
}
