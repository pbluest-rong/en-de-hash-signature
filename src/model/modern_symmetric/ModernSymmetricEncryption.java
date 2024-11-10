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

	public ModernSymmetricEncryption(EAlgorithmType algorithmType, EKeySize keySize) {
		this.keySize = keySize;
		this.algorithmType = algorithmType;
	}

	@Override
	public void setKeySize(EKeySize keySize) throws Exception {
		this.setKeySize(keySize);
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
		/**
		 * Padding:
		 * + PKCS5Padding: phổ biến nhất, kích thước của khối là 8 byte (64 bits)
		 * + PKCS7Padding: tương tự nhưng áp dụng cho bất kỳ kích thước khối nào
		 * + NoPadding:: chỉ nên dùng nếu bạn chắc chắn rằng dữ liệu đầu vào đã có kích thước phù hợp
		 * + ISO10126Padding: không phổ biến, các byte đệm có giá trị ngẫu nhiên, ngoại từ byte cuối cùng 
		 * + ZeroPadding: Đệm thêm các byte có giá trị 0x00 cho đến khi đủ khối cuối.,Phù hợp với dữ liệu chuỗi
		 * + ISO7816-4Padding: Thêm một byte 0x80 và sau đó là các byte 0x00 cho đến hết khối.
		 * + ISO10126Padding
		 */
		Cipher cipher = Cipher.getInstance(algorithmType.getCipherInstanceString());
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
		} else
			cipher.init(Cipher.ENCRYPT_MODE, this.key);
		return cipher.doFinal(data);
	}

	@Override
	public String decrypt(byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithmType.getCipherInstanceString());
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
		} else
			cipher.init(Cipher.DECRYPT_MODE, this.key);
		return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
	}

	@Override
	public boolean encryptFile(String srcFilePath, String desFilePath) throws Exception {
		if (ICryptoAlgorithm.isSymmetricChaCha(algorithmType)) {
			return chachaEncryptFile(srcFilePath, desFilePath);
		} else {
			Cipher cipher = Cipher.getInstance(algorithmType.getCipherInstanceString());
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
			return true;
		}
	}

	@Override
	public boolean decryptFile(String srcFilePath, String desFilePath) throws Exception {
		if (ICryptoAlgorithm.isSymmetricChaCha(algorithmType)) {
			return chachaDecryptFile(srcFilePath, desFilePath);
		} else {
			Cipher cipher = Cipher.getInstance(algorithmType.getCipherInstanceString());
			cipher.init(Cipher.DECRYPT_MODE, this.key);

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
			return true;
		}
	}

	public boolean chachaEncryptFile(String srcFilePath, String desFilePath) throws Exception {
		// Generate a new IV for each encryption operation
		byte[] ivBytes = new byte[12];
		SecureRandom random = new SecureRandom();
		random.nextBytes(ivBytes);
		ChaCha20ParameterSpec iv = new ChaCha20ParameterSpec(ivBytes, 0);

		Cipher cipher = Cipher.getInstance(algorithmType.getCipherInstanceString());
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

			Cipher cipher = Cipher.getInstance(algorithmType.getCipherInstanceString());
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
		this.key = ICryptoAlgorithm.loadSecretKey(filePath);
		return true;
	}

	@Override
	public int getKeySize() {
		return key.getEncoded().length * 8;
	}

	public static void main(String[] args) throws Exception {
		String srcFilePath = "resources/input/image_input.jpg";
		String desFilePathEn = "resources/encrypt/image_output.jpg";
		String desFilePathDe = "resources/decrypt/image_output.jpg";

		ModernSymmetricEncryption modernSymmetric = new ModernSymmetricEncryption(EAlgorithmType.RC4, EKeySize.AES_256);
		modernSymmetric.genKey();
		byte[] ciphertext = modernSymmetric.encrypt("Pblues");
		System.out.println(ICryptoAlgorithm.encryptBase64(ciphertext));
		System.out.println(modernSymmetric.decrypt(ciphertext));
//		modernSymmetric.encryptFile(srcFilePath, desFilePathEn);
//		modernSymmetric.decryptFile(desFilePathEn, desFilePathDe);
	}

}
