package com.dev.nutclass.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import android.util.Base64;

/**
 * @author Mr.Zheng
 * @date 2014年8月22日 下午1:44:23
 */
public final class RSAUtils {
	private static String RSA = "RSA";
	private static String RSA_CIPHER = "RSA/ECB/PKCS1Padding";

	/**
	 * 随机生成RSA密钥对(默认密钥长度为1024)
	 * 
	 * @return
	 */
	public static KeyPair generateRSAKeyPair() {
		return generateRSAKeyPair(1024);
	}

	/**
	 * 随机生成RSA密钥对
	 * 
	 * @param keyLength
	 *            密钥长度，范围：512～2048
	 * 
	 *            一般1024
	 * @return
	 */
	public static KeyPair generateRSAKeyPair(int keyLength) {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
			kpg.initialize(keyLength);
			return kpg.genKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 用公钥加密
	 * 
	 * 每次加密的字节数，不能超过密钥的长度值减去11
	 * 
	 * @param data
	 *            需加密数据的byte数据
	 * @param pubKey
	 *            公钥
	 * @return 加密后的byte型数据
	 */
	public static byte[] encryptData(byte[] data, PublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_CIPHER);
			// 编码前设定编码方式及密钥
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// 传入编码数据并返回编码结果
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加密
	 * 
	 * @param key
	 *            加密的密钥
	 * @param data
	 *            待加密的明文数据
	 * @return 加密后的数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, Key key) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(RSA_CIPHER);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// 获得加密块大小，如:加密前数据为128个byte，而key_size=1024 加密块大小为127
			// byte,加密后为128个byte;
			// 因此共有2个加密块，第一个127 byte第二个为1个byte
			int blockSize = cipher.getBlockSize();
			int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
			int leavedSize = data.length % blockSize;
			int blocksSize = leavedSize != 0 ? data.length / blockSize + 1
					: data.length / blockSize;
			byte[] raw = new byte[outputSize * blocksSize];
			int i = 0;
			while (data.length - i * blockSize > 0) {
				if (data.length - i * blockSize > blockSize)
					cipher.doFinal(data, i * blockSize, blockSize, raw, i
							* outputSize);
				else
					cipher.doFinal(data, i * blockSize, data.length - i
							* blockSize, raw, i * outputSize);
				// 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到ByteArrayOutputStream中
				// ，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了OutputSize所以只好用dofinal方法。
				i++;
			}
			return raw;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 解密
	 * 
	 * @param key
	 *            解密的密钥
	 * @param raw
	 *            已经加密的数据
	 * @return 解密后的明文
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] raw, Key key) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(RSA_CIPHER);
			cipher.init(cipher.DECRYPT_MODE, key);
			int blockSize = cipher.getBlockSize();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			int j = 0;
			while (raw.length - j * blockSize > 0) {
				bout.write(cipher.doFinal(raw, j * blockSize, raw.length - j
						* blockSize > blockSize ? blockSize : raw.length - j
						* blockSize));
				j++;
			}
			return bout.toByteArray();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 用私钥解密
	 * 
	 * @param encryptedData
	 *            经过encryptedData()加密返回的byte数据
	 * @param privateKey
	 *            私钥
	 * @return
	 */
	public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_CIPHER);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(encryptedData);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 用私钥解密
	 * 
	 * @param encryptedData
	 *            经过encryptedData()加密返回的byte数据
	 * @param PublicKey
	 *            私钥
	 * @return
	 */
	public static byte[] decryptData(byte[] encryptedData, PublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_CIPHER);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			return cipher.doFinal(encryptedData);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 通过公钥byte[](publicKey.getEncoded())将公钥还原，适用于RSA算法
	 * 
	 * @param keyBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKey(byte[] keyBytes)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		PublicKey  publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * 通过私钥byte[]将公钥还原，适用于RSA算法
	 * 
	 * @param keyBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getPrivateKey(byte[] keyBytes)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
	 * 使用N、e值还原公钥
	 * 
	 * @param modulus
	 * @param publicExponent
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKey(String modulus, String publicExponent)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		BigInteger bigIntModulus = new BigInteger(modulus);
		BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus,
				bigIntPrivateExponent);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * 使用N、d值还原私钥
	 * 
	 * @param modulus
	 * @param privateExponent
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getPrivateKey(String modulus,
			String privateExponent) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		BigInteger bigIntModulus = new BigInteger(modulus);
		BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus,
				bigIntPrivateExponent);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKeyStr
	 *            公钥数据字符串
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
		try {
			byte[] buffer = Base64.decode(publicKeyStr, Base64.DEFAULT);
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			X509EncodedKeySpec  keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	/**
	 * 从字符串中加载私钥
	 * 
	 * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
	 * 
	 * @param privateKeyStr
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey loadPrivateKey(String privateKeyStr)
			throws Exception {
		try {
			byte[] buffer = Base64.decode(privateKeyStr, Base64.DEFAULT);
			// X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	/**
	 * 从文件中输入流中加载公钥
	 * 
	 * @param in
	 *            公钥输入流
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public static PublicKey loadPublicKey(InputStream in) throws Exception {
		try {
			return loadPublicKey(readKey(in));
		} catch (IOException e) {
			throw new Exception("公钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥输入流为空");
		}
	}

	/**
	 * 从文件中加载私钥
	 * 
	 * @param keyFileName
	 *            私钥文件名
	 * @return 是否成功
	 * @throws Exception
	 */
	public static PrivateKey loadPrivateKey(InputStream in) throws Exception {
		try {
			return loadPrivateKey(readKey(in));
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		}
	}

	/**
	 * 读取密钥信息
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static String readKey(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String readLine = null;
		StringBuilder sb = new StringBuilder();
		while ((readLine = br.readLine()) != null) {
			if (readLine.charAt(0) == '-') {
				continue;
			} else {
				sb.append(readLine);
				sb.append("");
			}
		}

		return sb.toString();
	}

	/**
	 * 打印公钥信息
	 * 
	 * @param publicKey
	 */
	public static void printPublicKeyInfo(PublicKey publicKey) {
		RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
		System.out.println("----------RSAPublicKey----------");
		System.out.println("Modulus.length="
				+ rsaPublicKey.getModulus().bitLength());
		System.out.println("Modulus=" + rsaPublicKey.getModulus().toString());
		System.out.println("PublicExponent.length="
				+ rsaPublicKey.getPublicExponent().bitLength());
		System.out.println("PublicExponent="
				+ rsaPublicKey.getPublicExponent().toString());
	}

	public static void printPrivateKeyInfo(PrivateKey privateKey) {
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
		System.out.println("----------RSAPrivateKey ----------");
		System.out.println("Modulus.length="
				+ rsaPrivateKey.getModulus().bitLength());
		System.out.println("Modulus=" + rsaPrivateKey.getModulus().toString());
		System.out.println("PrivateExponent.length="
				+ rsaPrivateKey.getPrivateExponent().bitLength());
		System.out.println("PrivatecExponent="
				+ rsaPrivateKey.getPrivateExponent().toString());

	}

}