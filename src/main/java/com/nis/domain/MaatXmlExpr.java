package com.nis.domain;

import java.io.Serializable;

public class MaatXmlExpr implements Serializable {

	private static final long serialVersionUID = 1L;
	private String keyExpression;
	private String valueExpression;
	private Integer redisDB;

	public String getKeyExpression() {
		return keyExpression;
	}

	public void setKeyExpression(String keyExpression) {
		this.keyExpression = keyExpression;
	}

	public String getValueExpression() {
		return valueExpression;
	}

	public void setValueExpression(String valueExpression) {
		this.valueExpression = valueExpression;
	}

	public Integer getRedisDB() {
		return redisDB;
	}

	public void setRedisDB(Integer redisDB) {
		this.redisDB = redisDB;
	}

}
