package com.pan.society.know.design.pattern.behavioral.chainofresponsibility;

/**
 * Created by geely
 *
 * 责任链模式
 *
 * 适合于审批流，按次序操作。
 *
 */
public class Test {

    public static void main(String[] args) {
        Approver articleApprover = new ArticleApprover();
        Approver videoApprover = new VideoApprover();
        Approver textApprover = new TextApprover();

        articleApprover.setNextApprover(videoApprover);
        videoApprover.setNextApprover(textApprover);

        Course course = new Course();
        course.setName("Java设计模式精讲 -- By Geely");
        course.setArticle("Java设计模式精讲的手记");
        course.setVideo("Java设计模式精讲的视频");
        course.setText("java文本文件");
        articleApprover.deploy(course);
    }
}
