package com.reyco.dasbx.commons.utils.string;

public class StringUtils {
	
	/**
	 * 去除空格
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		str = convertUnicode(str);
		str = trimWhiteSpace(str);
		str = normalizeHtmlTransf(str);
		str = str.trim();
		str = normalizeSegTransf(str);
		return str;
	}
	/**
	 * 转义符替换
	 * @param s
	 * @return
	 */
	public static String normalizeHtmlTransf(String str){
		String ret = str.replaceAll("&bull;", "·");
		ret = ret.replaceAll("&middot;", "·");
		ret = ret.replaceAll("&nbsp;", " ");
		ret = ret.replaceAll("&quot;", "\"");
		ret = ret.replaceAll("&amp;", "&");
		ret = ret.replace('・', '·');
		ret = ret.replace("&ldquo;", "\"");
		ret = ret.replace("&rdquo;", "\"");
		ret = ret.replace("&hellip;", "...");
		ret = ret.replace("&lt;", "<");
		ret = ret.replace("&gt;", ">");
		ret = ret.replace("&mdash;", "—");
		ret = ret.replace("&ndash;", "–");
		ret = ret.replace("&tilde;", "~");
		ret = ret.replace("&lsquo;", "'");
		ret = ret.replace("&rsquo;", "'");
		ret = ret.replace("&sbquo;", ",");
		ret = ret.replace("&lsaquo;", "‹");
		ret = ret.replace("&rsaquo;", "›");
		ret = ret.replace("&hellip;", "…");
		ret = ret.replace("|", " ");
		return ret;
	}
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String normalizeSegTransf(String str){
		String ret = str.replaceAll("\r\n;", " ");
		ret = ret.replace("\n", "");
		ret = ret.replace("|", " ");
		return ret;
	}
	/**
	 * 去掉前后空白字符，空白符包含：空格、tab 键、换行符。
	 * @param str
	 * @return
	 */
	public static String trimWhiteSpace(String str){
		String s = replaceBlank(str);
        String ret = s.trim();
        return ret;
	}
	/**
	 * 替换空白字符，空白符包含：空格、tab 键、换行符。
	 * @param str
	 * @return
	 */
	private static String replaceBlank(String str) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < str.length(); i ++){
        	char c = str.charAt(i);
        	boolean bspace = Character.isWhitespace(c);
        	if (bspace){
            	c = ' ';
        	}
        	buffer.append(c);
        }
        return buffer.toString();
    }
	/**
	 * 字符串转Unicode编码
	 * @param origin
	 * @return
	 */
	private static String convertUnicode(String origin) {
		char c;
        int l = origin.length();
        StringBuffer sb = new StringBuffer(l);
        for (int x = 0; x < l;) {
            c = origin.charAt(x++);
            if (c == '\\') {
                c = origin.charAt(x++);
                if (c == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        c = origin.charAt(x++);
                        switch (c) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + c - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + c - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + c - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                    }
                    sb.append((char) value);
                } else {
                    if (c == 't') {
                        c = '\t';
                    }else if (c == 'r') {
                        c = '\r';
                    }else if (c == 'n') {
                        c = '\n';
                    }else if (c == 'f') {
                        c = '\f';
                    }
                    sb.append(c);
                }
            } else {
            	sb.append(c);
            }
        }
        return sb.toString();
	}
}
