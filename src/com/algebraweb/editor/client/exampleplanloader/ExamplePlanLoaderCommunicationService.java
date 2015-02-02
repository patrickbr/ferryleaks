package com.algebraweb.editor.client.exampleplanloader;

import com.algebraweb.editor.shared.exceptions.RemoteIOException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service for loading an example plan from the server.
 * 
 * @author Patrick Brosi
 * 
 */
@RemoteServiceRelativePath("example")
public interface ExamplePlanLoaderCommunicationService extends RemoteService {
	/**
	 * Load an example plan.
	 * 
	 * @param fileName
	 *            the file name on the server
	 * @return an integer array holding all plan ids.
	 * @throws RemoteIOException
	 */
	public Integer[] loadExamplePlan(String fileName) throws RemoteIOException;
}