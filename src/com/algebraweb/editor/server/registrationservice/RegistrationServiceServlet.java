package com.algebraweb.editor.server.registrationservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.algebraweb.editor.client.Configuration;
import com.algebraweb.editor.client.ConfigurationWithPlansInSession;
import com.algebraweb.editor.client.RegistrationService;
import com.algebraweb.editor.client.RemoteManipulationService;
import com.algebraweb.editor.server.logicalplan.QueryPlanBundle;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RegistrationServiceServlet extends RemoteServiceServlet implements RegistrationService{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1031957668151394521L;

	@Override
	public void keepAlive() {
		System.out.println("Received keep alive from "  + getThreadLocalRequest().getRemoteHost() + "(" + getThreadLocalRequest().getRemoteAddr() + ") (session " + getThreadLocalRequest().getSession(false).getId() + ")");
	}

	@Override
	public Configuration register() {

		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession(false);
		Configuration tmp;
		
		if (session == null) System.out.println("session is empty");

		if (session != null && ((QueryPlanBundle)session.getAttribute("queryPlans")).getPlans().size()>0 &&
				!(((QueryPlanBundle)session.getAttribute("queryPlans")).getPlans().size() == 1 && ((QueryPlanBundle)session.getAttribute("queryPlans")).getPlans().values().iterator().next().getPlan().size() == 0)) {

			//we do have plans already stored in the session
			
			tmp = new ConfigurationWithPlansInSession(((QueryPlanBundle)session.getAttribute("queryPlans")).getPlans().keySet().toArray(new Integer[0]));

		}else{
			if (session == null) {
				System.out.println("creating new session");
				session = request.getSession(true);
			}
			session.setAttribute("queryPlans", new QueryPlanBundle());
			tmp = new Configuration();
		}

		return tmp;
	}







}
