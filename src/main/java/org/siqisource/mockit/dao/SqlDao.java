package org.siqisource.mockit.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SqlDao {

	@Select("${sql}")
	List<LinkedHashMap<String, Object>> select(@Param("sql") String sql);
}
