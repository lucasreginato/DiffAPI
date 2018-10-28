package com.assignment.task.impl;

import java.util.Properties;

import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.assignment.task.impl.SaveDataTask;
import com.assignment.utils.DataEnum;
import com.assignment.utils.Utils;

/**
 * Unit Test class for SaveDataTask class.<br/>
 * Test all methods, positive and negative scenarios.<br/>
 * The unit tests validates that the task is able to:<br/>
 * 1) create new properties files;<br/>
 * 2) load existing properties files;<br/>
 * 3) save changes in a property file.
 * 
 * @author lucas.reginato@gmail.com
 */
public class SaveDataTaskTest {

	// global variables of this unit test
	private Properties properties = null;
	private Utils mockUtils = null;
	
	/**
	 * Creates the instances before each test.
	 */
	@Before
	public void setup() {
		// the properties instance that will hold the right attribute
		properties = new Properties();
		// mocking the Utils class
		mockUtils = Mockito.mock(Utils.class);
	}
	
	/**
	 * Test the creation of a new properties file and 
	 * adding the new property (key:value) inside it.
	 * The expected result is a JSONObject.
	 */
	@Test
	public void testCreatingNewPropertiesFilePositive() {
		// define variables to be used in the test:
		//  - id is the name of the filename that should be created.
		//  - key is the attribute name where we will save the value.
		//  - value is the base64 string that will be saved.
		String id = "123";
		String key = DataEnum.RIGHT.name();
		String value = "ABCDE"; 
		
		// mocking Utils calls
		Mockito.when(mockUtils.fileExists(id)).thenReturn(false);
		Mockito.when(mockUtils.createPropertiesFile(id)).thenReturn(properties);
		
		// creating SaveDataTask with initial parameters
		// and seeting the mocked Utils to the SaveDataTask instance
		SaveDataTask task = new SaveDataTask(id, key, value);
		task.setUtils(mockUtils);
		
		JSONObject jsonObject = task.executeTask();
		Assert.assertNotNull(jsonObject);
	}

	/**
	 * Test the loading of an existing properties file and 
	 * adding the new property (key:value) inside it.
	 * The expected result is a JSONObject.
	*/
	@Test
	public void testLoadingExistingPropertiesFilePositive() {
		// define variables to be used in the test:
		//  - id is the name of the filename that should be created.
		//  - key is the attribute name where we will save the value.
		//  - value is the base64 string that will be saved.
		String id = "321";
		String key = DataEnum.RIGHT.name();
		String value = "EDCBA"; 
		
		// mocking Utils calls
		Mockito.when(mockUtils.fileExists(id)).thenReturn(true);
		Mockito.when(mockUtils.loadPropertiesFile(id)).thenReturn(properties);
		
		// creating SaveDataTask with initial parameters
		// and seeting the mocked Utils to the SaveDataTask instance
		SaveDataTask task = new SaveDataTask(id, key, value);
		task.setUtils(mockUtils);
		
		JSONObject jsonObject = task.executeTask();
		Assert.assertNotNull(jsonObject);
	}
	
	/**
	 * Test the failure on creating a new proeprties file.
	 * THe expected result is null.
	 */
	@Test
	public void testExceptionDuringTaskExecutionNegative() {
		// define variables to be used in the test:
		//  - id is the name of the filename that should be created.
		//  - key is the attribute name where we will save the value.
		//  - value is the base64 string that will be saved.
		String id = "111";
		String key = DataEnum.RIGHT.name();
		String value = "AAAAA"; 
		
		// mocking Utils calls
		Mockito.when(mockUtils.fileExists(id)).thenReturn(false);
		Mockito.when(mockUtils.createPropertiesFile(id)).thenReturn(null);
		
		// creating SaveDataTask with initial parameters
		// and seeting the mocked Utils to the SaveDataTask instance
		SaveDataTask task = new SaveDataTask(id, key, value);
		task.setUtils(mockUtils);
		
		JSONObject jsonObject = task.executeTask();
		Assert.assertNull(jsonObject);
	}
}
