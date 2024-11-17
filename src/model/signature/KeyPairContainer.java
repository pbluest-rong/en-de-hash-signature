package model.signature;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyPairContainer implements Serializable {
	private static final long serialVersionUID = 1L;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private String signature; // Thêm trường để lưu chữ ký

	public KeyPairContainer(PublicKey publicKey, PrivateKey privateKey, String signature) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.signature = signature;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public String getSignature() {
		return signature;
	}

	public static void saveKeyPairAndSignature(String filePath, PublicKey publicKey, PrivateKey privateKey,
			String signature) throws Exception {
		KeyPairContainer keyPairContainer = new KeyPairContainer(publicKey, privateKey, signature);
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
			oos.writeObject(keyPairContainer);
		}
	}

	public static KeyPairContainer loadKeyPairAndSignature(String filePath) throws Exception {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
			return (KeyPairContainer) ois.readObject();
		}
	}
}
