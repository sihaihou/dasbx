package com.reyco.dasbx.common.core.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.common.core.constant.Constants;
import com.reyco.dasbx.common.core.dao.personage.PersonageDao;
import com.reyco.dasbx.common.core.dao.sys.AreaDao;
import com.reyco.dasbx.common.core.dao.sys.GirlNameDao;
import com.reyco.dasbx.common.core.dao.sys.MaleNameDao;
import com.reyco.dasbx.common.core.dao.sys.SurnameDao;
import com.reyco.dasbx.common.core.model.dto.personage.PersonageDeleteDto;
import com.reyco.dasbx.common.core.model.dto.personage.PersonageInsertDto;
import com.reyco.dasbx.common.core.model.dto.personage.PersonageSearchDto;
import com.reyco.dasbx.common.core.model.dto.personage.PersonageUpdateDto;
import com.reyco.dasbx.common.core.model.po.personage.PersonageDeletePO;
import com.reyco.dasbx.common.core.model.po.personage.PersonageInsertPO;
import com.reyco.dasbx.common.core.model.po.personage.PersonageUpdatePO;
import com.reyco.dasbx.common.core.model.po.sys.PersonageElasticsearchDocument;
import com.reyco.dasbx.common.core.model.vo.personage.PersonageInfoVO;
import com.reyco.dasbx.common.core.service.PersonageService;
import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.commons.utils.RandomUtils;
import com.reyco.dasbx.config.exception.core.ArgumentException;
import com.reyco.dasbx.config.exception.core.BusinessException;
import com.reyco.dasbx.config.utils.CodeUtils;
import com.reyco.dasbx.es.core.client.ElasticsearchClient;
import com.reyco.dasbx.es.core.model.GeoPoint;
import com.reyco.dasbx.es.core.search.SearchVO;
import com.reyco.dasbx.model.domain.Area;
import com.reyco.dasbx.model.domain.Fullname;
import com.reyco.dasbx.model.domain.Personage;
import com.reyco.dasbx.model.dto.ListDto;
import com.reyco.dasbx.model.vo.ListVO;

@Service
public class PersonageServiceImpl implements PersonageService {
	protected static final Logger log = LoggerFactory.getLogger(PersonageServiceImpl.class);
	@Autowired
	private ElasticsearchClient<PersonageElasticsearchDocument> elasticsearchClient;
	@Autowired
	private PersonageDao personageDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private MaleNameDao maleNameDao;
	@Autowired
	private GirlNameDao girlNameDao;
	@Autowired
	private SurnameDao surnameDao;
	@Autowired
	private PersonageSearch personageSearch;
	
	private Set<String> filter;

	public static ThreadPoolExecutor executor;
	public final static int coreProcessors = Runtime.getRuntime().availableProcessors();

	@PostConstruct
	public void init() {
		filter = new HashSet<String>();
		executor = new ThreadPoolExecutor(coreProcessors, coreProcessors * 3, 60, TimeUnit.SECONDS,
				new LinkedBlockingQueue<>(1000), new ThreadFactory() {
					LongAdder count = new LongAdder();
					@Override
					public Thread newThread(Runnable r) {
						count.increment();
						Thread thread = new Thread(r);
						thread.setDaemon(false);
						thread.setName("com.reyco.dasbx.randomPersonageThread-" + count.intValue());
						return thread;
					}
				});
	}

	@Override
	public void initElasticsearchPersonage() throws IOException {
		// List<Personage> personages = personageDao.getAll();
		Long maxId = personageDao.getMaxId();
		for (int i = 1; i < maxId; i += 10000) {
			log.info("###########i:" + i);
			List<Personage> personages = personageDao.getListByLimit(new Long(i), new Long(i + 10000));
			List<PersonageElasticsearchDocument> personageDocuments = new ArrayList<>(personages.size());
			PersonageElasticsearchDocument personageElasticsearchDocument = null;
			for (Personage personage : personages) {
				personageElasticsearchDocument = Convert.sourceToTarget(personage,
						PersonageElasticsearchDocument.class);
				personageElasticsearchDocument
						.setLocation(new GeoPoint(personage.getLatitude(), personage.getLongitude()));
				Set<String> suggestionSet = new HashSet<>();
				suggestionSet.add(personageElasticsearchDocument.getName());
				suggestionSet.add(personageElasticsearchDocument.getCode());
				suggestionSet.add(personageElasticsearchDocument.getGender().toString());
				suggestionSet.add(personageElasticsearchDocument.getMasterpiece());
				personageElasticsearchDocument.setSuggestion(new ArrayList<>(suggestionSet));
				personageDocuments.add(personageElasticsearchDocument);
			}
			elasticsearchClient.batchAddDocument("personage", personageDocuments);
		}
	}

