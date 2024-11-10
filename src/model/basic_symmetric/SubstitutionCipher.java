package model.basic_symmetric;

import java.util.HashMap;
import java.util.Map;

import model.EAlgorithmType;

public class SubstitutionCipher extends ABasicSymmetric {
	private Map<Character, Character> substitutionMap = new HashMap<>();
	private Map<Character, Character> reverseSubstitutionMap = new HashMap<>();

	public SubstitutionCipher(CipherConfig config) {
		super(config);
		setSubstitutionMap(config.getSubstitutionMap());
	}

	public void setSubstitutionMap(Map<Character, Character> customSubstitutionMap) {
		this.substitutionMap = customSubstitutionMap;
		reverseSubstitutionMap.clear();
		for (Map.Entry<Character, Character> entry : customSubstitutionMap.entrySet()) {
			reverseSubstitutionMap.put(entry.getValue(), entry.getKey());
		}
	}

	public void generateSubstitutionMap() {
		if (key == null || key.length() < ALPHABET.length()) {
			throw new IllegalStateException("Key must be generated and long enough to map all alphabet characters.");
		}

		substitutionMap.clear();
		reverseSubstitutionMap.clear();

		for (int i = 0; i < ALPHABET.length(); i++) {
			char plainChar = ALPHABET.charAt(i);
			char cipherChar = key.charAt(i);
			substitutionMap.put(plainChar, cipherChar);
			reverseSubstitutionMap.put(cipherChar, plainChar);
		}
	}

	@Override
	public String encrypt(String plainText) {
		StringBuilder cipherText = new StringBuilder();
		for (char ch : plainText.toCharArray()) {
			cipherText.append(substitutionMap.getOrDefault(ch, ch));
		}
		return cipherText.toString();
	}

	@Override
	public String decrypt(String cipherText) {
		StringBuilder plainText = new StringBuilder();
		for (char ch : cipherText.toCharArray()) {
			plainText.append(reverseSubstitutionMap.getOrDefault(ch, ch));
		}
		return plainText.toString();
	}
	 @Override
	    public EAlgorithmType type() {
	    	return EAlgorithmType.Substitution_Cipher;
	    }
}
