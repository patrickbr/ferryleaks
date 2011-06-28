package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.google.gwt.user.client.rpc.IsSerializable;

public class RemoteManipulationMessage implements IsSerializable{
	
	
	private int returnCode;
	private String message;
	private ValidationResult validationResult;
	
	private ArrayList<RawNode> nodesAffected = new ArrayList<RawNode>();
	private HashMap<Integer,Coordinate> coordinates = new HashMap<Integer,Coordinate>();
	
	private String action;
	
	
	public RemoteManipulationMessage(String action, int returnCode, String message, ValidationResult res) {
		
		this.returnCode=returnCode;
		this.action=action;
		this.message=message;
		this.validationResult = res;
		
		
	}
	
	public RemoteManipulationMessage() {
		
	
	}

	public int getReturnCode() {
		return returnCode;
	}
	

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}
	
	

	public HashMap<Integer, Coordinate> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(HashMap<Integer, Coordinate> coordinates) {
		this.coordinates = coordinates;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ValidationResult getValidationResult() {
		return validationResult;
	}

	public void setValidationResult(ValidationResult validationResult) {
		this.validationResult = validationResult;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public ArrayList<RawNode> getNodesAffected() {
		return nodesAffected;
	}
	


		

}
