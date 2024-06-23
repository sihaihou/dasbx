package com.reyco.dasbx.login.core.service.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.commons.utils.SimpleHash;
import com.reyco.dasbx.config.exception.core.AuthenticationException;

/**
 * 抽象正在认证
 * @author reyco
 *
 */
public abstract class AbstractAuthenticating implements Authenticating {

	protected static final Logger log = LoggerFactory.getLogger(AbstractAuthenticating.class);

	@Override
	public final AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		AuthenticationInfo info = doGetAuthenticationInfo(token);
		if(info!=null) {
			assertCredentialsMatch(token, info);
		}
		return info;
	}
	/**
	 * 验证凭证
	 * @param token
	 * @param info
	 * @throws AuthenticationException
	 */
	private void assertCredentialsMatch(AuthenticationToken token,AuthenticationInfo info) throws AuthenticationException {
		String credentials = token.getCredentials().toString();
		if(info instanceof SaltAuthenticationInfo) {
			SaltAuthenticationInfo saltInfo = (SaltAuthenticationInfo)info;
			SimpleHash simpleHash = new SimpleHash(saltInfo.getAlgorithmName(),credentials, saltInfo.getSalt(), saltInfo.getHashIterations());
			credentials = simpleHash.toHex();
		}
		if(!credentials.equals(info.getCredentials().toString())) {
			log.error("密码错误!");
			throw new AuthenticationException("用户名或密码错误!");
		}
	}
	protected abstract AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
			throws AuthenticationException;

}
