package model.asymmetric;

import java.awt.RenderingHints.Key;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import model.EKeySize;
import model.ICryptoAlgorithm;

public class RSA implements ICryptoAlgorithm {
	KeyPair keyPair;
	PublicKey publicKey;
	PrivateKey privateKey;
	private EKeySize keySize;

	public RSA(EKeySize keySize) {
		this.keySize = keySize;
	}

	@Override
	public void setKeySize(EKeySize keySize) throws Exception {
		this.setKeySize(keySize);
	}

	@Override
	public void genKey() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(this.keySize.getBits());
		this.keyPair = generator.generateKeyPair();
		this.publicKey = this.keyPair.getPublic();
		this.privateKey = this.keyPair.getPrivate();
	}

	@Override
	public void loadKey(Object key) throws Exception {
		if (key instanceof PublicKey) {
			this.publicKey = (PublicKey) key;
		} else if (key instanceof PrivateKey) {
			this.privateKey = (PrivateKey) key;
		} else {
			throw new IllegalArgumentException("Invalid key type for RSA");
		}
	}

	@Override
	public byte[] encrypt(String text) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(text.getBytes());
	}

	@Override
	public String decrypt(byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
	}

	@Override
	public boolean encryptFile(String srcFilePath, String desFilePath) throws Exception {
		throw new Exception(
				"RSA is not suitable for encrypting files. Please use a symmetric algorithm for file encryption.");
	}

	@Override
	public boolean decryptFile(String srcFilePath, String desFilePath) throws Exception {
		throw new Exception(
				"RSA is not suitable for decrypting files. Please use a symmetric algorithm for file encryption.");
	}

	@Override
	public boolean saveKeyToFile(String filePath) throws Exception {
		return false;
	}

	@Override
	public boolean loadKeyFromFile(String filePath) throws Exception {
		return false;
	}

	@Override
	public int getKeySize() {
		return ((RSAPrivateKey) privateKey).getModulus().bitLength();
	}
}
