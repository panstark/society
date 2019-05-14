package com.pan.society.vo.human;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * create by panstark
 * create date 2019/5/13
 */
@Component
@ConfigurationProperties(prefix = "china.user")
@Data
public class People {

    private String name;

    private String gender;

    private String password;

}
