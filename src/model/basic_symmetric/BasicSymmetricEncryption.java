package model.basic_symmetric;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import model.EAlgorithmType;
import model.EModes;
import model.EPadding;
import model.ICryptoAlgorithm;

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
		if (this.basicSymmetric != null)
			switch (basicSymmetric.type()) {
			case EAlgorithmType.Shift_Cipher: {
				this.basicSymmetric.config.generateShiftCipherKey();
				break;
			}
			case EAlgorithmType.Substitution_Cipher: {
				this.basicSymmetric.config.generateSubstitutionCipherKey();
				break;
			}
			case EAlgorithmType.Affine_Cipher: {
				this.basicSymmetric.config.generateAffineCipherKey();
				break;
			}
			case EAlgorithmType.Vigenere_Cipher: {
				this.basicSymmetric.config.generateVigenereCipherKey();
				break;
			}
			case EAlgorithmType.Permutation_Cipher: {
				this.basicSymmetric.config.generatePermutationCipherKey();
				break;
			}
			case EAlgorithmType.Hill_Cipher: {
				this.basicSymmetric.config.generateHillCipherKey();
				break;
			}
			default:
				break;
			}
	}

	@Override
	public void loadKey(Object key) throws Exception {

	}

	@Override
	public byte[] encrypt(String text) throws Exception {
		return basicSymmetric.encrypt(text).getBytes("UTF-8");
	}

	@Override
	public String decrypt(byte[] data) throws Exception {
		return basicSymmetric.decrypt(new String(data, "UTF-8"));
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
	public boolean saveKeyToFile(String filePath) throws Exception {
		try (FileOutputStream fos = new FileOutputStream(filePath);
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(this.config);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean loadKeyFromFile(String filePath) throws Exception {
		try (FileInputStream fis = new FileInputStream(filePath); ObjectInputStream ois = new ObjectInputStream(fis)) {
			this.config = (CipherConfig) ois.readObject();
			return true;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int getKeySize() {
		return 0;
	}

	@Override
	public String info() {
		return "Cipher Config: " + (this.basicSymmetric.config != null);

	}

	@Override
	public boolean setMode(EModes mode) {
		return false;
	}

	@Override
	public boolean setPadding(EPadding padding) {
		return false;
	}

	public static void main(String[] args) throws Exception {
		// ok
		ICryptoAlgorithm shiftCipher = new BasicSymmetricEncryption(EAlgorithmType.Shift_Cipher);
		// ok
		ICryptoAlgorithm affineCipher = new BasicSymmetricEncryption(EAlgorithmType.Affine_Cipher);
		// ok
		ICryptoAlgorithm subsitutionCipher = new BasicSymmetricEncryption(EAlgorithmType.Substitution_Cipher);
		//
		ICryptoAlgorithm vigenereCipher = new BasicSymmetricEncryption(EAlgorithmType.Vigenere_Cipher);
		// ok
		ICryptoAlgorithm permutationCipher = new BasicSymmetricEncryption(EAlgorithmType.Permutation_Cipher);
		//
		ICryptoAlgorithm hillCipher = new BasicSymmetricEncryption(EAlgorithmType.Hill_Cipher);

		String plaintext = "Trong một thế giới ngày càng phát triển,"
				+ " công nghệ đang chiếm lĩnh hầu hết mọi lĩnh vực của cuộc sống."
				+ " Từ trí tuệ nhân tạo đến Internet vạn vật,"
				+ " chúng ta đang chứng kiến sự thay đổi vượt bậc trong cách thức con người tương tác và làm việc."
				+ " Trong khi đó, việc bảo vệ dữ liệu và thông tin cá nhân đang trở thành một vấn đề quan trọng hơn bao giờ hết.";

		String src = "resources/input/text.txt";
		String fileKey = "resources/input/vigenere_key.data";
		String en = "resources/input/en.txt";
		String de = "resources/input/de.txt";

		try {
			ICryptoAlgorithm al = hillCipher;
			al.genKey();
//			al.loadKeyFromFile(fileKey);
			String ciphertext = Base64.getEncoder().encodeToString(al.encrypt(plaintext)); // mã hóa sang Base64
			String decryptedText = new String(al.decrypt(Base64.getDecoder().decode(ciphertext))); // giải mã và chuyển
																									// lại thành String

			System.out.println("Plaintext:      " + plaintext);
			System.out.println("Ciphertext: " + Base64.getEncoder().encodeToString(ciphertext.getBytes()));
			System.out.println("Decrypted Text: " + decryptedText);
			System.out.println(plaintext.equals(decryptedText));

//			shiftCipher.saveKeyToFile(fileKey);
//			shiftCipher.encryptFile(src, en);
//			shiftCipher.decryptFile(en, de);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
