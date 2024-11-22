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
