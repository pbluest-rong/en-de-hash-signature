package model;

public enum EKeySize {
	DES_56(56), // 7 byte
	TripleDES_168(168), // 56 byte * 3
	AES_128(128), // 16 byte
	AES_192(192), // 24 byte
	AES_256(256), // 32 byte
	Blowfish_32(32), // 32-448 byte
	Blowfish_128(128), Blowfish_256(256), Blowfish_448(448), RC4_40(40), RC4_128(128), ChaCha20_256(256),
	//
	RSA_1024(1024), RSA_2048(2048), RSA_3072(3072), RSA_4096(4096),
	// bouncy castle
	Twofish_128(128), Twofish_192(192), Twofish_256(256),

	Serpent_128(128), Serpent_Twofish_192(192), Serpent_256(256),

	CAST_40(40), CAST_128(128), CAST_256(256),

	Camellia_128(128), Camellia_192(292), Camellia_256(256);

	private final int bits;

	EKeySize(int bits) {
		this.bits = bits;
	}

	public int getBits() {
		return bits;
	}
}
