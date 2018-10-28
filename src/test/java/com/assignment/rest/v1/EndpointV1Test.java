package com.assignment.rest.v1;

import java.io.IOException;
import java.net.ServerSocket;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import com.assignment.rest.v1.EndpointV1;

/**
 * Unit Test class for EndpointV1 class. <br/>
 * It contains JUnit and JerseyTests to test all the methods, positive and
 * negative scenarios from the EdnpointV1 class.<br/>
 * 
 * @author lucas.reginato@gmail.com
 */
public class EndpointV1Test extends JerseyTest {

	/***
	 * Configure the Application instance to run JerseyTest tests
	 */
    @Override
    protected Application configure() {
        return new ResourceConfig(EndpointV1.class);
    }
	
    /**
     * Executes POST commands and then a GET command which generates the equals result.
     * @throws Exception Thrown to the upper layers
     */
	@Test
	public void executeHttpCommandsAndResultIsEqual() throws Exception {
		Response responseLeft = target("v1/diff/1/right").request().buildPost(Entity.json("[\"LucasRosaCruzReginato\"]")).invoke();
		Assert.assertEquals(responseLeft.getStatus(), 204);
		
		Response responseRight = target("v1/diff/1/right").request().buildPost(Entity.json("[\"LucasRosaCruzReginato\"]")).invoke();
		Assert.assertEquals(responseRight.getStatus(), 204);
		
		Response responseDiff = target("v1/diff/1").request().buildGet().invoke();
		Assert.assertEquals(responseDiff.getStatus(), 200);
	}

	/**
     * Executes POST commands and then a GET command which generates the equals with diffs result.
     * @throws Exception Thrown to the upper layers
	 */
	@Test
	public void executeHttpCommandsAndResultIsSameWithDiffs() throws Exception {
		Response responseLeft = target("v1/diff/1/right").request().buildPost(Entity.json("[\"AAAA-BBBB-CCCC-DDDD\"]")).invoke();
		Assert.assertEquals(responseLeft.getStatus(), 204);
		
		Response responseRight = target("v1/diff/1/right").request().buildPost(Entity.json("[\"BBBB-CCCC-DDDD-EEEE\"]")).invoke();
		Assert.assertEquals(responseRight.getStatus(), 204);
		
		Response responseDiff = target("v1/diff/1").request().buildGet().invoke();
		Assert.assertEquals(responseDiff.getStatus(), 200);
	}
	
	/**
     * Executes POST commands and then a GET command which generates the not same length result.
     * @throws Exception Thrown to the upper layers
	 */
	@Test
	public void executeHttpCommandsAndResultIsNotSameLength() throws Exception {
		Response responseLeft = target("v1/diff/1/right").request().buildPost(Entity.json("[\"0123456789\"]")).invoke();
		Assert.assertEquals(responseLeft.getStatus(), 204);
		
		Response responseRight = target("v1/diff/1/right").request().buildPost(Entity.json("[\"012345\"]")).invoke();
		Assert.assertEquals(responseRight.getStatus(), 204);
		
		Response responseDiff = target("v1/diff/1").request().buildGet().invoke();
		Assert.assertEquals(responseDiff.getStatus(), 200);
	}
	
	/**
	 * Test if the input parameters (id and inputData) are valid, which means id
	 * is not null and is not empty, and that JSON is a valid JSON array.
	 */
	@Test
	public void validInputTestPositive() {
		String id = "123";
		String inputData = " [ " + "\"Lucas Rosa Cruz Reginato\" , " + "\"Porto Alegre - RS - Brazil\" , "
				+ "\"Java, Rest, SOA, customer oriented and passioaned for difficult problems\"" + "]";

		EndpointV1 v1 = new EndpointV1();
		boolean result = v1.validInputs(id, inputData);
		Assert.assertTrue(result);
	}

	/**
	 * Test if the input parameters (id and inputData) are NO valid. in this
	 * test the id is NOT valid (null or empty) but the JSON array is valid.
	 */
	@Test
	public void validInputTestNegativeForId() {
		// the input parameter id is empty and JSON Array is valid
		String id = "";
		String inputData = " [ " + "\"Lucas Rosa Cruz Reginato\" , " + "\"Porto Alegre - RS - Brazil\" , "
				+ "\"Java, Rest, SOA, customer oriented and passioaned for difficult problems\"" + "]";

		EndpointV1 v1 = new EndpointV1();
		boolean result = v1.validInputs(id, inputData);
		Assert.assertFalse(result);

		// the input parameter id is null and JSON Array is valid
		id = null;
		result = v1.validInputs(id, inputData);
		Assert.assertFalse(result);
	}

	/**
	 * Test if the input parameters (id and inputData) are NO valid. in this
	 * test the id is valid but the JSON array is NOT valid.
	 */
	@Test
	public void validInputTestNegativeForJSON() {
		// the input parameter id is valid, but the JSON Array is NOT valid
		String id = "123";
		String inputData = " [ " + "\"Lucas Rosa Cruz Reginato\"" + "\"Porto Alegre - RS - Brazil\""
				+ "\"Java, Rest, SOA, customer oriented and passioaned for difficult problems\"" + "]";

		EndpointV1 v1 = new EndpointV1();
		boolean result = v1.validInputs(id, inputData);
		Assert.assertFalse(result);

		// the input parameter id is valid and JSON Array is null
		inputData = null;
		result = v1.validInputs(id, inputData);
		Assert.assertFalse(result);
	}

	/**
	 * Finds the next free port number by creating a ServerSocket with port 0.
	 * This results in a port to be automaticallt allocated by the OS. This port
	 * will then be used by the JAX RS server.
	 *
	 * @return The next free port number from the OS.
	 * @throws IOException
	 */
	public static int getNextFreePort() throws IOException {
		ServerSocket serverSocket = new ServerSocket(0);
		int port = serverSocket.getLocalPort();
		// close the socket before returning the port.
		serverSocket.close();
		return port;
	}
}
