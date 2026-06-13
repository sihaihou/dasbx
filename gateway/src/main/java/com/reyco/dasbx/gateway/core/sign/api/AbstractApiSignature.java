package com.reyco.dasbx.gateway.core.sign.api;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;

import com.reyco.dasbx.gateway.core.constant.Constants;
import com.reyco.dasbx.gateway.core.sign.SignatureException;
import com.reyco.dasbx.gateway.core.sign.SigningContent;
import com.reyco.dasbx.gateway.core.sign.SimpleHash;
import com.reyco.dasbx.gateway.core.utils.RequestUtils;

/**
 * 获取Api签名
 * @author reyco
 *
 */
public abstract class AbstractApiSignature implements ApiSignature{
	
	protected static final Logger log = LoggerFactory.getLogger(AbstractApiSignature.class);
	
	/**
	 * 获取Api签名
	 */
	@Override
	public String sign(ServerHttpRequest request) throws SignatureException{
		SigningContent context = getSigningContent(request);
		wrapSigningContent(context);
		return doSign(context);
	}
	/**
	 * 获取密匙
	 * @param first
	 * @return
	 */
	protected abstract String getSecret(String appKey) throws SignatureException;
	/**
	 * 获取Api签名
	 * @return
	 */
	protected abstract String doSign(SigningContent context) throws SignatureException;
	/**
	 * 
	 * @param context
	 * @throws SignatureException
	 */
	protected void wrapSigningContent(SigningContent context) throws SignatureException {
		
	}
	/**
	 * 获取加密算法名称
	 * @param type
	 * @return
	 */
	protected String getSignAlgorithmName(String type) throws SignatureException{
		if(SimpleHash.SHA_256.equalsIgnoreCase(type)){
            return SimpleHash.SHA_256;
        }
        if(SimpleHash.MD5.equalsIgnoreCase(type)){
        	return SimpleHash.MD5;
        }
        return null;
	}
	protected SigningContent getSigningContent(ServerHttpRequest request) throws SignatureException{
		SigningContent context = new SigningContent();
		//1.appId
		String appId = RequestUtils.getHeader(request, Constants.DASBX_APPID);
		if (StringUtils.isBlank(appId)) {
			log.warn("Dasbx-AppId is empty! url:{}", RequestUtils.getPath(request));
			throw new SignatureException(GateWayResultCode.DASBX_APPID.getMsg());
		}
		String secret = getSecret(appId);
		context.setSecret(secret);
		
		//2.nonce
		String nonce = RequestUtils.getHeader(request, Constants.DASBX_NONCE);
		if (StringUtils.isBlank(nonce)) {
			log.warn("Dasbx-Nonce is empty! url:{}",  RequestUtils.getPath(request));
			throw new SignatureException(GateWayResultCode.DASBX_NONCE.getMsg());
		}
		context.setNonce(nonce);	
		
		//3.timestamp 合规
		String timestamp = RequestUtils.getHeader(request, Constants.DASBX_TIMESTAMP);
		if (StringUtils.isBlank(timestamp)) {
			log.warn("Dasbx-Timestamp is empty! url:{}",  RequestUtils.getPath(request));
			throw new SignatureException(GateWayResultCode.DASBX_TIMESTAMP.getMsg());
		}
		if (!checkTimestamp(timestamp)) {
			log.warn("Dasbx-Timestamp has expired! url:{}",  RequestUtils.getPath(request));
			throw new SignatureException(GateWayResultCode.DASBX_TIMESTAMP.getMsg());
		}
		context.setTimestamp(timestamp);		
		
		//4.Content_Signature
		String contentSignature = RequestUtils.getHeader(request, Constants.DASBX_CONTENT_SIGNATURE);
		if (StringUtils.isBlank(contentSignature)) {
			log.debug("Dasbx-Content-Signature is empty! url:{}",  RequestUtils.getPath(request));
			throw new SignatureException(GateWayResultCode.DASBX_CONTENT_SIGNATURE.getMsg());
		}
		context.setBody(contentSignature);
		
		//5.method
		context.setMethod(request.getMethod().name());
		
		//6.path
		String url = RequestUtils.getURL(request);
		if(url.contains("?")) {
			url = url.split("\\?")[0];
		}
		context.setUrl(url);
		
		//7.返回
		return context;
		
	}
	/**
	 * 一分钟过期
	 * @param timestamp
	 * @return 时间验证结果
	 */
	private boolean checkTimestamp(String timestamp) {
		long now = System.currentTimeMillis();
		Long requestTime = Long.parseLong(timestamp);
		log.debug("checkTimestamp now:{} requestTime:{}",now, requestTime);
		if (Math.abs(now - Long.parseLong(timestamp)) > 60*1000) {
			return false;
		}
		return true;
	}
}
