package plugin.base32;
import org.apache.commons.codec.binary.Base32;
import sample.BaseObj;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PlugBase32 implements BaseObj {

    @Override
    public void encode(String fileName) throws IOException {
        Base32 base32 = new Base32();
        byte[] buffer = Files.readAllBytes(Paths.get(fileName));
        byte[] encodeString = base32.encode(buffer);
        Files.write(Paths.get(fileName),encodeString);
    }

    @Override
    public void decode(String fileName) throws IOException{
        Base32 base32 = new Base32();
        byte[] buffer = Files.readAllBytes(Paths.get(fileName));
        byte[] decodeByte = base32.decode(buffer);
        Files.write(Paths.get(fileName),decodeByte);
    }
}
