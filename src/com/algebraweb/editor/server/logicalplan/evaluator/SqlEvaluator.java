package com.algebraweb.editor.server.logicalplan.evaluator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.algebraweb.editor.client.logicalcanvas.EvaluationContext;

public class SqlEvaluator {

	private static Connection conn = null;

	private static String dbHost = "localhost";
	private static String dbPort = "5432";
	private static String database = "bugferrytest";
	private static String dbUser = "bugferry";
	private static String dbPassword = "test";
	
	private EvaluationContext c;

	public SqlEvaluator(EvaluationContext c) {
		
		this.c=c;
		
		
		try {

			Class.forName("org.postgresql.Driver");
			

			conn = DriverManager.getConnection("jdbc:postgresql://" + dbHost + ":"
					+ dbPort + "/" + database, dbUser, dbPassword);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public ArrayList<HashMap<String,String>> eval(String qry) {


		Statement query;
		ArrayList<HashMap<String,String>> res = null;
		QueryRunner qrun = new QueryRunner();


		try {
			query = conn.createStatement();

			res = (ArrayList<HashMap<String,String>>) qrun.query(conn, qry, new SerializableHandler());
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;

	}


}
