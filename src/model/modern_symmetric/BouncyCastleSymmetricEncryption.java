package model.modern_symmetric;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.*;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.modes.OFBBlockCipher;
import org.bouncycastle.crypto.modes.SICBlockCipher;
import org.bouncycastle.crypto.paddings.ISO10126d2Padding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.paddings.ZeroBytePadding;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import model.EAlgorithmType;
import model.EKeySize;
import model.EModes;
import model.EPadding;
import model.ICryptoAlgorithm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;

public class BouncyCastleSymmetricEncryption implements ICryptoAlgorithm {
	private SecureRandom secureRandom = new SecureRandom();
	private EKeySize keySize;
	private EAlgorithmType algorithmType;
	private EModes mode;
	private EPadding padding;
	private byte[] key;

	public BouncyCastleSymmetricEncryption(EAlgorithmType algorithmType, EKeySize keySize, EModes mode,
			EPadding padding) {
		this.algorithmType = algorithmType;
		this.keySize = keySize;
		this.mode = mode;
		this.padding = padding;
	}

	@Override
	public byte[] encrypt(String text) throws Exception {
		byte[] data = text.getBytes(StandardCharsets.UTF_8);
		return encryptData(data);
	}

	@Override
	public String decrypt(byte[] data) throws Exception {
		byte[] decryptedData = decryptData(data);
		return new String(decryptedData, StandardCharsets.UTF_8);
	}

	private byte[] encryptData(byte[] data) throws Exception {
		org.bouncycastle.crypto.BlockCipher baseCipher = getCipherInstance();
		BlockCipher blockCipher = wrapCipherWithMode(baseCipher);
		PaddedBufferedBlockCipher cipher = wrapCipherWithPadding(blockCipher);

		// Tạo IV nếu cần thiết
		byte[] iv = null;
		if (EAlgorithmType.requiresIV(mode, algorithmType)) {
			int blocksize = this.algorithmType.getIvLength();
			iv = new byte[blocksize];
			secureRandom.nextBytes(iv);
			cipher.init(true, new ParametersWithIV(new KeyParameter(key), iv));
		} else {
			cipher.init(true, new KeyParameter(key));
		}

		// Mã hóa dữ liệu
		byte[] output = new byte[cipher.getOutputSize(data.length)];
		int outputLength = cipher.processBytes(data, 0, data.length, output, 0);
		cipher.doFinal(output, outputLength);

		// Nếu IV được dùng, gắn vào đầu ciphertext
		if (iv != null) {
			byte[] result = new byte[iv.length + output.length];
			System.arraycopy(iv, 0, result, 0, iv.length);
			System.arraycopy(output, 0, result, iv.length, output.length);
			return result;
		}
		return output;
	}

	private byte[] decryptData(byte[] data) throws Exception {
		org.bouncycastle.crypto.BlockCipher baseCipher = getCipherInstance();
		BlockCipher blockCipher = wrapCipherWithMode(baseCipher);
		PaddedBufferedBlockCipher cipher = wrapCipherWithPadding(blockCipher);

		byte[] iv = null;
		byte[] ciphertext = null;

		if (EAlgorithmType.requiresIV(mode, algorithmType)) {
			int blocksize = this.algorithmType.getIvLength();
			iv = new byte[blocksize];
			System.arraycopy(data, 0, iv, 0, iv.length);

			// Tách ciphertext từ data
			ciphertext = new byte[data.length - iv.length];
			System.arraycopy(data, iv.length, ciphertext, 0, ciphertext.length);

			cipher.init(false, new ParametersWithIV(new KeyParameter(key), iv));
		} else {
			cipher.init(false, new KeyParameter(key));
			ciphertext = data;
		}

		// Giải mã ciphertext
		byte[] output = new byte[cipher.getOutputSize(ciphertext.length)];
		int outputLength = cipher.processBytes(ciphertext, 0, ciphertext.length, output, 0);
		outputLength += cipher.doFinal(output, outputLength);

		// Chỉ trả về phần dữ liệu hợp lệ, bỏ qua padding
		return Arrays.copyOfRange(output, 0, outputLength);
	}

	private BlockCipher wrapCipherWithMode(org.bouncycastle.crypto.BlockCipher cipher) throws Exception {
		int blockSize = cipher.getBlockSize() * 8;
		switch (mode) {
		case CBC:
			return new CBCBlockCipher(cipher);
		case ECB:
			return cipher;
		case CFB:
			return new CFBBlockCipher(cipher, blockSize);
		case OFB:
			return new OFBBlockCipher(cipher, blockSize);
		case CTR:
			return new SICBlockCipher(cipher);
		default:
			throw new Exception("Unsupported mode.");
		}
	}

