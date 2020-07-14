package com.pan.society.know.design.principle.singleresponsibility;

/**
 * Created by geely
 */
public interface ICourse {
    String getCourseName();
    byte[] getCourseVideo();

    void studyCourse();
    void refundCourse();

}
