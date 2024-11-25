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

	public String getCipherInstanceString(EModes mode, EPadding padding) {
		switch (this) {
		case AES:
			return "AES/" + mode.getModeName() + "/" + padding.getPaddingName();
		case DES:
			return "DES/" + mode.getModeName() + "/" + padding.getPaddingName();
		case TripleDES:
			return "DESede/" + mode.getModeName() + "/" + padding.getPaddingName();
		case Blowfish:
			return "Blowfish/" + mode.getModeName() + "/" + padding.getPaddingName();
		case RC4:
			return "RC4";
		case ChaCha20:
			return "ChaCha20";
		case RSA:
			return "RSA/" + mode.getModeName() + "/" + padding.getPaddingName();
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

	public Integer getIvLength() {
		switch (this) {
		case AES:
		case Twofish:
		case Serpent:
		case Camellia:
			return 16; // AES, Twofish, Camellia đều sử dụng IV dài 16 byte (128 bit)
		case DES:
		case TripleDES:
		case Blowfish:
		case CAST:
			return 8; // DES, TripleDES, Blowfish, CAST đều sử dụng IV dài 8 byte (64 bit)
		case ChaCha20:
			return 12; // ChaCha20 sử dụng IV dài 12 byte (96 bit)
		case RC4:
			return null; // RC4 không yêu cầu IV (Stream Cipher)
		case RSA:
		case RSA_AES:
			return null; // RSA không yêu cầu IV
		default:
			return null; // Mặc định trả về 0 nếu không có IV
		}
	}

	public static boolean requiresIV(EModes mode, EAlgorithmType algorithmType) {
		if (algorithmType == EAlgorithmType.RC4)
			return false;
		switch (mode) {
		case ECB:
		case CFB:
		case OFB:
			return false;
		case CBC:
		case CTR:
			return true;
		default:
			return false;
		}
	}

	public boolean isStreamCipher() {
		return this == EAlgorithmType.RC4 || this == EAlgorithmType.ChaCha20;
	}

}