	@Override
	public int randomPersonage() throws IOException {
		List<Personage> all = personageDao.getAll();
		for (Personage personage : all) {
			filter.add( personage.getName());
		}
		List<String> masterpieces = Arrays.asList("香港演员", "澳门演员", "台湾演员", "大陆演员", "群众演员", "人民群众");
		int sc = surnameDao.getCount();
		int gc = girlNameDao.getCount();
		int mc = maleNameDao.getCount();
		// 缓存
		Map<Long, String> csm = new ConcurrentHashMap<>();
		Map<Long, String> cmm = new ConcurrentHashMap<>();
		Map<Long, String> cgm = new ConcurrentHashMap<>();
		// 去重
		Set<String> ns = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

		int size = sc * gc * mc;
		int time = size / 10000;
		// 省市区
		Map<Area, List<Area>> cc = new ConcurrentHashMap<>();
		Map<Area, List<Area>> cd = new ConcurrentHashMap<>();
		List<Area> cp = areaDao.getAll();
		// List<Personage> personages = new ArrayList<>();
		for (int j = 0; j < 1000; j++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					List<Personage> personages = insertPersonage(masterpieces, 10000, csm, cmm, cgm, ns);
					builderArea(cp, cc, cd, personages);
					// 数据大小
					int size = personages.size();
					// 每次执行的数量
					int count = 1000;
					// 次数
					int frequency = size / count;
					// 余数
					int remainder = size % count;
					if (remainder > 0) {
						++frequency;
					}
					// 新增数量
					for (int i = 0; i < frequency; i++) {
						List<Personage> temp = null;
						if ((i + 1) == frequency) {
							temp = personages.subList(i * count, size);
						} else {
							temp = personages.subList(i * count, (i + 1) * count);
						}
						personageDao.batchInsert(temp);
					}
				}
			});
		}
		return 0;
	}

	private List<Personage> insertPersonage(List<String> ms, int countSize, Map<Long, String> csm,
			Map<Long, String> cmm, Map<Long, String> cgm, Set<String> ns) {
		int sc = surnameDao.getCount();
		int mc = maleNameDao.getCount();
		int gc = girlNameDao.getCount();
		Set<Personage> personageSet = new HashSet<>(countSize);
		Personage personage = null;
		Fullname fn = null;
		int maxReties = 5;
		for (int i = 0; i < countSize; i++) {
			if (i % 100 == 0) {
				log.info("##########当前到i：" + i);
			}
			int reties = 0;
			do {
				reties++;
				int ri = RandomUtils.randomInt(17);
				fn = randomMaleFullname(sc, mc, gc, csm, cmm, cgm, ri < 7 ? false : true);
			} while (exit(fn.getFullname(), ns) && reties < maxReties);
			if (reties > maxReties) {
				log.warn("人物" + fn + "重复多次!");
				continue;
			}
			filter.add(fn.getFullname());
			personage = new Personage();
			personage.setName(fn.getFullname());
			personage.setGender(fn.getGender() ? 1 : 0);
			personage.setMasterpiece(ms.get(RandomUtils.randomInt(ms.size())));
			personage.setCreateBy(1L);
			personage.setGmtCreate(Dasbx.getCurrentTime());
			personage.setModifiedBy(1L);
			personage.setGmtModified(Dasbx.getCurrentTime());
			personageSet.add(personage);
		}
		return new ArrayList<>(personageSet);
	}

	private boolean exit(String name, Set<String> ns) {
		List<Personage> tps;
		return filter.add(name)
				&& (ns.contains(name) || ((tps = personageDao.getList(name)) != null && tps.size() > 0
						&& ns.add(tps.get(0).getName()) && tps.size() < 1));
	}

	private Fullname randomMaleFullname(int surnameCount, int maleCount, int girlCount, Map<Long, String> surnameMap,
			Map<Long, String> maleNameMap, Map<Long, String> girlNameMap, boolean gender) {
		int surnameId = RandomUtils.randomInt(surnameCount) + 1;
		Long si;
		String sn;
		if (StringUtils.isBlank(sn = surnameMap.get(new Long(si = (long) surnameId)))) {
			surnameMap.put(si, sn = surnameDao.getById(si).getName());
		}
		int rLen = RandomUtils.randomInt(10);
		int len = 2;
		if (rLen == 0) {
			len = 1;
		}
		StringJoiner sj = new StringJoiner("");
		for (int j = 0; j < len; j++) {
			int ri;
			Long ni;
			String nn;
			if (gender) {
				ri = RandomUtils.randomInt(maleCount) + 1;
				if (StringUtils.isBlank((nn = maleNameMap.get(new Long(ni = (long) ri))))) {
					maleNameMap.put(ni, nn = maleNameDao.getById(ni).getName());
				}
				sj.add(nn);
			} else {
				ri = RandomUtils.randomInt(girlCount) + 1;
				if (StringUtils.isBlank((nn = girlNameMap.get(new Long(ni = (long) ri))))) {
					girlNameMap.put(ni, nn = girlNameDao.getById(ni).getName());
				}
				sj.add(nn);
			}
		}
		Fullname fullname = new Fullname();
		fullname.setFullname(sn + sj.toString());
		fullname.setGender(gender);
		return fullname;
	}

	@Override
	public int initPersonage() throws IOException {
		List<Personage> personages = collectPersonage();
		builderArea(personages);
		List<PersonageInsertPO> personageInsertPOs = Convert.sourceListToTargetList(personages,
				PersonageInsertPO.class);
		for (PersonageInsertPO personageInsertPO : personageInsertPOs) {
			personageInsertPO.setCode(CodeUtils.getPersonageCode());
			personageDao.insert(personageInsertPO);
		}
		return personageInsertPOs.size();
	}

	private void builderArea(List<Area> provinceAreas, Map<Area, List<Area>> cityCache,
			Map<Area, List<Area>> districtCache, List<Personage> personages) {
		for (Personage personage : personages) {
			personage.setCode(CodeUtils.getPersonageCode());
			Area provinceArea = provinceAreas.get(RandomUtils.randomInt(provinceAreas.size()));
			personage.setProvinceId(provinceArea.getId());
			if (!cityCache.containsKey(provinceArea)) {
				List<Area> cityAreas = areaDao.getChildsByParentId(provinceArea.getId());
				cityCache.put(provinceArea, cityAreas);
			}
			List<Area> cityAreas = cityCache.get(provinceArea);
			if (cityAreas == null || cityAreas.size() == 0) {
				personage.setCityId(provinceArea.getId());
				personage.setDistrictId(provinceArea.getId());
				personage.setLongitude(provinceArea.getLongitude());
				personage.setLatitude(provinceArea.getLatitude());
				continue;
			}
			Area cityArea = cityAreas.get(RandomUtils.randomInt(cityAreas.size()));
			personage.setCityId(cityArea.getId());
			if (!districtCache.containsKey(cityArea)) {
				List<Area> districtAreas = areaDao.getChildsByParentId(cityArea.getId());
				districtCache.put(cityArea, districtAreas);
			}
			List<Area> districtAreas = districtCache.get(cityArea);
			if (districtAreas == null || districtAreas.size() == 0) {
				personage.setDistrictId(cityArea.getId());
				personage.setLongitude(cityArea.getLongitude());
				personage.setLatitude(cityArea.getLatitude());
				continue;
			}
			Area districtArea = districtAreas.get(RandomUtils.randomInt(districtAreas.size()));
			personage.setDistrictId(districtArea.getId());
			personage.setLongitude(districtArea.getLongitude());
			personage.setLatitude(districtArea.getLatitude());
		}
	}

	private void builderArea(List<Personage> personages) {
		Map<Area, List<Area>> cityCache = new HashMap<>();
		Map<Area, List<Area>> districtCache = new HashMap<>();
		List<Area> provinceAreas = areaDao.getAll();
		for (Personage personage : personages) {
			Area provinceArea = provinceAreas.get(RandomUtils.randomInt(provinceAreas.size()));
			personage.setProvinceId(provinceArea.getId());
			if (!cityCache.containsKey(provinceArea)) {
				List<Area> cityAreas = areaDao.getChildsByParentId(provinceArea.getId());
				cityCache.put(provinceArea, cityAreas);
			}
			List<Area> cityAreas = cityCache.get(provinceArea);
			if (cityAreas == null || cityAreas.size() == 0) {
				personage.setCityId(provinceArea.getId());
				personage.setDistrictId(provinceArea.getId());
				personage.setLongitude(provinceArea.getLongitude());
				personage.setLatitude(provinceArea.getLatitude());
				continue;
			}
			Area cityArea = cityAreas.get(RandomUtils.randomInt(cityAreas.size()));
			personage.setCityId(cityArea.getId());
			if (!districtCache.containsKey(cityArea)) {
				List<Area> districtAreas = areaDao.getChildsByParentId(cityArea.getId());
				districtCache.put(cityArea, districtAreas);
			}
			List<Area> districtAreas = districtCache.get(cityArea);
			if (districtAreas == null || districtAreas.size() == 0) {
				personage.setDistrictId(cityArea.getId());
				personage.setLongitude(cityArea.getLongitude());
				personage.setLatitude(cityArea.getLatitude());
				continue;
			}
			Area districtArea = districtAreas.get(RandomUtils.randomInt(districtAreas.size()));
			personage.setDistrictId(districtArea.getId());
			personage.setLongitude(districtArea.getLongitude());
			personage.setLatitude(districtArea.getLatitude());
		}
	}

	private List<Personage> collectPersonage() {
		LinkedList<String> linkedList = Constants.PERSONAGE;
		Set<Personage> personageSet = new HashSet<>();
		Personage personage = null;
		for (String personageStr : linkedList) {
			String[] personageArray = personageStr.split("\\|");
			personage = new Personage();
			personageSet.add(personage);
			personage.setMasterpiece(personageArray[0]);
			personage.setGender(personageArray[1].equals("男") ? 1 : 0);
			personage.setName(personageArray[2]);
		}
		return new ArrayList<Personage>(personageSet);
	}

	@Override
	public List<String> getSuggestion(String keyword) throws IOException {
		List<String> suggestion = elasticsearchClient.getSuggestion("personage", keyword);
		if (suggestion == null) {
			suggestion = new ArrayList<>();
		}
		return suggestion;
	}
	@Override
	public SearchVO<PersonageInfoVO> search(PersonageSearchDto personageSearchDto) throws Exception {
		return personageSearch.search(personageSearchDto);
	}

	@Override
	public PersonageInfoVO get(Long id) {
		Personage personage = personageDao.getById(id);
		PersonageInfoVO personageInfoVO = Convert.sourceToTarget(personage, PersonageInfoVO.class);
		return personageInfoVO;
	}

	@Override
	public List<ListVO> list(ListDto listDto) {
		return null;
	}

	@Override
	public void update(PersonageUpdateDto personageUpdateDto) {
		PersonageUpdatePO personageUpdatePO = Convert.sourceToTarget(personageUpdateDto, PersonageUpdatePO.class);
		personageDao.update(personageUpdatePO);
	}

	@Override
	public PersonageInfoVO insert(PersonageInsertDto personageInsertDto) throws Exception {
		PersonageInsertPO personageInsertPO = Convert.sourceToTarget(personageInsertDto, PersonageInsertPO.class);
		if (StringUtils.isBlank(personageInsertPO.getName())) {
			throw new ArgumentException("用户名不能为空");
		}
		/*
		 * List<Personage> tps; if(redisBloomFilter.mightContain(bloomFilterHelper,
		 * "personage", personageInsertPO.getName()) &&
		 * (tps=personageDao.getList(personageInsertPO.getName()))!=null && tps.size()>1
		 * ) { throw new BusinessException("用户已存在"); }
		 */
		List<Personage> tps;
		if ((tps = personageDao.getList(personageInsertPO.getName())) != null && tps.size() > 1) {
			throw new BusinessException("用户已存在");
		}
		Area district = areaDao.getById(personageInsertPO.getDistrictId());
		personageInsertPO.setLongitude(district.getLongitude());
		personageInsertPO.setLatitude(district.getLatitude());
		personageInsertPO.setCode(CodeUtils.getPersonageCode());
		personageDao.insert(personageInsertPO);
		// redisBloomFilter.put(bloomFilterHelper, "personage",
		// personageInsertPO.getName());
		PersonageElasticsearchDocument personageElasticsearchDocument = null;
		personageElasticsearchDocument = Convert.sourceToTarget(personageInsertPO,
				PersonageElasticsearchDocument.class);
		personageElasticsearchDocument.setGender(personageInsertPO.getGender());
		personageElasticsearchDocument.setProvinceId(personageInsertPO.getProvinceId());
		personageElasticsearchDocument.setCityId(personageInsertPO.getCityId());
		personageElasticsearchDocument.setDistrictId(personageInsertPO.getDistrictId());
		personageElasticsearchDocument.setLocation(new GeoPoint(personageInsertPO.getLatitude(), personageInsertPO.getLongitude()));
		personageElasticsearchDocument.setRemark(personageElasticsearchDocument.getName());
		Set<String> suggestionSet = new HashSet<>();
		suggestionSet.add(personageElasticsearchDocument.getName());
		suggestionSet.add(personageElasticsearchDocument.getCode());
		suggestionSet.add(personageElasticsearchDocument.getGender().toString());
		suggestionSet.add(personageElasticsearchDocument.getMasterpiece());
		personageElasticsearchDocument.setSuggestion(new ArrayList<>(suggestionSet));
		elasticsearchClient.addDocument("personage", personageElasticsearchDocument);
		return Convert.sourceToTarget(personageInsertPO, PersonageInfoVO.class);
	}

	@Override
	public void delete(PersonageDeleteDto personageDeleteDto) {
		PersonageDeletePO personageDeletePO = Convert.sourceToTarget(personageDeleteDto, PersonageDeletePO.class);
		personageDao.deleteById(personageDeletePO);
	}
}
