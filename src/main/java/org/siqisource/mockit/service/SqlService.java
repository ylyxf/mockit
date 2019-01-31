package org.siqisource.mockit.service;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.siqisource.mockit.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yulei
 *
 */
@Service
public class SqlService {

	private static Logger logger = LoggerFactory.getLogger(SqlService.class);

	@Autowired
	SqlSessionFactory sqlSessionFactory;

	/**
	 *
	 * @param originUri
	 * @param distUri
	 *            packageName:MapperName:TableName:ModelName:sqlName.sql
	 * @return
	 */
	public Object service(String originUri, String distUri) {
		System.out.println(loadModel(distUri));
		/*
		 * Configuration configuration = sqlSessionFactory.getConfiguration();
		 * XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(null,
		 * configuration, "autogen.", configuration.getSqlFragments());
		 * xmlMapperBuilder.parse(); sqlSessionFactory.openSession();
		 */
		return 1;
	}

	private Model loadModel(String distUri) {
		Model model = new Model();
		String className = "";
		distUri = distUri.substring(0, distUri.length() - 4);
		String[] names = distUri.split(":");
		className = names[0] + "." + names[3];
		model.setMapperClazz(names[0] + "." + names[1]);
		model.setSimpleName(names[3]);
		model.setName(className);
		model.setTableName(names[2]);
		loadTable(model);
		return model;
	}

	private void loadTable(Model model) {
		SqlSession session = sqlSessionFactory.openSession();
		DatabaseMetaData metaData;
		try {
			metaData = session.getConnection().getMetaData();
			ModelClazzReader.readModelFromDatabase(model, metaData);
		} catch (SQLException e) {
			logger.error("read table info from table error", e);
		}
		session.close();
	}
}
