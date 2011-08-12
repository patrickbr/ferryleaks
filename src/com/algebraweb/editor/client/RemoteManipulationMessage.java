package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RemoteManipulationMessage implements IsSerializable{
	
	private int returnCode;
	private String message;
	private ValidationResult validationResult;
	private int planid;
	private List<RawNode> nodesAffected = new ArrayList<RawNode>();
	private Map<Integer,Coordinate> coordinates = new HashMap<Integer,Coordinate>();
	private String action;


	public RemoteManipulationMessage() {

	}

	public RemoteManipulationMessage(int planid, String action, int returnCode, String message, ValidationResult res) {
		this.planid=planid;
		this.returnCode=returnCode;
		this.action=action;
		this.message=message;
		this.validationResult = res;
	}

	public String getAction() {
		return action;
	}

	public Map<Integer, Coordinate> getCoordinates() {
		return coordinates;
	}

	public String getMessage() {
		return message;
	}

	public List<RawNode> getNodesAffected() {
		return nodesAffected;
	}

	public int getPlanid() {
		return planid;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public ValidationResult getValidationResult() {
		return validationResult;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setCoordinates(Map<Integer, Coordinate> coords) {
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
