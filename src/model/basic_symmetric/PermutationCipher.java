package model.basic_symmetric;

import model.EAlgorithmType;

public class PermutationCipher extends ABasicSymmetric {

    public PermutationCipher(CipherConfig config) {
        super(config);
    }

    @Override
    public String encrypt(String plainText) {
        int[] permutationKey = config.getPermutationKey();
        int blockSize = permutationKey.length;

        if (blockSize == 0) {
            throw new IllegalArgumentException("Permutation key cannot be empty.");
        }

        // Add padding if needed
        plainText = addPadding(plainText, blockSize);
        StringBuilder cipherText = new StringBuilder();

        for (int i = 0; i < plainText.length(); i += blockSize) {
            StringBuilder block = new StringBuilder();
            for (int j = 0; j < blockSize; j++) {
                block.append(plainText.charAt(i + permutationKey[j]));
            }
            cipherText.append(block);
        }

        return cipherText.toString();
    }

    @Override
    public String decrypt(String cipherText) {
        int[] permutationKey = config.getPermutationKey();
        int blockSize = permutationKey.length;

        if (blockSize == 0) {
            throw new IllegalArgumentException("Permutation key cannot be empty.");
        }

        StringBuilder plainText = new StringBuilder();

        for (int i = 0; i < cipherText.length(); i += blockSize) {
            char[] block = new char[blockSize];
            for (int j = 0; j < blockSize; j++) {
                block[permutationKey[j]] = cipherText.charAt(i + j);
            }
            plainText.append(new String(block));
        }

        return plainText.toString().replace("?", ""); // Remove padding
    }

    private String addPadding(String text, int blockSize) {
        int paddingNeeded = blockSize - (text.length() % blockSize);
        if (paddingNeeded < blockSize) {
            text += "?".repeat(paddingNeeded); // Add padding with the '?' character
        }
        return text;
    }
    @Override
    public EAlgorithmType type() {
    	return EAlgorithmType.Permutation_Cipher;
    }
}
