package model.modern_symmetric;

import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.*;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Base64;

import model.EAlgorithmType;
import model.EKeySize;
import model.ICryptoAlgorithm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;

public class BouncyCastleSymmetricEncryption implements ICryptoAlgorithm {
	private SecureRandom secureRandom = new SecureRandom();
	private EKeySize keySize;
	private EAlgorithmType algorithmType;
	private byte[] key;

	public BouncyCastleSymmetricEncryption(EAlgorithmType algorithmType, EKeySize keySize) {
		this.algorithmType = algorithmType;
		this.keySize = keySize;
	}

	// Mã hóa dữ liệu
	@Override
	public byte[] encrypt(String text) throws Exception {
		byte[] data = text.getBytes(StandardCharsets.UTF_8);
		return encryptData(data);
	}

	// Giải mã dữ liệu
	@Override
	public String decrypt(byte[] data) throws Exception {
		byte[] decryptedData = decryptData(data);
		return new String(decryptedData, StandardCharsets.UTF_8);
	}

	// Mã hóa dữ liệu với thuật toán Bouncy Castle
	private byte[] encryptData(byte[] data) throws Exception {
		org.bouncycastle.crypto.BlockCipher cipher = getCipherInstance();
		byte[] iv = new byte[ICryptoAlgorithm.BLOCK_SIZE];
		secureRandom.nextBytes(iv);
		PaddedBufferedBlockCipher cipherInstance = new PaddedBufferedBlockCipher(new CBCBlockCipher(cipher));
		cipherInstance.init(true, new ParametersWithIV(new KeyParameter(key), iv));

		byte[] output = new byte[cipherInstance.getOutputSize(data.length)];
		int outputLength = cipherInstance.processBytes(data, 0, data.length, output, 0);
		cipherInstance.doFinal(output, outputLength);

		byte[] result = new byte[iv.length + output.length];
		System.arraycopy(iv, 0, result, 0, iv.length);
		System.arraycopy(output, 0, result, iv.length, output.length);
		return result; // trả về kết quả mã hóa
	}

	// Giải mã dữ liệu với thuật toán Bouncy Castle
	private byte[] decryptData(byte[] data) throws Exception {
		byte[] iv = new byte[ICryptoAlgorithm.BLOCK_SIZE];
		System.arraycopy(data, 0, iv, 0, iv.length);
		byte[] ciphertext = new byte[data.length - iv.length];
		System.arraycopy(data, iv.length, ciphertext, 0, ciphertext.length);

		org.bouncycastle.crypto.BlockCipher cipher = getCipherInstance();
		PaddedBufferedBlockCipher cipherInstance = new PaddedBufferedBlockCipher(new CBCBlockCipher(cipher));
		cipherInstance.init(false, new ParametersWithIV(new KeyParameter(key), iv));

		byte[] output = new byte[cipherInstance.getOutputSize(ciphertext.length)];
		int outputLength = cipherInstance.processBytes(ciphertext, 0, ciphertext.length, output, 0);
		cipherInstance.doFinal(output, outputLength);
		return output;
	}

	// Chọn cipher phù hợp với thuật toán
	private org.bouncycastle.crypto.BlockCipher getCipherInstance() throws Exception {
		switch (algorithmType) {
		case Twofish:
			return new TwofishEngine();
		case Serpent:
			return new SerpentEngine();
		case CAST:
			return new CAST5Engine();
		case Camellia:
			return new CamelliaEngine();
		default:
			throw new Exception("Thuật toán không hỗ trợ.");
		}
	}

	@Override
	public boolean encryptFile(String srcFilePath, String desFilePath) throws Exception {

		return false;
	}

	@Override
	public boolean decryptFile(String srcFilePath, String desFilePath) throws Exception {

		return false;
	}

	@Override
	public int getKeySize() {
		return keySize.getBits();
	}

	@Override
	public void setKeySize(EKeySize keySize) throws Exception {
		this.keySize = keySize;
	}

	@Override
	public void genKey() throws Exception {
		key = new byte[keySize.getBits() / 8];
		secureRandom.nextBytes(key);
	}

	@Override
	public void loadKey(Object keyObj) throws Exception {
		if (keyObj instanceof byte[]) {
			key = (byte[]) keyObj;
		} else {
			throw new Exception("Khóa không hợp lệ");
		}
	}

	@Override
	public boolean saveKeyToFile(String filePath) throws Exception {
		try {
			byte[] keyData = key;

			Files.write(Paths.get(filePath), keyData);
			return true;
		} catch (IOException e) {
			throw new Exception("Lỗi khi lưu khóa vào tệp: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean loadKeyFromFile(String filePath) throws Exception {
		try {
			byte[] keyData = Files.readAllBytes(Paths.get(filePath));

			key = keyData;

			return true;
		} catch (IOException e) {
			throw new Exception("Lỗi khi tải khóa từ tệp: " + e.getMessage(), e);
		}
	}

	public static void main(String[] args) {
		try {
			// Khởi tạo đối tượng BouncyCastleSymmetricEncryption với thuật toán và kích
			// thước khóa
			BouncyCastleSymmetricEncryption encryption = new BouncyCastleSymmetricEncryption(EAlgorithmType.CAST,
					EKeySize.CAST_128);

			// Tạo khóa
			encryption.genKey();

			// Mã hóa một chuỗi văn bản
			String plainText = "Đây là văn bản thử nghiệm!";
			System.out.println("Văn bản gốc: " + plainText);
			byte[] encryptedData = encryption.encrypt(plainText);
			System.out
					.println("Dữ liệu mã hóa (Base64): " + java.util.Base64.getEncoder().encodeToString(encryptedData));

			// Giải mã chuỗi dữ liệu
			String decryptedText = encryption.decrypt(encryptedData);
			System.out.println("Dữ liệu giải mã: " + decryptedText);

			// Kiểm tra mã hóa và giải mã tệp
			String srcFilePath = "test.txt"; // Đảm bảo bạn có tệp này trong thư mục làm việc
			String encryptedFilePath = "test_encrypted.txt";
			String decryptedFilePath = "test_decrypted.txt";

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}