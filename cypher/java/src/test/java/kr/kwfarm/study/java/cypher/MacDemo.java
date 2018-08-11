package kr.kwfarm.study.java.cypher;

import com.google.common.io.BaseEncoding;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MacDemo {
    @Test
    void hmacSha1() throws NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        SecretKey secretKey = keyGenerator.generateKey();

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);

        String plainText = "안녕하세요. 잘 살아요";
        byte[] bytes = mac.doFinal(plainText.getBytes());

        log.info(BaseEncoding.base16().encode(bytes));
    }
}
