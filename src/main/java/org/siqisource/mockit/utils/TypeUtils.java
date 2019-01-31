package org.siqisource.mockit.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.type.JdbcType;

public class TypeUtils {

	private static List<String> ADDABLE_TYPE = new ArrayList<String>();

	protected static Map<String, JdbcType> JAVA2JDBC = new HashMap<String, JdbcType>();

	static {
		ADDABLE_TYPE.add(int.class.getName());
		ADDABLE_TYPE.add(short.class.getName());
		ADDABLE_TYPE.add(long.class.getName());
		ADDABLE_TYPE.add(Integer.class.getName());
		ADDABLE_TYPE.add(Short.class.getName());
		ADDABLE_TYPE.add(Long.class.getName());
	}

	static {
		// number
		JAVA2JDBC.put(Short.class.getName(), JdbcType.SMALLINT);
		JAVA2JDBC.put(Integer.class.getName(), JdbcType.INTEGER);
		JAVA2JDBC.put(Long.class.getName(), JdbcType.NUMERIC);
		JAVA2JDBC.put(Float.class.getName(), JdbcType.FLOAT);
		JAVA2JDBC.put(Double.class.getName(), JdbcType.DOUBLE);
		JAVA2JDBC.put(short.class.getName(), JdbcType.SMALLINT);
		JAVA2JDBC.put(int.class.getName(), JdbcType.INTEGER);
		JAVA2JDBC.put(long.class.getName(), JdbcType.NUMERIC);
		JAVA2JDBC.put(float.class.getName(), JdbcType.FLOAT);
		JAVA2JDBC.put(double.class.getName(), JdbcType.DOUBLE);

		// date time
		JAVA2JDBC.put(java.util.Date.class.getName(), JdbcType.TIMESTAMP);
		JAVA2JDBC.put(java.sql.Date.class.getName(), JdbcType.DATE);
		JAVA2JDBC.put(java.sql.Time.class.getName(), JdbcType.TIME);

		// boolean varchar
		JAVA2JDBC.put(String.class.getName(), JdbcType.VARCHAR);
		JAVA2JDBC.put(Enum.class.getName(), JdbcType.VARCHAR);
		JAVA2JDBC.put(UUID.class.getName(), JdbcType.OTHER);
		JAVA2JDBC.put(Boolean.class.getName(), JdbcType.BOOLEAN);
		JAVA2JDBC.put(boolean.class.getName(), JdbcType.BOOLEAN);
		JAVA2JDBC.put(Character.class.getName(), JdbcType.CHAR);

	}

	public static JdbcType jdbcType(Class<?> clazz) {
		if (Enum.class.isAssignableFrom(clazz)) {
			clazz = Enum.class;
		}
		JdbcType jdbcType = JAVA2JDBC.get(clazz.getName());
		return jdbcType == null ? JdbcType.OTHER : jdbcType;
	}

}
