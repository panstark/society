package com.pan.society.Common.util;

public class StringUtils4OCC {
    /**
     * 在StringUtils基础上，增加，如果为~,也返回false.
     * @param cs
     * @return
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !StringUtils4OCC.isBlank(cs);
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        if(cs.equals("~")){
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }


    public static boolean isEquals(String a, String b) {
        if(a != null && b !=null){
            if(a.equals(b))
                return true;
            else
                return false;
        }else if(a == null && b==null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 对string数组进行排序
     * @param strings
     * @return
     */
    public static String[] sortStringArray(String[] strings){
        for (int i=0;i<strings.length-1;i++){
            for (int j=0;j<strings.length-i-1;j++) {
                if(strings[j].compareTo(strings[j+1])>0){
                    String temp=strings[j];
                    strings[j]=strings[j+1];
                    strings[j+1]=temp;
                }
            }
        }
        return strings;
    }
}
