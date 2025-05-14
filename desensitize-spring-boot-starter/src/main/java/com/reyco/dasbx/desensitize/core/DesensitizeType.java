package com.reyco.dasbx.desensitize.core;

/**
 * 脱敏类型
 */
public interface DesensitizeType {
	
	/**
	 * 表达式脱敏接口
	 * @author reyco
	 *
	 */
	static interface ExpressionDesensitizeType extends DesensitizeType{
		String getExpression();
		
		String getReplaceExpression();
	}
	/**
	 * 索引脱敏接口
	 * @author reyco
	 *
	 */
	static interface IndexDesensitizeType extends DesensitizeType{
		default int getStart() {
			return Integer.MIN_VALUE;
		}
		default int getEnd() {
			return Integer.MAX_VALUE;
		}
	}
	
	static class DefaultIndexDesensitizeType implements IndexDesensitizeType{
		private int start;
		private int end;
		public DefaultIndexDesensitizeType() {
		}
		public DefaultIndexDesensitizeType(int start, int end) {
			super();
			this.start = start;
			this.end = end;
		}
		public int getStart() {
			return start;
		}
		public void setStart(int start) {
			this.start = start;
		}
		public int getEnd() {
			return end;
		}
		public void setEnd(int end) {
			this.end = end;
		}
	}
	static class DefaultAllExpressionDesensitizeType implements ExpressionDesensitizeType {
		@Override
		public String getExpression() {
			return "(\\w{0})[\\s\\S](\\w{0})";
		}
		@Override
		public String getReplaceExpression() {
			return "$1*$2";
		}
	}
	
	
	static class PasswordDesensitizeType extends DefaultAllExpressionDesensitizeType {
	}
	
	static abstract class CustomExpressionDesensitizeType implements ExpressionDesensitizeType {
	}
	
	static class PhoneDesensitizeType extends CustomExpressionDesensitizeType {
		@Override
		public String getExpression() {
			return "(\\d{3})\\d{4}(\\d{4})";
		}
		@Override
		public String getReplaceExpression() {
			return "$1****$2";
		}
	}
	static class EmailDesensitizeType extends CustomExpressionDesensitizeType {
		@Override
		public String getExpression() {
			return "(^\\w)[^@]*(@.*$)";
		}
		@Override
		public String getReplaceExpression() {
			return "$1****$2";
		}
	}
	static class IdCardDesensitizeType extends CustomExpressionDesensitizeType {
		@Override
		public String getExpression() {
			return "(?<=\\w{3})\\w(?=\\w{4})";
		}
		@Override
		public String getReplaceExpression() {
			return "*";
		}
	}
	static class NameDesensitizeType extends CustomExpressionDesensitizeType {
		@Override
		public String getExpression() {
			return "(?<=\\p{IsHan})\\p{IsHan}";
		}
		@Override
		public String getReplaceExpression() {
			return "*";
		}
	}
	
}
