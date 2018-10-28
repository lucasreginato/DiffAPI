package com.assignment.task.impl;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.assignment.task.impl.GetDiffTask;
import com.assignment.utils.DataEnum;
import com.assignment.utils.Utils;


/**
 * Unit Test class for GetDiffTask class.<br/>
 * Test all methods, positive and negative scenarios. <br/>
 * The unit tests validates that the class can return JSON
 * for 3 scenarios:<br/>
 * 1) when the data are equals;<br/>
 * 2) when the data are not equals in size;<br/>
 * 3) when the data have the same length but differences.
 *
 * @author lucas.reginato@gmail.com
 */
public class GetDiffTaskTest {

	// global variables of this unit test class
	private Utils mockUtils = null;
	private String id = null;
	private String left = null;
	private String right = null;

	/**
	 * Creates the instances before each test.
	 */
	@Before
	public void setup() {
		// mocking the Utils class
		mockUtils = Mockito.mock(Utils.class);
	}
	
	/**
	 * Test for when the data are equals
	 */
	@Test
	public void testDataAreEqualPositive() {
		id = "123";
		left  = "AAAAA-BBBBB-CCCCC=";
		right = "AAAAA-BBBBB-CCCCC=";

		// mocking Utils calls
		Mockito.when(mockUtils.getData(id, DataEnum.LEFT)).thenReturn(left);
		Mockito.when(mockUtils.getData(id, DataEnum.RIGHT)).thenReturn(right);

		// creating task and setting Utils mock class
		GetDiffTask task = new GetDiffTask(id);
		task.setUtils(mockUtils);
		
		// executing task
		JSONObject jsonObject = task.executeTask();
		
		// checking results from task
		try {
			String resultString = jsonObject.get(Utils.RESULT).toString();
			String actual = new JSONArray(resultString).get(0).toString();
			Assert.assertEquals(Utils.RESULT_ARE_EQUAL, actual);
		} catch (JSONException exc) {
			Assert.fail();
		}
	}

	/**
	 * Test for when the data does not have the same length.
	 */
	@Test
	public void testDataNotSameLengthPositive() {
		id = "456";
		left  = "AAAAA-BBBB-CCCCCCCCCCC=";
		right = "AAAAA-BBBBB-CCCCC=";

		// mocking Utils calls
		Mockito.when(mockUtils.getData(id, DataEnum.LEFT)).thenReturn(left);
		Mockito.when(mockUtils.getData(id, DataEnum.RIGHT)).thenReturn(right);

		// creating task and setting Utils mock class
		GetDiffTask task = new GetDiffTask(id);
		task.setUtils(mockUtils);
		
		// executing task
		JSONObject jsonObject = task.executeTask();
		
		// checking results from task
		try {
			String resultString = jsonObject.get(Utils.RESULT).toString();
			String actual = new JSONArray(resultString).get(0).toString();
			Assert.assertEquals(Utils.RESULT_NOT_SAME_LENGTH, actual);
		} catch (JSONException exc) {
			Assert.fail();
		}
	}
	
	/**
	 * Test for when the data have the same length but with diffs.
	 */
	@Test
	public void testDataSameLenghtButWithDiffsPositive() {
		id = "789";
		left  = "ABCDEFGH-IJKLMNOP-QRSTUVZ=";
		right = "AAADEFGH-IIIIIIIP-QRSTUUUU";
		
		int[] expectedOffsets = {1, 10, 23};
		int[] expectedLength = {2, 6, 3};
		
		// mocking Utils calls
		Mockito.when(mockUtils.getData(id, DataEnum.LEFT)).thenReturn(left);
		Mockito.when(mockUtils.getData(id, DataEnum.RIGHT)).thenReturn(right);

		// creating task and setting Utils mock class
		GetDiffTask task = new GetDiffTask(id);
		task.setUtils(mockUtils);

		// executing task
		JSONObject jsonObject = task.executeTask();
		
		try {
			// checking results from task
			String actual = jsonObject.get(Utils.RESULT).toString();
			Assert.assertEquals(Utils.RESULT_SAME_LENGTH_WITH_DIFFS, actual);

			// getting the differences (offsets and length information)
			JSONArray jsonArray = new JSONArray(jsonObject.getString(Utils.DIFFERENCES));
			for(int i=0; i<jsonArray.length(); i++) {
				JSONObject auxJsonObject = jsonArray.getJSONObject(i);
				
				int offset = auxJsonObject.getInt(Utils.OFFSET);
				Assert.assertEquals(expectedOffsets[i], offset);
				
				int length = auxJsonObject.getInt(Utils.LENGTH);
				Assert.assertEquals(expectedLength[i], length);
			}
		} catch (JSONException exc) {
			Assert.fail();
		}
	}
}
