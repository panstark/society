package com.pan.people.enums;

/**
 * 持久化状态
 */
public interface PersistStatus {
	/**
	 * 未变化的
	 */
	String UNCHANGED = "nrm";


	/**
	 * 新增的
	 */
	String ADDED = "new";

	/**
	 * 修改的
	 */
	String MODIFIED = "upd";

	/**
	 * 逻辑删除的
	 */
	String DELETED = "fdel";
}
