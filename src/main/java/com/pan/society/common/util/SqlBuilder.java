package com.pan.society.common.util;

/**
 * 查询用的sql拼装工具类
 * 
 * @author gaopengf
 * @date 2017-08-03 11:28:42
 */
public class SqlBuilder {

	private StringBuilder buffer = new StringBuilder();

	/**
	 * 对于String数组值构造in条件
	 * 
	 * @param name
	 *            sql字段名
	 * @param values
	 *            String数组值
	 */
	public void append(String name, String[] values) {

		if (values == null || values.length == 0) {
			this.isNull(name);
			return;
		}

		int length = values.length;
		if (length == 1) {
			this.append(name, values[0]);
			return;
		}
		if (length < 1000) {
			this.buffer.append(name);
			this.buffer.append(" in (");
			for (int i = 0; i < length; i++) {
				this.buffer.append("'");
				this.buffer.append(values[i]);
				this.buffer.append("'");
				this.buffer.append(",");
			}
			length = this.buffer.length();
			this.buffer.deleteCharAt(length - 1);
			this.buffer.append(") ");
			return;
		}
		if (length >= 1000) {

			this.buffer.append("(");
			for (int start = 0; start < length;) {
				int end = start + 200 > length ? length : start + 200;
				this.buffer.append(name);
				this.buffer.append(" in (");
				for (int i = start; i < end; i++) {
					this.buffer.append("'");
					this.buffer.append(values[i]);
					this.buffer.append("'");
					this.buffer.append(",");
				}
				int buflength = this.buffer.length();
				this.buffer.deleteCharAt(buflength - 1);
				this.buffer.append(") ");
				this.buffer.append(" or ");
				start = end;
			}
			length = this.buffer.length();
			this.buffer.delete(length - 4, length - 1);
			this.buffer.append(")");
			return;
		}

	}

	/**
	 * 对于字符串值构造“等于”条件
	 * 
	 * @param name
	 *            sql字段名
	 * @param value
	 *            String值 不能为空，否则抛异常。因为不知道是否要添加~
	 */
	public void append(String name, String value) {
		if (value == null || value.length() == 0) {
			this.isNull(name);
			return;
		}
		this.buffer.append(name);
		this.buffer.append("='");
		this.buffer.append(value);
		this.buffer.append("' ");

	}
	public void like(String name, String value) {
		if (value == null || value.length() == 0) {
			this.isNull(name);
			return;
		}
		this.buffer.append(name);
		this.buffer.append(" like'%");
		this.buffer.append(value);
		this.buffer.append("%' ");
	}

	/**
	 * 对于字符串值构造“等于”条件
	 * 
	 * @param name
	 *            sql字段名
	 * @param value
	 *            String值 不能为空，否则抛异常。因为不知道是否要添加~
	 */
	public void isNull(String name) {
		this.buffer.append("(");
		this.buffer.append(name + " is null ");
		this.buffer.append(" or ");
		this.buffer.append(name + " ='~' ");
		this.buffer.append(" or ");
		this.buffer.append(name + "='')");
	}

	@Override
	public String toString() {
		return this.buffer.toString();
	}

	/**
	 * 将一个字符串拼写入sql语句
	 * 
	 * @param str
	 *            字符串
	 */
	public void append(String str) {
		this.buffer.append(str);
	}

	public void append(String pkField, Object[] objvalues) {

		if (objvalues == null || objvalues.length == 0)
			return;
		if (objvalues[0] instanceof String) {
			this.append(pkField, (String[]) objvalues);
			;
		}

	}

	public void append(String[] fields) {
		for (String field : fields) {
			this.buffer.append(field);
			this.buffer.append(",");
		}
		buffer.deleteCharAt(buffer.length() - 1);
	}

	public void append(String name, int isShopSelf) {
		this.buffer.append(name);
		this.buffer.append("=");
		this.buffer.append(isShopSelf);

	}

	public void unequeal(String name, int i) {
		this.buffer.append("(");
		this.buffer.append(name);
		this.buffer.append("!=");
		this.buffer.append(i);
		this.buffer.append(" or ");
		this.buffer.append(name + " is null )");
	}

	public void append(String name, boolean obj) {
		this.buffer.append(name);
		this.buffer.append("=");
		if (obj) {
			this.buffer.append("1");
		} else {
			this.buffer.append("0");
		}

	}

	public void append(String name, int[] values) {

		if (values == null || values.length == 0) {
			this.isNull(name);
			return;
		}

		int length = values.length;
		if (length == 1) {
			this.append(name, values[0]);
			return;
		}
		if (length < 1000) {
			this.buffer.append(name);
			this.buffer.append(" in (");
			for (int i = 0; i < length; i++) {
				this.buffer.append("'");
				this.buffer.append(values[i]);
				this.buffer.append("'");
				this.buffer.append(",");
			}
			length = this.buffer.length();
			this.buffer.deleteCharAt(length - 1);
			this.buffer.append(") ");
			return;
		}
		if (length >= 1000) {

			this.buffer.append("(");
			for (int start = 0; start < length;) {
				int end = start + 200 > length ? length : start + 200;
				this.buffer.append(name);
				this.buffer.append(" in (");
				for (int i = start; i < end; i++) {
					this.buffer.append("'");
					this.buffer.append(values[i]);
					this.buffer.append("'");
					this.buffer.append(",");
				}
				int buflength = this.buffer.length();
				this.buffer.deleteCharAt(buflength - 1);
				this.buffer.append(") ");
				this.buffer.append(" or ");
				start = end;
			}
			length = this.buffer.length();
			this.buffer.delete(length - 4, length - 1);
			this.buffer.append(")");
			return;
		}

	}
}
