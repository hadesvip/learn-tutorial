package com.kevin.encrypt;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * 大哥写点注释吧！
 * @author: wangyong
 * @date: 2019/6/24 21:31
 */
public class Runner {

	public static void main(String[] args)
			throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException,
			BadPaddingException, IllegalBlockSizeException {
		KeyGenerator des = KeyGenerator.getInstance("DES");
		des.init(56);
		SecretKey secretKey = des.generateKey();

		//存放于文件中
		byte[] encoded = secretKey.getEncoded();

		//转换成密钥对象
		DESKeySpec desKey = new DESKeySpec(encoded);
		SecretKeyFactory desInstance = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey1 = desInstance.generateSecret(desKey);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey1);
		byte[] bytes = cipher.doFinal("hello".getBytes());
		System.out.println(bytes);
	}
}
