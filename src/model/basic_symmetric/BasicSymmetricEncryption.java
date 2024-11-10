package model.basic_symmetric;

import java.awt.Dialog;
import java.awt.RenderingHints.Key;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.jcajce.provider.asymmetric.ec.GMSignatureSpi.sha256WithSM2;

import model.EAlgorithmType;
import model.EKeySize;
import model.ICryptoAlgorithm;

/**
 * @author Ba Phung Le Mã hóa đối xứng cơ bản - Text - Bảng chữ cái Anh, Việt -
 *         Cơ chế phát sinh khóa nếu user chưa có - Cơ chế load khóa nếu user đã
 *         có
 */

public class BasicSymmetricEncryption implements ICryptoAlgorithm {
	private ABasicSymmetric basicSymmetric;
	private CipherConfig config;

	public BasicSymmetricEncryption(EAlgorithmType algorithmType) {
		config = new CipherConfig();
		switch (algorithmType) {
		case EAlgorithmType.Shift_Cipher: {
			this.basicSymmetric = new ShiftCipher(config);
			break;
		}
		case EAlgorithmType.Substitution_Cipher: {
			this.basicSymmetric = new SubstitutionCipher(config);
			break;
		}
		case EAlgorithmType.Affine_Cipher: {
			this.basicSymmetric = new AffineCipher(config);
			break;
		}
		case EAlgorithmType.Vigenere_Cipher: {
			this.basicSymmetric = new VigenereCipher(config);
			break;
		}
		case EAlgorithmType.Hill_Cipher: {
			this.basicSymmetric = new HillCipher(config);
			break;
		}
		case EAlgorithmType.Permutation_Cipher: {
			this.basicSymmetric = new PermutationCipher(config);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + algorithmType);
		}
	}

	@Override
	public void genKey() throws Exception {
		SecureRandom random = new SecureRandom();
	    StringBuilder keyBuilder = new StringBuilder();
	    for (int i = 0; i < 16; i++) {
	        keyBuilder.append((char) ('a' + random.nextInt(26)));
	    }
	    basicSymmetric.key = keyBuilder.toString();
	}

	@Override
	public void loadKey(Object key) throws Exception {
		if (key instanceof String) {
			basicSymmetric.key = (String) key;
		}
	}

	@Override
	public byte[] encrypt(String text) throws Exception {
		return basicSymmetric.encrypt(text).getBytes();
	}

	@Override
	public String decrypt(byte[] data) throws Exception {
		return basicSymmetric.decrypt(new String(data));
	}

	@Override
	public boolean encryptFile(String srcFilePath, String desFilePath) throws Exception {
		try (FileInputStream fis = new FileInputStream(srcFilePath);
				FileOutputStream fos = new FileOutputStream(desFilePath)) {

			byte[] buffer = new byte[1024];
			int bytesRead;

			while ((bytesRead = fis.read(buffer)) != -1) {
				String textChunk = new String(buffer, 0, bytesRead);
				byte[] encryptedChunk = basicSymmetric.encrypt(textChunk).getBytes();
				fos.write(encryptedChunk);
			}

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean decryptFile(String srcFilePath, String desFilePath) throws Exception {
		try (FileInputStream fis = new FileInputStream(srcFilePath);
				FileOutputStream fos = new FileOutputStream(desFilePath)) {

			byte[] buffer = new byte[1024];
			int bytesRead;

			while ((bytesRead = fis.read(buffer)) != -1) {
				String encryptedChunk = new String(buffer, 0, bytesRead);
				String decryptedChunk = basicSymmetric.decrypt(encryptedChunk);
				fos.write(decryptedChunk.getBytes());
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void setKeySize(EKeySize keySize) {
	}

	@Override
	public boolean saveKeyToFile(String filePath) throws Exception {
		return false;
	}

	@Override
	public boolean loadKeyFromFile(String filePath) throws Exception {
		return false;
	}

	@Override
	public int getKeySize() {
		return 0;
	}

	public static void main(String[] args) throws Exception {
		ICryptoAlgorithm shiftCipher = new BasicSymmetricEncryption(EAlgorithmType.Shift_Cipher);
		ICryptoAlgorithm affineCipher = new BasicSymmetricEncryption(EAlgorithmType.Affine_Cipher);
		ICryptoAlgorithm subsitutionCipher = new BasicSymmetricEncryption(EAlgorithmType.Substitution_Cipher);
		ICryptoAlgorithm vigenereCipher = new BasicSymmetricEncryption(EAlgorithmType.Vigenere_Cipher);
		ICryptoAlgorithm permutationCipher = new BasicSymmetricEncryption(EAlgorithmType.Permutation_Cipher);
		ICryptoAlgorithm hillCipher = new BasicSymmetricEncryption(EAlgorithmType.Hill_Cipher);

		shiftCipher.genKey();

		String plaintext = "“Pass đồ” Làng đại học là một website nhằm phục vụ các nhu cầu trao đổi đồ cũ của sinh viên trong và quanh làng đại học TPHCM cũng như một phần nhỏ giúp bảo vệ môi trường. ";
		try {
			String ciphertext = Base64.getEncoder().encodeToString(shiftCipher.encrypt(plaintext));
			String decryptedText = shiftCipher.decrypt(Base64.getDecoder().decode(ciphertext));

			System.out.println("Plaintext: " + plaintext);
			System.out.println("Ciphertext: " + Base64.getEncoder().encodeToString(ciphertext.getBytes()));
			System.out.println("Decrypted Text: " + decryptedText);
			System.out.println();

			shiftCipher.encryptFile("resources/input/text_input.txt","resources/input/text_output.txt");
			shiftCipher.decryptFile("resources/input/text_output.txt","resources/input/text_output_de.txt");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
