package org.siqisource.mockit.service;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.siqisource.mockit.model.Model;
import org.siqisource.mockit.model.Property;
import org.siqisource.mockit.utils.NameConverter;

public class ModelClazzReader {

	public static void readModelFromDatabase(Model model, DatabaseMetaData metaData) throws SQLException {
		String tableName = model.getTableName();
		String schemaName = null;
		String tableNameArray[] = tableName.split("[.]");
		if (tableNameArray.length == 2) {
			schemaName = tableNameArray[0];
			tableName = tableNameArray[1];
		}

		model.setTableSchema(schemaName);
		model.setTableName(tableName);

		ResultSet rs = metaData.getTables(null, schemaName, tableName, new String[] { "TABLE" });
		if (rs.next()) {
			rs.close();
			readColumn(model, metaData);
		} else {
			rs.close();
			throw new RuntimeException("  table " + tableName + "  is not exist in the database ");
		}

		readSinglePrimaryColumn(model, metaData);
	}

	private static void readColumn(Model model, DatabaseMetaData metaData) throws SQLException {

		ResultSet rs = metaData.getColumns(null, model.getTableSchema(), model.getTableName(), null);

		while (rs.next()) {
			String columnName = rs.getString("COLUMN_NAME");
			String propertyName = NameConverter.underlineToCamelFirstLower(columnName);

			Property property = new Property();
			property.setModel(model);

			property.setColumnName(columnName);
			property.setName(propertyName);

			Integer sqlType = rs.getInt("DATA_TYPE");
			property.setJdbcType(JdbcType.forCode(sqlType));

			// add to model
			model.addProperty(property);
		}
		rs.close();

	}

	private static String readSinglePrimaryColumn(Model model, DatabaseMetaData metaData) throws SQLException {
		ResultSet primaryRs = metaData.getPrimaryKeys(null, model.getTableSchema(), model.getTableName());
		String keyColumn = null;
		try {
			// not support not singleKey type yet.
			if (primaryRs.next()) {
				keyColumn = primaryRs.getString("COLUMN_NAME");
				String keyPropertyName = NameConverter.underlineToCamelFirstLower(keyColumn);
				model.setKeyColumn(keyColumn);
				model.setKeyProperty(keyPropertyName);
				model.setSingleKey(true);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			primaryRs.close();
		}
		return keyColumn;
	}

}