	private PaddedBufferedBlockCipher wrapCipherWithPadding(BlockCipher cipher) throws Exception {
		switch (padding) {
		case PKCS7Padding:
			return new PaddedBufferedBlockCipher(cipher, new PKCS7Padding());
		case ZeroPadding:
			return new PaddedBufferedBlockCipher(cipher, new ZeroBytePadding());
		case ISO10126Padding:
			return new PaddedBufferedBlockCipher(cipher, new ISO10126d2Padding());
		default:
			throw new Exception("Unsupported padding.");
		}
	}

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
			throw new Exception("Unsupported algorithm.");
		}
	}

	@Override
	public boolean encryptFile(String srcFilePath, String desFilePath) throws Exception {
		// Read the source file
		byte[] fileData = Files.readAllBytes(Paths.get(srcFilePath));

		// Get the cipher instance and initialize it
		org.bouncycastle.crypto.BlockCipher cipher = getCipherInstance();
		int blocksize = this.algorithmType.getIvLength();
		byte[] iv = new byte[blocksize];
		secureRandom.nextBytes(iv);
		PaddedBufferedBlockCipher cipherInstance = new PaddedBufferedBlockCipher(new CBCBlockCipher(cipher));
		cipherInstance.init(true, new ParametersWithIV(new KeyParameter(key), iv));

		// Encrypt the file data
		byte[] output = new byte[cipherInstance.getOutputSize(fileData.length)];
		int outputLength = cipherInstance.processBytes(fileData, 0, fileData.length, output, 0);
		cipherInstance.doFinal(output, outputLength);

		// Combine the IV and the encrypted data
		byte[] result = new byte[iv.length + output.length];
		System.arraycopy(iv, 0, result, 0, iv.length);
		System.arraycopy(output, 0, result, iv.length, output.length);

		// Write the result (IV + encrypted data) to the output file
		try (FileOutputStream fos = new FileOutputStream(desFilePath)) {
			fos.write(result);
			return true;
		} catch (IOException e) {
			throw new Exception("Error writing encrypted data to file: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean decryptFile(String srcFilePath, String desFilePath) throws Exception {
		// Đọc dữ liệu mã hóa từ file
		byte[] encryptedData = Files.readAllBytes(Paths.get(srcFilePath));

		// Tách IV
		int blocksize = this.algorithmType.getIvLength();
		byte[] iv = new byte[blocksize];
		System.arraycopy(encryptedData, 0, iv, 0, iv.length);

		// Dữ liệu còn lại là ciphertext
		byte[] ciphertext = new byte[encryptedData.length - iv.length];
		System.arraycopy(encryptedData, iv.length, ciphertext, 0, ciphertext.length);

		// Giải mã
		org.bouncycastle.crypto.BlockCipher cipher = getCipherInstance();
		PaddedBufferedBlockCipher cipherInstance = new PaddedBufferedBlockCipher(new CBCBlockCipher(cipher));
		cipherInstance.init(false, new ParametersWithIV(new KeyParameter(key), iv));

		byte[] decryptedData = new byte[cipherInstance.getOutputSize(ciphertext.length)];
		int outputLength = cipherInstance.processBytes(ciphertext, 0, ciphertext.length, decryptedData, 0);
		outputLength += cipherInstance.doFinal(decryptedData, outputLength);

		// Ghi dữ liệu giải mã vào file
		try (FileOutputStream fos = new FileOutputStream(desFilePath)) {
			fos.write(decryptedData, 0, outputLength); // Chỉ ghi dữ liệu hợp lệ
			return true;
		} catch (IOException e) {
			throw new Exception("Error writing decrypted data to file: " + e.getMessage(), e);
		}
	}

	@Override
	public int getKeySize() {
		return key.length * 8;
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
			throw new Exception("Invalid key format.");
		}
	}

	@Override
	public boolean saveKeyToFile(String filePath) throws Exception {
		try {
			Files.write(Paths.get(filePath), key);
			return true;
		} catch (IOException e) {
			throw new Exception("Error saving key to file: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean loadKeyFromFile(String filePath) throws Exception {
		try {
			key = Files.readAllBytes(Paths.get(filePath));
			return true;
		} catch (IOException e) {
			throw new Exception("Error loading key from file: " + e.getMessage(), e);
		}
	}

	@Override
	public String info() {
		return "Algorithm: " + algorithmType.getAlgorithm() + ", Mode: " + mode.getModeName() + ", Padding: "
				+ padding.getPaddingName() + ", Key size: " + getKeySize() + " bits";
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

	public static void main(String[] args) {
		try {
			// Khởi tạo đối tượng BouncyCastleSymmetricEncryption với thuật toán và kích
			// thước khóa
			EAlgorithmType[] types = new EAlgorithmType[] { EAlgorithmType.Twofish, EAlgorithmType.Serpent,
					EAlgorithmType.CAST, EAlgorithmType.Camellia };
			EModes[] modes = new EModes[] { EModes.ECB, EModes.CBC, EModes.CFB, EModes.OFB, EModes.CTR };
			EPadding[] paddings = new EPadding[] { EPadding.PKCS7Padding, EPadding.ISO10126Padding,
					EPadding.ZeroPadding };

			String plainText = "Đây là văn bản thử nghiệm!";
			System.out.println("Văn bản gốc: " + plainText);

			for (EAlgorithmType type : types)
				for (EModes mode : modes)
					for (EPadding p : paddings) {
						BouncyCastleSymmetricEncryption encryption = new BouncyCastleSymmetricEncryption(type,
								EKeySize.CAST_128, mode, p);
						System.out.println("===========> " + encryption.algorithmType + " - " + encryption.mode + " - "
								+ encryption.padding);
						encryption.genKey();
						try {
							byte[] encryptedData = encryption.encrypt(plainText);
							String decryptedText = encryption.decrypt(encryptedData);

							System.out.println("Dữ liệu mã hóa (Base64): "
									+ java.util.Base64.getEncoder().encodeToString(encryptedData));
							System.out.println("Dữ liệu giải mã: " + decryptedText);

						} catch (Exception e) {
							System.out.println("\u001B[31m" + "ERROR " + e.getMessage() + "\u001B[0m");
						}
						System.out.println("===============================\n");
					}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}