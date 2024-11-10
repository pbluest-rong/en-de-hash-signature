package model.hash;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JavaHash implements IHash {
	private EHashAlgorithm algorithm;

	public JavaHash(EHashAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	@Override
	public String hash(String data) throws Exception {
		byte[] bytes = data.getBytes();
		MessageDigest messageDigest = MessageDigest.getInstance(this.algorithm.getHashAlgorithm());
		byte[] digest = messageDigest.digest(bytes);
		BigInteger res = new BigInteger(1, digest);
		return res.toString(16);
	}

	@Override
	public String hashFile(String filePath) throws Exception {
		File file = new File(filePath);
		if (!file.exists())
			return null;

		MessageDigest messageDigest = MessageDigest.getInstance(this.algorithm.getHashAlgorithm());
		DigestInputStream di = new DigestInputStream(new BufferedInputStream(new FileInputStream(file)), messageDigest);
		byte[] buff = new byte[1024];
		int read;
		do {
			read = di.read(buff);
		} while (read != -1);

		byte[] digest = di.getMessageDigest().digest();
		BigInteger res = new BigInteger(1, digest);
		return res.toString(16);
	}

	@Override
	public void setHashAlgorithm(EHashAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	@Override
	public boolean supportsJavaHash() {
		return true;
	}
}
