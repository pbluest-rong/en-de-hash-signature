package model.asymmetric;

import java.awt.RenderingHints.Key;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import model.CryptoAlgorithm;

public class RSA implements CryptoAlgorithm {
	KeyPair keyPair;
	PublicKey publicKey;
	PrivateKey privateKey;

	@Override
	public void genKey() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2028);
		this.keyPair = generator.generateKeyPair();
		this.publicKey = this.keyPair.getPublic();
		this.privateKey = this.keyPair.getPrivate();
	}

	@Override
	public void loadKey(Key key) throws Exception {
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
		return false;
	}

	@Override
	public boolean decryptFile(String srcFilePath, String desFilePath) throws Exception {
		return false;
	}
}
