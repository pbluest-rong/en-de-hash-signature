package model.basic_symmetric;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CipherConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	private int shiftCipherShift;// key là số lần dịch chuyển: 4
	private int affineCipherA;// 5
	private int affineCipherB;// 9
	private Map<Character, Character> substitutionMap = new HashMap<>();// key là 1 chuỗi hoán vị
	// Mỗi ký tự trong plaintext được mã hóa dựa trên phép dịch ký tự theo key tương
	// ứng.
	private String vigenereKey;
	// Nếu key là [3,1,4,2], ký tự thứ 1 sẽ chuyển đến vị trí thứ 3, ký tự thứ 2
	// chuyển đến vị trí thứ 1,...
	private int[] permutationKey = { 2, 0, 3, 1 };
	// 1 ma trận n x n với các phần tử là số nguyên modulo kích thước bảng chữ cái
	// Điều kiện: Determinant của ma trận phải khả nghịch modulo kích thước bảng chữ
	// cái.
	private int[][] hillCipherKeyMatrix;

	public int getShiftCipherShift() {
		return shiftCipherShift;
	}

	public int getAffineCipherA() {
		return affineCipherA;
	}

	public int getAffineCipherB() {
		return affineCipherB;
	}

	public Map<Character, Character> getSubstitutionMap() {
		return substitutionMap;
	}

	public String getVigenereKey() {
		return vigenereKey;
	}

	public int[] getPermutationKey() {
		return permutationKey;
	}

	public int[][] getHillCipherKeyMatrix() {
		return hillCipherKeyMatrix;
	}

	// Tạo key cho Shift Cipher
	public void generateShiftCipherKey() {
		Random random = new Random();
		this.shiftCipherShift = random.nextInt(26); // Giá trị từ 0 đến 25
	}

	public void generateAffineCipherKey() {
		int alphabetSize = ABasicSymmetric.ALPHABET.length(); // Độ dài bảng chữ cái
		Random random = new Random();
		do {
			this.affineCipherA = random.nextInt(alphabetSize); // Giá trị từ 0 đến alphabetSize - 1
		} while (gcd(this.affineCipherA, alphabetSize) != 1 || !hasModularInverse(this.affineCipherA, alphabetSize));
		this.affineCipherB = random.nextInt(alphabetSize); // Giá trị từ 0 đến alphabetSize - 1
	}

	private boolean hasModularInverse(int a, int m) {
		try {
			modularInverse(a, m);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private int modularInverse(int a, int m) {
		a = (a % m + m) % m; // Đảm bảo a không âm
		for (int x = 1; x < m; x++) {
			if ((a * x) % m == 1) {
				return x;
			}
		}
		throw new IllegalArgumentException("No modular inverse found for a = " + a);
	}

	// Tạo key cho Substitution Cipher
	public void generateSubstitutionCipherKey() {
		Random random = new Random();
		List<Character> characters = new ArrayList<>();
		for (char c : ABasicSymmetric.ALPHABET.toCharArray()) {
			characters.add(c);
		}
		Collections.shuffle(characters); // Hoán vị bảng chữ cái
		Map<Character, Character> substitutionMap = new HashMap<>();
		for (int i = 0; i < characters.size(); i++) {
			substitutionMap.put(ABasicSymmetric.ALPHABET.charAt(i), characters.get(i));
		}
		this.substitutionMap = substitutionMap;
	}

	// Tạo key cho Vigenère Cipher
	public void generateVigenereCipherKey() {
		Random random = new Random();
		int keyLength = random.nextInt(10) + 5; // Độ dài key từ 5 đến 15
		StringBuilder vigenereKey = new StringBuilder();
		for (int i = 0; i < keyLength; i++) {
			vigenereKey.append(ABasicSymmetric.ALPHABET.charAt(random.nextInt(ABasicSymmetric.ALPHABET.length())));
		}
		this.vigenereKey = vigenereKey.toString();
	}

	// Tạo key cho Permutation Cipher
	public void generatePermutationCipherKey() {
		Random random = new Random();
		int permLength = 4; // Độ dài khối là 4 (có thể thay đổi)
		int[] permutationKey = new int[permLength];
		List<Integer> positions = new ArrayList<>();
		for (int i = 0; i < permLength; i++) {
			positions.add(i);
		}
		Collections.shuffle(positions); // Hoán vị các vị trí
		for (int i = 0; i < permLength; i++) {
			permutationKey[i] = positions.get(i);
		}
		this.permutationKey = permutationKey;
	}

	// Tạo key cho Hill Cipher
	public void generateHillCipherKey() {
		int[][][] FIXED_KEYS = { { { 6, 24, 1 }, { 13, 16, 10 }, { 20, 17, 15 } } };
		Random rand = new Random();
		this.hillCipherKeyMatrix = FIXED_KEYS[rand.nextInt(FIXED_KEYS.length)];
	}

	// Helper: Tính GCD
	private int gcd(int a, int b) {
		if (b == 0)
			return a;
		return gcd(b, a % b);
	}
	
}
