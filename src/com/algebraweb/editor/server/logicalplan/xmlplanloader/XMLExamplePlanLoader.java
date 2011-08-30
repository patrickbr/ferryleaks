package com.algebraweb.editor.server.logicalplan.xmlplanloader;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.xml.sax.SAXException;

import com.algebraweb.editor.client.exampleplanloader.ExamplePlanLoaderCommunicationService;
import com.algebraweb.editor.shared.exceptions.RemoteIOException;
import com.algebraweb.editor.shared.logicalplan.QueryPlanBundle;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * See client.
 * 
 * @author Patrick Brosi
 *
 */
public class XMLExamplePlanLoader extends RemoteServiceServlet implements
		ExamplePlanLoaderCommunicationService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Integer[] loadExamplePlan(String fileName) throws RemoteIOException {

		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		XMLPlanLoader planLoader = new XMLPlanLoader();

		QueryPlanBundle sessionBundle;
		try {
			sessionBundle = planLoader.parsePlans(getServletContext()
					.getRealPath(fileName), this.getServletContext(), request
					.getSession());
		} catch (IOException e) {
			throw new RemoteIOException(e.getMessage());
		} catch (SAXException e) {
			throw new RemoteIOException(e.getMessage());
		}
		session.setAttribute("queryPlans", sessionBundle);
		
		return sessionBundle.getPlans().keySet().toArray(new Integer[0]);
	}
}
