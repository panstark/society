package com.pan.society.know.datastructure.arrays.arraybasics;

import com.pan.society.common.util.printUtil;

public class Main {

    public static void main(String[] args) {

        int[] arr = new int[10];
        for(int i = 0 ; i < 5 ; i ++)
            arr[i] = i;

        printUtil.printArray(arr);

        int[] scores = new int[]{100, 99, 66};
        printUtil.printArray(arr);

        scores[0] = 96;
        printUtil.printArray(arr);

        String[] names = new String[10];
        for(int i = 0 ; i < 5 ; i ++)
            names[i] = i+"";

        printUtil.printArray(names);

    }
}
