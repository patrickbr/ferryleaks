package com.algebraweb.editor.server.logicalplan.evaluator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.ResultSetHandler;

public class SerializableHandler implements
		ResultSetHandler<List<Map<String, String>>> {

	@Override
	public List<Map<String, String>> handle(ResultSet set) throws SQLException {

		List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
		while (set.next()) {
			Map<String, String> row = new HashMap<String, String>();

			for (int i = 0; i < set.getMetaData().getColumnCount(); i++) {
				row.put(set.getMetaData().getColumnLabel(i + 1), set
						.getString(i + 1));
			}
			ret.add(row);
		}
		return ret;
	}
}