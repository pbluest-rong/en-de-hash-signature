package model.hash;

public class CustomHashAlgorithm implements IHash {
	private ACustomHashAlgorithm customHashAlgorithm;

	public CustomHashAlgorithm(EHashAlgorithm algorithm) {
		super();
		chooseHashAlgorithm(algorithm);
	}

	@Override
	public void setHashAlgorithm(EHashAlgorithm hashAlgorithm) {
		chooseHashAlgorithm(hashAlgorithm);
	}

	@Override
	public String hash(String data) throws Exception {
		return this.customHashAlgorithm.hash(data);
	}

	@Override
	public String hashFile(String filePath) throws Exception {
		return this.customHashAlgorithm.hashFile(filePath);
	}

	private void chooseHashAlgorithm(EHashAlgorithm algorithm) {
		switch (algorithm) {
		case EHashAlgorithm.MD4: {
			this.customHashAlgorithm = new CustomMD4();
			break;
		}
		case EHashAlgorithm.RIPEMD160: {
			this.customHashAlgorithm = new CustomRIPEMD160();
			break;
		}
		case EHashAlgorithm.Whirlpool: {
			this.customHashAlgorithm = new CustomWhirlpool();
			break;
		}
		case EHashAlgorithm.Tiger: {
			this.customHashAlgorithm = new CustomTiger();
			break;
		}
		case EHashAlgorithm.GOST3411: {
			this.customHashAlgorithm = new CustomGOST3411();
			break;
		}
		default:
			throw new IllegalArgumentException("Không hỗ trợ thuật toán băm: " + algorithm);
		}
	}

	@Override
	public boolean supportsJavaHash() {
		return false;
	}
}
