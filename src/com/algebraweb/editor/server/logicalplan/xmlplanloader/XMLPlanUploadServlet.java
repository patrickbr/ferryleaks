package com.algebraweb.editor.server.logicalplan.xmlplanloader;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;

import com.algebraweb.editor.server.logicalplan.QueryPlanBundle;

public class XMLPlanUploadServlet extends UploadAction{


	private static final long serialVersionUID = 4992498870698384055L;
	Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
	Hashtable<String, File> receivedFiles = new Hashtable<String, File>();

	public XMLPlanUploadServlet() {

	}

	/**
	 * Save the received file to a temp file, parse it, save it to the session variable
	 */
	@Override
	public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {

		String response = "";

		int cont = 0;
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				cont ++;
				try {			
					File file = File.createTempFile("upload-", ".bin");
					item.write(file);

					receivedFiles.put(item.getFieldName(), file);
					receivedContentTypes.put(item.getFieldName(), item.getContentType());

					HttpSession session = request.getSession();
					XMLPlanLoader planLoader = new XMLPlanLoader();


					QueryPlanBundle sessionBundle = planLoader.parsePlans(file.getAbsolutePath(),this.getServletContext(),request.getSession());
					session.setAttribute("queryPlans",sessionBundle);

					System.out.println(request.getParameter("file_id"));
					response = request.getParameter("file_id");
					response += "!";

					Iterator<Integer> it = sessionBundle.getPlans().keySet().iterator();

					while (it.hasNext()) {
						response += it.next() + ":";
					}

				} catch (Exception e) {
					throw new UploadActionException(e.getMessage());
				}
			}
		}

		removeSessionFileItems(request);
		return response;
	}
}
