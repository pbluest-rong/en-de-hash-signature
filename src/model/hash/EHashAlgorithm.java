package model.hash;

public enum EHashAlgorithm {
	// java support
	MD2("MD2"), MD5("MD5"), SHA_1("SHA-1"), SHA_224("SHA-224"), SHA_256("SHA-256"), SHA_512("SHA-512"),
	SHA_512_224("SHA-512/224"), SHA_512_256("SHA-512/256"), SHA3_224("SHA3-224"), SHA3_256("SHA3-256"),
	SHA3_384("SHA3-384"), SHA3_512("SHA3-512"),
	// Bouncy Castle
	MD4("MD4"), Whirlpool("Whirlpool"), Tiger("Tiger"), GOST3411("GOST3411"), RIPEMD160("RIPEMD160");

	private final String hashAlgorithm;

	private EHashAlgorithm(String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}

	public String getHashAlgorithm() {
		return hashAlgorithm;
	}

	public static EHashAlgorithm get(String hashAlgorithm) {
		for (EHashAlgorithm type : values()) {
			if (type.getHashAlgorithm().equalsIgnoreCase(hashAlgorithm)) {
				return type;
			}
		}
		return null;
	}

	public boolean isJavaHashAlgorithmInstances() {
		switch (this) {
		case MD4:
			return false;
		case Whirlpool:
			return false;
		case Tiger:
			return false;
		case GOST3411:
			return false;
		case RIPEMD160:
			return false;
		default:
			return true;
		}
	}
}
