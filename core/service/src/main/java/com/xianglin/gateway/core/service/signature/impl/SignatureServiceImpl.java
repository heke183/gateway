/**
 * 
 */
package com.xianglin.gateway.core.service.signature.impl;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.xianglin.gateway.core.service.signature.SignatureService;

/**
 * 签名管理器
 * 
 * @author pengpeng 2015年5月4日下午2:47:13
 */
public class SignatureServiceImpl implements SignatureService, InitializingBean {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(SignatureServiceImpl.class);

	/** 字符编码 */
	private static final String CHARSET = "utf-8";

	/** 签名算法 */
	private static final String ALGORITHM = "SHA256WithRSA";

	/** 密钥算法 */
	private static final String KEY_ALGORITHM = "RSA";

	/** 加签私钥 */
	private PrivateKey privateKey;

	/** 验签公钥 */
	private PublicKey publicKey;

	/** 加签私钥 */
	private String privateKeyBase64;

	/** 验签公钥 */
	private String publicKeyBase64;

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		privateKey = createPrivateKey();
		publicKey = createPublicKey();
	}

	/**
	 * @see com.pingan.gateway.SignatureService.shared.security.SignatureManager#sign(java.lang.String)
	 */
	@Override
	public String sign(String toBeSigned) throws Exception {
		Signature signature = Signature.getInstance(ALGORITHM);
		signature.initSign(privateKey);
		signature.update(toBeSigned.getBytes(CHARSET));
		byte[] signBytes = signature.sign();
		return Base64.encodeBase64String(signBytes);
	}

	/**
	 * @see com.pingan.gateway.SignatureService.shared.security.SignatureManager#verify(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean verify(String toBeSigned, String sign) throws Exception {
		Signature signature = Signature.getInstance(ALGORITHM);
		signature.initVerify(publicKey);
		signature.update(toBeSigned.getBytes(CHARSET));
		return signature.verify(Base64.decodeBase64(sign));
	}

	/**
	 * 创建加签私钥
	 * 
	 * @return
	 */
	private PrivateKey createPrivateKey() {
		try {
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyBase64));
			return KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(pkcs8EncodedKeySpec);
		} catch (Exception e) {
			logger.error("createPrivateKey error!", e);
		}
		return null;
	}

	/**
	 * 创建验签公钥
	 * 
	 * @return
	 */
	private PublicKey createPublicKey() {
		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyBase64));
			return KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(x509EncodedKeySpec);
		} catch (Exception e) {
			logger.error("createPublicKey error!", e);
		}
		return null;
	}

	/**
	 * @param privateKeyBase64
	 *            the privateKeyBase64 to set
	 */
	public void setPrivateKeyBase64(String privateKeyBase64) {
		this.privateKeyBase64 = privateKeyBase64;
	}

	/**
	 * @param publicKeyBase64
	 *            the publicKeyBase64 to set
	 */
	public void setPublicKeyBase64(String publicKeyBase64) {
		this.publicKeyBase64 = publicKeyBase64;
	}

}
