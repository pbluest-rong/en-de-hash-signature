package model.basic_symmetric;

import model.EAlgorithmType;

public class VigenereCipher extends ABasicSymmetric {

	public VigenereCipher(CipherConfig config) {
		super(config);
	}

	@Override
	public String encrypt(String plainText) {
		String key = config.getVigenereKey();
		StringBuilder cipherText = new StringBuilder();
		int keyIndex = 0;

		for (char ch : plainText.toCharArray()) {
			int index = ALPHABET.indexOf(ch);
			if (index != -1) {
				int shift = ALPHABET.indexOf(key.charAt(keyIndex % key.length()));
				index = (index + shift) % ALPHABET.length();
				cipherText.append(ALPHABET.charAt(index));
				keyIndex++;
			} else {
				cipherText.append(ch);
			}
		}
		return cipherText.toString();
	}

	 @Override
	    public String decrypt(String cipherText) {
	        String key = config.getVigenereKey();
	        StringBuilder plainText = new StringBuilder();
	        int keyIndex = 0;
	        
	        for (char ch : cipherText.toCharArray()) {
	            int index = ALPHABET.indexOf(ch);
	            if (index != -1) {
	                int shift = ALPHABET.indexOf(key.charAt(keyIndex % key.length()));
	                index = (index - shift + ALPHABET.length()) % ALPHABET.length();
	                plainText.append(ALPHABET.charAt(index));
	                keyIndex++;
	            } else {
	                plainText.append(ch);
	            }
	        }
	        return plainText.toString();
	    }

	@Override
	public EAlgorithmType type() {
		return EAlgorithmType.Vigenere_Cipher;
	}
}
