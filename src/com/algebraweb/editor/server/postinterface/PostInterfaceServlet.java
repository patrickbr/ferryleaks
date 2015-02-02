package com.algebraweb.editor.server.postinterface;

import java.util.UUID;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.algebraweb.editor.server.logicalplan.xmlplanloader.XMLPlanLoader;
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
		String redirect = request.getParameter("redirect");
		String log = request.getParameter("log");

		if (getServletContext().getAttribute("configuration") == null) {
			Configuration c;

			try {
				c = new PropertiesConfiguration("algebraeditor.properties");
				getServletContext().setAttribute("configuration", c);
			} catch (ConfigurationException e) {
				res.getWriter().print("Error.");
			}

		}

		XMLPlanLoader planLoader = new XMLPlanLoader();

		InputStream s = new ByteArrayInputStream(xmlplan.getBytes());

		QueryPlanBundle bundle;
		UUID id;
		try {
			bundle = planLoader.parsePlans(s, this.getServletContext(),	request.getSession());

			id = UUID.randomUUID();
			this.getServletContext().setAttribute("postplan" + id.toString(), bundle);

			if (redirect != null &&redirect.equals("false")) {
				if (log != null && log.equals("true")) {
					res.getWriter().print("0::" + request.getContextPath() + "/?autoload=" +id.toString() +"&logger");
				}else{
					res.getWriter().print("0::" + request.getContextPath() + "/?autoload=" +id.toString());	
				}
			}else{
				res.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
				if (log != null && log.equals("true")) {
					res.setHeader("Location", request.getContextPath() + "/?autoload=" +id.toString() +"&logger");
				}else{
					res.setHeader("Location", request.getContextPath() + "/?autoload=" +id.toString());	
				}
			}

		} catch (SAXParseException e) {
			res.getWriter().print("1::Error while parsing XML: " + e.getMessage() + "(Line " + e.getLineNumber() +", column " + e.getColumnNumber() + ")");
		} catch (RemoteIOException e) {
			res.getWriter().print("1::Error while parsing plan: " + e.getMessage());
		} catch (SAXException e) {
			res.getWriter().print("1::Error while parsing XML: " + e.getMessage());
		}
	}
}