package com.reyco.dasbx.common.core.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.reyco.dasbx.common.core.dao.sys.AreaDao;
import com.reyco.dasbx.common.core.model.dto.sys.AreaDeleteDto;
import com.reyco.dasbx.common.core.model.dto.sys.AreaInsertDto;
import com.reyco.dasbx.common.core.model.dto.sys.AreaListDto;
import com.reyco.dasbx.common.core.model.dto.sys.AreaUpdateDto;
import com.reyco.dasbx.common.core.model.po.sys.AreaDeletePO;
import com.reyco.dasbx.common.core.model.po.sys.AreaInsertPO;
import com.reyco.dasbx.common.core.model.po.sys.AreaUpdatePO;
import com.reyco.dasbx.common.core.model.po.sys.SysAreaElasticsearchDocument;
import com.reyco.dasbx.common.core.model.vo.sys.AreaInfoVO;
import com.reyco.dasbx.common.core.model.vo.sys.AreaListVO;
import com.reyco.dasbx.common.core.service.AreaService;
import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.es.core.client.ElasticsearchClient;
import com.reyco.dasbx.es.core.model.GeoPoint;
import com.reyco.dasbx.model.domain.Area;

@Service
public class AreaServiceImpl implements AreaService {
	
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ElasticsearchClient<SysAreaElasticsearchDocument> elasticsearchDocumentUtils;
	@Override
	public void init() {
		AreaInsertPO areaInsertPO = new AreaInsertPO();
		areaInsertPO.setName("中华人们共和国");
		String[] provinces = {"北京市","天津市","河北省","山西省","内蒙古自治区","辽宁省","吉林省","黑龙江省","上海市","江苏省",
				"浙江省","安徽省","福建省","江西省","山东省","河南省","湖北省","湖南省","广东省","广西壮族自治区",
				"海南省","重庆市","四川省","贵州省","云南省","西藏自治区","陕西省","甘肃省","青海省","宁夏回族自治区",
				"新疆维吾尔自治区","台湾省","香港特别行政区","澳门特别行政区"};
		for (int i = 0; i < provinces.length; i++) {
			String key = "bc8d21c0dfb5509cc976ab0fde3f8837";
			String k = provinces[i];
			int subdistrict = 4;
			JSONObject JSONObject = restTemplate.getForObject("https://restapi.amap.com/v3/config/district?key="+key+"&keywords="+k+"&subdistrict="+subdistrict, JSONObject.class);
			parse(areaInsertPO, JSONObject.getJSONArray("districts"));
		}
		insert(areaInsertPO,areaInsertPO.getAreaInsertPOs());
	}
	private void insert(AreaInsertPO parentAreaInsertPO,Set<AreaInsertPO> areaInsertPOs) {
		if(areaInsertPOs!=null && areaInsertPOs.size()>0) {
			for (AreaInsertPO areaInsertPO : areaInsertPOs) {
				if(parentAreaInsertPO.getId()!=null) {
					areaInsertPO.setParentId(parentAreaInsertPO.getId());
				}else {
					areaInsertPO.setParentId(0L);
				}
				areaDao.insert(areaInsertPO);
				insert(areaInsertPO, areaInsertPO.getAreaInsertPOs());
			}
		}
	}
	/**
	 * 递归解析json
	 * @param districtArray
	 * @return
	 */
	private void parse(AreaInsertPO areaInsertPO,JSONArray districtArray) {
		for (Object object : districtArray) {
			JSONObject jsonObject = (JSONObject)object;
			String name = jsonObject.getString("name");
			String code = jsonObject.getString("adcode");
			String citycode = jsonObject.getString("citycode");
			String longitudeLatitudeCenter = jsonObject.getString("center");
			String level = jsonObject.getString("level");
			String[] longitudeLatitudeArray = longitudeLatitudeCenter.split(",");
			String longitude = longitudeLatitudeArray[0];
			String latitude = longitudeLatitudeArray[1];
			AreaInsertPO areaInsertPOTemp = new AreaInsertPO();
			areaInsertPOTemp.setName(name);
			areaInsertPOTemp.setCode(code);
			if(StringUtils.isNotBlank(citycode)) {
				areaInsertPOTemp.setCitycode(citycode);
			}
			areaInsertPOTemp.setLongitude(longitude);
			areaInsertPOTemp.setLatitude(latitude);
			areaInsertPOTemp.setLevel(level);
			if(areaInsertPO.getAreaInsertPOs()==null) {
				areaInsertPO.setAreaInsertPOs(new HashSet<>());
			}
			areaInsertPO.getAreaInsertPOs().add(areaInsertPOTemp);
			JSONArray districtArrayTemp = jsonObject.getJSONArray("districts");
			parse(areaInsertPOTemp, districtArrayTemp);
		}
	}
	
