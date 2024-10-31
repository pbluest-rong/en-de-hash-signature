package model.symmetric;

import java.awt.RenderingHints.Key;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import model.CryptoAlgorithm;
import model.KeySize;
/**
 * @author Ba Phung Le
 * Mã hóa đối xứng hiện đại
    - Xử lý với các giải thuật java hỗ trợ
    - Cơ chế phát sinh khóa nếu user chưa có
    - Cơ chế load khóa nếu user đã có
    - Cơ chế hỗ trợ mod, padding
    - Hỗ trợ tất cả các kích thước
    - Tim kiếm thư viện thứ 3 hỗ trợ khi java không có
 */
public class ModernSymmetricEncryption implements CryptoAlgorithm{
	private SecretKey key;
	private KeySize keySize;
	private AlgorithmType algorithmType;
	
	public ModernSymmetricEncryption(AlgorithmType algorithmType, KeySize keySize) {
		this.keySize = keySize;
		this.algorithmType = algorithmType;
	}
	@Override
	public void genKey() throws Exception {
		KeyGenerator keyGen = KeyGenerator.getInstance(algorithmType.getAlgorithm());
		
		try {
			keyGen.init(keySize.getBits());
			this.key = keyGen.generateKey();
		} catch (InvalidParameterException e) {
			throw new Exception("Oops! Key size does not match the selected algorithm.");
		}
	}

	@Override
	 public void loadKey(Key key) throws Exception {
        if (key instanceof SecretKey) {
            this.key = (SecretKey) key;
        } else {
            throw new IllegalArgumentException("Invalid key type for AES");
        }
    }

	@Override
	public byte[] encrypt(String text) throws Exception {
		byte[] data = text.getBytes(StandardCharsets.UTF_8);
		Cipher cipher = Cipher.getInstance(algorithmType.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, this.key);
		return cipher.doFinal(data);
	}

	@Override
	public String decrypt(byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithmType.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, this.key);
		return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
	}

	@Override
	public boolean encryptFile(String srcFilePath, String desFilePath) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithmType.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, this.key);
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFilePath));
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desFilePath));

		CipherInputStream en = new CipherInputStream(in, cipher);
		int i;
		byte[] bytes = new byte[1024];
		while ((i = en.read(bytes)) != -1) {
			out.write(bytes, 0, i);
		}

		bytes = cipher.doFinal();
		if (bytes != null) {
			out.write(bytes);
		}
		out.flush();
		out.close();
		en.close();
		return true;
	}

	@Override
	public boolean decryptFile(String srcFilePath, String desFilePath) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithmType.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, this.key);
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFilePath));
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desFilePath));

		CipherInputStream de = new CipherInputStream(in, cipher);
		int i;
		byte[] bytes = new byte[1024];
		while ((i = de.read(bytes)) != -1) {
			out.write(bytes, 0, i);
		}

		bytes = cipher.doFinal();
		if (bytes != null) {
			out.write(bytes);
		}
		out.flush();
		out.close();
		de.close();
		return true;
	}
	 public static void main(String[] args) throws Exception {
	        String srcFilePath = "resources/input/image_input.jpg";
	        String desFilePathEn = "resources/encrypt/image_output.jpg";
	        String desFilePathDe = "resources/decrypt/image_output.jpg";

	        ModernSymmetricEncryption modernSymmetric = new ModernSymmetricEncryption(AlgorithmType.RC4, KeySize.AES_256);
	        modernSymmetric.genKey();
	        modernSymmetric.encryptFile(srcFilePath, desFilePathEn);
	        modernSymmetric.decryptFile(desFilePathEn, desFilePathDe);
	    }

}
