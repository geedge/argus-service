package com.nis.test;



import com.zdjizhi.crypt.AESUtil;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;


public class CryptTest {
	
	@Test
	public  void testDbPasswordCrypt(){
		try {
			String scretKey = AESUtil.initKeyString();
			
		  String cryptKey =	new String(Base64.encodeBase64(AESUtil.encrypt("ictsoft".getBytes(), scretKey)));
		  
		  String decryptKey = new String(AESUtil.decrypt(Base64.decodeBase64(cryptKey), scretKey));
		  System.out.println(scretKey);
		  System.out.println(cryptKey);
		  System.out.println(decryptKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
