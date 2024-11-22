package model.basic_symmetric;

import model.EAlgorithmType;

import java.util.Arrays;

public class HillCipher extends ABasicSymmetric {
	private static final String ALPHABET = "aáàạảãăắằặẳẵâấầậẩẫbcdđeèéẹẻẽêếềệểễfghiíìịỉĩjklmnoóòọỏõôốồộổỗơớờợởỡpqrstuúùụủũưứừựửữvwxyýỳỵỷỹAÁÀẠẢÃĂẮẰẶẲẴÂẤẦẬẨẪBCDĐEÉẸẺẼÊẾỀỆỂỄFGHIÍÌỊỈĨJKLMNOÓÒỌỎÕÔỐỒỘỔỖƠỚỜỢỞỠPQRSTUÚÙỤỦŨƯỨỪỰỬỮVWXYÝỲỴỶỸ0123456789`~!@#$%^&*()?,. ";
	private static final char PADDING_CHAR = '?';
	private static final int ALPHABET_SIZE = ALPHABET.length();

	public HillCipher(CipherConfig config) {
		super(config);
	}

	public HillCipher(int[][] keyMatrix) {
		super(new CipherConfig());
		if (!isValidKeyMatrix(keyMatrix)) {
			throw new IllegalArgumentException(
					"Key matrix is not valid. Determinant must be coprime with alphabet size.");
		}
	}

	// Kiểm tra xem ma trận khóa có hợp lệ không
	private boolean isValidKeyMatrix(int[][] matrix) {
		int determinant = calculateDeterminant(matrix);
		return gcd(determinant, ALPHABET.length()) == 1;
	}

