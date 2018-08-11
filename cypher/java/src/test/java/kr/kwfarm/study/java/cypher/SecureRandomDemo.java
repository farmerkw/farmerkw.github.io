package kr.kwfarm.study.java.cypher;

import com.google.common.io.BaseEncoding;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Slf4j
public class SecureRandomDemo {
    @Test
    void test() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        log.info("{}", secureRandom.getAlgorithm());
        byte[] bytes = new byte[10];
        secureRandom.nextBytes(bytes);
        log.info(byteArrayToHex(bytes));
        log.info(BaseEncoding.base16().lowerCase().encode(bytes));
    }

    String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for(final byte b: a)
            sb.append(String.format("%02x ", b&0xff));
        return sb.toString();
    }
}
