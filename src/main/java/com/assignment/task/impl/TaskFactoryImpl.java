package com.assignment.task.impl;

import com.assignment.task.Task;
import com.assignment.task.TaskFactory;

/**
 * Concrete class that implements the factory of tasks.
 * 
 * @author lucas.reginato@gmail.com
 *
 */
public class TaskFactoryImpl implements TaskFactory {

	@Override
	public Task createSaveDataTask(String id, String key, String value) {
        SaveDataTask task = new SaveDataTask(id, key, value);
        return task;
	}

	@Override
	public Task createGetDiffTask(String id) {
            GetDiffTask task = new GetDiffTask(id);
            return task;
	}
}
