package com.pan.society.Common.enums;

/**
 * 多枚举类
 * @author by panstark
 * @description
 * @notice
 * @date 2020/5/13
 */
public class Constants {

    public  enum Dict{

        PROSTA("PROSTA","产品状态"),
        COUNTRY("COUNTRY","国家"),
        YWLX("YWLX","业务类型");

        Dict(String value,String name){
            this.value=value;
            this.name=name;
        }
        private final String value;
        private final String name;

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }
    /**
     * 订单状态
     * <p>Company:rayootech</p>
     * @author zhangxueshen
     * @date 2016-6-14
     */
    public  enum OrderStats{

        DELETE(0,"删除"),
        RESERVE(1,"订单预定"),
        CONFIRM(2,"订单确认"),
        COMPLETE(3,"订单完成"),
        CLOSE(4,"订单关闭");

         OrderStats(Integer value,String name){
            this.value = value;
            this.name = name;
        }
        private final Integer value;
        private final String name;

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

}
