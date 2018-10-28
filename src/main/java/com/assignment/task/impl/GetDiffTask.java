package com.assignment.task.impl;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.assignment.task.Task;
import com.assignment.utils.DataEnum;
import com.assignment.utils.Utils;

/**
 * Represents the task that executes the diff between the two data values (right and left).<br/>
 * This task does a diff over the String value, not over the byte[] values.<br/>
 * But in order to achieve that, a small change needs to be done at the code level.<br/> 
 * 
 * @author lucas.reginato@gmail.com
 */
public class GetDiffTask implements Task {

	// the id of the operation
	private String id;
	// other internal variables
	private Utils utils = new Utils();
	
	// logger of the class.
	private Logger log = Logger.getLogger("com.assignment.task.impl.GetDiffTask");

	/**
	 * Constructor of the GetDiffTask class.
	 * 
	 * @param id The id of the operation.
	 */
	public GetDiffTask(String id) {
		this.id = id;
	}
	
	/**
	 * Performs the diff operation over the right and left data.
	 * Return a JSON object with the the result of the operation.
	 * 
	 * @return JSONObject JSONObject with the result of the diff operation.
	 */
	@Override
	public JSONObject executeTask() {
		try {
			log.info("Loading data (right and left) from file system...");
			String right = utils.getData(id, DataEnum.RIGHT);
			String left = utils.getData(id, DataEnum.LEFT);
			
			// if data have the same length, they could be equal
			if (right.length() == left.length()) {
				// if equals, return that
				if (right.equals(left)) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.append(Utils.RESULT, Utils.RESULT_ARE_EQUAL);
					return jsonObject;
				} else {
					// Not equal, but the same size, so need to look for the differences.
					// Identify the differences between the 2 base64 encoded binary strings.
					// It assumes that there are no differences in the start of the execution.
					// Once a difference is recognized, a JSON object (with the offset of the difference) is added to the JSON array. 
					// Once the difference ends, the object is updated one last time (with the length of the difference).
					// In the end, all differences will be in the JSONArray, ready to be presented to the user.
					JSONObject resultJsonObject = new JSONObject();
					JSONArray jsonArrayDiff = new JSONArray();
					boolean diffRecognized = false;
					int diffSize = 0;
					int numberOfDiffs = 0;
					
					for (int i=0; i<right.length(); i++) {
						if ((right.charAt(i) == left.charAt(i))) {
							if (diffRecognized) {
								// mark the difference
								JSONObject jsonObject = (JSONObject)jsonArrayDiff.get(numberOfDiffs);
								jsonObject.put(Utils.LENGTH, diffSize);
								diffRecognized = false;
								diffSize = 0;	
								numberOfDiffs++;
							}
						} else {
							if (!diffRecognized) {
								// mark the offset, just the first time that the difference is recognized.
								jsonArrayDiff.put(numberOfDiffs, getJSON(Utils.OFFSET, i));
								diffRecognized = true;
							}
							diffSize++;
						}
					}
					if (diffRecognized) {
						// mark the difference (as a last step)
						JSONObject jsonObject = (JSONObject)jsonArrayDiff.get(numberOfDiffs);
						jsonObject.put(Utils.LENGTH, diffSize);
					}
					// add the last difference to the JSONArray
					resultJsonObject.put(Utils.RESULT, Utils.RESULT_SAME_LENGTH_WITH_DIFFS);
					resultJsonObject.put(Utils.DIFFERENCES, jsonArrayDiff);
					return resultJsonObject;
				}
			} else {
				// if data does not have the same length, just state that.
				JSONObject jsonObject = new JSONObject();
				jsonObject.append(Utils.RESULT, Utils.RESULT_NOT_SAME_LENGTH);
				return jsonObject;
			}
		} catch (JSONException jsonExc) {
			log.debug("Exception while getting the diff: " + jsonExc.getMessage(), jsonExc);
			// return null in case of failure.
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
	
	/**
	 * Returns JSON object with the key and value already populated.
	 * 
	 * @param key The key of the JSONObject
	 * @param value The value of the JSONObject.
	 * @return The JSONObject object with the key and value.
	 */
	private JSONObject getJSON(String key, int value) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(key, value);
			return jsonObject;
		} catch (JSONException jsonExc) {}
		return null;
	}

}
