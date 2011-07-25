package com.algebraweb.editor.server.fileserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.QueryPlan;
import com.algebraweb.editor.server.logicalplan.QueryPlanBundle;
import com.algebraweb.editor.server.logicalplan.xmlbuilder.XMLNodePlanBuilder;

public class FileServerServlet extends HttpServlet {

	private static final long serialVersionUID = -4356636877078339046L;

	byte[] bbuf = new byte[1024];


	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {



		try {
			ServletOutputStream out = response.getOutputStream();
			ServletContext context = getServletContext();

			String filename="plan.xml";

			response.setContentType("application/octet-stream");
			//response.setContentLength((int) file.length());
			response.setHeader("Content-Disposition", "attachement; filename=\"" + filename + "\"");

			int pid = Integer.parseInt(request.getParameter("pid"));

			System.out.println("Requestet plan #" + pid);

			Document d;
			XMLNodePlanBuilder builder = new XMLNodePlanBuilder();

			System.out.println("Downloading plan '" + pid + "'");
			
			if (pid == -1) {

				QueryPlanBundle b = ((QueryPlanBundle)request.getSession().getAttribute("queryPlans"));
				d = builder.getPlanBundle(b,getServletContext());


			}else{

				QueryPlan planToWork = ((QueryPlanBundle)request.getSession().getAttribute("queryPlans")).getPlan(pid);		
				d = builder.getNodePlan(planToWork,getServletContext());

			}

			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			outputter.output(d, out);


			out.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
