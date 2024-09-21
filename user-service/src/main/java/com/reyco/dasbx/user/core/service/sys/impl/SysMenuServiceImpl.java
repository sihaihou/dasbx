package com.reyco.dasbx.user.core.service.sys.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.model.constants.CachePrefixInfoConstants;
import com.reyco.dasbx.model.constants.Constants;
import com.reyco.dasbx.user.core.dao.sys.SysMenuDao;
import com.reyco.dasbx.user.core.model.domain.SysMenu;
import com.reyco.dasbx.user.core.model.dto.sys.SysMenuDeleteDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysMenuDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysMenuInsertDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysMenuUpdateDto;
import com.reyco.dasbx.user.core.model.po.sys.SysMenuDeletePO;
import com.reyco.dasbx.user.core.model.po.sys.SysMenuInsertPO;
import com.reyco.dasbx.user.core.model.po.sys.SysMenuUpdatePO;
import com.reyco.dasbx.user.core.model.vo.sys.SysMenuInsertVO;
import com.reyco.dasbx.user.core.service.sys.SysMenuService;

/**
 * 菜单信息
 * 
 * @author reyco
 *
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuDao sysMenuDao;

	@Override
	@Cacheable(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.USER_MENU_COMMENT_INFO_PREFIX,key="#menuId")
	public SysMenuDto get(Long menuId) {
		SysMenu sysMenu = sysMenuDao.get(menuId);
		SysMenuDto sysMenuDto = Convert.sourceToTarget(sysMenu, SysMenuDto.class);
		return sysMenuDto;
	}

	@Override
	public Integer getCountByNameAndById(Long menuId, Long menuParentId, Integer menuType, String menuName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("menuType", menuType);
		map.put("menuParentId", menuParentId);
		map.put("menuName", menuName);
		if (null != menuId) {
			map.put("menuId", menuId);
		}
		return sysMenuDao.getCountByNameAndById(map);
	}

	@Override
	public SysMenuInsertVO save(SysMenuInsertDto sysMenuInsertDto) {
		SysMenuInsertPO sysMenuInsertPO = Convert.sourceToTarget(sysMenuInsertDto, SysMenuInsertPO.class);
		sysMenuDao.save(sysMenuInsertPO);
		SysMenuInsertVO sysMenuInsertVO = Convert.sourceToTarget(sysMenuInsertPO, SysMenuInsertVO.class);
		return sysMenuInsertVO;
	}

	@Override
	@CacheEvict(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.USER_MENU_COMMENT_INFO_PREFIX,key="#sysMenuUpdateDto.id")
	public void update(SysMenuUpdateDto sysMenuUpdateDto) {
		SysMenuUpdatePO sysMenuUpdatePO = Convert.sourceToTarget(sysMenuUpdateDto, SysMenuUpdatePO.class);
		sysMenuDao.update(sysMenuUpdatePO);
	}

	@Override
	@CacheEvict(cacheManager="redisCacheManager",value=CachePrefixInfoConstants.USER_MENU_COMMENT_INFO_PREFIX,key="#sysMenuDeleteDto.id")
	public void delete(SysMenuDeleteDto sysMenuDeleteDto) {
		SysMenuDeletePO sysMenuDeletePO = Convert.sourceToTarget(sysMenuDeleteDto, SysMenuDeletePO.class);
		sysMenuDao.delete(sysMenuDeletePO);
	}

	@Override
	public Map<String, List<?>> nav(Long userId) {
		List<SysMenuDto> sysMenuDtos = null;
		if(userId.equals(Constants.SUPER_ADMIN)) {
			sysMenuDtos = sysMenuDao.queryAll();
		}else {
			sysMenuDtos = sysMenuDao.queryAllByUserId(userId);
		}
		if(CollectionUtils.isEmpty(sysMenuDtos)) {
			HashMap<String, List<?>> data = new HashMap<String, List<?>>();
		    data.put("menuList", new ArrayList<>());
		    data.put("permissions", new ArrayList<>());
		    return data;
		}
		List<SysMenuDto> menus = new ArrayList<SysMenuDto>(sysMenuDtos.size());
		List<SysMenuDto> perms = new ArrayList<SysMenuDto>(sysMenuDtos.size());
		for (SysMenuDto sysMenuDto : sysMenuDtos) {
			if(sysMenuDto.getType()==2) {
				perms.add(sysMenuDto);
			}else {
				menus.add(sysMenuDto);
			}
		}
		List<SysMenuDto> menuList = buildTree(menus);
		List<String> permissions = buildPerms(perms);
        HashMap<String, List<?>> data = new HashMap<String, List<?>>();
        data.put("menuList", menuList);
        data.put("permissions", permissions);
		return data;
	}
	
	@Override
	public List<SysMenuDto> tree() {
		List<SysMenuDto> menus = sysMenuDao.queryAll();
		List<SysMenuDto> buildTree = buildTree(menus);
		return buildTree;
	}

	@Override
	public List<SysMenuDto> queryDirectoryByParentId(Long parentId) {
		if (null == parentId) {
			parentId = 0L;
		}
		return sysMenuDao.queryDirectoryByParentId(parentId);
	}

	@Override
	public List<SysMenuDto> queryChildrensByParentId(Long parentId) {
		return sysMenuDao.queryChildrensByParentId(parentId);
	}
	/**
	 * 构建树状菜单消息
	 * @param menus
	 * @return
	 */
	private List<SysMenuDto> buildTree(List<SysMenuDto> menus){
		Map<Long, SysMenuDto> menuMap = menus.stream()
				.collect(Collectors.toMap(SysMenuDto::getId, Function.identity()));
		menus.stream().forEach(menu->{
			SysMenuDto sysMenuDto = menuMap.get(menu.getParentId());
			if (sysMenuDto != null) {
				List<SysMenuDto> children = sysMenuDto.getChildren();
				if (children == null) {
					children = new ArrayList<>();
					sysMenuDto.setChildren(children);
				}
				children.add(menu);
			}
		});
		List<SysMenuDto> firstMenus = menuMap.values().stream()
				.filter(sysMenuDto -> sysMenuDto.getParentId().intValue() == 0)
				.collect(Collectors.toList());
		sortSysMenuDtos(firstMenus);
		return firstMenus;
	}
	/**
	 * 构建权限列表Perms
	 * @param perms
	 * @return
	 */
	private List<String> buildPerms(List<SysMenuDto> perms){
        return perms.stream()
        		.filter(sysMenuDto->StringUtils.isNotBlank(sysMenuDto.getPerms()))
        		//.map(sysMenuDto->sysMenuDto.getPerms().split(","))
        		//.flatMap(Stream::of)
        		.flatMap(sysMenuDto->Arrays.stream(sysMenuDto.getPerms().split(",")))
        		.distinct()
        		.collect(Collectors.toList());
	}
	/**
	 * 排序SysMenuDto
	 * @param menus
	 */
	private void sortSysMenuDtos(List<SysMenuDto> menus) {
		if (CollectionUtils.isEmpty(menus)) {
			return;
		}
		Collections.sort(menus, new SysMenuDtoComparator());
		menus.parallelStream().forEach(sysMenuDto -> {
			sortSysMenuDtos(sysMenuDto.getChildren());
		});
	}
	/**
	 * SysMenuDto比较器
	 * @author reyco
	 *
	 */
	public static class SysMenuDtoComparator implements Comparator<SysMenuDto>{
		@Override
		public int compare(SysMenuDto o1, SysMenuDto o2) {
			return (o1.getOrderNum() == null ? 0 : o1.getOrderNum())
					- (o2.getOrderNum() == null ? 0 : o2.getOrderNum());
		}
	}
}