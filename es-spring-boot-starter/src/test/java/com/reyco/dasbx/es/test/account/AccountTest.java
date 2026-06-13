package com.reyco.dasbx.es.test.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.reyco.dasbx.es.core.query.builder.factory.SearchBuilderFactory;
import com.reyco.dasbx.es.core.result.SearchResult;
import com.reyco.dasbx.es.core.result.page.Pages;
import com.reyco.dasbx.es.support.execution.annotation.EsExecutionContext;
import com.reyco.dasbx.es.support.execution.annotation.EsExecutionFactory;
import com.reyco.dasbx.es.test.TestApplication;
import com.reyco.dasbx.es.test.domain.SysAccountInfoVO;

@ActiveProfiles("test")
@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringRunner.class)
public class AccountTest {

	@Autowired
	private SearchBuilderFactory factory;

	@Test
	public void testSearch() throws Exception {
		SysAccountInfoDto sysAccountInfoDto = new SysAccountInfoDto();
		sysAccountInfoDto.setKeyword("18307200213");
		EsExecutionContext esExecutionContext = EsExecutionFactory.create(sysAccountInfoDto);
		SearchResult<SysAccountInfoVO> search = factory.builder(SysAccountInfoVO.class)
				 .index(esExecutionContext.getIndex())
				 .page(Pages.offset(1,10))
				 .query(esExecutionContext.getBoolCondition())
				 .aggregation(esExecutionContext.getAggregations())
				 .highlight(esExecutionContext.getHighlights())
				 .search();
		
		System.out.println(search.getAggregations());
		
		System.out.println(search.getPage());
		
		System.out.println(search.getRecords());
	}

}
