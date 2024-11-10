package model;

import java.util.Arrays;
import java.util.List;

public enum EModes {
	ECB("ECB"), CBC("CBC"), CFB("CFB"), OFB("OFB"), CTR("CTR"), GCM("GCM");

	private final String modeName;

	EModes(String modeName) {
		this.modeName = modeName;
	}

	public String getModeName() {
		return modeName;
	}

	public static List<EModes> getSupportedModes(EAlgorithmType algorithmType) {
		switch (algorithmType) {
		case AES:
		case DES:
		case TripleDES:
		case Blowfish:
		case Twofish:
		case Camellia:
			return Arrays.asList(ECB, CBC, CFB, OFB, CTR, GCM);
		case RC4:
		case ChaCha20:
			return Arrays.asList(ECB, CTR);
		case RSA:
		case RSA_AES:
			return Arrays.asList(ECB);
		default:
			return Arrays.asList(ECB);
		}
	}
}
