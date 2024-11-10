package model.hash;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HavalHash extends ACustomHashAlgorithm {

	private static final int BLOCK_SIZE = 128;
	private static final int HASH_SIZE = 128;

	@Override
	public String hash(String data) throws Exception {
		// Hàm hash chuỗi đầu vào
		return processHash(data.getBytes());
	}

	@Override
	public String hashFile(String filePath) throws Exception {
		// Hàm hash tệp tin
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException("File not found: " + filePath);
		}

		try (FileInputStream fis = new FileInputStream(file)) {
			byte[] buffer = new byte[1024];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesRead);
			}
			return processHash(baos.toByteArray());
		}
	}

	private String processHash(byte[] inputData) throws NoSuchAlgorithmException {
		// Khởi tạo các biến
		byte[] message = inputData;
		int numRounds = 5; // Số vòng lặp (3, 4, 5 vòng)
		byte[] hashValue = new byte[HASH_SIZE / 8]; // Kích thước hash, ví dụ: 128 bit = 16 byte

		// Bắt đầu quá trình băm (ở đây ta chỉ minh họa đơn giản với MessageDigest của
		// Java)
		MessageDigest md = MessageDigest.getInstance("SHA-256"); // Cơ bản là SHA-256, cần thay đổi để xử lý HAVAL
		md.update(message);
		byte[] digest = md.digest();

		// Quá trình tính toán qua các vòng lặp (đơn giản hóa, HAVAL có quy trình phức
		// tạp hơn)
		for (int round = 0; round < numRounds; round++) {
			md.update(digest);
			digest = md.digest();
		}

		// Chuyển giá trị băm sang dạng chuỗi hexa
		StringBuilder sb = new StringBuilder();
		for (byte b : digest) {
			sb.append(String.format("%02x", b));
		}

		return sb.toString();
	}
}
