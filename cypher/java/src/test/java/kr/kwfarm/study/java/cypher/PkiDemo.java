package kr.kwfarm.study.java.cypher;

import com.google.common.io.BaseEncoding;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.*;

import javax.crypto.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Slf4j
public class PkiDemo {
    private final String PLAIN_TEXT = "안녕하세요. 만나서 방갑습니다.";

    @Test
    void keyGenerator() throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 운영 모드와 패딩을 집어 넣지 않으면 RSA/ECB/PKCS1Padding 이 기본 값
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        // 초기화
        generator.initialize(1024);

        log.info("Algorithm: {}", generator.getAlgorithm());
        log.info("Provider: {}", generator.getProvider());

        KeyPair keyPair = generator.generateKeyPair();
        PrivateKey privateKey1 = keyPair.getPrivate();
        PublicKey publicKey1 = keyPair.getPublic();

        log.info("Private Key Format: {}", privateKey1.getFormat());
        log.info("Private Key Encode: {}", BaseEncoding.base16().encode(privateKey1.getEncoded()));
        log.info("Public Key Format: {}", publicKey1.getFormat());
        log.info("Public Key Encode: {}", BaseEncoding.base16().encode(publicKey1.getEncoded()));

        // 키에서 객체 생성하기
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey2 = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKey1.getEncoded()));
        PublicKey publicKey2 = keyFactory.generatePublic(new X509EncodedKeySpec(publicKey1.getEncoded()));

        assertThat(privateKey1, is(privateKey2));
        assertThat(publicKey1, is(publicKey2));
    }

    @Test
    void publicKeyToPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Path buildPath = Paths.get(System.getProperty("user.dir"), "build");
        Path publicKeyPath = Paths.get(buildPath.toString(), "public.key");
        Path privateKeyPath = Paths.get(buildPath.toString(), "private.key");

        PublicKey publicKey = null;
        PrivateKey privateKey = null;

        if (Files.exists(publicKeyPath) && Files.exists(privateKeyPath)) {
            byte[] publicBytes = Files.readAllBytes(publicKeyPath);
            byte[] privateByes = Files.readAllBytes(privateKeyPath);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicBytes));
            privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateByes));
        } else {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();

            Files.write(publicKeyPath, publicKey.getEncoded());
            Files.write(privateKeyPath, privateKey.getEncoded());
        }

        // 파일 암호화
        String transformation = "RSA/ECB/PKCS1Padding";
        Cipher encrypt = Cipher.getInstance(transformation);
        encrypt.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] bs = encrypt.doFinal(PLAIN_TEXT.getBytes());

        Cipher decript = Cipher.getInstance(transformation);
        decript.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] bytes = decript.doFinal(bs);

        MatcherAssert.assertThat(PLAIN_TEXT, Is.is(new String(bytes)));
    }

    @Test
    void privateKeyToPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Path buildPath = Paths.get(System.getProperty("user.dir"), "build");
        Path publicKeyPath = Paths.get(buildPath.toString(), "public.key");
        Path privateKeyPath = Paths.get(buildPath.toString(), "private.key");

        PublicKey publicKey = null;
        PrivateKey privateKey = null;

        if (Files.exists(publicKeyPath) && Files.exists(privateKeyPath)) {
            byte[] publicBytes = Files.readAllBytes(publicKeyPath);
            byte[] privateByes = Files.readAllBytes(privateKeyPath);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicBytes));
            privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateByes));
        } else {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();

            Files.write(publicKeyPath, publicKey.getEncoded());
            Files.write(privateKeyPath, privateKey.getEncoded());
        }

        // 파일 암호화
        String transformation = "RSA/ECB/PKCS1Padding";
        Cipher encrypt = Cipher.getInstance(transformation);
        encrypt.init(Cipher.ENCRYPT_MODE, privateKey);

        byte[] bs = encrypt.doFinal(PLAIN_TEXT.getBytes());

        Cipher decript = Cipher.getInstance(transformation);
        decript.init(Cipher.DECRYPT_MODE, publicKey);

        byte[] bytes = decript.doFinal(bs);

        MatcherAssert.assertThat(PLAIN_TEXT, Is.is(new String(bytes)));
    }

    @Nested
    class Padding {
        @RepeatedTest(2)
        void pkcs1Padding(RepetitionInfo repetitionInfo) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
            Path buildPath = Paths.get(System.getProperty("user.dir"), "build");
            Path publicKeyPath = Paths.get(buildPath.toString(), "pkcs1Padding-public.key");

            // padding이 붙기 때문에 결과가 항상 달라야함
            test(repetitionInfo.getCurrentRepetition(), publicKeyPath, "RSA/ECB/PKCS1Padding");
        }

        @RepeatedTest(2)
        void nopadding(RepetitionInfo repetitionInfo) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
            Path buildPath = Paths.get(System.getProperty("user.dir"), "build");
            Path publicKeyPath = Paths.get(buildPath.toString(), "pkcs1Padding-public.key");

            // padding이 붙기 때문에 결과가 항상 같음
            test(repetitionInfo.getCurrentRepetition(), publicKeyPath, "RSA/ECB/NoPadding");
        }

        private void test(int currentCount, Path publicKeyPath, String transformation) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
            Path buildPath = Paths.get(System.getProperty("user.dir"), "build");
            PublicKey publicKey = null;

            // Key 생성
            if (currentCount == 1) {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(1024);

                publicKey = keyPairGenerator.generateKeyPair().getPublic();
                Files.write(publicKeyPath, publicKey.getEncoded());
            } else {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Files.readAllBytes(publicKeyPath)));
            }

            Cipher encrypt = Cipher.getInstance(transformation);
            encrypt.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] bs = encrypt.doFinal(PLAIN_TEXT.getBytes());

            log.info("{}", BaseEncoding.base16().encode(bs));
        }
    }

    @Test
    void rsaoaepe() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // RSAES(RSA Encryption Scheme)에는 PKCS1Padding 외에도 OAEP(Optimal Asymmetric Encryption Padding
        // 라는 난수화 패딩 알고리즘이 있다.
        // OAEP는 PCKS#1 v2와 RFC 2437에 표준화되어 있고, PCKS1Padding보다 보안 강도가 높아서 현재 많이 사용 한다.

        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // 파일 암호화
        String transformation = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
        Cipher encrypt = Cipher.getInstance(transformation);
        encrypt.init(Cipher.ENCRYPT_MODE, privateKey);

        byte[] bs = encrypt.doFinal(PLAIN_TEXT.getBytes());

        Cipher decript = Cipher.getInstance(transformation);
        decript.init(Cipher.DECRYPT_MODE, publicKey);

        byte[] bytes = decript.doFinal(bs);

        MatcherAssert.assertThat(PLAIN_TEXT, Is.is(new String(bytes)));
    }
}
