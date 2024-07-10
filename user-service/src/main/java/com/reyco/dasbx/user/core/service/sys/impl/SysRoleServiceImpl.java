package com.reyco.dasbx.user.core.service.sys.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.es.core.client.ElasticsearchClient;
import com.reyco.dasbx.es.core.search.SearchVO;
import com.reyco.dasbx.user.core.constant.Constants;
import com.reyco.dasbx.user.core.dao.sys.SysAccountDao;
import com.reyco.dasbx.user.core.dao.sys.SysRoleDao;
import com.reyco.dasbx.user.core.model.domain.SysRole;
import com.reyco.dasbx.user.core.model.dto.sys.SysRoleDeleteDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysRoleDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysRoleInsertDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysRoleSearchDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysRoleUpdateDto;
import com.reyco.dasbx.user.core.model.es.po.SysRoleElasticsearchDocument;
import com.reyco.dasbx.user.core.model.es.search.impl.SysRoleSearch;
import com.reyco.dasbx.user.core.model.po.sys.SysRoleDeletePO;
import com.reyco.dasbx.user.core.model.po.sys.SysRoleInsertPO;
import com.reyco.dasbx.user.core.model.po.sys.SysRoleReq;
import com.reyco.dasbx.user.core.model.po.sys.SysRoleSelectPO;
import com.reyco.dasbx.user.core.model.po.sys.SysRoleUpdatePO;
import com.reyco.dasbx.user.core.model.vo.sys.SysRoleInfoVO;
import com.reyco.dasbx.user.core.service.sys.SysRoleMenuService;
import com.reyco.dasbx.user.core.service.sys.SysRoleService;

/**
 * 权限管理
 * @author reyco
 *
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    private static Logger logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);
    @Autowired
    private SysRoleDao sysRoleMapper;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysAccountDao sysAccountDao;
    @Autowired
	private ElasticsearchClient<SysRoleElasticsearchDocument> elasticsearchClient;
    @Autowired
    private SysRoleSearch sysRoleSearch;
    
    @Override
    public SysRoleInfoVO get(Long roleId) {
        SysRole sysRole = sysRoleMapper.get(roleId);
        SysRoleInfoVO sysRoleInfoVO = Convert.sourceToTarget(sysRole, SysRoleInfoVO.class);
        sysRoleInfoVO.setCreateByDesc(sysAccountDao.getById(sysRoleInfoVO.getCreateBy()).getUsername());
        sysRoleInfoVO.setModifiedByDesc(sysAccountDao.getById(sysRoleInfoVO.getModifiedBy()).getUsername());
        List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
        sysRoleInfoVO.setMenuIdList(menuIdList);
        return sysRoleInfoVO;
    }
    @Override
	public List<String> getSuggestion(String keyword) throws Exception {
		List<String> suggestion = elasticsearchClient.getSuggestion(Constants.ROLE_INDEX_NAME, keyword);
		if (suggestion == null) {
			suggestion = new ArrayList<>();
		}
		return suggestion;
	}
    @Override
    public SearchVO<SysRoleInfoVO> search(SysRoleSearchDto sysRoleSearchDto) throws IOException {
    	logger.debug("role搜索");
    	return sysRoleSearch.search(sysRoleSearchDto);
    }
    @Override
    public List<SysRoleDto> select(SysRoleReq sysRoleReq) {
    	SysRoleSelectPO sysRoleSelectPO = Convert.sourceToTarget(sysRoleReq, SysRoleSelectPO.class);
        List<SysRole> sysRoleList = sysRoleMapper.list(sysRoleSelectPO);
        if (CollectionUtils.isEmpty(sysRoleList)) {
            return new ArrayList<SysRoleDto>();
        }
        List<SysRoleDto> sysRoleDtoList = Convert.sourceListToTargetList(sysRoleList, SysRoleDto.class);
        return sysRoleDtoList;
    }

    @Override
    public Integer initElasticsearchSysRole() throws IOException {
    	int count = 0;
    	List<SysRole> sysRoles = sysRoleMapper.getAll();
    	List<SysRoleElasticsearchDocument> sysRoleElasticsearchDocuments = new ArrayList<>(); 
    	SysRoleElasticsearchDocument sysRoleElasticsearchDocument=null;
    	for (SysRole sysRole : sysRoles) {
    		sysRoleElasticsearchDocument = Convert.sourceToTarget(sysRole, SysRoleElasticsearchDocument.class);
    		Set<String> suggestionSet = new HashSet<>();
    		suggestionSet.add(sysRoleElasticsearchDocument.getName());
    		sysRoleElasticsearchDocument.setSuggestion(new ArrayList<>(suggestionSet));
    		sysRoleElasticsearchDocuments.add(sysRoleElasticsearchDocument);
		}
    	int batchAddDocument = elasticsearchClient.batchAddDocument(Constants.ROLE_INDEX_NAME, sysRoleElasticsearchDocuments);
    	count+=batchAddDocument;
    	return count;
    }
    
    @Override
    public void update(SysRoleUpdateDto sysRoleUpdateDto){
    	SysRoleUpdatePO sysRoleUpdatePO = Convert.sourceToTarget(sysRoleUpdateDto, SysRoleUpdatePO.class);
        //修改角色的基本数据信息
        sysRoleMapper.update(sysRoleUpdatePO);
        //修改角色和菜单的关联关系
        sysRoleMenuService.saveOrUpdate(sysRoleUpdatePO.getId(), sysRoleUpdateDto.getMenuIdList());
    }

    @Override
    public void save(SysRoleInsertDto sysRoleInsertDto){
    	SysRoleInsertPO sysRoleInsertPO = Convert.sourceToTarget(sysRoleInsertDto, SysRoleInsertPO.class);
        //新建角色基本信息数据
        sysRoleMapper.save(sysRoleInsertPO);
        //新建角色和菜单的关联信息
        sysRoleMenuService.saveOrUpdate(sysRoleInsertPO.getId(), sysRoleInsertDto.getMenuIdList());
    }
    @Override
    public Integer getCountByTypeAndNameOrById(String username, Long roleId) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", username);
        if (roleId != null) {
            map.put("id", roleId);
        }
        return sysRoleMapper.getCountByNameAndById(map);
    }
    @Override
    public void delete(SysRoleDeleteDto sysRoleDeleteDto) {
    	SysRoleDeletePO sysRoleDeletePO = Convert.sourceToTarget(sysRoleDeleteDto, SysRoleDeletePO.class);
        sysRoleMapper.delete(sysRoleDeletePO);
    }
}