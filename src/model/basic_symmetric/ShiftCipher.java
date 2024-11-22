package model.basic_symmetric;

import model.EAlgorithmType;
public class ShiftCipher extends ABasicSymmetric {

    public ShiftCipher(CipherConfig config) {
        super(config);
    }

    @Override
    public String encrypt(String plainText) {
        int shift = config.getShiftCipherShift();
        StringBuilder cipherText = new StringBuilder();
        for (char ch : plainText.toCharArray()) {
            int index = ALPHABET.indexOf(ch);
            if (index != -1) {
                index = (index + shift) % ALPHABET.length();
                ch = ALPHABET.charAt(index);
            }
            cipherText.append(ch);
        }
        return cipherText.toString();
    }

    @Override
    public String decrypt(String cipherText) {
        int shift = config.getShiftCipherShift();
        StringBuilder plainText = new StringBuilder();
        for (char ch : cipherText.toCharArray()) {
            int index = ALPHABET.indexOf(ch);
            if (index != -1) {
                index = (index - shift + ALPHABET.length()) % ALPHABET.length();
                ch = ALPHABET.charAt(index);
            }
            plainText.append(ch);
        }
        return plainText.toString();
    }

    @Override
    public EAlgorithmType type() {
        return EAlgorithmType.Shift_Cipher;
    }
}