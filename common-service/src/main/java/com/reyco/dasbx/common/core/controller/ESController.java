package com.reyco.dasbx.common.core.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.common.core.service.EsService;
import com.reyco.dasbx.commons.domain.R;

@RestController
@RequestMapping("es")
public class ESController {
	
	@Autowired
	private EsService esService;
	
	public final static String INDEX_DSL = "{\n" + 
			"  \"settings\": {\n" + 
			"    \"number_of_shards\": 1,\n" + 
			"    \"number_of_replicas\": 0,\n" + 
			"    \"blocks.read_only_allow_delete\": \"false\",\n" + 
			"    \"analysis\": {\n" + 
			"      \"analyzer\": {\n" + 
			"        \"text_analyzer\": {\n" + 
			"          \"tokenizer\": \"ik_max_word\",\n" + 
			"          \"filter\": \"py\"\n" + 
			"        },\n" + 
			"        \"completion_analyzer\": {\n" + 
			"          \"tokenizer\": \"keyword\",\n" + 
			"          \"filter\": \"py\"\n" + 
			"        }\n" + 
			"      },\n" + 
			"      \"filter\": {\n" + 
			"        \"py\": {\n" + 
			"          \"type\": \"pinyin\",\n" + 
			"          \"keep_full_pinyin\": false,\n" + 
			"          \"keep_joined_full_pinyin\": true,\n" + 
			"          \"keep_original\": true,\n" + 
			"          \"limit_first_letter_length\": 16,\n" + 
			"          \"remove_duplicated_term\": true,\n" + 
			"          \"none_chinese_pinyin_tokenizer\": false\n" + 
			"        }\n" + 
			"      }\n" + 
			"    }\n" + 
			"  },\n" + 
			"  \"mappings\": {\n" + 
			"    \"properties\": {\n" + 
			"      \"id\": {\n" + 
			"        \"type\": \"long\"\n" + 
			"      },\n" + 
			"      \"all\": {\n" + 
			"        \"type\": \"text\",\n" + 
			"        \"analyzer\": \"text_analyzer\",\n" + 
			"        \"search_analyzer\": \"ik_max_word\"\n" + 
			"      },\n" + 
			"      \"name\": {\n" + 
			"        \"type\": \"text\",\n" + 
			"        \"analyzer\": \"text_analyzer\",\n" + 
			"        \"search_analyzer\": \"ik_max_word\",\n" + 
			"        \"copy_to\":\"all\"\n" + 
			"      },\n" + 
			"      \"code\": {\n" + 
			"        \"type\": \"text\",\n" + 
			"        \"analyzer\": \"text_analyzer\",\n" + 
			"        \"search_analyzer\": \"ik_max_word\",\n" + 
			"        \"copy_to\":\"all\"\n" + 
			"      },\n" + 
			"      \"gender\": {\n" + 
			"        \"type\": \"keyword\"\n" + 
			"      },\n" + 
			"      \"masterpiece\": {\n" + 
			"        \"type\": \"keyword\",\n" + 
			"        \"copy_to\":\"all\"\n" + 
			"      },\n" + 
			"      \"province\": {\n" + 
			"        \"type\": \"keyword\",\n" + 
			"        \"copy_to\":\"all\"\n" + 
			"      },\n" + 
			"      \"city\": {\n" + 
			"        \"type\": \"keyword\",\n" + 
			"        \"copy_to\":\"all\"\n" + 
			"      },\n" + 
			"      \"district\": {\n" + 
			"        \"type\": \"keyword\",\n" + 
			"        \"copy_to\":\"all\"\n" + 
			"      },\n" + 
			"      \"location\": {\n" + 
			"        \"type\": \"geo_point\"\n" + 
			"      },\n" + 
			"      \"remark\": {\n" + 
			"        \"type\": \"text\",\n" + 
			"        \"analyzer\": \"text_analyzer\",\n" + 
			"        \"search_analyzer\": \"ik_max_word\",\n" + 
			"        \"copy_to\":\"all\"\n" + 
			"      },\n" + 
			"      \"gmtCreate\": {\n" + 
			"        \"type\": \"date\",\n" + 
			"        \"format\": \"yyyy-MM-dd HH:mm:ss\"\n" + 
			"      },\n" + 
			"      \"createBy\": {\n" + 
			"        \"type\": \"long\"\n" + 
			"      },\n" + 
			"      \"gmtModified\": {\n" + 
			"        \"type\": \"date\",\n" + 
			"        \"format\": \"yyyy-MM-dd HH:mm:ss\"\n" + 
			"      },\n" + 
			"      \"modifiedBy\": {\n" + 
			"        \"type\": \"long\"\n" + 
			"      },\n" + 
			"      \"suggestion\": {\n" + 
			"        \"type\": \"completion\",\n" + 
			"        \"analyzer\": \"completion_analyzer\"\n" + 
			"      }\n" + 
			"    }\n" + 
			"  }\n" + 
			"}";

	public static final String MAPPING = "{\n" + 
			"  \"properties\": {\n" + 
			"    \"all\": {\n" + 
			"      \"type\": \"text\"\n" + 
			"    }\n" + 
			"  }\n" + 
			"}";
	
	@PostMapping
	public Object createIndex(String indexName,String indexDSL) throws Exception {
		if(StringUtils.isBlank(indexDSL)) {
			indexDSL = INDEX_DSL;
		}
		boolean createIndex = esService.createIndex(indexName, indexDSL);
		if(!createIndex) {
			return R.fail("索引创建失败");
		}
		return R.success("索引创建成功");
	}
	@PatchMapping
	public Object addFieldIndex(String indexName,String mapping) throws Exception {
		if(StringUtils.isBlank(mapping)) {
			mapping = MAPPING;
		}
		boolean addFieldIndex = esService.addFieldIndex(indexName, mapping);
		if(!addFieldIndex) {
			return R.fail("添加属性失败");
		}
		return R.success("添加属性成功");
	}
	
	@GetMapping
	public Object existsIndex(String indexName) throws Exception{
		boolean existsIndex = esService.existsIndex(indexName);
		if(!existsIndex) {
			return R.fail("索引不存在");
		}
		return R.success("索引存在");
	}
	
	@DeleteMapping
	public Object deleteIndex(String indexName) throws Exception {
		boolean deleteIndex = esService.deleteIndex(indexName);
		if(!deleteIndex) {
			return R.fail("删除索引失败");
		}
		return R.success("删除索引成功");
	}
	
}
