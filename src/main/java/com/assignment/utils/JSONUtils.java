package com.assignment.utils;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Utility class that works with JSON objects.
 * 
 * @author lucas.reginato@gmail.com
 */
public class JSONUtils {
	
	/**
	 * Validates if the input parameter (inputData) is a valid JSONObject. <br/>
	 * The JSON string is wrapped in curly braces, and presents colons between the
	 * names and values, and commas between the values and names.<br/>
	 * 
	 * @param inputData String in JSON format that will be validated as a valid JSON.
	 * @return true if a valid JSON object, otherwise false.
	 */
	@SuppressWarnings("unused")
	public boolean isValidJSONObject(String inputData) {
		try {
			JSONObject jsonObejct = new JSONObject(inputData);
		} catch (JSONException jsonExc) {
			return false;
		}
		return true;
	}
	
	/**
	 * Validates if the input parameter (inputData) is a valid JSONArray.<br/>
	 * The JSON array string is wrapped in square braces, and presents commas between the values.<br/>
	 * 
	 * @param inputData String in JSON format that will be validated as a valid JSON Array.
	 * @return true if a valid JSON array, otherwise false.
	 */
	@SuppressWarnings("unused")
	public boolean isValidJSONArray(String inputData) {
		try {
			if (inputData == null)
				return false;
			
			JSONArray jsonArray = new JSONArray(inputData);
		} catch (JSONException jsonExc) {
			return false;
		}
		return true;
	}
}
