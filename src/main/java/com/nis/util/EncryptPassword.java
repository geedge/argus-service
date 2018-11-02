package com.nis.util;

import com.google.api.client.util.Base64;
import com.zdjizhi.crypt.AESUtil;
/**
 * 加密解密工具类
 * @author RenKaiGe-Office
 *
 */
public class EncryptPassword {
	public static void main(String[] args) {
		encryptPassword("k18");
	}

	/**
	 * 解密密码
	 * 
	 * @param key
	 * @param encryptPassword
	 */
	private static void decipheringPassword(String key, String encryptPassword) {
		try {
			String realPwd = new String(AESUtil.decrypt(Base64.decodeBase64(encryptPassword), key));
			System.out.println("实际密码:" + realPwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加密密码
	 * 
	 * @param pwd
	 */
	private static void encryptPassword(String pwd) {
		try {
			String initKeyString = AESUtil.initKeyString();
			System.out.println("加密的key:" + initKeyString);
			String password = new String(Base64.encodeBase64(AESUtil.encrypt(pwd.getBytes(), initKeyString)));
			System.out.println("加密后密码:	" + password);
			decipheringPassword(initKeyString, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
