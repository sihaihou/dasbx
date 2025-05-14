package com.reyco.dasbx.login.core.service.impl;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.commons.utils.convert.Convert;
import com.reyco.dasbx.commons.utils.convert.JsonUtils;
import com.reyco.dasbx.commons.utils.encrypt.SecretKeyUtils;
import com.reyco.dasbx.commons.utils.net.CusAccessObjectUtil;
import com.reyco.dasbx.commons.utils.net.IPDataUtils;
import com.reyco.dasbx.commons.utils.random.RandomNumberUtils;
import com.reyco.dasbx.config.exception.core.ArgumentException;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.mail.MailEventListener;
import com.reyco.dasbx.config.mail.MailEventListener.MailContent;
import com.reyco.dasbx.config.mail.MailEventListener.MailEvent;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.id.core.IdGenerator;
import com.reyco.dasbx.login.core.feign.AccountFeignClientService;
import com.reyco.dasbx.login.core.feign.FullnameFeignClientService;
import com.reyco.dasbx.login.core.model.dto.EmailLoginDto;
import com.reyco.dasbx.login.core.model.dto.PasswordLoginDto;
import com.reyco.dasbx.login.core.model.msg.AccountRegisterMessage;
import com.reyco.dasbx.login.core.model.msg.SysLoginLogLoginMessage;
import com.reyco.dasbx.login.core.model.msg.SysLoginLogLogoutMessage;
import com.reyco.dasbx.login.core.service.LoginService;
import com.reyco.dasbx.login.core.service.authentication.AuthenticationInfo;
import com.reyco.dasbx.login.core.service.authentication.AuthenticationToken;
import com.reyco.dasbx.login.core.service.authentication.Authenticator;
import com.reyco.dasbx.login.core.service.authentication.UsernamePasswordToken;
import com.reyco.dasbx.login.core.utils.AESUtils;
import com.reyco.dasbx.model.constants.AccountType;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.constants.Constants;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.model.vo.SysAccountToken;
import com.reyco.dasbx.rabbitmq.service.RabbitProducrService;
import com.reyco.dasbx.redis.auto.configuration.RedisUtil;

