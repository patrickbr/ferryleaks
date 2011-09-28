package com.algebraweb.editor.server.postinterface;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.algebraweb.editor.server.logicalplan.xmlplanloader.XMLPlanLoader;
import com.algebraweb.editor.shared.exceptions.RemoteConfigurationException;
import com.algebraweb.editor.shared.exceptions.RemoteIOException;
import com.algebraweb.editor.shared.logicalplan.QueryPlanBundle;

public class PostInterfaceServlet extends HttpServlet  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4298624527659355950L;

	@Override
	public void  doPost (HttpServletRequest request, HttpServletResponse res) throws ServletException, IOException {

		String xmlplan = request.getParameter("xmlplan");

		if (getServletContext().getAttribute("configuration") == null) {
			Configuration c;

			try {
				c = new PropertiesConfiguration("algebraeditor.properties");
				getServletContext().setAttribute("configuration", c);
			} catch (ConfigurationException e) {
				res.getWriter().print("Error.");
			}
		
		}

		System.out.println("received xml plan via post: " + xmlplan);

		HttpSession session = request.getSession();
		XMLPlanLoader planLoader = new XMLPlanLoader();

		InputStream s = new ByteArrayInputStream(xmlplan.getBytes());

		QueryPlanBundle sessionBundle;
		try {
			sessionBundle = planLoader.parsePlans(s, this.getServletContext(),	request.getSession());
			session.setAttribute("queryPlans", sessionBundle);
			session.setAttribute("loadedFromPost", true);
			res.sendRedirect(request.getContextPath());
		} catch (SAXParseException e) {
			res.getWriter().print("Error while parsing XML: " + e.getMessage() + "(Line " + e.getLineNumber() +", column " + e.getColumnNumber() + ")");
		} catch (RemoteIOException e) {
			res.getWriter().print("Error while parsing plan: " + e.getMessage());
		} catch (SAXException e) {
			res.getWriter().print("Error while parsing XML: " + e.getMessage());
		}


	}

}
