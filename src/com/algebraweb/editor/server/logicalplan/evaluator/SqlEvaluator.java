package com.algebraweb.editor.server.logicalplan.evaluator;

import javax.servlet.ServletContext;
import org.apache.commons.configuration.Configuration;

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

	public SqlEvaluator(EvaluationContext c, ServletContext sCon) throws LogicalCanvasSQLException {
		try {
			Class.forName("org.postgresql.Driver");

			Configuration cc = (Configuration) sCon.getAttribute(
							"configuration");

			String dbHost = cc.getString("server.postgres.host");
			String dbPort = cc.getString("server.postgres.port");
			String database = cc.getString("server.postgres.database");
			String dbUser = cc.getString("server.postgres.user");
			String dbPassword = cc.getString("server.postgres.password");

			if (dbHost.equals("")) {
				throw new LogicalCanvasSQLException(
						"No DB host configured.");
			}
			if (database.equals("")) {
				throw new LogicalCanvasSQLException(
						"No DB configured.");
			}
			if (dbUser.equals("")) {
				throw new LogicalCanvasSQLException(
						"No DB host configured.");
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
