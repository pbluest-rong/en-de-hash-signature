package model.modern_symmetric;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.ChaCha20ParameterSpec;
import javax.crypto.spec.IvParameterSpec;

import model.EAlgorithmType;
import model.ICryptoAlgorithm;
import model.EKeySize;
import model.EModes;
import model.EPadding;

/**
 * @author Ba Phung Le Mã hóa đối xứng hiện đại - Xử lý với các giải thuật java
 *         hỗ trợ - Cơ chế phát sinh khóa nếu user chưa có - Cơ chế load khóa
 *         nếu user đã có - Cơ chế hỗ trợ mod, padding - Hỗ trợ tất cả các kích
 *         thước - Tim kiếm thư viện thứ 3 hỗ trợ khi java không có
 */
public class ModernSymmetricEncryption implements ICryptoAlgorithm {
	public SecretKey key;
	private EKeySize keySize;
	private EAlgorithmType algorithmType;
	private EModes mode;
	private EPadding padding;
	private IvParameterSpec iv;

	public ModernSymmetricEncryption(EAlgorithmType algorithmType, EKeySize keySize, EModes mode, EPadding padding) {
		this.keySize = keySize;
		this.algorithmType = algorithmType;
		this.mode = mode;
		this.padding = padding;
		Integer ivBytes = this.algorithmType.getIvLength();
		this.iv = ivBytes == null ? null : ICryptoAlgorithm.generateIV(ivBytes);
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
	public void loadKey(Object key) throws Exception {
		if (key instanceof SecretKey) {
			this.key = (SecretKey) key;
		} else {
			throw new IllegalArgumentException("Invalid key type for AES");
		}
	}

	@Override
	public byte[] encrypt(String text) throws Exception {
		byte[] data = text.getBytes(StandardCharsets.UTF_8);
		Cipher cipher = Cipher.getInstance(algorithmType.getCipherInstanceString(mode, padding));
		if (ICryptoAlgorithm.isSymmetricChaCha(this.algorithmType)) {
			byte[] ivBytes = new byte[12];
			SecureRandom random = new SecureRandom();
			random.nextBytes(ivBytes);
			ChaCha20ParameterSpec iv = new ChaCha20ParameterSpec(ivBytes, 0);
			cipher.init(Cipher.ENCRYPT_MODE, this.key, iv);

			byte[] encryptedData = cipher.doFinal(data);

			// Kết hợp IV và dữ liệu đã mã hóa thành một mảng duy nhất
			byte[] result = new byte[ivBytes.length + encryptedData.length];
			System.arraycopy(ivBytes, 0, result, 0, ivBytes.length);
			System.arraycopy(encryptedData, 0, result, ivBytes.length, encryptedData.length);
			return result;
		} else {
			if (EAlgorithmType.requiresIV(mode, algorithmType)) {
				if (iv == null && algorithmType.getIvLength() != null) {
					iv = ICryptoAlgorithm.generateIV(algorithmType.getIvLength());
				}
				cipher.init(Cipher.ENCRYPT_MODE, this.key, iv);

			} else
				cipher.init(Cipher.ENCRYPT_MODE, this.key);

			return cipher.doFinal(data);
		}
	}

	@Override
	public String decrypt(byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithmType.getCipherInstanceString(mode, padding));

		if (ICryptoAlgorithm.isSymmetricChaCha(this.algorithmType)) {
			// Tách IV từ dữ liệu đầu vào
			byte[] ivBytes = new byte[12];
			System.arraycopy(data, 0, ivBytes, 0, ivBytes.length);
			ChaCha20ParameterSpec iv = new ChaCha20ParameterSpec(ivBytes, 0);

			// Dữ liệu thực tế cần giải mã (bỏ qua phần IV)
			byte[] encryptedData = new byte[data.length - ivBytes.length];
			System.arraycopy(data, ivBytes.length, encryptedData, 0, encryptedData.length);

			cipher.init(Cipher.DECRYPT_MODE, this.key, iv);
			return new String(cipher.doFinal(encryptedData), StandardCharsets.UTF_8);
		} else {
			if (EAlgorithmType.requiresIV(mode, algorithmType) && iv != null) {
				cipher.init(Cipher.DECRYPT_MODE, this.key, iv);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, this.key);
			}

			byte[] decryptedData = cipher.doFinal(data);

			return new String(decryptedData, StandardCharsets.UTF_8);
		}
	}

	@Override
	public boolean encryptFile(String srcFilePath, String desFilePath) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithmType.getCipherInstanceString(mode, padding));

		if (ICryptoAlgorithm.isSymmetricChaCha(algorithmType)) {
			return chachaEncryptFile(srcFilePath, desFilePath);
		} else {
			// Tạo IV mới nếu cần
			if (EAlgorithmType.requiresIV(mode, algorithmType)) {
				if (iv == null && algorithmType.getIvLength() != null)
					iv = ICryptoAlgorithm.generateIV(algorithmType.getIvLength());

				cipher.init(Cipher.ENCRYPT_MODE, this.key, iv);

				try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFilePath));
						BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desFilePath))) {

					// Lưu IV vào đầu tệp
					out.write(iv.getIV());

					try (CipherInputStream cipherIn = new CipherInputStream(in, cipher)) {
						byte[] buffer = new byte[1024];
						int bytesRead;
						while ((bytesRead = cipherIn.read(buffer)) != -1) {
							out.write(buffer, 0, bytesRead);
						}
						out.flush();
					}
				}
			} else {
				cipher.init(Cipher.ENCRYPT_MODE, this.key);
				try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFilePath));
						BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desFilePath));
						CipherInputStream cipherIn = new CipherInputStream(in, cipher)) {

					byte[] buffer = new byte[1024];
					int bytesRead;
					while ((bytesRead = cipherIn.read(buffer)) != -1) {
						out.write(buffer, 0, bytesRead);
					}
					out.flush();
				}
			}
			return true;
		}
	}

	@Override
	public boolean decryptFile(String srcFilePath, String desFilePath) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithmType.getCipherInstanceString(mode, padding));

		if (ICryptoAlgorithm.isSymmetricChaCha(algorithmType)) {
			return chachaDecryptFile(srcFilePath, desFilePath);
		} else {
			try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFilePath));
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desFilePath))) {
				if (EAlgorithmType.requiresIV(mode, algorithmType)) {
					// Đọc IV từ đầu tệp
					byte[] ivBytes = new byte[algorithmType.getIvLength()];
					in.read(ivBytes); // Đọc IV
					IvParameterSpec iv = new IvParameterSpec(ivBytes);

					// Khởi tạo cipher với IV đọc từ tệp
					cipher.init(Cipher.DECRYPT_MODE, this.key, iv);

					try (CipherInputStream cipherIn = new CipherInputStream(in, cipher)) {
						byte[] buffer = new byte[1024];
						int bytesRead;
						while ((bytesRead = cipherIn.read(buffer)) != -1) {
							out.write(buffer, 0, bytesRead);
						}
						out.flush();
					}
				} else {
					cipher.init(Cipher.DECRYPT_MODE, this.key);
					CipherInputStream cipherIn = new CipherInputStream(in, cipher);

					byte[] buffer = new byte[1024];
					int bytesRead;
					while ((bytesRead = cipherIn.read(buffer)) != -1) {
						out.write(buffer, 0, bytesRead);
					}
					out.flush();
				}
			}
			return true;
		}

	}

	public boolean chachaEncryptFile(String srcFilePath, String desFilePath) throws Exception {
		// Generate a new IV for each encryption operation
		byte[] ivBytes = new byte[12];
		SecureRandom random = new SecureRandom();
		random.nextBytes(ivBytes);
		ChaCha20ParameterSpec iv = new ChaCha20ParameterSpec(ivBytes, 0);

		Cipher cipher = Cipher.getInstance(algorithmType.getCipherInstanceString(mode, padding));
		cipher.init(Cipher.ENCRYPT_MODE, this.key, iv);

		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFilePath));
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desFilePath))) {
			out.write(ivBytes);

			try (CipherInputStream en = new CipherInputStream(in, cipher)) {
				byte[] buffer = new byte[1024];
				int len;
				while ((len = en.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
			}
		}
		return true;
	}

	public boolean chachaDecryptFile(String srcFilePath, String desFilePath) throws Exception {
		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFilePath));
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desFilePath))) {
			byte[] ivBytes = new byte[12];

			in.read(ivBytes);
			ChaCha20ParameterSpec iv = new ChaCha20ParameterSpec(ivBytes, 0);

			Cipher cipher = Cipher.getInstance(algorithmType.getCipherInstanceString(mode, padding));
			cipher.init(Cipher.DECRYPT_MODE, this.key, iv);

			try (CipherInputStream de = new CipherInputStream(in, cipher)) {
				byte[] buffer = new byte[1024];
				int len;
				while ((len = de.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
			}
		}
		return true;
	}

	@Override
	public boolean saveKeyToFile(String filePath) throws Exception {
		ICryptoAlgorithm.saveSecretKey(filePath, this.key);
		return true;
	}

	@Override
	public boolean loadKeyFromFile(String filePath) throws Exception {
		key = ICryptoAlgorithm.loadSecretKey(filePath);
		return true;
	}

	@Override
	public int getKeySize() {
		return key.getEncoded().length * 8;
	}

	@Override
	public String info() {
		return "Secret Key: " + this.key + "\n" + "Key Size: " + keySize + "\n" + "Mode: " + mode + "\n" + "IV"
				+ (iv != null);
	}

	@Override
	public boolean setMode(EModes mode) {
		if (EModes.getSupportedModes(this.algorithmType).contains(mode)) {
			this.mode = mode;
			return true;
		}
		return false;
	}

	@Override
	public boolean setPadding(EPadding padding) {
		if (EPadding.getSupportedPadding(algorithmType, mode).contains(padding)) {
			this.padding = padding;
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		String srcFilePath = "resources/input/image_input.jpg";
		String desFilePathEn = "resources/encrypt/image_output.jpg";
		String key = "resources/key.data";
		String desFilePathDe = "resources/decrypt/image_output.jpg";

		ModernSymmetricEncryption modernSymmetric = new ModernSymmetricEncryption(EAlgorithmType.AES, EKeySize.AES_256,
				EModes.CBC, EPadding.getSupportedPadding(EAlgorithmType.AES, EModes.CBC).get(1));
		modernSymmetric.genKey();
		byte[] ciphertext = modernSymmetric.encrypt("Pblues");
		System.out.println(ICryptoAlgorithm.encryptBase64(ciphertext));
		System.out.println(modernSymmetric.decrypt(ciphertext));
//		modernSymmetric.encryptFile(srcFilePath, desFilePathEn);
//		modernSymmetric.saveKeyToFile(key);
//
//		modernSymmetric.loadKeyFromFile(key);
//		modernSymmetric.decryptFile(desFilePathEn, desFilePathDe);
	}

}