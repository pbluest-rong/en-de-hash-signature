package model;

import java.awt.RenderingHints.Key;
import java.io.File;
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
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import model.asymmetric.RSA_AES;

public interface ICryptoAlgorithm {
	public static final int BLOCK_SIZE = 16;

	public static String encryptBase64(byte[] text) {
		return Base64.getEncoder().encodeToString(text);
	}

	public static String decryptBase64(byte[] text) {
		return new String(Base64.getDecoder().decode(text), StandardCharsets.UTF_8);
	}

	public static IvParameterSpec generateIV() {
		byte[] iv = new byte[BLOCK_SIZE];
		new SecureRandom().nextBytes(iv);
		return new IvParameterSpec(iv);
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

	public static boolean isBasicSymmetric(EAlgorithmType type) {
		if (type.getAlgorithm().equals("Shift Cipher") || type.getAlgorithm().equals("Substitution Cipher")
				|| type.getAlgorithm().equals("Affine") || type.getAlgorithm().equals("Vingenere Cipher")
				|| type.getAlgorithm().equals("Hill Cipher") || type.getAlgorithm().equals("Permutation Cipher"))
			return true;
		return false;
	}

	public static boolean isModernSymmetric(EAlgorithmType type) {
		if (type.getAlgorithm().equals("AES") || type.getAlgorithm().equals("DES")
				|| type.getAlgorithm().equals("DESede") || type.getAlgorithm().equals("Blowfish")
				|| type.getAlgorithm().equals("RC4") || type.getAlgorithm().equals("ChaCha20"))
			return true;
		return false;
	}

	public static boolean isAsymmetricRSA(EAlgorithmType type) {
		if (type.getAlgorithm().equals("RSA"))
			return true;
		return false;
	}

	public static boolean isAsymmetricRSA_AES(EAlgorithmType type) {
		if (type.getAlgorithm().equals("RSA_AES"))
			return true;
		return false;
	}

	public static boolean isSymmetricChaCha(EAlgorithmType type) {
		if (type.getAlgorithm().equals("ChaCha20"))
			return true;
		return false;
	}

	public static boolean isFileEncryption(EAlgorithmType type) {
		if (type == EAlgorithmType.RSA)
			return false;
		return true;
	}

	int getKeySize();

	void setKeySize(EKeySize keySize) throws Exception;

	void genKey() throws Exception;

	boolean saveKeyToFile(String filePath) throws Exception;

	boolean loadKeyFromFile(String filePath) throws Exception;

	void loadKey(Object keyObj) throws Exception;

	byte[] encrypt(String text) throws Exception;

	String decrypt(byte[] data) throws Exception;

	boolean encryptFile(String srcFilePath, String desFilePath) throws Exception;

	boolean decryptFile(String srcFilePath, String desFilePath) throws Exception;

	public default boolean encryptFolder(String srcFolderPath, String desFolderPath) throws Exception {
		File srcFolder = new File(srcFolderPath);
		File desFolder = new File(desFolderPath);

		if (!desFolder.exists()) {
			desFolder.mkdirs();
		}

		File[] files = srcFolder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					String desFilePath = desFolderPath + File.separator + file.getName();
					boolean success = encryptFile(file.getAbsolutePath(), desFilePath);
					if (!success) {
						System.out.println("Mã hóa tệp " + file.getName() + " không thành công.");
					}
				} else if (file.isDirectory()) {
					String newDesFolderPath = desFolderPath + File.separator + file.getName();
					encryptFolder(file.getAbsolutePath(), newDesFolderPath);
				}
			}
		}
		return true;
	}

	public default boolean decryptFolder(String srcFolderPath, String desFolderPath) throws Exception {
		File srcFolder = new File(srcFolderPath);
		File desFolder = new File(desFolderPath);

		if (!desFolder.exists()) {
			desFolder.mkdirs();
		}

		File[] files = srcFolder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					String desFilePath = desFolderPath + File.separator + file.getName();
					boolean success = decryptFile(file.getAbsolutePath(), desFilePath);
					if (!success) {
						System.out.println("Giải mã tệp " + file.getName() + " không thành công.");
					}
				} else if (file.isDirectory()) {
					String newDesFolderPath = desFolderPath + File.separator + file.getName();
					decryptFolder(file.getAbsolutePath(), newDesFolderPath);
				}
			}
		}
		return true;
	}
}
