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

import org.bouncycastle.crypto.engines.CAST5Engine;
import org.bouncycastle.crypto.engines.CamelliaEngine;
import org.bouncycastle.crypto.engines.SerpentEngine;
import org.bouncycastle.crypto.engines.TwofishEngine;

import model.asymmetric.RSA_AES;

public interface ICryptoAlgorithm {
	public static IvParameterSpec generateIV(int ivBytesLength) {
		byte[] ivBytes = new byte[ivBytesLength];
		new SecureRandom().nextBytes(ivBytes);
		return new IvParameterSpec(ivBytes);
	}

	public static String encryptBase64(byte[] text) {
		return Base64.getEncoder().encodeToString(text);
	}

	public static String decryptBase64(byte[] text) {
		return new String(Base64.getDecoder().decode(text), StandardCharsets.UTF_8);
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
		if (type == EAlgorithmType.Shift_Cipher || type == EAlgorithmType.Substitution_Cipher
				|| type == EAlgorithmType.Affine_Cipher || type == EAlgorithmType.Vigenere_Cipher
				|| type == EAlgorithmType.Hill_Cipher || type == EAlgorithmType.Permutation_Cipher)
			return true;
		return false;
	}

	public static boolean isBouncyCastleSymmetric(EAlgorithmType type) {
		if (type == EAlgorithmType.Twofish || type == EAlgorithmType.Serpent || type == EAlgorithmType.CAST
				|| type == EAlgorithmType.Camellia)
			return true;
		return false;
	}

	public static boolean isModernSymmetric(EAlgorithmType type) {
		if (type == EAlgorithmType.AES || type == EAlgorithmType.DES || type == EAlgorithmType.TripleDES
				|| type == EAlgorithmType.Blowfish || type == EAlgorithmType.RC4 || type == EAlgorithmType.ChaCha20)
			return true;
		return false;
	}

	public static boolean isAsymmetricRSA(EAlgorithmType type) {
		if (type == EAlgorithmType.RSA)
			return true;
		return false;
	}

	public static boolean isAsymmetricRSA_AES(EAlgorithmType type) {
		if (type == EAlgorithmType.RSA_AES)
			return true;
		return false;
	}

	public static boolean isSymmetricChaCha(EAlgorithmType type) {
		if (type == EAlgorithmType.ChaCha20)
			return true;
		return false;
	}

	public static boolean isFileEncryption(EAlgorithmType type) {
		if (type == null || type == EAlgorithmType.RSA)
			return false;
		return true;
	}

	int getKeySize();

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

	public boolean setMode(EModes mode);

	public boolean setPadding(EPadding padding);

	public String info();
}
