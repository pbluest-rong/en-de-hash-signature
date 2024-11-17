package test;

import java.util.Base64;
import java.util.List;

import model.EAlgorithmType;
import model.EKeySize;
import model.EModes;
import model.ICryptoAlgorithm;
import model.MainModel;
import model.hash.CustomHashAlgorithm;
import model.hash.EHashAlgorithm;
import model.hash.IHash;
import model.hash.JavaHash;

public class TestModel {
	// Problems: Hill, BouncyCastleSymmetricEncryption
	private MainModel model = new MainModel();

	public static void main(String[] args) throws Exception {
		TestModel tester = new TestModel();
		String text = "Chào, tôi là Pblues!";

		String src = "resources/image.jpg";
		String en = "resources/en.jpg";
		String de = "resources/de.jpg";

		// 1. Test Algorithm with text
//		tester.testBasicSymmetricAl(text);
		tester.testModernSymmetricAl(text);
//		tester.testAsymmetricAl(text);

		// 2. Test Algorigm with file

//		for (EAlgorithmType type : tester.model.basicSymmetricAlgorithmTypes) {
//			if (ICryptoAlgorithm.isFileEncryption(type))
//				tester.testEncryption(src, en, de, type, null);
//		}
//
		for (EAlgorithmType type : tester.model.modernSymmetricAlgorithmTypes.keySet()) {
			for (EKeySize keySize : tester.model.modernSymmetricAlgorithmTypes.get(type))
				if (ICryptoAlgorithm.isFileEncryption(type))
					for (EModes mode : EModes.getSupportedModes(type)) {
						System.out.println(type.getAlgorithm() + " => " + keySize.getBits() + " => " + mode.getModeName());
						tester.testEncryption(src, en, de, type, keySize, mode);
					}
		}
//
//		for (EAlgorithmType type : tester.model.asymmetricAlgorithmTypes.keySet()) {
//			for (EKeySize keySize : tester.model.asymmetricAlgorithmTypes.get(type))
//				if (ICryptoAlgorithm.isFileEncryption(type))
//					tester.testEncryption(src, en, de, type, keySize);
//		}

		// 3. Test Hash
//		tester.testHash(text, src);
	}

	private void testBasicSymmetricAl(String text) throws Exception {
		for (EAlgorithmType type : model.basicSymmetricAlgorithmTypes) {
			testEncryption(text, type, null, null);
		}
	}

	private void testModernSymmetricAl(String text) throws Exception {
		for (EAlgorithmType type : model.modernSymmetricAlgorithmTypes.keySet()) {
			for (EKeySize keySize : model.modernSymmetricAlgorithmTypes.get(type)) {
				for (EModes mode : EModes.getSupportedModes(type)) {
					System.out.println(type.getAlgorithm() + " => " + keySize.getBits() + " => " + mode.getModeName());
					testEncryption(text, type, keySize, mode);
				}
			}
		}
	}

	private void testAsymmetricAl(String text) throws Exception {
		for (EAlgorithmType type : model.asymmetricAlgorithmTypes.keySet()) {
			for (EKeySize keySize : model.asymmetricAlgorithmTypes.get(type))
				testEncryption(text, type, keySize, null);
		}
	}

	private void testEncryption(String text, EAlgorithmType algorithmType, EKeySize keySize, EModes mode)
			throws Exception {
		model.algorithmType = algorithmType;
		model.keySize = keySize;
		model.mode = mode;
		model.chooseAlgorithm();
		if (model.algorithm != null) {
			model.algorithm.genKey();
			byte[] out = this.model.algorithm.encrypt(text);
			String ciphertext = Base64.getEncoder().encodeToString(out);
			String plaintext = model.algorithm.decrypt(out);
			System.out.println(ciphertext);
			System.out.println(plaintext);
		} else {
			System.out.println("Oops! Algorithm null.");
		}
	}

	private void testEncryption(String srcFilePath, String enFilePath, String deFilePath, EAlgorithmType algorithmType,
			EKeySize keySize, EModes mode) throws Exception {
		model.algorithmType = algorithmType;
		model.keySize = keySize;
		model.mode = mode;
		model.chooseAlgorithm();
		if (model.algorithm != null) {
			model.algorithm.genKey();
			this.model.algorithm.encryptFile(srcFilePath, enFilePath);
			this.model.algorithm.decryptFile(enFilePath, deFilePath);
			System.out.print(model.algorithmType.getAlgorithm() + "\t key size: " + model.keySize + " ---> "
					+ checkSameFile(srcFilePath, deFilePath) + "\n");
		} else {
			System.out.println("Oops! Algorithm null.");
		}
	}

	private void testHash(String message, String filePath) throws Exception {
		EHashAlgorithm hashAlgorithm = EHashAlgorithm.MD5;
		IHash hash = new JavaHash(hashAlgorithm);
		for (EHashAlgorithm e : model.hashAlgorithms) {
			if (e.isJavaHashAlgorithmInstances()) {
				hashAlgorithm = e;
				if (!hash.supportsJavaHash())
					hash = new JavaHash(hashAlgorithm);

				hash.setHashAlgorithm(hashAlgorithm);
			} else if (!e.isJavaHashAlgorithmInstances()) {
				hashAlgorithm = e;
				hash = new CustomHashAlgorithm(hashAlgorithm);
			}
			System.out.println(hashAlgorithm.getHashAlgorithm() + " => " + hash.hash(message));
			System.out.println(hashAlgorithm.getHashAlgorithm() + " => " + hash.hashFile(filePath));
			System.out.println();
		}
	}

	private boolean checkSameFile(String filePath1, String filePath2) throws Exception {
		this.model.choooseHashAlgorithm(EHashAlgorithm.MD5);
		String hash1 = this.model.hash.hashFile(filePath1);
		String hash2 = this.model.hash.hashFile(filePath2);
		return hash1.equals(hash2);
	}
}
