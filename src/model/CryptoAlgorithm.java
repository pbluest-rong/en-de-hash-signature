package model;

import java.awt.RenderingHints.Key;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public interface CryptoAlgorithm {
	public static String encryptBase64(byte[] text) {
		return Base64.getEncoder().encodeToString(text);
	}

	public static String decryptBase64(byte[] text) {
		return new String(Base64.getDecoder().decode(text), StandardCharsets.UTF_8);
	}

	public static IvParameterSpec generateIV() {
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		return new IvParameterSpec(iv);
	}

	public static void savePublicKey(String publicKeyPath, PublicKey publicKey) throws Exception {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(publicKeyPath))) {
			oos.writeObject(publicKey);
		}
	}

	public static PublicKey loadPublicKey(String publicKeyPath)
			throws ClassNotFoundException, FileNotFoundException, IOException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(publicKeyPath))) {
			return (PublicKey) ois.readObject();
		}
	}

	public static void savePrivateKey(String privateKeyPath, PrivateKey privateKey) throws Exception {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(privateKeyPath))) {
			oos.writeObject(privateKey);
		}
	}

	public static PrivateKey loadPrivateKey(String privateKeyPath)
			throws ClassNotFoundException, FileNotFoundException, IOException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(privateKeyPath))) {
			return (PrivateKey) ois.readObject();
		}
	}

	public static void saveSecretKey(String secretKeyPath, SecretKey secretKey) throws Exception {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(secretKeyPath))) {
			oos.writeObject(secretKey);
		}
	}

	public static SecretKey loadSecretKey(String secretKeyPath)
			throws ClassNotFoundException, FileNotFoundException, IOException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(secretKeyPath))) {
			return (SecretKey) ois.readObject();
		}
	}

	void genKey() throws Exception;

	void loadKey(Key key) throws Exception;

	byte[] encrypt(String text) throws Exception;

	String decrypt(byte[] data) throws Exception;

	boolean encryptFile(String srcFilePath, String desFilePath) throws Exception;

	boolean decryptFile(String srcFilePath, String desFilePath) throws Exception;
}
