package com.algebraweb.editor.server.logicalplan.evaluator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.dbutils.ResultSetHandler;

public class SerializableHandler implements ResultSetHandler{

	@Override
	public ArrayList<HashMap<String,String>> handle(ResultSet set) throws SQLException {

		ArrayList<HashMap<String,String>> ret = new  ArrayList<HashMap<String,String>>();


		while(set.next()) {

			HashMap<String,String> row = new HashMap<String,String>();

			for (int i=0;i<set.getMetaData().getColumnCount();i++) {


				row.put(set.getMetaData().getColumnLabel(i+1), set.getString(i+1));


			}

			ret.add(row);			 

		}		 

	

		return ret;
	}

}
