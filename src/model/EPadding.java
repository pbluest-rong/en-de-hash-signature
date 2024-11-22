package model;

import java.util.ArrayList;
import java.util.List;

public enum EPadding {
	// Java hỗ trợ
	NoPadding("NoPadding"), PKCS5Padding("PKCS5Padding"),
	// RSA
	PKCS1Padding("PKCS1Padding"), OAEPWithSHA1AndMGF1Padding("OAEPWithSHA-1AndMGF1Padding"),
	OAEPWithSHA256AndMGF1Padding("OAEPWithSHA-256AndMGF1Padding"),
	// Java không hỗ trợ
	PKCS7Padding("PKCS7Padding"), ZeroPadding("ZeroBytePadding"), ISO10126Padding("ISO10126Padding"),;

	private final String paddingName;

	EPadding(String paddingName) {
		this.paddingName = paddingName;
	}

	public String getPaddingName() {
		return paddingName;
	}

	public static EPadding fromString(String paddingName) {
		for (EPadding padding : EPadding.values()) {
			if (padding.getPaddingName().equalsIgnoreCase(paddingName)) {
				return padding;
			}
		}
		throw new IllegalArgumentException("Unsupported padding: " + paddingName);
	}

	public static List<EPadding> getSupportedPadding(EAlgorithmType algorithmType, EModes mode) {
		List<EPadding> paddings = new ArrayList<>();

		if (algorithmType == EAlgorithmType.RC4 || algorithmType == EAlgorithmType.ChaCha20) {
			return paddings;
		}

		switch (algorithmType) {
		case AES:
		case DES:
		case TripleDES:
		case Blowfish:
			if (mode == EModes.CTR) {
				paddings.add(EPadding.NoPadding);
			} else if (mode == EModes.ECB || mode == EModes.CBC || mode == EModes.CFB || mode == EModes.OFB) {
				paddings.add(EPadding.PKCS5Padding);
				paddings.add(EPadding.NoPadding);
			}
			break;
		case Twofish:
		case Serpent:
		case CAST:
		case Camellia:
			paddings.add(PKCS7Padding);
			paddings.add(ISO10126Padding);
			paddings.add(ZeroPadding);
			break;
		case RSA:
		case RSA_AES:
			paddings.add(EPadding.PKCS1Padding);
			paddings.add(EPadding.OAEPWithSHA1AndMGF1Padding);
			paddings.add(EPadding.OAEPWithSHA256AndMGF1Padding);
			break;

		default:
			break;
		}
		return paddings;
	}

}
