package model.hash;

public interface IHash {
	public void setHashAlgorithm(EHashAlgorithm hashAlgorithm);

	String hash(String data) throws Exception;

	String hashFile(String filePath) throws Exception;

	boolean supportsJavaHash();

	public static void main(String[] args) throws Exception {
		EHashAlgorithm[] type = new EHashAlgorithm[] { EHashAlgorithm.MD2, EHashAlgorithm.MD5, EHashAlgorithm.SHA_1,
				EHashAlgorithm.SHA_224, EHashAlgorithm.SHA_256, EHashAlgorithm.SHA_512, EHashAlgorithm.SHA_512_224,
				EHashAlgorithm.SHA_512_256, EHashAlgorithm.SHA3_224, EHashAlgorithm.SHA3_256, EHashAlgorithm.SHA3_384,
				EHashAlgorithm.SHA3_512, EHashAlgorithm.HAVAL, EHashAlgorithm.RIPEMD160, EHashAlgorithm.Whirlpool,
				EHashAlgorithm.Tiger, EHashAlgorithm.MD4, EHashAlgorithm.GOST3411 };

		String message = "Lê Bá Phụng";
		String filePath = "resources/input/text_input.txt";
		EHashAlgorithm hashAlgorithm = EHashAlgorithm.MD5;
		IHash hash = new JavaHash(hashAlgorithm);
		for (EHashAlgorithm e : type) {
			if (e.isJavaHashAlgorithmInstances()) {
				hashAlgorithm = e;
				if (!hash.supportsJavaHash())
					hash = new JavaHash(hashAlgorithm);

				hash.setHashAlgorithm(hashAlgorithm);
			} else if (!e.isJavaHashAlgorithmInstances()) {
				hashAlgorithm = e;
				hash = new CustomHashAlgorithm(hashAlgorithm);
			}
			System.out.println(hashAlgorithm.getHashAlgorithm() + " => " + hash.hash(message));
			System.out.println(hashAlgorithm.getHashAlgorithm() + " => " + hash.hashFile(filePath));
			System.out.println();
		}
	}
}
