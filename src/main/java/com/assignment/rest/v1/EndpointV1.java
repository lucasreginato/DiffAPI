package com.assignment.rest.v1;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.assignment.task.Task;
import com.assignment.task.TaskFactory;
import com.assignment.task.impl.TaskFactoryImpl;
import com.assignment.utils.DataEnum;
import com.assignment.utils.JSONUtils;
import com.assignment.utils.Utils;

/**
 * Represents the REST API V1.<br/>
 * This API has 3 endpoints (right, left and diff).<br/>
 * The right and left endpoints accepts a JSON base64 encoded binary data.
 * The diff endpoint returns the following information:<br/>
 *  A) If data are equals return that they are equal.<br/>
 *  B) If data are not equal in size return that they are not equal in size.<br/>
 *  C) If data has the same size, but has differences, the endpoint provide 
 *  insight in where the differences are (offsets + length in the data).<br/>
 *  
 * @author lucas.reginato@gmail.com
 */
@Path("/v1/diff/")
public class EndpointV1 {

	// logger of the class.
	private Logger log = Logger.getLogger("com.assignment.rest.v1.EndpointV1");
	
	/**
	 * The left endpoint, which is a HTTP POST.<br/>
	 * It accepts a JSON base64 encoded binary data.<br/>
	 * This value will be used later in the diff endpoint.<br/>
	 * <br/>
	 * @param id The id of the request. This id will be used to save a file on the disk and keep the "left" data.
	 * @param inputData JSON base64 encoded binary data.
	 * @return A HTTP response: 500 in case of failure, or 204 in case of success.
	 */
	@Path("/{id}/left")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response left(@PathParam("id") String id, String inputData) {
		log.info("EndpointV1.left is executing, id is (" + id + ") and inputData is (" + inputData + ")");

		try {
			// if the path param ID is null or empty then return an error
			// if inputData is not a valid JSON array then return an error
			if (!validInputs(id, inputData)) {
				return Response.status(Utils.INTERNAL_SERVER_ERROR).build();
			}
			
			// the input data is a JSON array, it can contains chunks 
			// of Base64 encoded binary data.
			// encapsulates all data into a StringBuilder.
			StringBuilder stringBuilder = encapsulateInputData(inputData);
			if (stringBuilder == null) {
				return Response.status(Utils.INTERNAL_SERVER_ERROR).build();
			}
			
			// saves the data in a file which name is the {ID} value.
			// today this is a synchronous job, but it should be asynchronous
			// and the data should be saved in a database.
			TaskFactory taskFactory = new TaskFactoryImpl();
			Task task = taskFactory.createSaveDataTask(id, DataEnum.LEFT.name(), stringBuilder.toString());
			task.executeTask();
		} 
		catch (Exception exc) {
			log.debug("Exception during EndpointV1.left execution: " + exc.getMessage());
			return Response.status(Utils.INTERNAL_SERVER_ERROR).build();
		}
		log.info("EndpointV1.left execution ended with success.");
		return Response.status(Utils.OK_WITH_NO_CONTENT).build();
	}
	
	/**
	 * The right endpoint, which is a HTTP POST.<br/>
	 * It accepts a JSON base64 encoded binary data.<br/>
	 * This value will be used later in the diff endpoint.<br/>
	 * <br/>
	 * @param id The id of the request. This id will be used to save a file on the disk and keep the "right" data.
	 * @param inputData JSON base64 encoded binary data.
	 * @return A HTTP response: 500 in case of failure, or 204 in case of success.
	 */
	@Path("/{id}/right")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response right(@PathParam("id") String id, String inputData) {
		log.info("EndpointV1.right is executing, id is (" + id + ") and inputData is (" + inputData + ")");
		
		try {
			// if the path param ID is null or empty then return an error
			// if inputData is not a valid JSON array then return an error
			if (!validInputs(id, inputData)) {
				return Response.status(Utils.INTERNAL_SERVER_ERROR).build();
			}
			
			// the input data is a JSON array, it can contains chunks 
			// of Base64 encoded binary data.
			// encapsulates all data into a StringBuilder.
			StringBuilder stringBuilder = encapsulateInputData(inputData);
			if (stringBuilder == null) {
				return Response.status(Utils.INTERNAL_SERVER_ERROR).build();
			}
			
			// saves the data in a file which name is the {ID} value
			// today this is a synchronous job, but it should be asynchronous
			// and the data should be saved in a database.
			TaskFactory taskFactory = new TaskFactoryImpl();
			Task task = taskFactory.createSaveDataTask(id, DataEnum.RIGHT.name(), stringBuilder.toString());
			task.executeTask();
		} 
		catch (Exception exc) {
			log.debug("Exception during EndpointV1.right execution: " + exc.getMessage());
			return Response.status(Utils.INTERNAL_SERVER_ERROR).build();
		}
		log.info("EndpointV1.right execution ended with success.");
		return Response.status(Utils.OK_WITH_NO_CONTENT).build();
	}

