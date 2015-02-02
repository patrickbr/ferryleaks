package com.algebraweb.editor.server.fileserver;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.algebraweb.editor.server.logicalplan.xmlbuilder.XMLNodePlanBuilder;
import com.algebraweb.editor.shared.logicalplan.QueryPlanBundle;
import com.algebraweb.editor.shared.node.QueryPlan;

/**
 * A servlet which provides methods for download assembled XML plans
 * 
 * @author Patrick Brosi
 * 
 */
public class FileServerServlet extends HttpServlet {
	private static final long serialVersionUID = -4356636877078339046L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			ServletOutputStream out = response.getOutputStream();
			String filename = "plan.xml";

			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition",
					"attachement; filename=\"" + filename + "\"");

			int pid = Integer.parseInt(request.getParameter("pid"));

			Document d;
			XMLNodePlanBuilder builder = new XMLNodePlanBuilder();

			if (pid == -1) {
				QueryPlanBundle b = (QueryPlanBundle) request.getSession()
						.getAttribute("queryPlans");
				d = builder.getPlanBundle(b, getServletContext());
			} else {
				QueryPlan planToWork = ((QueryPlanBundle) request.getSession()
						.getAttribute("queryPlans")).getPlan(pid);
				d = builder.getNodePlan(planToWork, getServletContext());
			}
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			outputter.output(d, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
