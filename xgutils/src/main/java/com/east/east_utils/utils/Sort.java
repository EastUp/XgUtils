package com.east.east_utils.utils;

import java.util.Random;

/**
 * Created by EastRiseWM on 2017/1/11.
 */
/*排序*/
public class Sort {
    /**
     * @param positions 需要排序的数组
     * @param random    Random实例
     */
    // 重排序
    public static void changePosition(int[] positions,Random random) {
        //使用for循环的目的是使得输出的数更加无序
        for (int index = positions.length - 1; index >= 0; index--) {
            // 从0到index处之间随机取一个值，跟index处的元素交换
            exchange(positions, random.nextInt(index + 1), index);
        }
        printPositions(positions);
    }

    // 交换位置
    private static void exchange(int[] positions, int m, int n) {
        int temp = positions[m];
        positions[m] = positions[n];
        positions[n] = temp;
    }

    // 依次打印数组的值
    private static void printPositions(int[] positions) {
        for (int index = 0; index < positions.length; index++) {
//            System.out.print(positions[index] + " ");
            ShowLog.v("printPositions", positions[index] + " ");
        }
        System.out.println();
    }
}