	/**
	 * The diff endpoint, which is a HTTP GET.<br/>
	 * <br/>
	 * It executes a diff over the rigth and left data and returns a JSON result to the user.<br/>
 	 * The diff endpoint returns the following information:<br/>
	 *  A) If data are equals return that they are equal:<br/>
	 *  { "Result":[ "The right and left data are equals."] }<br/>
	 *  <br/>
	 *  B) If data are not equal in size return that they are not equal in size:<br/>
	 *  { "Result":[ "The right and left data does not have the same length." ] }<br/>
	 *  <br/>
	 *  C) If data has the same size, but has differences, the endpoint provide 
	 *  insight in where the differences are (offsets + length in the data):<br/>
	 *  { "Result":"The right and left data have the same length, but differences were found.",
	 *	"Differences":[ {
     *    "Offset":0,
     *    "Length":4 },
     *  {
     *    "Offset":21,
     *    "Length":5 }]
     *  }<br/>
 	 *  
	 * @param id String that represents the ID of the request.
	 * @return A HTTP response: 500 in case of failure, or 200 in case of success with a JSON result.
	 */
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response diff(@PathParam("id") String id) {
		log.info("EndpointV1.diff is executing, id is (" + id + ").");
		try {
			// validating if id is not null or empty.
			if((id == null) || (id.isEmpty())) {
				log.info("Id is null or empty, returning internal error.");
				return Response.status(Utils.INTERNAL_SERVER_ERROR).build();
			}

			// generates the diff between the 2 data (right and left).
			// this is a synchronous job
			TaskFactory taskFactory = new TaskFactoryImpl();
			Task task = taskFactory.createGetDiffTask(id);
			JSONObject jsonObject = task.executeTask();
			if (jsonObject == null) {
				log.info("Error while executing diff algorithm, returning internal error.");
				return Response.status(Utils.INTERNAL_SERVER_ERROR).build();
			}
			
			log.info("Result is: " + jsonObject.toString());
			return Response.ok().entity(jsonObject.toString()).build();
		} 
		catch(Exception exc) {
			log.debug("Exception: " + exc.getMessage() + " returning internal error...", exc);
			return Response.status(Utils.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	/**
	 * Validate the inputs of the endpoints right and left.<br/>
	 * The id input is an string and cannot be null or empty.<br/>
	 * The inputData input is a JSONArray and cannot be null, empty or valid JSON.<br/>
	 * 
	 * @param id The id of the request.
	 * @param inputData The JSON string that contains base64 encoded binary data.
	 * @return True in case the inputs are valid, otherwise false.
	 */
	public boolean validInputs(String id, String inputData) {
		
		// validates if the path param ID is null or empty then return an error
		// path param ID is key for the execution of this assignment
		if((id == null) || (id.isEmpty())) {
			log.info("Invalid id, returning false.");
			return false;
		}
		
		// validates if inputData is NOT a JSON string, then return an error
		JSONUtils jsonUtils = new JSONUtils();
		if (!jsonUtils.isValidJSONArray(inputData)) {
			log.info("Invalid JSON, returning false.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Encapsulate the InputData into a single String.<br/>
	 * The nputData can be a JSONArray object, which has different values in the array.<br/>
	 * All the values are concatenated into a unique string.<br/>
	 * 
	 * @param inputData The JSON string that contains base64 encoded binary data. 
	 * @return StringBuilder Which contains all the Strings inside the JSONArray (inputdata).
	 */
	public StringBuilder encapsulateInputData(String inputData) {
		log.info("Parsing inputData (" + inputData + ") into JSONArray object");
		
		try {
			StringBuilder stringBuilder = new StringBuilder();
			JSONArray jsonArray = new JSONArray(inputData);
		
			for(int index = 0; index < jsonArray.length(); index++) {
				// saves the JSON into a new StringBuilder for later processing.
				stringBuilder.append(jsonArray.get(index));
			}

			log.info("Returning String (" + stringBuilder.toString()+ ").");	
			return stringBuilder;
		} catch (JSONException jsonExc) {
			log.debug("Exception encapsulating data: " + jsonExc.getMessage(), jsonExc);
			return null;
		}
	}
}
