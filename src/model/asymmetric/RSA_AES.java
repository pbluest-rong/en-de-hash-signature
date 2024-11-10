package model.asymmetric;

import java.awt.RenderingHints.Key;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import model.ICryptoAlgorithm;
import model.EKeySize;

public class RSA_AES implements ICryptoAlgorithm {
	KeyPair keyPair;
	public SecretKey secretKey;
	PublicKey publicKey;
	public PrivateKey privateKey;
	private EKeySize keySize;
	
	IvParameterSpec iv;
	byte[] encryptedAESKey;

	public RSA_AES(EKeySize keySize) {
		this.keySize = keySize;
	}

	@Override
	public void genKey() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(this.keySize.getBits());
		this.keyPair = generator.generateKeyPair();
		this.publicKey = this.keyPair.getPublic();
		this.privateKey = this.keyPair.getPrivate();

		KeyGenerator keyGen = KeyGenerator.getInstance("AES");

		switch (this.keySize) {
		case RSA_1024:
			keyGen.init(128);
			break;
		case RSA_2048:
			keyGen.init(256);
			break;
		case RSA_3072:
			keyGen.init(256);
			break;
		case RSA_4096:
			keyGen.init(256);
			break;
		default:
			throw new IllegalArgumentException("Unsupported key size.");
		}

		this.secretKey = keyGen.generateKey();

		this.iv = ICryptoAlgorithm.generateIV();

		// Mã hóa khóa AES bằng RSA và lưu vào encryptedAESKey
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		this.encryptedAESKey = cipher.doFinal(secretKey.getEncoded());
	}

	@Override
	public void loadKey(Object key) throws Exception {
		if (key instanceof PublicKey) {
			this.publicKey = (PublicKey) key;
		} else if (key instanceof PrivateKey) {
			this.privateKey = (PrivateKey) key;
		} else if (key instanceof SecretKey) {
			this.secretKey = (SecretKey) key;
		} else {
			throw new IllegalArgumentException("Invalid key type for RSA combined AES ");
		}
	}

	@Override
	public byte[] encrypt(String text) throws Exception {
		byte[] plainBytes = text.getBytes("UTF-8");
		Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		aesCipher.init(Cipher.ENCRYPT_MODE, this.secretKey, iv);
		return aesCipher.doFinal(plainBytes);
	}

	@Override
	public String decrypt(byte[] data) throws Exception {
		Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		aesCipher.init(Cipher.DECRYPT_MODE, this.secretKey, iv);
		byte[] decryptedBytes = aesCipher.doFinal(data);
		return new String(decryptedBytes, "UTF-8");
	}

	@Override
	public boolean encryptFile(String srcFilePath, String desFilePath) throws Exception {
		// encrypt secretkey
		Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		aesCipher.init(Cipher.ENCRYPT_MODE, this.secretKey, iv);

		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFilePath));
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desFilePath));
				CipherOutputStream cipherOut = new CipherOutputStream(out, aesCipher)) {

			// Ghi encryptedAESKey (đã mã hóa bằng RSA) vào đầu file
			out.write(encryptedAESKey);
			out.write(iv.getIV()); // Ghi IV vào file để giải mã

			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				cipherOut.write(buffer, 0, bytesRead);
			}
			cipherOut.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean decryptFile(String srcFilePath, String desFilePath) throws Exception {
		// Đọc encryptedAESKey từ file
		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFilePath));
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desFilePath))) {

			// Đọc khóa AES đã mã hóa:
			// AES 128 - RSA 1024
			// AES 256 - RSA 2048
			// AES 384 - RSA 3072
			// AES 512 - RSA 4096
			int aesKeyByte = 128;
			if (this.keySize == EKeySize.RSA_2048)
				aesKeyByte = 256;
			if (this.keySize == EKeySize.RSA_3072)
				aesKeyByte = 384;
			if (this.keySize == EKeySize.RSA_4096)
				aesKeyByte = 512;

			byte[] encryptedAESKey = new byte[aesKeyByte]; // Thay đổi kích thước
			in.read(encryptedAESKey);

			// Giải mã khóa AES bằng khóa private
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] aesKeyBytes = rsaCipher.doFinal(encryptedAESKey);
			this.secretKey = new javax.crypto.spec.SecretKeySpec(aesKeyBytes, "AES");

			// Đọc IV từ file
			byte[] ivBytes = new byte[16];
			in.read(ivBytes);
			this.iv = new IvParameterSpec(ivBytes);

			// Giải mã nội dung file với khóa AES
			Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			aesCipher.init(Cipher.DECRYPT_MODE, this.secretKey, iv);

			try (CipherInputStream cipherIn = new CipherInputStream(in, aesCipher)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = cipherIn.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
				out.flush();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void setKeySize(EKeySize keySize) throws Exception {
		this.setKeySize(keySize);
	}

	@Override
	public boolean saveKeyToFile(String filePath) throws Exception {
		ICryptoAlgorithm.savePrivateKey(filePath, privateKey);
		return false;
	}

	@Override
	public boolean loadKeyFromFile(String filePath) throws Exception {
		this.privateKey = ICryptoAlgorithm.loadPrivateKey(filePath);
		return true;
	}

	@Override
	public int getKeySize() {
		return ((RSAPrivateKey) privateKey).getModulus().bitLength();
	}

	public static void main(String[] args) {
		try {
			RSA_AES rsa_aes = new RSA_AES(EKeySize.RSA_3072);
			rsa_aes.genKey();

			byte[] ciphertext = rsa_aes.encrypt("Pblues");
			System.out.println(ICryptoAlgorithm.encryptBase64(ciphertext));
			System.out.println(rsa_aes.decrypt(ciphertext));

//
//			String originalFilePath = "resources/input/originalFile.txt";
//			String encryptedFilePath = "resources/encrypt/encryptedFile.dat";
//			String decryptedFilePath = "resources/decrypt/decryptedFile.txt";
//
//			// Ghi nội dung mẫu vào file gốc
//			String content = "Đây là nội dung mẫu để kiểm tra mã hóa kết hợp RSA và AES.";
//			try (FileOutputStream fos = new FileOutputStream(originalFilePath)) {
//				fos.write(content.getBytes());
//			}
//
//			// Mã hóa file gốc
//			boolean isEncrypted = rsa_aes.encryptFile(originalFilePath, encryptedFilePath);
//			if (isEncrypted) {
//				System.out.println("Mã hóa thành công.");
//			} else {
//				System.out.println("Mã hóa thất bại.");
//				return;
//			}
//
//			// Giải mã file đã mã hóa
//			boolean isDecrypted = rsa_aes.decryptFile(encryptedFilePath, decryptedFilePath);
//			if (isDecrypted) {
//				System.out.println("Giải mã thành công.");
//			} else {
//				System.out.println("Giải mã thất bại.");
//				return;
//			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Oops!");
		}
	}
}
