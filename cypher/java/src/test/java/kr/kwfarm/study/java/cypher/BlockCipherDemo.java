package kr.kwfarm.study.java.cypher;

import com.google.common.io.BaseEncoding;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@Slf4j
public class BlockCipherDemo {
    private static final String PLAIN_TEXT = "안녕하세요. 방가워요";

    @Test
    void keyGenerator() throws NoSuchAlgorithmException, InvalidKeySpecException {
        // keyGenerator을 이용 한 키 생성
        SecretKey keyGeneratorKey = getGenerateAesKey();
        log.info("{}", keyGeneratorKey.getEncoded().length);
        byte[] key = keyGeneratorKey.getEncoded();

        // 키를 불러와서 키 생성
        SecretKey specKey = new SecretKeySpec(key, "AES");
        assertThat(key, is(specKey.getEncoded()));
    }

    @Test
    void keyFactory() throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(56);

        SecretKey key1 = keyGenerator.generateKey();


        // KEYSPEC -> SecretKey
        DESKeySpec desKeySpec = new DESKeySpec(key1.getEncoded());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key2 = secretKeyFactory.generateSecret(desKeySpec);

        assertThat(key1.getEncoded(), is(key2.getEncoded()));
    }

    @Test
    void aesCipher() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 비밀키 생성
        SecretKey secretKey = getGenerateAesKey();

        // 암호화
        String transformation = "AES/ECB/PKCS5Padding";
        Cipher encrypt = Cipher.getInstance(transformation);
        encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] bs = encrypt.doFinal(PLAIN_TEXT.getBytes());

        // 복호화
        Cipher decrypt = Cipher.getInstance(transformation);
        decrypt.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] bytes = decrypt.doFinal(bs);

        assertThat(PLAIN_TEXT, is(new String(bytes)));
    }

    /**
     * CBC 운영 모드일 경우 iv을 생성하지 않아도 암호시에는 에러가 발생하지 않고 복호화 시에 에러 발생
     * 암호화 시점에는 iv가 없는 경우 자동으로 생성
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    @TestFactory
    Collection<DynamicTest> aesCbcCipher() throws NoSuchAlgorithmException {

        // 비밀키 생성
        SecretKey secretKey = getGenerateAesKey();

        String transformation = "AES/CBC/PKCS5Padding";
        return Arrays.asList(
                dynamicTest("iv 미 적용", () -> Assertions.assertThrows(InvalidKeyException.class, () -> {
                    // 암호화
                    Cipher encrypt = Cipher.getInstance(transformation);
                    encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
                    byte[] bs = encrypt.doFinal(PLAIN_TEXT.getBytes());

                    // 복호화
                    Cipher decrypt = Cipher.getInstance(transformation);
                    decrypt.init(Cipher.DECRYPT_MODE, secretKey);
                    byte[] bytes = decrypt.doFinal(bs);
                })),
                dynamicTest("자동 생성된 iv 가져오기", () -> {
                    // 암호화
                    Cipher encrypt = Cipher.getInstance(transformation);
                    encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
                    byte[] bs = encrypt.doFinal(PLAIN_TEXT.getBytes());

                    log.info("Auto Iv: {}", encrypt.getIV());
                    // 복호화
                    Cipher decrypt = Cipher.getInstance(transformation);
                    decrypt.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(encrypt.getIV()));
                    byte[] bytes = decrypt.doFinal(bs);

                    assertThat(PLAIN_TEXT, is(new String(bytes)));
                }),
                dynamicTest("명시적으로 IV 생성", () -> {
                    // IV 생성
                    SecureRandom secureRandom = new SecureRandom();
                    byte[] ivData = new byte[16];
                    secureRandom.nextBytes(ivData);
                    IvParameterSpec ivParameterSpec = new IvParameterSpec(ivData);

                    // 암호화
                    Cipher encrypt = Cipher.getInstance(transformation);
                    encrypt.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
                    byte[] bs = encrypt.doFinal(PLAIN_TEXT.getBytes());

                    // 복호화
                    Cipher decrypt = Cipher.getInstance(transformation);
                    decrypt.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
                    byte[] bytes = decrypt.doFinal(bs);

                    assertThat(PLAIN_TEXT, is(new String(bytes)));
                })
        );
    }

    @Test
    void aesCfbCipher() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        /**
         * iv가 필요하지만 패딩은 필요 없다
         * 키 스트림 즉, 이전 블록의 암호 알고리즘 출력값을 현재 암호 알고리즘에 입력한 다음 그 결과값을
         * 평문 블록과 xor 연산하여 암호문 블록을 생성하기 때문이다.
         * 이 키 스트림과 평문 블록을 몇 비트씩 XOR 연산할 것 인지 지정할 수 있어서 CFBn 형식으로 사용한다
         * CFB8: 8bit
         * SunJCE 프로바이더가 제공하는 ase의 기본값은 128비트로 CFB로 이정하면 CFB128로 동작한다.
         */
        // 비밀키 생성
        SecretKey secretKey = getGenerateAesKey();

        // IV 생성
        SecureRandom secureRandom = new SecureRandom();
        byte[] ivData = new byte[16]; // 128bit
        secureRandom.nextBytes(ivData);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivData);

        // 암호화
        String transformation = "AES/CFB128/NoPadding";
        Cipher encrypt = Cipher.getInstance(transformation);
        encrypt.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] bs = encrypt.doFinal(PLAIN_TEXT.getBytes());

        // 복호화
        Cipher decrypt = Cipher.getInstance(transformation);
        decrypt.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] bytes = decrypt.doFinal(bs);

        assertThat(PLAIN_TEXT, is(new String(bytes)));
    }

    private SecretKey getGenerateAesKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    @Test
    void aesCtrCipher() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        /**
         * CTR운영 모드에서는 넌스가 필요하다.
         * IV를 사용하지 않고 넌스를 사용하는 이유는 넌스의 유일성이 보장되어야 하기 때문이다.
         * CTR운영 모드는 키 스트림을 생성하기 위하여 매우 간단한 방법을 사용한다.
         * 넌스에 카운터 값을 붙이고 이를 암호화해 키 스트림을 만든다.
         * 따라서 넌스의 유일성이 보장되지 않는다면 다른 운영모드보다 공격에 취약해질 수 밖에 없다.
         */
        SecretKey secretKey = getGenerateAesKey();

        // IV 생성
        SecureRandom secureRandom = new SecureRandom();
        byte[] ivData = new byte[16];
        secureRandom.nextBytes(ivData);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivData);

        // 암호화
        String transformation = "AES/CTR/NoPadding";
        Cipher encrypt = Cipher.getInstance(transformation);
        encrypt.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] bs = encrypt.doFinal(PLAIN_TEXT.getBytes());

        // 복호화
        Cipher decrypt = Cipher.getInstance(transformation);
        decrypt.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] bytes = decrypt.doFinal(bs);

        assertThat(PLAIN_TEXT, is(new String(bytes)));
    }

    @Test
    void aesCCMCipher() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        /**
         * CCM/GCM 운영 모드는 암호문과 MAC이 결합된 형태이다.
         */
        Security.addProvider(new BouncyCastleProvider());

        SecretKey secretKey = getGenerateAesKey();

        // IV 생성
        SecureRandom secureRandom = new SecureRandom();
        byte[] ivData = new byte[13]; // 7 to 13 bytes
        secureRandom.nextBytes(ivData);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivData);

        // 암호화
        String transformation = "AES/CCM/NoPadding";
        Cipher encrypt = Cipher.getInstance(transformation);
        encrypt.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] bs = encrypt.doFinal(PLAIN_TEXT.getBytes());

        // 복호화
        Cipher decrypt = Cipher.getInstance(transformation);
        decrypt.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] bytes = decrypt.doFinal(bs);

        assertThat(PLAIN_TEXT, is(new String(bytes)));
    }

    @Test
    void seedCipher() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // SEED를 기본으로 제공하지 않기 때문에 Bouncy Castle Provider 추가
        Security.addProvider(new BouncyCastleProvider());

        // 키 생성
        KeyGenerator keyGenerator = KeyGenerator.getInstance("SEED");
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();

        // IV 생성
        SecureRandom secureRandom = new SecureRandom();
        byte[] ivData = new byte[16];
        secureRandom.nextBytes(ivData);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivData);

        // 암호화
        String transformation = "SEED/CBC/PKCS5Padding";
        Cipher encrypt = Cipher.getInstance(transformation);
        encrypt.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] bs = encrypt.doFinal(PLAIN_TEXT.getBytes());


        // 복호화
        Cipher decrypt = Cipher.getInstance(transformation);
        decrypt.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] bytes = decrypt.doFinal(bs);

        assertThat(PLAIN_TEXT, is(new String(bytes)));
    }

    @Test
    void aesStream() throws NoSuchAlgorithmException, URISyntaxException, IOException, NoSuchPaddingException, InvalidKeyException {
        SecretKey secretKey = getGenerateAesKey();

        Path buildPath = Paths.get(System.getProperty("user.dir"), "build");
        if (Files.exists(buildPath) == false) {
            Files.createDirectories(buildPath);
        }

        Path encryptPath = buildPath.resolve("encrypt.txt");
        Path decryptPath = buildPath.resolve("decrypt.txt");
        String transformation = "AES/ECB/PKCS5Padding";

        // 파일 암호화

        Cipher encrypt = Cipher.getInstance(transformation);
        encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
        try (InputStream is = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream("plain.txt"));
        CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(encryptPath.toFile()), encrypt)) {
            int read = 0;
            byte[] buffer = new byte[1024];
            while ((read = is.read(buffer)) != -1) {
                cos.write(buffer, 0, read);
            }
        }

        Cipher descrypt = Cipher.getInstance(transformation);
        descrypt.init(Cipher.DECRYPT_MODE, secretKey);

        try (InputStream is = new CipherInputStream(new BufferedInputStream(new FileInputStream(encryptPath.toFile())), descrypt);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(decryptPath.toFile()))) {
            int read = 0;
            byte[] buffer = new byte[1024];
            while((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
        }
    }
}
