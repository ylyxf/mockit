package org.siqisource.mockit.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.siqisource.mockit.dao.SqlDao;
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
	SqlDao sqlDAO;

	/**
	 *
	 * @param originUri
	 * @param distUri
	 *            packageName:MapperName:TableName:ModelName:sqlName.sql
	 * @return
	 */
	public Object service(String originUri, String distUri) {
		String sql = distUri.substring(0, distUri.length() - 4);
		List<LinkedHashMap<String, Object>> result = sqlDAO.select(sql);
		return result;
	}

}
