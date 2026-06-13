package com.reyco.dasbx.es.test.template;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.reyco.dasbx.es.support.annotation.page.EsPageParam;
import com.reyco.dasbx.es.support.annotation.sort.EsSortParam;
import com.reyco.dasbx.es.support.annotation.sort.EsSortType;
import com.reyco.dasbx.es.support.result.Result;
import com.reyco.dasbx.es.support.template.SearchTemplate;
import com.reyco.dasbx.es.test.TestApplication;

@ActiveProfiles("test")
@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringRunner.class)
public class SearchTemplateTest {
	
	@Autowired
	private SearchTemplate searchTemplate;
	
	@Test
	public void testSearch() throws Exception {
		
		PersonageDto personageDto = new PersonageDto();
		personageDto.setKeyword("侯四海");
		personageDto.setLocation(new EsSortParam().setType(EsSortType.GEO_DISTANCE).setUnit("km").setLatitude(32.02).setLongitude(112.21));
		personageDto.setPageParam(new EsPageParam());
		Result<PersonageVo> search = searchTemplate.search(personageDto, PersonageVo.class);
		
		System.out.println(search.getAggregations());
		
		System.out.println(search.getPage());
		
		System.out.println(search.getPage().getList());
	}
	
}