	@Override
	public AreaInfoVO get(Long id) {
		Area area = areaDao.getById(id);
		AreaInfoVO areaInfoVO = Convert.sourceToTarget(area, AreaInfoVO.class);
		return areaInfoVO;
	}
	@Override
	public AreaInfoVO getByName(String name) {
		Area area = areaDao.getByName(name);
		AreaInfoVO areaInfoVO = Convert.sourceToTarget(area, AreaInfoVO.class);
		return areaInfoVO;
	}
	@Override
	public List<AreaInfoVO> getAll() {
		List<Area> areas = areaDao.getAll();
		List<AreaInfoVO> areaInfoVOs = Convert.sourceListToTargetList(areas, AreaInfoVO.class);
		return areaInfoVOs;
	}
	@Override
	public List<AreaInfoVO> getChildsByParentId(Long id) {
		List<Area> areas = areaDao.getChildsByParentId(id);
		List<AreaInfoVO> areaInfoVOs = Convert.sourceListToTargetList(areas, AreaInfoVO.class);
		return areaInfoVOs;
	}
	@Override
	public List<AreaListVO> list(AreaListDto areaListDto) {
		return null;
	}
	
	@Override
	public Integer initElasticsearchSysArea() throws IOException {
		Long maxId = areaDao.getMaxId();
		int count = 0;
		for (int i=1;i<maxId;i+=10000) {
			List<Area> areas = areaDao.getListByLimit(new Long(i), new Long(i+10000));
			List<SysAreaElasticsearchDocument> sysAreaElasticsearchDocuments = new ArrayList<>();
			SysAreaElasticsearchDocument sysAreaElasticsearchDocument = null;
			for (Area area : areas) {
				sysAreaElasticsearchDocument = Convert.sourceToTarget(area, SysAreaElasticsearchDocument.class);
				sysAreaElasticsearchDocument.setLocation(new GeoPoint(area.getLatitude(),area.getLongitude()));
				Set<String> suggestionSet = new HashSet<>();
				suggestionSet.add(sysAreaElasticsearchDocument.getName());
				suggestionSet.add(sysAreaElasticsearchDocument.getCode());
				suggestionSet.add(sysAreaElasticsearchDocument.getCitycode());
				sysAreaElasticsearchDocuments.add(sysAreaElasticsearchDocument);
			}
			int batchAddDocument = elasticsearchDocumentUtils.batchAddDocument("sys_area", sysAreaElasticsearchDocuments);
			count+=batchAddDocument;
		}
		return count;
	}
	
	@Override
	public void update(AreaUpdateDto areaUpdateDto) {
		AreaUpdatePO areaUpdatePO = Convert.sourceToTarget(areaUpdateDto, AreaUpdatePO.class);
		areaDao.update(areaUpdatePO);
	}

	@Override
	public AreaInfoVO insert(AreaInsertDto areaInsertDto) {
		if(areaInsertDto.getParentId()!=0) {
			Area area = areaDao.getById(areaInsertDto.getParentId());
			if(area.getLeaf()==1) {
				AreaUpdatePO areaUpdatePO = new AreaUpdatePO();
				areaUpdatePO.setId(area.getId());
				areaUpdatePO.setLeaf((byte)0);
				areaDao.update(areaUpdatePO);
			}
		}
		AreaInsertPO areaInsertPO = Convert.sourceToTarget(areaInsertDto, AreaInsertPO.class);
		areaInsertPO.setLeaf((byte)1);
		areaDao.insert(areaInsertPO);
		return Convert.sourceToTarget(areaInsertPO, AreaInfoVO.class);
	}

	@Override
	public void delete(AreaDeleteDto areaDeleteDto) {
		Area deleteArea = areaDao.getById(areaDeleteDto.getId());
		if(deleteArea.getLeaf()==0) {
			List<Area> childs = areaDao.getChildsByParentId(deleteArea.getParentId());
			if(childs==null || childs.size()==0) {
				AreaUpdatePO areaUpdatePO = new AreaUpdatePO();
				areaUpdatePO.setId(deleteArea.getParentId());
				areaUpdatePO.setLeaf((byte)1);
				areaDao.update(areaUpdatePO);
			}
		}
		AreaDeletePO areaDeletePO = Convert.sourceToTarget(areaDeleteDto, AreaDeletePO.class);
		areaDeletePO.setDeleted((byte)1);
		areaDao.deleteById(areaDeletePO);
	}
}
