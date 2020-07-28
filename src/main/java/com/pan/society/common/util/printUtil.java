package com.pan.society.common.util;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/7/15
 */
public class printUtil {

    public  static void printArray(int[] arrays){

        StringBuilder sb = new StringBuilder();
        for(int i=0;i<arrays.length;i++){

            if(i==0){
                sb.append("[");
            }
            sb.append(arrays[i]);
            if(i==arrays.length-1){
                sb.append("]");
            }else{
                sb.append(",");
            }
        }

        System.out.println(sb.toString());
    }

    public  static void printDoubleArray(int[][] doubleArrays){

        StringBuilder sb = new StringBuilder();
        for(int i=0;i<doubleArrays.length;i++){
            int[] arrays = doubleArrays[i];
            printArray(arrays);
        }

    }

    public  static void printArray(Object[] arrays){

        StringBuilder sb = new StringBuilder();
        for(int i=0;i<arrays.length;i++){

            if(i==0){
                sb.append("[");
            }
            sb.append(arrays[i]);
            if(i==arrays.length-1){
                sb.append("]");
            }else{
                sb.append(",");
            }
        }

        System.out.println(sb.toString());
    }

    public static void main(String[] args) {

        int[] nums = new int[]{1,2,3,4};
        printArray(nums);
    }
}