	// Tính định thức của ma trận
	private int calculateDeterminant(int[][] matrix) {
		if (matrix.length == 2) {
			return (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]) % ALPHABET.length();
		} else if (matrix.length == 3) {
			return (matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1])
					- matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0])
					+ matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0])) % ALPHABET.length();
		}
		throw new UnsupportedOperationException(
				"Determinant calculation only supports 2x2 and 3x3 matrices for simplicity.");
	}

	// Tìm GCD (Ước số chung lớn nhất)
	private int gcd(int a, int b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	// Thêm padding nếu cần
	private String addPadding(String plaintext) {
		while (plaintext.length() % config.getHillCipherKeyMatrix().length != 0) {
			plaintext += PADDING_CHAR;
		}
		return plaintext;
	}

	// Chuyển ký tự sang chỉ số
	private int charToIndex(char c) {
		return ALPHABET.indexOf(c);
	}

	// Chuyển chỉ số sang ký tự
	private char indexToChar(int index) {
	    // Ensure the index is positive by using modulo
	    int adjustedIndex = (index % ALPHABET.length() + ALPHABET.length()) % ALPHABET.length();
	    return ALPHABET.charAt(adjustedIndex);
	}


	@Override
	public String encrypt(String plainText) {
		plainText = addPadding(plainText);
		StringBuilder ciphertext = new StringBuilder();

		for (int i = 0; i < plainText.length(); i += config.getHillCipherKeyMatrix().length) {
			int[] block = new int[config.getHillCipherKeyMatrix().length];
			for (int j = 0; j < config.getHillCipherKeyMatrix().length; j++) {
				block[j] = charToIndex(plainText.charAt(i + j));
			}
			int[] encryptedBlock = multiplyMatrixAndVector(config.getHillCipherKeyMatrix(), block);
			for (int index : encryptedBlock) {
				ciphertext.append(indexToChar(index % ALPHABET.length()));
			}
		}
		return ciphertext.toString();
	}

	@Override
	public String decrypt(String cipherText) {
		int[][] inverseKeyMatrix = invertKeyMatrix();
		StringBuilder plaintext = new StringBuilder();

		for (int i = 0; i < cipherText.length(); i += config.getHillCipherKeyMatrix().length) {
			int[] block = new int[config.getHillCipherKeyMatrix().length];
			for (int j = 0; j < config.getHillCipherKeyMatrix().length; j++) {
				block[j] = charToIndex(cipherText.charAt(i + j));
			}
			int[] decryptedBlock = multiplyMatrixAndVector(inverseKeyMatrix, block);
			for (int index : decryptedBlock) {
				plaintext.append(indexToChar((index + ALPHABET.length()) % ALPHABET.length()));
			}
		}
		return plaintext.toString().replace(String.valueOf(PADDING_CHAR), "");
	}

	// Nhân ma trận với vector
	private int[] multiplyMatrixAndVector(int[][] matrix, int[] vector) {
		int[] result = new int[vector.length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				result[i] += matrix[i][j] * vector[j];
			}
			result[i] %= ALPHABET.length();
		}
		return result;
	}

	// Tìm nghịch đảo của ma trận khóa
	private int[][] invertKeyMatrix() {
		if (config.getHillCipherKeyMatrix().length == 3) {
			int determinant = calculateDeterminant(config.getHillCipherKeyMatrix());
			int inverseDeterminant = modInverse(determinant, ALPHABET.length());

			// Tạo ma trận phụ hợp
			int[][] adjoint = new int[3][3];
			adjoint[0][0] = (config.getHillCipherKeyMatrix()[1][1] * config.getHillCipherKeyMatrix()[2][2]
					- config.getHillCipherKeyMatrix()[1][2] * config.getHillCipherKeyMatrix()[2][1])
					% ALPHABET.length();
			adjoint[0][1] = (config.getHillCipherKeyMatrix()[0][2] * config.getHillCipherKeyMatrix()[2][1]
					- config.getHillCipherKeyMatrix()[0][1] * config.getHillCipherKeyMatrix()[2][2])
					% ALPHABET.length();
			adjoint[0][2] = (config.getHillCipherKeyMatrix()[0][1] * config.getHillCipherKeyMatrix()[1][2]
					- config.getHillCipherKeyMatrix()[0][2] * config.getHillCipherKeyMatrix()[1][1])
					% ALPHABET.length();

			adjoint[1][0] = (config.getHillCipherKeyMatrix()[1][2] * config.getHillCipherKeyMatrix()[2][0]
					- config.getHillCipherKeyMatrix()[1][0] * config.getHillCipherKeyMatrix()[2][2])
					% ALPHABET.length();
			adjoint[1][1] = (config.getHillCipherKeyMatrix()[0][0] * config.getHillCipherKeyMatrix()[2][2]
					- config.getHillCipherKeyMatrix()[0][2] * config.getHillCipherKeyMatrix()[2][0])
					% ALPHABET.length();
			adjoint[1][2] = (config.getHillCipherKeyMatrix()[0][2] * config.getHillCipherKeyMatrix()[1][0]
					- config.getHillCipherKeyMatrix()[0][0] * config.getHillCipherKeyMatrix()[1][2])
					% ALPHABET.length();

			adjoint[2][0] = (config.getHillCipherKeyMatrix()[1][0] * config.getHillCipherKeyMatrix()[2][1]
					- config.getHillCipherKeyMatrix()[1][1] * config.getHillCipherKeyMatrix()[2][0])
					% ALPHABET.length();
			adjoint[2][1] = (config.getHillCipherKeyMatrix()[0][1] * config.getHillCipherKeyMatrix()[2][0]
					- config.getHillCipherKeyMatrix()[0][0] * config.getHillCipherKeyMatrix()[2][1])
					% ALPHABET.length();
			adjoint[2][2] = (config.getHillCipherKeyMatrix()[0][0] * config.getHillCipherKeyMatrix()[1][1]
					- config.getHillCipherKeyMatrix()[0][1] * config.getHillCipherKeyMatrix()[1][0])
					% ALPHABET.length();

			// Điều chỉnh giá trị về dương
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					adjoint[i][j] = (adjoint[i][j] * inverseDeterminant) % ALPHABET.length();
					if (adjoint[i][j] < 0) {
						adjoint[i][j] += ALPHABET.length();
					}
				}
			}

			return adjoint;
		}

		throw new UnsupportedOperationException("Matrix inversion only supports 2x2 and 3x3 matrices for simplicity.");
	}

	// Tìm nghịch đảo modulo
	private int modInverse(int a, int m) {
		a = a % m;
		for (int x = 1; x < m; x++) {
			if ((a * x) % m == 1)
				return x;
		}
		throw new ArithmeticException("No modular inverse found.");
	}

	@Override
	public EAlgorithmType type() {
		return EAlgorithmType.Hill_Cipher;
	}
}