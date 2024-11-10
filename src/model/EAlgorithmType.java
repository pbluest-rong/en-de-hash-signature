package model;

public enum EAlgorithmType {
	Shift_Cipher("Shift Cipher"), Substitution_Cipher("Substitution Cipher"), Affine_Cipher("Affine"),
	Vigenere_Cipher("Vigenere Cipher"), Hill_Cipher("Hill Cipher"), Permutation_Cipher("Permutation Cipher"),
	//
	AES("AES"), DES("DES"), TripleDES("DESede"), Blowfish("Blowfish"), RC4("RC4"), ChaCha20("ChaCha20"),
	//
	Twofish("Twofish"), Serpent("Serpent"), CAST("CAST"), Camellia("Camellia"),
	//
	RSA("RSA"), RSA_AES("RSA_AES");

	private final String algorithm;

	EAlgorithmType(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getAlgorithm() {
		return algorithm;
	}
	
	public String getCipherInstanceString() {
		switch (this) {
		case AES:
			return "AES/CBC/PKCS5Padding"; // AES với CBC và PKCS5Padding
		case DES:
			return "DES/CBC/PKCS5Padding"; // DES với CBC và PKCS5Padding
		case TripleDES:
			return "DESede/CBC/PKCS5Padding"; // TripleDES (DESede) với CBC và PKCS5Padding
		case Blowfish:
			return "Blowfish/CBC/PKCS5Padding"; // Blowfish với CBC và PKCS5Padding
		case RC4:
			return "RC4"; // RC4 không cần padding
		case ChaCha20:
			return "ChaCha20"; // ChaCha20 không cần padding
		case Twofish:
			return "Twofish/CBC/PKCS5Padding"; // Twofish với CBC và PKCS5Padding
		case RSA:
			return "RSA/ECB/PKCS1Padding"; // RSA với ECB và PKCS1Padding
		default:
			return this.getAlgorithm();
		}
	}

	public static EAlgorithmType get(String algorithm) {
		for (EAlgorithmType type : values()) {
			if (type.getAlgorithm().equalsIgnoreCase(algorithm)) {
				return type;
			}
		}
		return null;
	}
}