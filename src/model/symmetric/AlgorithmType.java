package model.symmetric;

public enum AlgorithmType {
	AES("AES"),
    DES("DES"),
    TripleDES("DESede"),
    Blowfish("Blowfish"),
    Twofish("Twofish"),
    RC4("RC4"),
    ChaCha20("ChaCha20"),
    RSA("RSA");
    
    private final String algorithm;

    AlgorithmType(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}