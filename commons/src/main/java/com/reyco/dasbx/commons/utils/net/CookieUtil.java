package com.reyco.dasbx.commons.utils.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

public class CookieUtil {
	//private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static Map<String, String> getCookies(HttpURLConnection conn) {
        Map<String, String> cookies = new HashMap<>();
        Map<String, List<String>> resHeaders = conn.getHeaderFields();
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, List<String>> entry : resHeaders.entrySet()) {
            String name = entry.getKey();
            if (name == null)
                continue; // http/1.1 line
            List<String> values = entry.getValue();
            if (name.equalsIgnoreCase("Set-Cookie")) {
                for (String value : values) {
                    if (value == null) {
                        continue;
                    }
                    String cookie = value.substring(0, value.indexOf(";"));
                    String[] cookieArry = cookie.split("=");
                    cookies.put(cookieArry[0], cookieArry[1]);
                    sb.append(cookie);
                }
            }
        }

        return cookies;
    }

    public static String getCookie(Map<String, String> cookieMap) {
        if (CollectionUtils.isEmpty(cookieMap)) {
            return "";
        }
        String cookie = "";
        for (String s : cookieMap.keySet()) {
            cookie += s + "=" + cookieMap.get(s) + "; ";
        }
        return cookie;
    }

    /**
	 * 根据名字获取cookie值
	 * @param request
	 * @param CookicName
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public static String getCookieByName(HttpServletRequest request,String CookicName){
		try {
			//获取请求中的cookie
			Cookie[] cookies = request.getCookies();
			//遍历cookie
			if(null != cookies) {
				for (Cookie cookie : cookies) {
					if(cookie.getName().equalsIgnoreCase(CookicName)) {
						return cookie.getValue();
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    

	/**
	 * 将cookie封装到Map里面
	 * @param request
	 * @return
	 */
	private static Map<String,Cookie> readCookieMap(HttpServletRequest request){ 
	    Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
	    Cookie[] cookies = request.getCookies();
	    if(null != cookies){
	        for(Cookie cookie : cookies){
	            cookieMap.put(cookie.getName(), cookie);
	        }
	    }
	    return cookieMap;
	}
	/**
	 *   设置Cookie
	 * @param request           
	 * @param response
	 * @param cookieName     cookie名称
	 * @param path           cookie有限路径
	 * @param domain          
	 * @param maxTime        cookie有效时间
	 * @throws Exception 
	 */
	public static void setCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,String cookieValue,Integer maxAge,String path,Boolean isEncode,String encodeName){
		try {
			if(StringUtils.isBlank(cookieName)) {
				cookieName ="user_token";
			}
			// cookie值如果为null
			if(null == cookieValue) {
				cookieValue="";
			}else if(isEncode) { //cookie值是否进行编码
				if(StringUtils.isBlank(encodeName)) {
					encodeName="utf-8";
				}
				cookieValue=URLEncoder.encode(cookieValue,encodeName);
			}
			Cookie cookie = new Cookie(cookieName,cookieValue);
			cookie.setMaxAge(maxAge);
			if(null != request) {
				String dominName = getDomainName(request);
				if(!"localhost".equals(dominName)) {
					cookie.setDomain(dominName);
				}
			}
			if(StringUtils.isBlank(path)) {
				path="/";
			}
			cookie.setPath(path);
			response.addCookie(cookie);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 *   设置cookie
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param path
	 * @param maxAge
	 */
	public static void setCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,String cookieValue,Integer maxAge,String path){
		setCookie(request,response,cookieName,cookieValue,maxAge,path,false,null);
	}
	public static void setCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,String cookieValue,Integer maxAge){
		setCookie(request,response,cookieName,cookieValue,maxAge,null);
		
	}
	/**
	 * 
	 */
	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,String cookieName) {
		setCookie(request, response, cookieName, "", 0);
	}
	
	
	 /**
     * 得到cookie的域名
     */
    private static final String getDomainName(HttpServletRequest request) {
        String domainName = null;

        // 获取完整的请求URL地址。
        String serverName = request.getRequestURL().toString();
        if (serverName == null || serverName.equals("")) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
			if (serverName.startsWith("http://")){
			    serverName = serverName.substring(7);
			} else if (serverName.startsWith("https://")){
			    serverName = serverName.substring(8);
			}
            final int end = serverName.indexOf("/");
            // .test.com  www.test.com.cn/sso.test.com.cn/.test.com.cn  spring.io/xxxx/xxx
            serverName = serverName.substring(0, end);
            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3) {
                domainName = "." + domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len <= 3 && len > 1) {
                domainName = "." + domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }

        if (domainName != null && domainName.indexOf(":") > 0) {
            String[] ary = domainName.split("\\:");
            domainName = ary[0];
        }
        return domainName;
    }

}
