package com.algebraweb.editor.server.registrationservice;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.algebraweb.editor.client.RemoteConfiguration;
import com.algebraweb.editor.client.RemoteConfigurationWithPlansInSession;
import com.algebraweb.editor.client.services.RegistrationService;
import com.algebraweb.editor.shared.exceptions.RemoteConfigurationException;
import com.algebraweb.editor.shared.logicalplan.QueryPlanBundle;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * A servlet for client registrations. Returns possible plans saved in the
 * session as well as the central server configuration
 *
 * @author Patrick Brosi
 *
 */
public class RegistrationServiceServlet extends RemoteServiceServlet implements
RegistrationService {

	/**
	 *
	 */
	private static final long serialVersionUID = -1031957668151394521L;

	public RegistrationServiceServlet() {

	}

	private RemoteConfiguration getRemoteConfiguration(
			HttpServletRequest request, HttpSession session) {
		RemoteConfiguration tmp;
		if (session != null
				&& ((QueryPlanBundle) session.getAttribute("queryPlans")) != null
				&& ((QueryPlanBundle) session.getAttribute("queryPlans"))
				.getPlans().size() > 0
				&& !(((QueryPlanBundle) session.getAttribute("queryPlans"))
						.getPlans().size() == 1 && ((QueryPlanBundle) session
								.getAttribute("queryPlans")).getPlans().values()
								.iterator().next().getPlan().size() == 0)) {

			tmp = new RemoteConfigurationWithPlansInSession(
					((QueryPlanBundle) session.getAttribute("queryPlans"))
					.getPlans().keySet().toArray(new Integer[0]));
		} else {
			initSession(request, session);
			tmp = new RemoteConfiguration();
		}
		return tmp;
	}

	private HttpSession initSession(HttpServletRequest request, HttpSession session) {
		if (session == null) {
			session = request.getSession(true);
		}
		session.setAttribute("queryPlans", new QueryPlanBundle());
		return session;
	}

	@Override
	public void keepAlive() {
		System.out.println("Received keep alive from "
				+ this.getThreadLocalRequest().getRemoteAddr() + " ("
				+ this.getThreadLocalRequest().getSession().getId() + ")");
	}

	@Override
	public RemoteConfiguration register()
	throws RemoteConfigurationException {
		return register(null);
	}

	@Override
	public RemoteConfiguration register(String id) throws RemoteConfigurationException {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession(false);

		if (id != null) {
			if (getServletContext().getAttribute("postplan" + id) == null) throw new RemoteConfigurationException("Plan not found.");
			QueryPlanBundle b = (QueryPlanBundle) getServletContext().getAttribute("postplan" + id);
			session = initSession(request, session);
			session.setAttribute("queryPlans", SerializationClone.clone(b));
		}

		RemoteConfiguration remoteConfig = getRemoteConfiguration(request,
				session);

		if (getServletContext().getAttribute("configuration") == null) {
			Configuration c;
			try {
				c = new PropertiesConfiguration("algebraeditor.properties");
			} catch (Exception e) {
				throw new RemoteConfigurationException(e.getMessage());
			}
			getServletContext().setAttribute("configuration", c);
		}


		Configuration c = (Configuration) getServletContext().getAttribute(
		"configuration");
		remoteConfig.setKeepAliveInterval(c.getInt(
				"client.editor.keepaliveinterval", 60000));
		remoteConfig.setLoadEmptyCanvas(c.getBoolean(
				"client.editor.loademptycanvas", true));
		remoteConfig.setInvertArrows(c.getBoolean("client.canvas.invertarrows",
				true));
		return remoteConfig;
	}
}
