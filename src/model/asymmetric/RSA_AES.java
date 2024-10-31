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

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import model.CryptoAlgorithm;

public class RSA_AES implements CryptoAlgorithm {
	SecretKey secretKey;
	KeyPair keyPair;
	PublicKey publicKey;
	PrivateKey privateKey;
	IvParameterSpec iv;
	byte[] encryptedAESKey;

	@Override
	public void genKey() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		this.keyPair = generator.generateKeyPair();
		this.publicKey = this.keyPair.getPublic();
		this.privateKey = this.keyPair.getPrivate();

		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128);// 128 bit (16 byte), 192 bit (24 byte), hoặc 256 bit (32 byte).
		this.secretKey = keyGen.generateKey();

		this.iv = CryptoAlgorithm.generateIV();

		// Mã hóa khóa AES bằng RSA và lưu vào encryptedAESKey
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		this.encryptedAESKey = cipher.doFinal(secretKey.getEncoded());
	}

	@Override
	public void loadKey(Key key) throws Exception {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String decrypt(byte[] data) throws Exception {
		// TODO Auto-generated method stub
		return null;
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
			return false;
		}
	}

	@Override
	public boolean decryptFile(String srcFilePath, String desFilePath) throws Exception {
		// Đọc encryptedAESKey từ file
		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFilePath));
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desFilePath))) {

			// Đọc khóa AES đã mã hóa (256 byte đối với RSA 2048)
			byte[] encryptedAESKey = new byte[256];
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

	public static void main(String[] args) {
		try {
			RSA_AES rsa_aes = new RSA_AES();
			rsa_aes.genKey();

			String originalFilePath = "resources/input/originalFile.txt";
			String encryptedFilePath = "resources/encrypt/encryptedFile.dat";
			String decryptedFilePath = "resources/decrypt/decryptedFile.txt";

			 // Ghi nội dung mẫu vào file gốc
            String content = "Đây là nội dung mẫu để kiểm tra mã hóa kết hợp RSA và AES.";
            try (FileOutputStream fos = new FileOutputStream(originalFilePath)) {
                fos.write(content.getBytes());
            }

            // Mã hóa file gốc
            boolean isEncrypted = rsa_aes.encryptFile(originalFilePath, encryptedFilePath);
            if (isEncrypted) {
                System.out.println("Mã hóa thành công.");
            } else {
                System.out.println("Mã hóa thất bại.");
                return;
            }
            
         // Giải mã file đã mã hóa
            boolean isDecrypted = rsa_aes.decryptFile(encryptedFilePath, decryptedFilePath);
            if (isDecrypted) {
                System.out.println("Giải mã thành công.");
            } else {
                System.out.println("Giải mã thất bại.");
                return;
            }
		} catch (Exception e) {
			System.out.println("Oops!");
		}
	}
}