@Service
public class LoginServiceImpl implements LoginService{
	@Autowired
	private Authenticator authenticator;
	@Autowired
	private RabbitProducrService rabbitProducrService;
	@Autowired
	private AccountFeignClientService accountFeignClientService;
	@Autowired
	private IdGenerator<Long> idGenerator;
	@Autowired
	private FullnameFeignClientService fullnameFeignClientService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public SysAccountToken login(String t) throws AuthenticationException, ArgumentException {
		String secretKey = SecretKeyUtils.getSecretKey();
		String tokenJson = AESUtils.decrypt(t,secretKey);
		PasswordLoginDto passwordLoginDto = JsonUtils.jsonToObj(tokenJson, PasswordLoginDto.class);
		if(StringUtils.isBlank(passwordLoginDto.getPassword()) || StringUtils.isBlank(passwordLoginDto.getUsername())) {
			throw new ArgumentException("用户名密码不能为空...");
		}
		AuthenticationToken token = new UsernamePasswordToken(passwordLoginDto.getUsername(),passwordLoginDto.getPassword());
		AuthenticationInfo info = authenticator.authenticate(token);
		SysAccountToken accountToken = Convert.sourceToTarget(info.getPrincipal(), SysAccountToken.class);
		TokenUtils.createToken(accountToken);
		publishLogin(accountToken);
		return accountToken;
	}
	@Override
	public void createEmailCode(String email) {
		String verificationCode = RandomNumberUtils.randomNumber();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String deviceId = TokenUtils.getDeviceId(request);
		redisUtil.set(CachePrefixConstants.DEVICE_BING_EMAIL_BING_CODE_PREFIX+deviceId+":"+email, verificationCode,Constants.VERIFICATION_CODE_TIME,TimeUnit.MILLISECONDS);
		MailContent mailContent = new MailContent();
		mailContent.setSubject("验证您的电子邮件地址");
		mailContent.setContent("感谢您登录/注册Da爱.只为关注你账户。为确保当前是您本人操作，请您输入此邮件中提示的验证码以完成账户注册/注册。如您无需登录/注册Da爱.只为关注你账户，请忽略该信息。验证码："+verificationCode+"(此验证码将在发送后5分钟过期)。");
		mailContent.setTos(email);
		mailContent.setIsHtml(true);
		MailEvent mailEvent = new MailEventListener.MailEvent(this,mailContent);
		applicationContext.publishEvent(mailEvent);
	}
	@Override
	public SysAccountToken emailLogin(EmailLoginDto emailLoginDto) throws ArgumentException, AuthenticationException {
		//非空验证
		if(StringUtils.isBlank(emailLoginDto.getEmail()) || StringUtils.isBlank(emailLoginDto.getCode())) {
			throw new ArgumentException("邮箱或验证码不能为空...");
		}
		//验证验证码是否正确
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String deviceId = TokenUtils.getDeviceId(request);
		String verificationCodeKey = CachePrefixConstants.DEVICE_BING_EMAIL_BING_CODE_PREFIX+deviceId+":"+emailLoginDto.getEmail();
		if(!redisUtil.hasKey(verificationCodeKey)
				|| !redisUtil.get(verificationCodeKey).equalsIgnoreCase(emailLoginDto.getCode())) {
			throw new ArgumentException("验证码错误");
		}
		redisUtil.delete(verificationCodeKey);
		//创建token
		SysAccount account = accountFeignClientService.getByEmail(emailLoginDto.getEmail());
		if(account==null) {
			//是否需要注册
			AccountRegisterMessage accountRegisterMessage = new AccountRegisterMessage();
			accountRegisterMessage.setUid(idGenerator.getGeneratorId()+"");
			accountRegisterMessage.setNickname(fullnameFeignClientService.randomFullName().getFullname());
			accountRegisterMessage.setType(AccountType.ACCOUNT_TYPE_BACK.getType());
			account = Convert.sourceToTarget(accountRegisterMessage, SysAccount.class);
			rabbitProducrService.send(RabbitConstants.ACCOUNT_EXCHANGE, RabbitConstants.ACCOUNT_REGISTER_ROUTE_KEY,accountRegisterMessage);
		}
		SysAccountToken accountToken = Convert.sourceToTarget(account, SysAccountToken.class);
		TokenUtils.createToken(accountToken);
		publishLogin(accountToken);
		return accountToken;
	}
	@Override
	public Boolean isLogin() throws ArgumentException, AuthenticationException {
		SysAccountToken token = TokenUtils.getToken();
		return token!=null;
	}
	/**
	 * 发布登录
	 * @param sysAccountToken
	 */
	private void publishLogin(SysAccountToken sysAccountToken) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String device = request.getHeader("User-Agent");
		String ip = CusAccessObjectUtil.getIpAddress(request);
		String city = IPDataUtils.getCityName(ip);
		SysLoginLogLoginMessage sysLoginLogLoginMessage = new SysLoginLogLoginMessage();
		sysLoginLogLoginMessage.setCode(Long.parseLong(sysAccountToken.getToken()));
		sysLoginLogLoginMessage.setUserId(sysAccountToken.getId());
		sysLoginLogLoginMessage.setUsername(sysAccountToken.getUsername());
		sysLoginLogLoginMessage.setLoginDevice(device);
		sysLoginLogLoginMessage.setLoginCity(city);
		sysLoginLogLoginMessage.setLoginIp(ip);
		sysLoginLogLoginMessage.setGmtLogin(Dasbx.getCurrentTime());
		rabbitProducrService.send(RabbitConstants.LOG_EXCHANGE, RabbitConstants.LOG_LOGIN_ROUTE_KEY, sysLoginLogLoginMessage);
	}
	
	@Override
	public void logout(HttpServletRequest request) throws AuthenticationException {
		SysAccountToken sysAccountToken = TokenUtils.getToken(request);
		TokenUtils.removeToken(request);
		String device = request.getHeader("User-Agent");
		String ip = CusAccessObjectUtil.getIpAddress(request);
		String city = IPDataUtils.getCityName(ip);
		SysLoginLogLogoutMessage sysLoginLogLogoutMessage = new SysLoginLogLogoutMessage();
		sysLoginLogLogoutMessage.setCode(Long.parseLong(sysAccountToken.getToken()));
		sysLoginLogLogoutMessage.setLogoutDevice(device);
		sysLoginLogLogoutMessage.setLogoutCity(city);
		sysLoginLogLogoutMessage.setLogoutIp(ip);
		sysLoginLogLogoutMessage.setGmtLogout(Dasbx.getCurrentTime());
		sysLoginLogLogoutMessage.setGmtModified(Dasbx.getCurrentTime());
		sysLoginLogLogoutMessage.setModifiedBy(sysAccountToken.getId());
		rabbitProducrService.send(RabbitConstants.LOG_EXCHANGE, RabbitConstants.LOG_LOGOUT_ROUTE_KEY, sysLoginLogLogoutMessage);
	}
	
}
