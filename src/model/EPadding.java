package model;

import java.util.Arrays;
import java.util.List;

public enum EPadding {
    NO_PADDING("NoPadding"),
    PKCS1_PADDING("PKCS1Padding"),
    OAEP_PADDING("OAEPPadding"),
    PKCS5_PADDING("PKCS5Padding"),
    PKCS7_PADDING("PKCS7Padding"),
    ISO_IEC_7816_4("ISO7816-4Padding"),
    //
    ZERO_PADDING("ZeroPadding"),
    ANSI_X_923("ANSIX923Padding");

    private final String paddingName;

    EPadding(String paddingName) {
        this.paddingName = paddingName;
    }

    public String getPaddingName() {
        return paddingName;
    }

    public static List<EPadding> getSupportedPadding(EAlgorithmType algorithmType) {
        switch (algorithmType) {
        case AES:
        case DES:
        case TripleDES:
        case Blowfish:
        case Twofish:
        case Serpent:
        case Camellia:
            return Arrays.asList(PKCS5_PADDING, PKCS7_PADDING, ISO_IEC_7816_4, ZERO_PADDING, ANSI_X_923, NO_PADDING);
        case RSA:
        case RSA_AES:
            return Arrays.asList(PKCS1_PADDING, OAEP_PADDING);
        case RC4:
        case ChaCha20:
            return Arrays.asList(NO_PADDING);
        default:
            return Arrays.asList(NO_PADDING);
        }
    }
}
