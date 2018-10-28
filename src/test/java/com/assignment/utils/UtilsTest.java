package com.assignment.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.assignment.utils.Utils;


/**
 * Unit Test class of the Utils class.<br/>
 * Test all methods, positive and negative scenarios.
 * 
 * @author lucas.reginato@gmail.com
 */
public class UtilsTest {

	private String key = "name";
	private String value = "LucasRosaCruzReginato";
	
	private byte[] byteArrayOriginal = value.getBytes();
	private String base64String = new Utils().encodeBase64(byteArrayOriginal);

	/**
	 * Test an encode process over a known byte array.
	 */
	@Test
	public void encodeBase64Test() {
		Utils utils = new Utils();
		String result = utils.encodeBase64(byteArrayOriginal);
		Assert.assertEquals(base64String, result);
	}

	/**
	 * Test a decode process over a known String.
	 */
	@Test
	public void decodeBase64Test() {
		Utils utils = new Utils();
		byte[] result = utils.decodeBase64(base64String);
		Assert.assertTrue(Arrays.equals(byteArrayOriginal, result));
	}
	
	/**
	 * Test the ability to get data (right/left) from a file.
	 */
	@Test
	public void getDataInBinaryTest() {
		Utils utils = new Utils();
		try {
			// create the temp file
			String uuid = UUID.randomUUID().toString();
			Properties properties = utils.createPropertiesFile(uuid);
			
			// sets data on this file
			properties.setProperty(key, value);
			
			// save data from this file
			utils.savePropertiesFile(uuid, properties);
			
			// load new properties file and check loaded data
			Properties newProperties = utils.loadPropertiesFile(uuid);
			Assert.assertEquals(newProperties.getProperty(key), properties.getProperty(key));

			// delete the temp file
			File file = new File(uuid);
			if (!file.delete())
				Assert.fail();
		} catch (Exception exc) {
			Assert.fail();
		}
	}
}
