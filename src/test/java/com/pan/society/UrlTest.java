package com.pan.society;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.net.URLDecoder;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/5/26
 */
@Slf4j
public class UrlTest {

    @Test
    public void urlDecode(){
        String userName = "%25E8%25A2%2581%25E8%25B6%2585";
        //汉字需要转两回
        try {
            userName = URLDecoder.decode(userName, "UTF-8");
            userName = URLDecoder.decode(userName, "UTF-8");
            log.info("userName:"+userName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
