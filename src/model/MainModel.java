package model;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import model.asymmetric.RSA;
import model.asymmetric.RSA_AES;
import model.basic_symmetric.BasicSymmetricEncryption;
import model.hash.CustomHashAlgorithm;
import model.hash.EHashAlgorithm;
import model.hash.IHash;
import model.hash.JavaHash;
import model.modern_symmetric.ModernSymmetricEncryption;

public class MainModel {
	public ICryptoAlgorithm algorithm;
	public IHash hash;

	public EAlgorithmType algorithmType;
	public EModes mode;
	public EPadding padding;
	public EKeySize keySize;

	public List<EAlgorithmType> basicSymmetricAlgorithmTypes;
	public Map<EAlgorithmType, List<EKeySize>> modernSymmetricAlgorithmTypes;
	public Map<EAlgorithmType, List<EKeySize>> asymmetricAlgorithmTypes;
	public List<EHashAlgorithm> hashAlgorithms;

	public MainModel() {
		this.asymmetricAlgorithmTypes = new LinkedHashMap<EAlgorithmType, List<EKeySize>>();
		this.modernSymmetricAlgorithmTypes = new LinkedHashMap<EAlgorithmType, List<EKeySize>>();
		this.basicSymmetricAlgorithmTypes = new ArrayList<EAlgorithmType>();

		List<EKeySize> des = new ArrayList<>(Arrays.asList(EKeySize.DES_56));
		List<EKeySize> tripleDes = new ArrayList<>(Arrays.asList(EKeySize.TripleDES_168));
		List<EKeySize> aes = new ArrayList<>(Arrays.asList(EKeySize.AES_128, EKeySize.AES_192, EKeySize.AES_256));
		List<EKeySize> blowfish = new ArrayList<>(Arrays.asList(EKeySize.Blowfish_32, EKeySize.Blowfish_128,
				EKeySize.Blowfish_256, EKeySize.Blowfish_448));
		List<EKeySize> rc4 = new ArrayList<>(Arrays.asList(EKeySize.RC4_40, EKeySize.RC4_128, EKeySize.Twofish_256));
		List<EKeySize> chacha20 = new ArrayList<>(Arrays.asList(EKeySize.ChaCha20_256));
		List<EKeySize> rsa = new ArrayList<>(
				Arrays.asList(EKeySize.RSA_1024, EKeySize.RSA_2048, EKeySize.RSA_3072, EKeySize.RSA_4096));

		this.modernSymmetricAlgorithmTypes.put(EAlgorithmType.DES, des);
		this.modernSymmetricAlgorithmTypes.put(EAlgorithmType.TripleDES, tripleDes);
		this.modernSymmetricAlgorithmTypes.put(EAlgorithmType.AES, aes);
		this.modernSymmetricAlgorithmTypes.put(EAlgorithmType.Blowfish, blowfish);
		this.modernSymmetricAlgorithmTypes.put(EAlgorithmType.RC4, rc4);
		this.modernSymmetricAlgorithmTypes.put(EAlgorithmType.ChaCha20, chacha20);

		this.asymmetricAlgorithmTypes.put(EAlgorithmType.RSA, rsa);
		this.asymmetricAlgorithmTypes.put(EAlgorithmType.RSA_AES, rsa);

		this.basicSymmetricAlgorithmTypes.add(EAlgorithmType.Shift_Cipher);
		this.basicSymmetricAlgorithmTypes.add(EAlgorithmType.Substitution_Cipher);
		this.basicSymmetricAlgorithmTypes.add(EAlgorithmType.Affine_Cipher);
		this.basicSymmetricAlgorithmTypes.add(EAlgorithmType.Vigenere_Cipher);
		this.basicSymmetricAlgorithmTypes.add(EAlgorithmType.Permutation_Cipher);
		this.basicSymmetricAlgorithmTypes.add(EAlgorithmType.Hill_Cipher);

		// hash
		this.hashAlgorithms = new ArrayList<EHashAlgorithm>();
		this.hashAlgorithms.add(EHashAlgorithm.MD2);
		this.hashAlgorithms.add(EHashAlgorithm.MD5);
		this.hashAlgorithms.add(EHashAlgorithm.SHA_1);
		this.hashAlgorithms.add(EHashAlgorithm.SHA_224);
		this.hashAlgorithms.add(EHashAlgorithm.SHA_256);
		this.hashAlgorithms.add(EHashAlgorithm.SHA_512);
		this.hashAlgorithms.add(EHashAlgorithm.SHA_512_224);
		this.hashAlgorithms.add(EHashAlgorithm.SHA_512_256);
		this.hashAlgorithms.add(EHashAlgorithm.SHA3_224);
		this.hashAlgorithms.add(EHashAlgorithm.SHA3_256);
		this.hashAlgorithms.add(EHashAlgorithm.SHA3_384);
		this.hashAlgorithms.add(EHashAlgorithm.SHA3_512);
		this.hashAlgorithms.add(EHashAlgorithm.HAVAL);
		this.hashAlgorithms.add(EHashAlgorithm.RIPEMD160);
		this.hashAlgorithms.add(EHashAlgorithm.Whirlpool);
		this.hashAlgorithms.add(EHashAlgorithm.Tiger);
		this.hashAlgorithms.add(EHashAlgorithm.MD4);
		this.hashAlgorithms.add(EHashAlgorithm.GOST3411);
	}

