package com.pan.society.Common;

import com.yonyou.ocm.common.entity.BaseEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 作者 E-mail: yongyg
 * @version 创建时间：2017 年 07 月 19 日 20:04
 *          类说明：
 */
public class CollectionUtils<T extends BaseEntity> {

    private CollectionUtils() {
    }

    private static CollectionUtils instance;

    public static CollectionUtils getInstance() {
        if (instance == null) {
            instance = new CollectionUtils();
        }
        return instance;
    }

    /**
     * Get set keys string [ ].
     *
     * @param sets the sets
     * @return the string [ ]
     */
    public String[] getSetkeys(Set<T> sets) {
        Set<String> re = new HashSet<String>();
        for (T t : sets) {
            re.add(t.getId());
        }
        return re.toArray(new String[] {});
    }
}
