package model.basic_symmetric;

import model.EAlgorithmType;

public abstract class ABasicSymmetric {
	protected static final String ALPHABET = "AĂÂBCDĐEÊGHIKLMNOÔƠPQRSTUƯVXYaăâbcdđeêghiklmnôơpqrsưuvxy";
	protected CipherConfig config;
	protected String key;

	public ABasicSymmetric(CipherConfig config) {
		this.config = config;
	}

	public abstract String encrypt(String plainText);

	public abstract String decrypt(String cipherText);

	public abstract EAlgorithmType type();
}