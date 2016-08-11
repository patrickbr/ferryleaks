package com.algebraweb.editor.server.logicalplan.evaluator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;

import com.algebraweb.editor.shared.exceptions.LogicalCanvasSQLException;
import com.algebraweb.editor.shared.logicalplan.EvaluationContext;

/**
 * Evaluates an SQL query against a back end database
 *
 * @author patrick
 *
 */
public class SqlEvaluator {
	private static Connection conn = null;

	public SqlEvaluator(EvaluationContext c) throws LogicalCanvasSQLException {
		try {
			Class.forName("org.postgresql.Driver");

			String dbHost = c.getDatabaseServer();
			String dbPort = Integer.toString(c.getDatabasePort());
			String database = c.getDatabase();
			String dbUser = c.getDatabaseUser();
			String dbPassword = c.getDatabasePassword();

			if (dbHost.equals("")) {
				throw new LogicalCanvasSQLException(
						"Please provide a valid host with PostgreSQL running!");
			}
			if (database.equals("")) {
				throw new LogicalCanvasSQLException(
						"Please provide a valid database name!");
			}
			if (dbUser.equals("")) {
				throw new LogicalCanvasSQLException(
						"Please provide a valid database user!");
			}

			conn = DriverManager.getConnection("jdbc:postgresql://" + dbHost
					+ ":" + dbPort + "/" + database, dbUser, dbPassword);
		} catch (ClassNotFoundException e) {
			throw new LogicalCanvasSQLException(e.getMessage());
		} catch (SQLException e) {
			throw new LogicalCanvasSQLException(e.getMessage()
					+ " (state was: " + e.getSQLState() + ")");
		}
	}

	/**
	 * Returns a list of maps where every list entry is a row, every map entry a
	 * pair column,value
	 *
	 * @param qry
	 *            the sql query to evaluate
	 * @return the resulting list of maps
	 * @throws LogicalCanvasSQLException
	 */
	public List<Map<String, String>> eval(String qry)
			throws LogicalCanvasSQLException {
		List<Map<String, String>> res = null;
		QueryRunner qrun = new QueryRunner();
		try {
			conn.createStatement();
			res = qrun.query(conn, qry, new SerializableHandler());
		} catch (SQLException e) {
			throw new LogicalCanvasSQLException(e.getMessage()
					+ " (state was: " + e.getSQLState() + ")");
		}
		return res;
	}
}