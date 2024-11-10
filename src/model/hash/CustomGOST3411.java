package model.hash;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;

import org.bouncycastle.jcajce.provider.digest.GOST3411;

public class CustomGOST3411 extends ACustomHashAlgorithm {
    @Override
    public String hash(String data) throws Exception {
        MessageDigest digest = new GOST3411.Digest();
        byte[] hashBytes = digest.digest(data.getBytes("UTF-8"));
        return bytesToHex(hashBytes);
    }

    @Override
    public String hashFile(String filePath) throws Exception {
        MessageDigest digest = new GOST3411.Digest();
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        byte[] hashBytes = digest.digest(fileBytes);
        return bytesToHex(hashBytes);
    }
}
