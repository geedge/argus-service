/**
 * 
 */
package com.nis.util;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * @ClassName:BlobTypeHandler
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年6月25日 下午2:11:03
 * @version V1.0
 */
public class BlobTypeHandler extends BaseTypeHandler<String> {

	// ###指定字符集
	private static final String DEFAULT_CHARSET = "utf-8";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.BaseTypeHandler#setNonNullParameter(java.sql.
	 * PreparedStatement, int, java.lang.Object,
	 * org.apache.ibatis.type.JdbcType)
	 */
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			String parameter, JdbcType jdbcType) throws SQLException {
		// TODO Auto-generated method stub
		ByteArrayInputStream bis;
		try {
			// / ###把String转化成byte流
			bis = new ByteArrayInputStream(parameter.getBytes(DEFAULT_CHARSET));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Blob Encoding Error!");
		}
		ps.setBinaryStream(i, bis, parameter.length());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet
	 * , java.lang.String)
	 */
	@Override
	public String getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		// TODO Auto-generated method stub
		
		 Blob blob = rs.getBlob(columnName);
	        byte[] returnValue = null;
	        if (null != blob) {
	            returnValue = blob.getBytes(1, (int) blob.length());
	        }
	        try {
//	            ###把byte转化成string
	            return new String(returnValue, DEFAULT_CHARSET);
	        } catch (UnsupportedEncodingException e) {
	            throw new RuntimeException("Blob Encoding Error!");
	        }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet
	 * , int)
	 */
	@Override
	public String getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.
	 * CallableStatement, int)
	 */
	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		// TODO Auto-generated method stub
		 Blob blob = cs.getBlob(columnIndex);
	        byte[] returnValue = null;
	        if (null != blob) {
	            returnValue = blob.getBytes(1, (int) blob.length());
	        }
	        try {
	            return new String(returnValue, DEFAULT_CHARSET);
	        } catch (UnsupportedEncodingException e) {
	            throw new RuntimeException("Blob Encoding Error!");
	        }
	}

}
