package model.basic_symmetric;

import java.util.HashMap;
import java.util.Map;

public class CipherConfig {
    private int shiftCipherShift = 5; // 0-25
    private int affineCipherA = 5; // 0-25
    private int affineCipherB = 9; // 0-25
    private Map<Character, Character> substitutionMap = new HashMap<>();
    private String vigenereKey = "pblues";
    private int[] permutationKey = { 2, 0, 3, 1 };
    private int[][] hillCipherKeyMatrix = { { 1, 3 }, { 1, 5 } };

    public int getShiftCipherShift() {
        return shiftCipherShift;
    }

    public void setShiftCipherShift(int shiftCipherShift) {
        if (shiftCipherShift >= 0 && shiftCipherShift <= 25) {
            this.shiftCipherShift = shiftCipherShift;
        }
    }

    public int getAffineCipherA() {
        return affineCipherA;
    }

    public void setAffineCipherA(int affineCipherA) {
        if (affineCipherA >= 0 && affineCipherA <= 25) {
            this.affineCipherA = affineCipherA;
        }
    }

    public int getAffineCipherB() {
        return affineCipherB;
    }

    public void setAffineCipherB(int affineCipherB) {
        if (affineCipherB >= 0 && affineCipherB <= 25) {
            this.affineCipherB = affineCipherB;
        }
    }

    public Map<Character, Character> getSubstitutionMap() {
        return substitutionMap;
    }

    public void setSubstitutionMap(Map<Character, Character> substitutionMap) {
        this.substitutionMap = substitutionMap;
    }

    public String getVigenereKey() {
        return vigenereKey;
    }

    public void setVigenereKey(String vigenereKey) {
        this.vigenereKey = vigenereKey;
    }

    public int[] getPermutationKey() {
        return permutationKey;
    }

    public void setPermutationKey(int[] permutationKey) {
        this.permutationKey = permutationKey;
    }

    public int[][] getHillCipherKeyMatrix() {
        return hillCipherKeyMatrix;
    }

    public void setHillCipherKeyMatrix(int[][] hillCipherKeyMatrix) {
        // Kiểm tra tính khả nghịch của ma trận khóa
        if (isInvertible(hillCipherKeyMatrix)) {
            this.hillCipherKeyMatrix = hillCipherKeyMatrix;
        } else {
            throw new IllegalArgumentException("Ma trận khóa không khả nghịch.");
        }
    }

    // Phương thức kiểm tra tính khả nghịch của ma trận
    private boolean isInvertible(int[][] matrix) {
        // Tính toán định thức và kiểm tra
        // Đảm bảo rằng ma trận là 2x2
    	 if (matrix.length != 2 || matrix[0].length != 2) {
             return false;
         }

         int determinant = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
         return determinant != 0;
    }
}
