package com.assignment.task;

/**
 * Interface used to define the factory that creates new tasks.
 * 
 * @author lucas.reginato@gmail.com
 */
public interface TaskFactory {
	/**
	 * Creates the task that saves the data in the file system.
	 * @return
	 */
	Task createSaveDataTask(String id, String key, String value);
	
	/**
	 * Creates the task that executes the diff and return to the user.
	 * @return
	 */
	Task createGetDiffTask(String id);
}
