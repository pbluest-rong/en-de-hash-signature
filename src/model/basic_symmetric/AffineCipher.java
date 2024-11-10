package model.basic_symmetric;

import model.EAlgorithmType;

public class AffineCipher extends ABasicSymmetric {

    public AffineCipher(CipherConfig config) {
        super(config);
    }

    @Override
    public String encrypt(String plainText) {
        int a = config.getAffineCipherA();
        int b = config.getAffineCipherB();
        StringBuilder cipherText = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            int index = ALPHABET.indexOf(c);
            if (index != -1) {
                int newIndex = (a * index + b) % ALPHABET.length();
                cipherText.append(ALPHABET.charAt(newIndex));
            } else {
                cipherText.append(c); // Giữ nguyên ký tự không nằm trong ALPHABET
            }
        }
        return cipherText.toString();
    }

    @Override
    public String decrypt(String cipherText) {
        int a = config.getAffineCipherA();
        int b = config.getAffineCipherB();
        StringBuilder plainText = new StringBuilder();
        int aInverse = modularInverse(a, ALPHABET.length());

        for (char c : cipherText.toCharArray()) {
            int index = ALPHABET.indexOf(c);
            if (index != -1) {
                int newIndex = (aInverse * (index - b + ALPHABET.length())) % ALPHABET.length();
                plainText.append(ALPHABET.charAt(newIndex));
            } else {
                plainText.append(c); // Giữ nguyên ký tự không nằm trong ALPHABET
            }
        }
        return plainText.toString();
    }

    private int modularInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        throw new IllegalArgumentException("No modular inverse found for a = " + a);
    }

    private boolean areCoprime(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a == 1;
    }

    @Override
    public EAlgorithmType type() {
        return EAlgorithmType.Affine_Cipher;
    }
}
