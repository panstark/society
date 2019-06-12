package com.pan.society.Common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 树相关的工具类。
 *
 * @author wangruiv
 * @date 2018-05-05 11:10:22
 */
@Slf4j
public class TreeUtils {
    /**
     * 树形编码的字段名称
     */
    public final static String TREE_CODE_FIELD_NAME = "treeCode";

    private final static int SPAN_LENGTH = 2;

    private final static char[] CODE_CHAR_ARRAY =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                    'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private final static char FIRST_CHAR = CODE_CHAR_ARRAY[0];

    private final static char LAST_CHAR = CODE_CHAR_ARRAY[CODE_CHAR_ARRAY.length - 1];

    /**
     * 生成第一个子节点的树形编码。
     *
     * @param treeCode 当前树形编码。
     * @return 下一个树形编码。
     */
    public static String genFirstChildTreeCode(String treeCode) {
        if (StringUtils.isBlank(treeCode)) {
            return "" + FIRST_CHAR + FIRST_CHAR;
        }

        treeCode = treeCode.trim();
        int length = treeCode.length();
        if (length < SPAN_LENGTH || length % SPAN_LENGTH != 0) {
            throw new RuntimeException("树形编码长度应不小于" + SPAN_LENGTH + "且必须是" + SPAN_LENGTH + "的倍数。");
        }

        return treeCode + FIRST_CHAR + FIRST_CHAR;
    }

    /**
     * 生成下一个树形编码。
     *
     * @param treeCode 当前最大的树形编码。
     * @return 下一个树形编码。
     */
    public static String genNextTreeCode(String treeCode) {
        if (StringUtils.isBlank(treeCode)) {
            return "" + FIRST_CHAR + FIRST_CHAR;
        }

        treeCode = treeCode.trim();
        int length = treeCode.length();
        if (length < SPAN_LENGTH || length % SPAN_LENGTH != 0) {
            throw new RuntimeException("树形编码长度应不小于" + SPAN_LENGTH + "且必须是" + SPAN_LENGTH + "的倍数。");
        }
        String parentTreeCode = treeCode.substring(0, length - SPAN_LENGTH);
        String spanCode = treeCode.substring(length - SPAN_LENGTH);
        String nextTreeCode = parentTreeCode + getNextSpanCode(spanCode);

        return nextTreeCode;
    }

    /**
     * 获取下一个编码小节。
     *
     * @param spanCode 当前编码小节。
     * @return 下一个编码小节。
     */
    private static String getNextSpanCode(String spanCode) {
        char first = spanCode.charAt(0);
        char last = spanCode.charAt(1);
        if (last == LAST_CHAR) {
            last = FIRST_CHAR;
            int indexFirst = Arrays.binarySearch(CODE_CHAR_ARRAY, first);
            if (indexFirst == CODE_CHAR_ARRAY.length - 1) {
                log.warn("编码超出范围！");
                return spanCode;
            }
            first = CODE_CHAR_ARRAY[indexFirst + 1];
        } else {
            int indexLast = Arrays.binarySearch(CODE_CHAR_ARRAY, last);
            if (indexLast == CODE_CHAR_ARRAY.length - 1) {
                log.warn("编码超出范围！");
                return spanCode;
            }
            last = CODE_CHAR_ARRAY[indexLast + 1];
        }
        return "" + first + last;
    }
}
