package com.assignment.task;

import org.codehaus.jettison.json.JSONObject;

/**
 * Interface used as an abstraction of tasks One task represents a work unit and
 * is executed asynchronously (different thread) or synchronously (same thread).
 * 
 * @author lucas.reginato@gmail.com
 */
public interface Task {
	/**
	 * Contains the logic necessary to make the task run.
	 */
	JSONObject executeTask();

}
