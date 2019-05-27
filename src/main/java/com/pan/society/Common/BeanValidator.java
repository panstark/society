package com.pan.society.Common;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;

import static com.google.common.collect.Iterables.getFirst;

/**
 * Bean对象验证器。
 *
 * @author wangruiv
 * @date 2017-07-01 11:57:22
 */
public class BeanValidator {
	/**
	 * 验证Bean对象的属性。
	 *
	 * @param object 待验证的Bean对象。
	 * @param <T>    如果校验不成功则抛出此异常。
	 */
	public static <T> void validate(T object) {
		// 获得验证器
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

		// 执行验证
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);

		// 如果有验证信息，则将第一个取出来包装成异常返回
		ConstraintViolation<T> constraintViolation = getFirst(constraintViolations, null);
		if (constraintViolation != null) {
			throw new ValidationException(constraintViolation.getMessage());
		}
	}
}
