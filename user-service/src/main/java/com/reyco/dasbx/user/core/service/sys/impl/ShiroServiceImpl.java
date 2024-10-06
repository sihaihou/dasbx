package com.reyco.dasbx.user.core.service.sys.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.convert.JsonUtils;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.constants.Constants;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.model.vo.SysAccountToken;
import com.reyco.dasbx.user.core.dao.sys.SysAccountDao;
import com.reyco.dasbx.user.core.dao.sys.SysMenuDao;
import com.reyco.dasbx.user.core.service.sys.SysShiroService;

/**
 * 权限管理
 * @author reyco
 *
 */
@Service
public class ShiroServiceImpl implements SysShiroService {

    @Autowired
    private SysAccountDao sysAccountDao;
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Set<String> getUserPermissions(Long userId) {
        List<String> permsList = null;
        // 系统管理员，拥有最高权限
        if (userId.equals(Constants.SUPER_ADMIN)) {
        	permsList = sysMenuDao.queryPerms();
           
        } else {
            permsList = sysMenuDao.queryPermsByUserId(userId);
        }
        return permsList.stream()
        		.filter(StringUtils::isNotBlank)
        		//.map(term->term.split(","))
        		//.flatMap(Stream::of)
        		.flatMap(term->Arrays.stream(term.split(",")))
        		.collect(Collectors.toSet());
    }

    @Override
    public SysAccount getUser(Long userId) {
        return sysAccountDao.getById(userId);
    }

    @Override
    public SysAccountToken getToken(String token) {
        String accountTokenJson = redisTemplate.opsForValue().get(CachePrefixConstants.TOKEN_PREFIX+token);
        SysAccountToken sysAccountToken = JsonUtils.jsonToObj(accountTokenJson, SysAccountToken.class);
        return sysAccountToken;
    }
}