package kr.kwfarm.study.java.cypher;

import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * PBE(password-based encryption)은 패스워드 기반의 암호화 방식으로 입력받은 패스워드에 기반하여 비밀키를 만든다음
 * 생성한 비밀키를 사용하여 암호화 한다.
 * PKCS#5와 PKCS#12에 패스워드를 사용한 비밀키 생성 방법이 정의되어 있다.
 *
 * PKCS#5 기준으로 설명하면 PBE는 PBKDF(password-based key derivation function)라는 패스워드 기반 키 파생 함수를
 * 사용하여 비밀키를 생성한 후 암/복호화를 처리한다.
 * 현재는 PBES1과 PBES2라는 두 가지 방법이 존재한다.
 */
@Slf4j
public class PasswordDemo {
    private static final String PLAIN_TEXT = "안녕하세요. 방가워요";

    @Test
    void pbes1() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String transformation = "PBEWithMD5AndDES";

        char[] password = " ".toCharArray();

        // salt 생성
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[8];
        secureRandom.nextBytes(salt);

        // 반복 횟수
        int iterationCount = 1000;

        // KEY 생성
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
        SecretKey secretKey = SecretKeyFactory.getInstance(transformation).generateSecret(pbeKeySpec);

        // 암호화
        PBEParameterSpec params = new PBEParameterSpec(salt, iterationCount);
        Cipher encrypt = Cipher.getInstance(transformation);
        encrypt.init(Cipher.ENCRYPT_MODE, secretKey, params);
        byte[] bs = encrypt.doFinal(PLAIN_TEXT.getBytes());


        // 복호화
        Cipher decrypt = Cipher.getInstance(transformation);
        decrypt.init(Cipher.DECRYPT_MODE, secretKey, params);
        byte[] bytes = decrypt.doFinal(bs);

        MatcherAssert.assertThat(PLAIN_TEXT, Is.is(new String(bytes)));
    }

    @Test
    void pbes2() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        /**
         * pbes1과 기본 원리는 같지만 해시 함수 대신 의사 난수 함수(PRF, Pseudo Random Function)로 비밀키를 생성한다.
         * 일반적으로 PRF에 HMAC등을 사용 한다.
         */
        String transformation = "AES/ECB/PKCS5Padding";

        char[] password = "mynameiskhan".toCharArray();

        // salt
        byte[] salt = new byte[8];
        new SecureRandom().nextBytes(salt);

        // count
        int iterationCount = 1000;

        // key 생성
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, iterationCount, 128);
        SecretKey secretKey = new SecretKeySpec(keyFactory.generateSecret(pbeKeySpec).getEncoded(), "AES");

        // 암호화
        Cipher encrypt = Cipher.getInstance(transformation);
        encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] bs = encrypt.doFinal(PLAIN_TEXT.getBytes());

        // 복호화
        Cipher decrypt = Cipher.getInstance(transformation);
        decrypt.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] bytes = decrypt.doFinal(bs);

        MatcherAssert.assertThat(PLAIN_TEXT, Is.is(new String(bytes)));
    }
}
