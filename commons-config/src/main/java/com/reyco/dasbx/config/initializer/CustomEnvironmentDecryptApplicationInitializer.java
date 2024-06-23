package com.reyco.dasbx.config.initializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.bootstrap.BootstrapApplicationListener;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.util.StringUtils;

/**
 * 自定义环境解密初始化器
 * @author reyco
 *
 */
public class CustomEnvironmentDecryptApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered{
	private static Logger logger = LoggerFactory.getLogger(CustomEnvironmentDecryptApplicationInitializer.class);
	public static final String DECRYPTED_PROPERTY_SOURCE_NAME = "customDecrypted";
	public static final String DECRYPTED_BOOTSTRAP_PROPERTY_SOURCE_NAME = "customDecryptedBootstrap";
	public static String ENCRYPTED_PROPERTY_PREFIX = "cipher{";
	public static String ENCRYPTED_PROPERTY_SUFFIX = "}";
	private TextEncryptor encryptor;
	public CustomEnvironmentDecryptApplicationInitializer() {
		this(new CustomTextEncryptor());
	}
	public CustomEnvironmentDecryptApplicationInitializer(TextEncryptor encryptor) {
		setEncryptor(encryptor);
	}
	public TextEncryptor getEncryptor() {
		return encryptor;
	}
	public void setEncryptor(TextEncryptor encryptor) {
		this.encryptor = encryptor;
	}
	@Override
	public int getOrder() {
		return  Ordered.HIGHEST_PRECEDENCE + 16;
	}
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		ConfigurableEnvironment environment = applicationContext.getEnvironment();
		MutablePropertySources propertySources = environment.getPropertySources();
		Set<String> found = new LinkedHashSet<>();
		Map<String, Object> map = decrypt(propertySources);
		if (!map.isEmpty()) {
			found.addAll(map.keySet());
			insert(applicationContext, new SystemEnvironmentPropertySource(DECRYPTED_PROPERTY_SOURCE_NAME, map));
		}
		if (!found.isEmpty()) {
			ApplicationContext parent = applicationContext.getParent();
			if (parent != null) {
				parent.publishEvent(new EnvironmentChangeEvent(parent, found));
			}

		}
	}
	private void insert(ApplicationContext applicationContext,
			PropertySource<?> propertySource) {
		ApplicationContext parent = applicationContext;
		while (parent != null) {
			if (parent.getEnvironment() instanceof ConfigurableEnvironment) {
				ConfigurableEnvironment mutable = (ConfigurableEnvironment) parent.getEnvironment();
				insert(mutable.getPropertySources(), propertySource);
			}
			parent = parent.getParent();
		}
	}
	private void insert(MutablePropertySources propertySources,
			PropertySource<?> propertySource) {
		if (propertySources.contains(BootstrapApplicationListener.BOOTSTRAP_PROPERTY_SOURCE_NAME)) {
			if (DECRYPTED_BOOTSTRAP_PROPERTY_SOURCE_NAME.equals(propertySource.getName())) {
				propertySources.addBefore(BootstrapApplicationListener.BOOTSTRAP_PROPERTY_SOURCE_NAME,propertySource);
			}
			else {
				propertySources.addAfter(BootstrapApplicationListener.BOOTSTRAP_PROPERTY_SOURCE_NAME,propertySource);
			}
		}
		else {
			propertySources.addFirst(propertySource);
		}
	}
	private Map<String, Object> decrypt(PropertySources propertySources) {
		Map<String, Object> properties = merge(propertySources);
		decrypt(properties);
		return properties;
	}
	private Map<String, Object> merge(PropertySources propertySources) {
		Map<String, Object> properties = new LinkedHashMap<>();
		List<PropertySource<?>> sources = new ArrayList<>();
		for (PropertySource<?> source : propertySources) {
			sources.add(0, source);
		}
		for (PropertySource<?> source : sources) {
			merge(source, properties);
		}
		return properties;
	}
	private void merge(PropertySource<?> source, Map<String, Object> properties) {
		if (source instanceof CompositePropertySource) {
			List<PropertySource<?>> sources = new ArrayList<>(((CompositePropertySource) source).getPropertySources());
			Collections.reverse(sources);
			sources.stream().forEach(nested->{
				merge(nested, properties);
			});
		}else if (source instanceof EnumerablePropertySource) {
			EnumerablePropertySource<?> enumerable = (EnumerablePropertySource<?>) source;
			Arrays.stream(enumerable.getPropertyNames()).forEach(key->{
				Object property = source.getProperty(key);
				if (property != null) {
					String value = property.toString();
					if (isEncrypted(value)) {
						properties.put(key, value);
					}else {
						properties.remove(key);
					}
				}
			});
		}
	}
	private void decrypt(Map<String, Object> properties) {
		properties.replaceAll((key, value)->decrypt(key, value.toString()));
	}
	private String decrypt(String key, String value) {
		try {
			value = unwrapEncryptedValue(value);
			return value;
		}
		catch (Exception e) {
			String message = "Cannot decrypt: key=" + key;
			logger.warn(message, e);
			return "";
		}
	}
	/** 
	 *  解密
	 * @param value
	 * @return
	 */
	private String unwrapEncryptedValue(String v) {
		String r;
		if(isEncrypted(v)) {
			int si,ei=0;
			if ((si=v.lastIndexOf(CustomEnvironmentDecryptApplicationInitializer.ENCRYPTED_PROPERTY_PREFIX)) != -1) {
				if ((ei=findEncryptedEndIndex(v, si)) != -1) {
					String svp = v.substring(0,si);
					String p = v.substring(si + CustomEnvironmentDecryptApplicationInitializer.ENCRYPTED_PROPERTY_PREFIX.length(), ei);
					p = this.encryptor.decrypt(p);
					String svs = v.substring(ei+1);
					r = svp + p + svs;
					return unwrapEncryptedValue(r);
				}
			}
		}
		return v;
	}
	/**
	 * 获取接密结束索引位置
	 * @param b
	 * @param si
	 * @return
	 */
	private int findEncryptedEndIndex(CharSequence b, int si) {
		int i = si + CustomEnvironmentDecryptApplicationInitializer.ENCRYPTED_PROPERTY_PREFIX.length();
		int withinNestedPlaceholder = 0;
		while (i < b.length()) {
			if (StringUtils.substringMatch(b, i, CustomEnvironmentDecryptApplicationInitializer.ENCRYPTED_PROPERTY_SUFFIX)) {
				if (withinNestedPlaceholder > 0) {
					withinNestedPlaceholder--;
					i = i + CustomEnvironmentDecryptApplicationInitializer.ENCRYPTED_PROPERTY_SUFFIX.length();
				} else {
					return i;
				}
			} else if (StringUtils.substringMatch(b, i, CustomEnvironmentDecryptApplicationInitializer.ENCRYPTED_PROPERTY_PREFIX)) {
				withinNestedPlaceholder++;
				i = i + CustomEnvironmentDecryptApplicationInitializer.ENCRYPTED_PROPERTY_PREFIX.length();
			} else {
				i++;
			}
		}
		return -1;
	}
	/**
	 * 是否加密过
	 * @param value
	 * @return
	 */
	private boolean isEncrypted(String value) {
        if (value == null) {
            return false;
        }
        int pi,ps;
        if((pi=value.lastIndexOf(CustomEnvironmentDecryptApplicationInitializer.ENCRYPTED_PROPERTY_PREFIX))!=-1 
        	&& (ps=value.indexOf(CustomEnvironmentDecryptApplicationInitializer.ENCRYPTED_PROPERTY_SUFFIX))!=-1 
        	&& pi<ps) {
        	return true;
        }
        return false;
    }
}
