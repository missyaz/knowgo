package com.fw.know.go.user.infrastructure.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;

import java.util.Base64;

/**
 * @Description AES加解密工具类
 * @Date 25/2/2026 下午3:51
 * @Author Leo
 */
public class AesUtil {

    private static final String KEY = "uTfe6WtWICU/6rk0Gr7qKrAvHaRvQj+HRaHKvSe9UJI=";
    private static final AES AES = SecureUtil.aes(Base64.getDecoder().decode(KEY));

    public static String encrypt(String content){
        // 判空
        if (StrUtil.isBlankIfStr(content)){
            return content;
        }
        return AES.encryptHex(content);
    }

    public static String decrypt(String content){
        if (StrUtil.isBlankIfStr(content)){
            return content;
        }
        return AES.decryptStr(content);
    }
}
