package com.nis.domain;

import java.io.Serializable;

public class MaatXmlSeq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sequenceKey;
	private Integer operation;
	private Integer redisDB;

	public String getSequenceKey() {
		return sequenceKey;
	}

	public void setSequenceKey(String sequenceKey) {
		this.sequenceKey = sequenceKey;
	}

	public Integer getOperation() {
		return operation;
	}

	public void setOperation(Integer operation) {
		this.operation = operation;
	}

	public Integer getRedisDB() {
		return redisDB;
	}

	public void setRedisDB(Integer redisDB) {
		this.redisDB = redisDB;
	}

}
