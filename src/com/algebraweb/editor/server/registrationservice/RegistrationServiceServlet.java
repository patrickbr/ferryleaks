package com.algebraweb.editor.server.registrationservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.algebraweb.editor.client.RemoteConfiguration;
import com.algebraweb.editor.client.RemoteConfigurationWithPlansInSession;
import com.algebraweb.editor.client.RegistrationService;
import com.algebraweb.editor.client.logicalcanvas.RemoteConfigurationException;
import com.algebraweb.editor.server.logicalplan.QueryPlanBundle;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RegistrationServiceServlet extends RemoteServiceServlet implements RegistrationService{

	/**
	 * 
	 */
	
	public RegistrationServiceServlet() {
		
	}
	
	private static final long serialVersionUID = -1031957668151394521L;

	@Override
	public void keepAlive() {
		System.out.println("Received keep alive from "  + getThreadLocalRequest().getRemoteHost() + "(" + getThreadLocalRequest().getRemoteAddr() + ") (session " + getThreadLocalRequest().getSession(false).getId() + ")");
	}

	@Override
	public RemoteConfiguration register() throws RemoteConfigurationException {

		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession(false);
		
		
		RemoteConfiguration remoteConfig = getRemoteConfiguration(request, session);

		if (getServletContext().getAttribute("configuration") == null) {
			System.out.println("loading configuration...");
			Configuration c;
			try {
				c = new PropertiesConfiguration("algebraeditor.properties");
			} catch (Exception e) {
				throw new RemoteConfigurationException(e.getMessage());
			}
			getServletContext().setAttribute("configuration", c);
		}
		
		Configuration c = (Configuration) getServletContext().getAttribute("configuration");
		
		
		System.out.println(c.getString("server","dotpath"));
		remoteConfig.setKeepAliveInterval(c.getInt("client.editor.keepaliveinterval", 60000));
		remoteConfig.setLoadEmptyCanvas(c.getBoolean("client.editor.loademptycanvas", true));
		remoteConfig.setInvertArrows(c.getBoolean("client.canvas.invertarrows", true));
		return remoteConfig;
	}

	private RemoteConfiguration getRemoteConfiguration(
			HttpServletRequest request, HttpSession session) {
		RemoteConfiguration tmp;
		
		if (session != null && ((QueryPlanBundle)session.getAttribute("queryPlans")).getPlans().size()>0 &&
				!(((QueryPlanBundle)session.getAttribute("queryPlans")).getPlans().size() == 1 && ((QueryPlanBundle)session.getAttribute("queryPlans")).getPlans().values().iterator().next().getPlan().size() == 0)) {
			tmp = new RemoteConfigurationWithPlansInSession(((QueryPlanBundle)session.getAttribute("queryPlans")).getPlans().keySet().toArray(new Integer[0]));
		}else{
			if (session == null) session = request.getSession(true);
			session.setAttribute("queryPlans", new QueryPlanBundle());
			tmp = new RemoteConfiguration();
		}
		return tmp;
	}



}
