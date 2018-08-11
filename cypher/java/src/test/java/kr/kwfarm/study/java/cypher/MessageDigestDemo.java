package kr.kwfarm.study.java.cypher;

import com.google.common.io.BaseEncoding;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Slf4j
public class MessageDigestDemo {
    private Charset UTF8 = Charset.forName("UTF-8");

    String FILE_NAME = "commons-crypto-1.0.0-bin.tar.gz";

    String CHECK_SUM = "059a7b27785e17dd93f0dec01c655ba0";

    @Test
    void test() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String message = "HelloWorld";
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(message.getBytes(UTF8));
        log.info(BaseEncoding.base16().lowerCase().encode(md5.digest()));
    }

    @Test
    void checksum() throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        try (InputStream resourceAsStream = getClassLoader().getResourceAsStream(FILE_NAME)) {
            byte[] bs = new byte[1024];
            int read = -1;
            while( (read = resourceAsStream.read(bs)) != -1) {
                md5.update(Arrays.copyOfRange(bs, 0, read));
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        byte[] digest = md5.digest();

        String encode = BaseEncoding.base16().lowerCase().encode(digest);
        log.info("result: {}", encode);
        assertThat(CHECK_SUM, is(encode));
    }

    private ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }

    @Test
    void checksumDigestInputStream() throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        try (InputStream is = new DigestInputStream(getClassLoader().getResourceAsStream(FILE_NAME), md5)) {
            while(is.read() != -1);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        byte[] digest = md5.digest();

        String encode = BaseEncoding.base16().lowerCase().encode(digest);
        log.info("result: {}", encode);
        assertThat(CHECK_SUM, is(encode));
    }

    @Test
    void sha1() throws NoSuchAlgorithmException {
        String message = "HelloWorldKwfarm";

        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        byte[] digest = sha1.digest(message.getBytes());
        String encode = BaseEncoding.base16().lowerCase().encode(digest);

        log.info("result: {}", encode);
        // link: http://www.sha1-online.com/
        assertThat("5f1fb61de338d91c544e268471067c4d6b3835ba", is(encode));
    }
}
