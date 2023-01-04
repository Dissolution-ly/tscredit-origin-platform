package com.tscredit.origin.main;


import com.aurora.base.util.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Slf4j
@SpringBootTest
public class ApplicationTest {


    /**
     * 生成项目密钥
     */
    @Test
    public void ossTest() throws NoSuchAlgorithmException {
        Map<String, String> result = RSAUtils.genRsaKeyPairText(2048);
        log.error("公钥(前端)：\n" + result.get("publicKey"));
        log.error("私钥(后端)：\n" + result.get("privateKey"));
    }

}