package model.hash;

public abstract class ACustomHashAlgorithm {
	// HAVAL, RIPEMD, Blake2, Whirlpool,
	// Tiger, Skein, GOST, MD4
	/// BLAKE, MurmurHash, CityHash và FarmHash

	public abstract String hash(String data) throws Exception;

	public abstract String hashFile(String filePath) throws Exception;

	protected String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}
