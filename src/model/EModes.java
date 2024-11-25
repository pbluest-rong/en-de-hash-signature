package model;

import java.util.Arrays;
import java.util.List;

public enum EModes {
	ECB("ECB"), CBC("CBC"), CFB("CFB"), OFB("OFB"), CTR("CTR");

	private final String modeName;

	EModes(String modeName) {
		this.modeName = modeName;
	}

	public String getModeName() {
		return modeName;
	}

	public static EModes fromString(String modeName) {
		for (EModes mode : EModes.values()) {
			if (mode.getModeName().equalsIgnoreCase(modeName)) {
				return mode;
			}
		}
		return null;
	}

	public static List<EModes> getSupportedModes(EAlgorithmType algorithmType) {
		switch (algorithmType) {
		case AES:
		case DES:
		case TripleDES:
		case Blowfish:
		case Twofish:
		case Serpent:
		case Camellia:
			return Arrays.asList(ECB, CBC, CFB, OFB, CTR);
		case CAST:
			return Arrays.asList(ECB, CBC, CFB, OFB);
		case RC4:
		case ChaCha20:
			return Arrays.asList();
		case RSA:
		case RSA_AES:
			return Arrays.asList(ECB);
		default:
			return Arrays.asList(ECB);
		}
	}
}