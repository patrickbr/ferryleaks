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

public class SqlEvaluator {

	private static Connection conn = null;

	// Hostname
	private static String dbHost = "localhost";

	// Port -- Standard: 3306
	private static String dbPort = "5432";

	// Datenbankname
	private static String database = "bugferrytest";

	// Datenbankuser
	private static String dbUser = "bugferry";

	// Datenbankpasswort
	private static String dbPassword = "test";

	public SqlEvaluator() {
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
