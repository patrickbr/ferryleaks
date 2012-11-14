package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.graphcanvas.Tuple;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.shared.node.RawNode;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The answer to manipulation requests. Holds information on the action to be
 * performed on the view, the status code etc.
 * 
 * @author Patrick Brosi
 * 
 */
public class RemoteManipulationMessage implements IsSerializable {

	private int returnCode;
	private String message;
	private ValidationResult validationResult;
	private int planid;
	private List<RawNode> nodesAffected = new ArrayList<RawNode>();
	private Map<Integer, Tuple> coordinates = new HashMap<Integer, Tuple>();
	private String action;

	public RemoteManipulationMessage() {

	}

	public RemoteManipulationMessage(int planid, String action, int returnCode,
			String message, ValidationResult res) {
		this.planid = planid;
		this.returnCode = returnCode;
		this.action = action;
		this.message = message;
		this.validationResult = res;
	}

	/**
	 * Returns the action to be performed on the server
	 * 
	 * @return the action string
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Returns coordinates for "add" messages
	 * 
	 * @return the coordinates
	 */
	public Map<Integer, Tuple> getCoordinates() {
		return coordinates;
	}

	/**
	 * Returns the error message
	 * 
	 * @return the error message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns a list of affected nodes
	 * 
	 * @return the affected nodes
	 */
	public List<RawNode> getNodesAffected() {
		return nodesAffected;
	}

	/**
	 * Returns the affected plan's id
	 * 
	 * @return the pid
	 */
	public int getPlanid() {
		return planid;
	}

	/**
	 * Returns the return code. 1 = OK, 3 = ERROR
	 * 
	 * @return
	 */
	public int getReturnCode() {
		return returnCode;
	}

	/**
	 * Returns the validation result after the manipulation
	 * 
	 * @return the validaiton result
	 */
	public ValidationResult getValidationResult() {
		return validationResult;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setCoordinates(Map<Integer, Tuple> coords) {
		this.coordinates = coords;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public void setValidationResult(ValidationResult validationResult) {
		this.validationResult = validationResult;
	}
}
