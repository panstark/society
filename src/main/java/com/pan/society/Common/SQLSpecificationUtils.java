package com.pan.society.Common;

import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * sql处理工具类
 * @author gaopengf
 * @date 2017-10-18
 *
 */
public class SQLSpecificationUtils {

	/**
	 * @Fields ORACLE_IN_MAX_LENGTH : oracle in 最大的长度
	 */
	private static final int ORACLE_IN_MAX_LENGTH = 1000;

	/**
	 * @Title: createPredicateWithStringIn @Description:
	 * 创建in的语句，当结果大于1000条写or,每多1000增加一个or @param @param ids @param @param
	 * cb @param @param path @param @return 设定文件 @return Predicate 返回类型 @throws
	 */
	public static Predicate createPredicateWithStringIn(final List<String> ids, CriteriaBuilder cb, Path<String> path) {
		if (ids.size() < ORACLE_IN_MAX_LENGTH) {
			return createPredicatWithStringInThatLengthLessThanThousand(ids, cb, path);
		} else {
			Integer idsSize = ids.size();
			Integer numOf1000 = idsSize / 1000;
			Integer numOfLeft = idsSize % 1000;
			Predicate predicate = null;
			for (int i = 0; i < numOf1000; i++) {
				Predicate temp = createPredicatWithStringInThatLengthLessThanThousand(
						ids.subList(i * ORACLE_IN_MAX_LENGTH, (i + 1) * ORACLE_IN_MAX_LENGTH), cb, path);
				if (i == 0) {
					predicate = temp;
				} else {
					predicate = cb.or(predicate, temp);
				}
			}
			if (numOfLeft != 0) {
				Predicate temp = createPredicatWithStringInThatLengthLessThanThousand(
						ids.subList(numOf1000 * ORACLE_IN_MAX_LENGTH, idsSize),
						cb, path);
				predicate = cb.or(predicate, temp);
			}
			return predicate;
		}
	}

	/**
	 * @Title: createPredicatWithStringInThatLenthLessThanThousand @Description:
	 * 长度小于1000的 @param @param ids @param @param cb @param @param
	 * path @param @return 设定文件 @return Predicate 返回类型 @throws
	 */
	private static Predicate createPredicatWithStringInThatLengthLessThanThousand(final List<String> ids,
                                                                                  CriteriaBuilder cb, Path<String> path) {
		Assert.notNull(ids, "ids参数不能为空");
		Assert.notNull(cb, "cb参数不能为空");
		Assert.notNull(path, "path参数不能为空");
		Assert.isTrue(ids.size() < ORACLE_IN_MAX_LENGTH, "ids长度不能超过1000");
		In<String> inPath = cb.in(path);
		for (String id : ids) {
			inPath.value(id);
		}
		Predicate predicate = inPath;
		return predicate;
	}

}
