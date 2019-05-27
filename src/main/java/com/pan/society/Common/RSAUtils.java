package com.pan.society.Common;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.HashMap;
import java.util.Map;

public abstract class RSAUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtils.class);
    private static final String ALGORITHOM = "RSA";
    private static final int KEY_SIZE = 1024;
    private static final Provider DEFAULT_PROVIDER = new BouncyCastleProvider();
    private static KeyPairGenerator keyPairGen = null;
    private static KeyFactory keyFactory = null;
    private static KeyPair oneKeyPair = null;
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    public static final String PUBLIC_KEY = "RSAPublicKey";
    public static final String PRIVATE_KEY = "RSAPrivateKey";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    private RSAUtils() {
    }

    public static synchronized KeyPair generateKeyPair() {
        try {
            keyPairGen.initialize(1024, new SecureRandom(DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd").getBytes()));
            oneKeyPair = keyPairGen.generateKeyPair();
            return oneKeyPair;
        } catch (InvalidParameterException var1) {
            LOGGER.error("KeyPairGenerator does not support a key length of 1024.", var1);
        } catch (NullPointerException var2) {
            LOGGER.error("RSAUtils#KEY_PAIR_GEN is null, can not generate KeyPairGenerator instance.", var2);
        }

        return null;
    }

    public static KeyPair getKeyPair() {
        return oneKeyPair;
    }

    public static void setKeyPair(KeyPair redisKeyPair) {
        oneKeyPair = redisKeyPair;
    }

    public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) {
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));

        try {
            return (RSAPublicKey)keyFactory.generatePublic(publicKeySpec);
        } catch (InvalidKeySpecException var4) {
            LOGGER.error("RSAPublicKeySpec is unavailable.", var4);
        } catch (NullPointerException var5) {
            LOGGER.error("RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.", var5);
        }

        return null;
    }

    public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) {
        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));

        try {
            return (RSAPrivateKey)keyFactory.generatePrivate(privateKeySpec);
        } catch (InvalidKeySpecException var4) {
            LOGGER.error("RSAPrivateKeySpec is unavailable.", var4);
        } catch (NullPointerException var5) {
            LOGGER.error("RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.", var5);
        }

        return null;
    }

    public static RSAPrivateKey getRSAPrivateKey(String hexModulus, String hexPrivateExponent) {
        if(!StringUtils.isBlank(hexModulus) && !StringUtils.isBlank(hexPrivateExponent)) {
            byte[] modulus = null;
            byte[] privateExponent = null;

            try {
                modulus = Hex.decodeHex(hexModulus.toCharArray());
                privateExponent = Hex.decodeHex(hexPrivateExponent.toCharArray());
            } catch (DecoderException var5) {
                LOGGER.error("hexModulus or hexPrivateExponent value is invalid. return null(RSAPrivateKey).");
            }

            return modulus != null && privateExponent != null?generateRSAPrivateKey(modulus, privateExponent):null;
        } else {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("hexModulus and hexPrivateExponent cannot be empty. RSAPrivateKey value is null to return.");
            }

            return null;
        }
    }

    public static RSAPublicKey getRSAPublidKey(String hexModulus, String hexPublicExponent) {
        if(!StringUtils.isBlank(hexModulus) && !StringUtils.isBlank(hexPublicExponent)) {
            byte[] modulus = null;
            byte[] publicExponent = null;

            try {
                modulus = Hex.decodeHex(hexModulus.toCharArray());
                publicExponent = Hex.decodeHex(hexPublicExponent.toCharArray());
            } catch (DecoderException var5) {
                LOGGER.error("hexModulus or hexPublicExponent value is invalid. return null(RSAPublicKey).");
            }

            return modulus != null && publicExponent != null?generateRSAPublicKey(modulus, publicExponent):null;
        } else {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("hexModulus and hexPublicExponent cannot be empty. return null(RSAPublicKey).");
            }

            return null;
        }
    }

    public static byte[] encrypt(PublicKey publicKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance("RSA", DEFAULT_PROVIDER);
        ci.init(1, publicKey);
        return ci.doFinal(data);
    }

    public static byte[] decrypt(PrivateKey privateKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance("RSA", DEFAULT_PROVIDER);
        ci.init(2, privateKey);
        return ci.doFinal(data);
    }

    public static String encryptString(PublicKey publicKey, String plaintext) {
        if(publicKey != null && plaintext != null) {
            byte[] data = plaintext.getBytes();

            try {
                byte[] en_data = encrypt(publicKey, data);
                return new String(Hex.encodeHex(en_data));
            } catch (Exception var4) {
                LOGGER.error(var4.getCause().getMessage());
                return null;
            }
        } else {
            return null;
        }
    }

    public static String encryptString(String plaintext) {
        if(plaintext == null) {
            return null;
        } else {
            byte[] data = plaintext.getBytes();
            KeyPair keyPair = getKeyPair();

            try {
                byte[] en_data = encrypt((RSAPublicKey)keyPair.getPublic(), data);
                return new String(Hex.encodeHex(en_data));
            } catch (NullPointerException var4) {
                LOGGER.error("keyPair cannot be null.");
            } catch (Exception var5) {
                LOGGER.error(var5.getCause().getMessage());
            }

            return null;
        }
    }

    public static String decryptString(PrivateKey privateKey, String encrypttext) {
        if(privateKey != null && !StringUtils.isBlank(encrypttext)) {
            try {
                byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
                byte[] data = decrypt(privateKey, en_data);
                return new String(data);
            } catch (Exception var4) {
                LOGGER.error(String.format("\"%s\" Decryption failed. Cause: %s", new Object[]{encrypttext, var4.getCause().getMessage()}));
                return null;
            }
        } else {
            return null;
        }
    }

    public static String decryptString(String encrypttext) throws Exception {
        if(StringUtils.isBlank(encrypttext)) {
            LOGGER.error("----encrypttest----为空！----{}----", encrypttext);
            return null;
        } else {
            KeyPair keyPair = getKeyPair();

            try {
                byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
                if(en_data.length != 128) {
                    LOGGER.error("en_data length : ----- {}, en_data : ---- {} ", Integer.valueOf(en_data.length), new String(en_data));
                    LOGGER.error("encrypttext : ---- {} ", encrypttext);
                    LOGGER.error("keyPair ---- private key: {} ---- public key : {} ", keyPair.getPrivate(), keyPair.getPublic());
                }

                byte[] data = decrypt((RSAPrivateKey)keyPair.getPrivate(), en_data);
                LOGGER.error("en_data ---- {}", en_data);
                LOGGER.error("keyPair ---- private key: {} ---- public key : {} ", keyPair.getPrivate(), keyPair.getPublic());
                return new String(data);
            } catch (NullPointerException var4) {
                LOGGER.error("keyPair cannot be null.", var4);
                throw new Exception("keyPair cannot be null.", var4);
            } catch (Exception var5) {
                LOGGER.error(String.format("\"%s\" Decryption failed. Cause: %s", new Object[]{encrypttext, var5.getMessage()}));
                LOGGER.error("Decryption failed----", var5);
                throw new Exception("Decryption failed----", var5);
            }
        }
    }

    public static String decryptStringByJs(String encrypttext) throws Exception {
        String text = decryptString(encrypttext);
        return text == null?null:StringUtils.reverse(text);
    }

    public static RSAPublicKey getDefaultPublicKey() {
        KeyPair keyPair = getKeyPair();
        return keyPair != null?(RSAPublicKey)keyPair.getPublic():null;
    }

    public static RSAPrivateKey getDefaultPrivateKey() {
        KeyPair keyPair = getKeyPair();
        return keyPair != null?(RSAPrivateKey)keyPair.getPrivate():null;
    }

    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap(2);
        keyMap.put("RSAPublicKey", publicKey);
        keyMap.put("RSAPrivateKey", privateKey);
        return keyMap;
    }

    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }

    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decodeBase64(sign));
    }

    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;

        for(int i = 0; inputLen - offSet > 0; offSet = i * 128) {
            byte[] cache;
            if(inputLen - offSet > 128) {
                cache = cipher.doFinal(encryptedData, offSet, 128);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }

            out.write(cache, 0, cache.length);
            ++i;
        }

        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;

        for(int i = 0; inputLen - offSet > 0; offSet = i * 128) {
            byte[] cache;
            if(inputLen - offSet > 128) {
                cache = cipher.doFinal(encryptedData, offSet, 128);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }

            out.write(cache, 0, cache.length);
            ++i;
        }

        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;

        for(int i = 0; inputLen - offSet > 0; offSet = i * 117) {
            byte[] cache;
            if(inputLen - offSet > 117) {
                cache = cipher.doFinal(data, offSet, 117);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }

            out.write(cache, 0, cache.length);
            ++i;
        }

        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;

        for(int i = 0; inputLen - offSet > 0; offSet = i * 117) {
            byte[] cache;
            if(inputLen - offSet > 117) {
                cache = cipher.doFinal(data, offSet, 117);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }

            out.write(cache, 0, cache.length);
            ++i;
        }

        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key)keyMap.get("RSAPrivateKey");
        return Base64.encodeBase64String(key.getEncoded());
    }

    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key)keyMap.get("RSAPublicKey");
        return Base64.encodeBase64String(key.getEncoded());
    }

    public static RSAPublicKey getPublicKey(String key) {
        try {
            byte[] keyBytes = Base64.decodeBase64(key);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey)keyFactory.generatePublic(x509KeySpec);
        } catch (InvalidKeySpecException var4) {
            LOGGER.error(var4.getMessage(), var4);
        } catch (NoSuchAlgorithmException var5) {
            LOGGER.error(var5.getMessage(), var5);
        }

        return null;
    }

    public static RSAPrivateKey getPrivateKey(String key) {
        try {
            byte[] keyBytes = Base64.decodeBase64(key);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey)keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (InvalidKeySpecException var4) {
            LOGGER.error(var4.getMessage(), var4);
        } catch (NoSuchAlgorithmException var5) {
            LOGGER.error(var5.getMessage(), var5);
        }

        return null;
    }

    static {
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA", DEFAULT_PROVIDER);
            keyFactory = KeyFactory.getInstance("RSA", DEFAULT_PROVIDER);
        } catch (NoSuchAlgorithmException var1) {
            LOGGER.error(var1.getMessage());
        }

    }
}
