package model.signature;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

import javax.crypto.SecretKey;

import model.ICryptoAlgorithm;

public class DS {
	KeyPair keyPair;
	SecureRandom secureRandom;
	Signature signature;
	public PublicKey publicKey;
	public PrivateKey privateKey;

	public DS() {
	}

	public DS(String alg, String algRandom, String prov) throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(alg, prov);
		secureRandom = SecureRandom.getInstance(algRandom, prov);
		keyPairGenerator.initialize(1024, secureRandom);
		keyPair = keyPairGenerator.generateKeyPair();
		signature = Signature.getInstance(alg, prov);
	}

	public DS(String alg, String prov, PublicKey publicKey, PrivateKey privateKey) throws Exception {
		signature = Signature.getInstance(alg, prov);
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	public boolean genKey() {
		if (keyPair == null) {
			if (publicKey != null && privateKey != null)
				return true;
			return false;
		}
		publicKey = keyPair.getPublic();
		privateKey = keyPair.getPrivate();
		return true;
	}

	public boolean verify(String message, String sign) throws InvalidKeyException, SignatureException {
		signature.initVerify(publicKey);
		byte[] data = message.getBytes();
		byte[] signValue = Base64.getDecoder().decode(sign);
		signature.update(data);
		return signature.verify(signValue);
	}

	public boolean verifyFile(String path, String sign) throws Exception {
		signature.initVerify(publicKey);
		byte[] signValue = Base64.getDecoder().decode(sign);

		signature.initVerify(publicKey);

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
		byte[] buff = new byte[1024];
		int read;
		while ((read = bis.read(buff)) != -1) {
			signature.update(buff, 0, read);
		}
		bis.close();

		return signature.verify(signValue);
	}

	public String sign(String message) throws InvalidKeyException, SignatureException {
		byte[] data = message.getBytes();
		signature.initSign(privateKey);
		signature.update(data);
		byte[] sign = signature.sign();
		return Base64.getEncoder().encodeToString(sign);
	}

	public String signFile(String srcFilePath) throws InvalidKeyException, SignatureException, IOException {
		signature.initSign(privateKey);
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFilePath));
		byte[] buff = new byte[1024];
		int read;
		while ((read = bis.read(buff)) != -1) {
			signature.update(buff, 0, read);
		}
		bis.close();
		byte[] sign = signature.sign();
		return Base64.getEncoder().encodeToString(sign);
	}

	public static void saveSignatureToFile(String signature, String filePath) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(signature);
			System.out.println("Signature saved to file: " + filePath);
		} catch (IOException e) {
			System.err.println("Error saving signature to file: " + e.getMessage());
		}
	}

	public static String loadSignatureFromFile(String filePath) {
		StringBuilder signature = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				signature.append(line);
			}
			System.out.println("Signature loaded from file: " + filePath);
		} catch (IOException e) {
			System.err.println("Error loading signature from file: " + e.getMessage());
		}
		return signature.toString();
	}

	public static void main(String[] args) throws Exception {
		DS ds = new DS("DSA", "SHA1PRNG", "SUN");
		ds.genKey();

		// Tạo chữ ký
		String message = "pham duc dai";
		String sign = ds.sign(message);

		// Lưu cặp khóa và chữ ký vào tệp
		String filePath = "resources/keys/keypair_with_signature.data";
		KeyPairContainer.saveKeyPairAndSignature(filePath, ds.publicKey, ds.privateKey, sign);

		// Load lại từ tệp
		KeyPairContainer keyPairContainer = KeyPairContainer.loadKeyPairAndSignature(filePath);
		ds = new DS("DSA", "SUN", keyPairContainer.getPublicKey(), keyPairContainer.getPrivateKey());

		// Xác thực chữ ký
		System.out.println("Loaded Signature: " + keyPairContainer.getSignature());
		System.out.println("Verification: " + ds.verify(message, keyPairContainer.getSignature()));
	}

}