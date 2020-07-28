package com.pan.society.know.datastructure.arrays.arraybasics;

import com.pan.society.common.util.printUtil;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/7/15
 */
public class DoubleArray {

    public static void main(String[] args) {

        int[][] doubleArray = new int[10][10];


        for(int i=0;i<doubleArray.length;i++){
            int[] ints = doubleArray[i];
            ints[0]=1;
            ints[doubleArray.length-1]=1;

            for(int j=0;j<ints.length;j++){
                if(i==0){
                    ints[j]=1;
                }else if(i ==j){
                    ints[j]=1;
                }else if(i==doubleArray.length-1){
                    ints[j]=1;
                }else if(i+j==ints.length-1){
                    ints[j]=1;
                }
            }
        }

        printUtil.printDoubleArray(doubleArray);
    }
}
