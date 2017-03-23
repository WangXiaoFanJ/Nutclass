package com.dev.nutclass.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5{

	public static String md5File(File filename) {
        InputStream fis;
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        try{
            fis = new FileInputStream(filename);
            md5 = MessageDigest.getInstance("MD5");
            while((numRead=fis.read(buffer)) > 0) {
                md5.update(buffer,0,numRead);
            }
            fis.close();
            return byteArrayToHexString(md5.digest());  
        } catch (Exception e) {
            System.out.println("error");
            return null;
        }
    }
	
	public static String md5(byte b[]) throws NoSuchAlgorithmException{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(b, 0, b.length);
		return byteArrayToHexString(md5.digest());
	}

	public static String md5(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte b[] = data.getBytes("utf-8");
		md5.update(b, 0, b.length);
		return byteArrayToHexString(md5.digest());
	}
	public static String md532(String data){
		String restr = "";
		try {
			restr=md5(data);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return restr;
	}

	private static String byteArrayToHexString(byte b[]){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			sb.append(byteToHexString(b[i]));

		return sb.toString();
	}

	private static String byteToHexString(byte b){
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	private static String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e","f" };

}
