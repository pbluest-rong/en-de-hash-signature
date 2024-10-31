package model;

public enum KeySize {
    DES_56(56),//7 byte
    TripleDES_168(168),//56 bit * 3
	AES_128(128),//16 bit
    AES_192(192),//24 bit
    AES_256(256),//32 bit
    Blowfish_32(32),// 32-448 bit
    Blowfish_128(128),
    Blowfish_256(256),
    Blowfish_448(448),
    Twofish_128(128),
    Twofish_192(192),
    Twofish_256(256),
    RC4_40(40),
    RC4_128(128), 
    ChaCha20_256(256),
    RSA_1024(1024),
    RSA_2048(2048),
    RSA_3072(3072),
    RSA_4096(4096);
	
    private final int bits;

    KeySize(int bits) {
        this.bits = bits;
    }

    public int getBits() {
        return bits;
    }
}
