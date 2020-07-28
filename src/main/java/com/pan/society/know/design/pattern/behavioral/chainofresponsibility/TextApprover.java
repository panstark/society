package com.pan.society.know.design.pattern.behavioral.chainofresponsibility;

import com.pan.society.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/7/14
 */
@Slf4j
public class TextApprover extends Approver {
    @Override
    public void deploy(Course course) {

        if(StringUtils.isNotBlank(course.getText())){
            System.out.println("课程含有文本文件,审批通过");

            if(null!=approver){
                approver.deploy(course);
            }
        }else{
            System.out.println("课程不含有文本文件,审批不通过，结束。");
        }
    }
}
