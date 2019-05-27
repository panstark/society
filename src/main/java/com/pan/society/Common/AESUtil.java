package com.pan.society.Common;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;

public class AESUtil {
    //使用指定转换的 Cipher 对象

    public static final String CIPHER_ALGORITHM_AES = "AES";

    public static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";

    public static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";

    //【AES/CBC/NoPadding】模式下，待加密内容的长度必须是16的倍数，否则： javax.crypto.IllegalBlockSizeException: Input length not multiple of 16 bytes

    public static final String CIPHER_ALGORITHM_CBC_NoPadding = "AES/CBC/NoPadding";
    private static final String ALGORITHM_MD5 = "md5";
    private static final String CHARSET = "UTF-8";
    private static final String ALGORITHM_AES = "AES";
//    public static void main(String[] args) throws Exception {
//        byte[] key = getAESKeyBytes("1");//要求密钥必须是16位的
//        String str = addSecurity("0123456789123456".getBytes(CHARSET), key);
//        String string = removeSecurity(str,key);
//        System.out.println(string);
//    }

    //加密
    public static String addSecurity(byte[] source, byte[] key) throws Exception {

        //生成的密文

        byte[] cryptograph = encryptOrDecrypt(source, key, CIPHER_ALGORITHM_AES, Cipher.ENCRYPT_MODE);

        //通过Base64编码为ASCII字符后传输

        String cryptographStr = Base64.getEncoder().encodeToString(cryptograph);

        return cryptographStr;
    }
    //解密
    public static String removeSecurity(String cryptographStr, byte[] key) throws Exception {

        //收到后先用Base64解码

        byte[] targetBase64 = Base64.getDecoder().decode(cryptographStr.getBytes(CHARSET));

        // 解密密文

        byte[] target = encryptOrDecrypt(targetBase64, key, CIPHER_ALGORITHM_AES, Cipher.DECRYPT_MODE);

        return new String(target, CHARSET);
    }
    /**

     * 加密或解密。加密和解密用的同一个算法和密钥

     * @param source    要加密或解密的数据

     * @param key    密钥

     * @param transformation

     * @param mode        加密或解密模式。值请选择Cipher.DECRYPT_MODE或Cipher.ENCRYPT_MODE

     * @return         加密或解密后的数据

     */

    public static byte[] encryptOrDecrypt(byte[] source, byte[] key, String transformation, int mode) throws Exception {

        Cipher cipher = Cipher.getInstance(transformation);

        Key secretKey = new SecretKeySpec(key, ALGORITHM_AES); //密钥

        if (transformation.equals(CIPHER_ALGORITHM_CBC) || transformation.equals(CIPHER_ALGORITHM_CBC_NoPadding)) {

            cipher.init(mode, secretKey, new IvParameterSpec(getIV()));//指定一个初始化向量 (Initialization vector，IV)， IV 必须是16位

            return cipher.doFinal(source);

        } else {

            cipher.init(mode, secretKey);

            return cipher.doFinal(source);

        }

    }


    /**

     * 根据字符串生成AES的密钥字节数组<br>

     */

    public static byte[] getAESKeyBytes(String sKey) throws Exception {

        //获得指定摘要算法的 MessageDigest 对象

        MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);

        //使用指定的字节更新摘要（继续多部分加密或解密操作，以处理其他数据部分）

        md.update(sKey.getBytes(CHARSET));

        //获得密文。注意：长度为16而不是32。一个字节(byte)占8位(bit)

        return md.digest();

    }


    /**

     * 指定一个初始化向量 (Initialization vector，IV)，IV 必须是16位

     */

    private static final byte[] getIV() throws Exception {

        return "1234567812345678".getBytes(CHARSET);

    }
}

