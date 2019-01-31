package org.siqisource.mockit.model;

import java.util.ArrayList;
import java.util.List;

import org.siqisource.mockit.utils.NameConverter;

public class Model {

	private Class<?> clazz;

	private String mapperClazz;

	private String simpleName;

	private String name;

	private String tableSchema;

	private String tableName;

	private String keyProperty;

	private String keyColumn;

	private boolean singleKey = true;

	private String keySequence;

	private List<Property> properties = new ArrayList<Property>();;

	public void addProperty(Property property) {
		this.properties.add(property);
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getTableSchema() {
		return tableSchema;
	}

	public void setTableSchema(String tableSchema) {
		this.tableSchema = tableSchema;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public String getKeyProperty() {
		return keyProperty;
	}

	public void setKeyProperty(String keyProperty) {
		this.keyProperty = keyProperty;
	}

	public String getKeySequence() {
		return keySequence;
	}

	public void setKeySequence(String keySequence) {
		this.keySequence = keySequence;
	}

	public boolean isSingleKey() {
		return singleKey;
	}

	public void setSingleKey(boolean singleKey) {
		this.singleKey = singleKey;
	}

	public String getKeyColumn() {
		return keyColumn;
	}

	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}

	public String getMapperClazz() {
		return mapperClazz;
	}

	public void setMapperClazz(String mapperClazz) {
		this.mapperClazz = mapperClazz;
	}

	public String getFullTableName() {
		if (this.tableSchema != null && !"".equals(this.tableSchema)) {
			return this.tableSchema + "." + this.tableName;
		} else {
			return this.tableName;
		}
	}

	public List<Property> getNoPrimaryProperties() {
		List<Property> noPrimaryProperties = new ArrayList<Property>();
		for (Property property : this.properties) {
			if (this.keyProperty.equals(property.getName())) {
				continue;
			}
			noPrimaryProperties.add(property);
		}
		return noPrimaryProperties;
	}

	public Property getPrimaryProperty() {
		for (Property property : this.properties) {
			if (this.keyProperty.equals(property.getName())) {
				return property;
			}
		}
		if (this.keyProperty != null) {
			throw new RuntimeException("the keyProperty can't found in table");
		}
		return null;
	}

	public Property getPropertyByName(String name) {
		name = NameConverter.firstLetterLower(name);
		for (Property property : properties) {
			if (name.equals(property.getName())) {
				return property;
			}
		}
		throw new RuntimeException("can't get property:" + name + " in model " + this.name);
	}

	public List<Property> getPropertiesByNames(List<String> names) {
		List<Property> result = new ArrayList<Property>();
		for (String name : names) {
			result.add(getPropertyByName(name));
		}
		return result;
	}

	public List<Property> getPropertiesExcludeNames(List<String> names) {
		List<Property> result = new ArrayList<Property>();
		for (Property property : properties) {
			if (!names.contains(property.getName())) {
				result.add(property);
			}
		}
		return result;
	}

}
