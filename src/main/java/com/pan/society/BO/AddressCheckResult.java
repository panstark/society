package com.pan.society.BO;

import lombok.Data;

/**
 * create by panstark
 * create date 2019/5/9
 */
@Data
public class AddressCheckResult {

    private boolean postCodeResult = false; // true:通过校验；false：未通过校验

}
