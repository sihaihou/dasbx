package com.reyco.dasbx.gateway.core.sign.api;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;

import com.reyco.dasbx.gateway.core.constant.Constants;
import com.reyco.dasbx.gateway.core.sign.SignatureException;
import com.reyco.dasbx.gateway.core.sign.SimpleHash;
import com.reyco.dasbx.gateway.core.utils.RequestUtils;

/**
 * 获取Api签名
 * @author reyco
 *
 */
public abstract class AbstractApiSignature implements ApiSignature{
	protected static final Logger log = LoggerFactory.getLogger(AbstractApiSignature.class);
	private int hashIterations;
	public void setHashIterations(int hashIterations) {
		this.hashIterations = hashIterations;
	}
	public int getHashIterations() {
		return hashIterations;
	}
	/**
	 * 获取Api签名
	 */
	@Override
	public String getSign(ServerHttpRequest request) throws SignatureException{
		String key = RequestUtils.getHeader(request, Constants.DASBX_KEY);
		String algorithmName = getSignAlgorithmName(key);
		String source = getSource(request);
		String secret = getSecret(key);
		return doGetSign(algorithmName, source, secret, 1);
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
	protected abstract String doGetSign(String algorithmName,String source,String salt,int hashIterations) throws SignatureException;
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
	/**
	 * 获取源数据
	 * @param request
	 * @return
	 * @throws SignatureException
	 */
	protected String getSource(ServerHttpRequest request) throws SignatureException{
		String key = RequestUtils.getHeader(request, Constants.DASBX_KEY);
		if (StringUtils.isBlank(key)) {
			log.debug("DefaultApiSignature key fail:{}", key);
			throw new SignatureException(GateWayResultCode.DASBX_KEY.getMsg());
		}
		// nonce
		String nonce = RequestUtils.getHeader(request, Constants.DASBX_NONCE);
		if (StringUtils.isBlank(key)) {
			log.debug("DefaultApiSignature nonce fail:{}", key);
			throw new SignatureException(GateWayResultCode.DASBX_NONCE.getMsg());
		}
		// timestamp 合规
		String timestamp = RequestUtils.getHeader(request, Constants.DASBX_TIMESTAMP);
		if (StringUtils.isBlank(timestamp) || !checkTimestamp(timestamp)) {
			log.debug("DefaultApiSignature checktime fail:{}", timestamp);
			throw new SignatureException(GateWayResultCode.DASBX_TIMESTAMP.getMsg());
		}
		// Content_Signature
		String contentSignature = RequestUtils.getHeader(request, Constants.DASBX_CONTENT_SIGNATURE);
		if (StringUtils.isBlank(contentSignature)) {
			log.debug("DefaultApiSignature contentSignature fail:{}", contentSignature);
			throw new SignatureException(GateWayResultCode.DASBX_CONTENT_SIGNATURE.getMsg());
		}
		String url = RequestUtils.getURL(request);
		if(url.contains("?")) {
			url = url.split("\\?")[0];
		}
		String sign = new String("Dasbx-Key="+key+"&Dasbx-Timestamp="+timestamp
				+"&Dasbx-Nonce="+nonce+"&Dasbx-Content-Signature="+contentSignature+"&url="+url);
		log.debug("DefaultApiSignature sign fail:{}", sign);
		return sign;
	}
	/**
	 * @param timestamp
	 * @return 时间验证结果
	 */
	private boolean checkTimestamp(String timestamp) {
		Date date = new Date();
		long time = date.getTime();
		// 1分钟/毫秒
		long add = 1000 * 10;
		long begin = time - add;
		long end = time + add;
		Long catime = Long.valueOf(timestamp);
		log.debug("checkTimestamp time:{} begin:{} after:{} timestamp:{} catime:{}", time, begin, end, timestamp, catime);
		if (catime > begin && catime < end) {
			return true;
		}
		return false;
	}
}