	public boolean choooseHashAlgorithm(EHashAlgorithm algorithm) {
		if (algorithm.isJavaHashAlgorithmInstances()) {
			if (hash == null) {
				hash = new JavaHash(algorithm);
				return true;
			} else if (!hash.supportsJavaHash()) {
				hash = new JavaHash(algorithm);
				return true;
			} else if (hash.supportsJavaHash()) {
				hash.setHashAlgorithm(algorithm);
				return true;
			}
		} else {
			// THUẬT TOÁN MÀ JAVA KHÔNG HỖ TRỢ
			if (hash == null) {
				hash = new CustomHashAlgorithm(algorithm);
				return true;
			}
			// 2. hash cũ + đang support java -> new custom
			else if (hash != null && hash.supportsJavaHash()) {
				hash = new CustomHashAlgorithm(algorithm);
				return true;
			}
			// 3
			else if (hash != null && !hash.supportsJavaHash()) {
				hash.setHashAlgorithm(algorithm);
				return true;
			}
		}
		return false;
	}

	public boolean chooseAlgorithm() {
		if (this.algorithmType != null) {
			if (ICryptoAlgorithm.isBasicSymmetric(this.algorithmType)) {
				this.algorithm = new BasicSymmetricEncryption(this.algorithmType);
				return true;
			} else {
				if (this.keySize == null)
					return false;
				try {
					if (ICryptoAlgorithm.isModernSymmetric(this.algorithmType))
						this.algorithm = new ModernSymmetricEncryption(this.algorithmType, this.keySize);
					else if (ICryptoAlgorithm.isAsymmetricRSA(this.algorithmType)) {
						this.algorithm = new RSA(this.keySize);
					} else if (ICryptoAlgorithm.isAsymmetricRSA_AES(this.algorithmType)) {
						this.algorithm = new RSA_AES(this.keySize);
					} else {
						return false;
					}
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	public List<EModes> getModes() {
		return EModes.getSupportedModes(algorithmType);
	}

	public List<EPadding> getPaddings() {
		return EPadding.getSupportedPadding(algorithmType);
	}

	public static void main(String[] args) throws Exception {
		String srcFilePath = "resources/input/image_input.jpg";
		String desFilePathEn = "resources/encrypt/image_output.jpg";
		String desFilePathDe = "resources/decrypt/image_output.jpg";

		MainModel model = new MainModel();
		model.algorithmType = EAlgorithmType.RSA;
		model.keySize = EKeySize.RSA_1024;
		model.chooseAlgorithm();
		if (model.algorithm != null) {
			model.algorithm.genKey();

			model.algorithm.saveKeyToFile("resources/key.data");
			model.algorithm.encryptFile(srcFilePath, desFilePathEn);

//			PrivateKey privateKey = ICryptoAlgorithm.loadPrivateKey("resources/privateKeyPath.key");
//			((RSA_AES) model.algorithm).privateKey = privateKey;
//			model.algorithm.decryptFile(desFilePathEn, desFilePathDe);
		}
	}
}
