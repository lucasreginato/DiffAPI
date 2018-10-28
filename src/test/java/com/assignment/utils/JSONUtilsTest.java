package com.assignment.utils;

import org.junit.Assert;
import org.junit.Test;

import com.assignment.utils.JSONUtils;


/**
 * Unit Test class of the JSONUtils class.<br/>
 * Test all methods, positive and negative scenarios.<br/>
 * 
 * @author lucas.reginato@gmail.com
 */
public class JSONUtilsTest {

	/**
	 * Test if the inputData string is a valid JSON object
	 */
	@Test
	public void isValidJSONObjectTestPositive() {
		String inputData = " { "
				+ "\"name\" : \"Lucas Rosa Cruz Reginato\" , "
				+ "\"age\" : 35 ,  "
					+ "\"address\" : {"
							+ "\"street\" : \"Carlos S. M. Pacheco\" , "
							+ "\"city\" : \"Porto Alegre\""
					+ "}"
				+ "} ";
		
		JSONUtils jsonUtils = new JSONUtils();
		boolean result = jsonUtils.isValidJSONObject(inputData);
		Assert.assertTrue(result);
	}

	/**
	 * Test if the inputData string is NOT a valid JSON object.<br/>
	 * In the end of the key "address" there is a missing curly braces, which is
	 * a JSON parsing error.
	 */
	@Test
	public void isValidJSONObjectTestNegative() {
		String inputData = " { "
				+ "\"name\" : \"Lucas Rosa Cruz Reginato\" , "
				+ "\"age\" : 35 , "
					+ "\"address\" : "
							+ "\"street\" : \"Carlos S. M. Pacheco\" , "
							+ "\"city\" : \"Porto Alegre\""
					+ "}"
				+ "} ";
		
		JSONUtils jsonUtils = new JSONUtils();
		boolean result = jsonUtils.isValidJSONObject(inputData);
		Assert.assertFalse(result);
	}
	
	/**
	 * Test if the inputData string is a valid JSON array
	 */
	@Test
	public void isValidJSONArrayTestPositive() {
		String inputData = " [ "
				+ "\"Lucas Rosa Cruz Reginato\" , "
				+ "\"Porto Alegre - RS - Brazil\" , "
				+ "\"Java, Rest, SOA, customer oriented and passioaned for difficult problems\""
				+ "]";
		JSONUtils jsonUtils = new JSONUtils();
		boolean result = jsonUtils.isValidJSONArray(inputData);
		Assert.assertTrue(result);
	}

	/**
	 * Test if the inputData string is NOT a valid JSON array.<br/>
	 * In between the values there is no comma separating the values, which is a JSON
	 * parsing error.
	 */
	@Test
	public void isValidJSONArrayTestNegative() {
		String inputData = " [ "
				+ "\"Lucas Rosa Cruz Reginato\""
				+ "\"Porto Alegre - RS - Brazil\""
				+ "\"Java, Rest, SOA, customer oriented and passioaned for difficult problems\""
				+ "]";
		
		JSONUtils jsonUtils = new JSONUtils();
		boolean result = jsonUtils.isValidJSONArray(inputData);
		Assert.assertFalse(result);
	}
}
