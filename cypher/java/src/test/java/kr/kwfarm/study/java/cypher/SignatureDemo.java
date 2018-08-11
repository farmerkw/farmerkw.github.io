package kr.kwfarm.study.java.cypher;

import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import java.security.*;

@Slf4j
public class SignatureDemo {
    private final String PLAIN_TEXT = "안녕하세요. 메롱입니다.";
    @Test
    void instance() throws NoSuchAlgorithmException {
        // <해시함수>with<서명알고리즘> 형식으로 알고리즘 전달
        Signature.getInstance("SHA1withRSA");
    }

    @Test
    void test() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // signin
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(PLAIN_TEXT.getBytes());

        byte[] signatureData = sign.sign();

        // verify
        Signature verify = Signature.getInstance("SHA256withRSA");
        verify.initVerify(publicKey);
        verify.update(PLAIN_TEXT.getBytes());

        log.info("{}", verify.verify(signatureData));

        // byte 하나만 변경
        byte[] bs = new byte[signatureData.length];
        for (int i = 1; i < signatureData.length; i++) {
            bs[i] = signatureData[i];
        }
        bs[0] = (byte)1;


        log.info("{}", verify.verify(signatureData));
    }
}
