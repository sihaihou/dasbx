package com.reyco.dasbx.model.es;

public class IndexElasticSearchDoc {
	/**
	 * 
	 	PUT /sys_area
		{
		 "settings" : {
		    "index" : {
		      "number_of_shards" : "1",
		      "number_of_replicas" : "0",
		      "blocks.read_only_allow_delete": false,
		      "analysis" : {
		        "analyzer" : {
		          "text_analyzer" : {
		            "filter" : "py",
		            "tokenizer" : "ik_max_word"
		          },
		          "completion_analyzer" : {
		            "filter" : "py",
		            "tokenizer" : "keyword"
		          }
		        },
		        "filter" : {
		          "py" : {
		            "none_chinese_pinyin_tokenizer" : "false",
		            "keep_joined_full_pinyin" : "true",
		            "keep_original" : "true",
		            "remove_duplicated_term" : "true",
		            "type" : "pinyin",
		            "limit_first_letter_length" : "16",
		            "keep_full_pinyin" : "false"
		          }
		        }
		      }
		    }
		  },
		  "mappings" : {
		    "properties" : {
		      "id" : {
		        "type" : "long"
		      },
		      "all" : {
		        "type" : "text",
		        "analyzer" : "text_analyzer",
		        "search_analyzer" : "ik_smart"
		      },
		      "parentId" : {
		        "type" : "long"
		      },
		      "name" : {
		        "type" : "text",
		        "analyzer" : "text_analyzer",
		        "search_analyzer" : "ik_smart",
		        "copy_to" : "all"
		      },
		      "code" : {
		        "type" : "keyword",
		        "copy_to" : "all"
		      },
		      "citycode" : {
		        "type" : "keyword",
		        "copy_to" : "all"
		      },
		      "level" : {
		        "type" : "keyword"
		      },
		      "location": {
				"type": "geo_point"
			  },
		      "remark" : {
		        "type": "text",
		        "analyzer" : "text_analyzer",
		        "search_analyzer" : "ik_smart",
		        "copy_to" : "all"
		      },
		      "createBy" : {
		        "type": "long",
		        "index": false
		      },
		      "gmtCreate" : {
		        "type": "long",
		        "index": false
		      },
		      "modifiedBy" : {
		        "type": "long",
		        "index": false
		      },
		      "gmtModified" : {
		        "type": "long",
		        "index": false
		      },
		      "suggestion" : {
		        "type" : "completion",
		        "analyzer" : "completion_analyzer",
		        "search_analyzer" : "ik_smart"
		      }
		    }
		  }
		}
	 */
	private String sys_area = "";
	
	
	/**
	  * 用户
	   PUT /sys_account
		{
		 "settings" : {
		    "index" : {
		      "number_of_shards" : "1",
		      "number_of_replicas" : "0",
		      "blocks.read_only_allow_delete": false,
		      "analysis" : {
		        "analyzer" : {
		          "text_analyzer" : {
		            "filter" : "py",
		            "tokenizer" : "ik_max_word"
		          },
		          "completion_analyzer" : {
		            "filter" : "py",
		            "tokenizer" : "keyword"
		          }
		        },
		        "filter" : {
		          "py" : {
		            "none_chinese_pinyin_tokenizer" : "false",
		            "keep_joined_full_pinyin" : "true",
		            "keep_original" : "true",
		            "remove_duplicated_term" : "true",
		            "type" : "pinyin",
		            "limit_first_letter_length" : "16",
		            "keep_full_pinyin" : "false"
		          }
		        }
		      }
		    }
		  },
		  "mappings" : {
		    "properties" : {
		      "id" : {
		        "type" : "long"
		      },
		      "all" : {
		        "type" : "text",
		        "analyzer" : "text_analyzer",
		        "search_analyzer" : "ik_smart"
		      },
		      "uid" : {
		        "type" : "keyword"
		      },
		      "faceUri" : {
		        "type" : "keyword",
		        "index": false
		      },
		      "developerId" : {
		        "type" : "long"
		      },
		      "nickname" : {
		        "type" : "text",
		        "analyzer" : "text_analyzer",
		        "search_analyzer" : "ik_smart",
		        "copy_to" : "all"
		      },
		      "username" : {
		        "type" : "text",
		        "analyzer" : "standard",
		        "copy_to" : "all"
		      },
		      "password" : {
		        "type" : "keyword",
		        "index": false
		      },
		      "salt" : {
		        "type" : "keyword",
		        "index": false
		      },
		      "type" : {
		        "type" : "byte"
		      },
		      "integral" : {
		        "type" : "integer",
		        "index": false
		      },
		      "gender" : {
		        "type" : "byte"
		      },
		      "birthday" : {
		        "type" : "long",
		        "index": false
		      },
		      "phone" : {
		        "type" : "text",
		        "analyzer" : "standard",
		        "copy_to" : "all"
		      },
		      "email" : {
		        "type" : "text",
		        "analyzer" : "standard",
		        "copy_to" : "all"
		      },
		      "address" : {
		        "type" : "text",
		        "analyzer" : "text_analyzer",
		        "search_analyzer" : "ik_smart",
		        "copy_to" : "all"
		      },
		      "state" : {
		        "type" : "byte"
		      },
		      "remark" : {
		        "type" : "text",
		        "analyzer" : "text_analyzer",
		        "search_analyzer" : "ik_smart",
		        "copy_to" : "all"
		      },
		      "createBy" : {
		        "type" : "long",
		        "index": false
		      },
		      "gmtCreate" : {
		        "type" : "long",
		        "index": false
		      },
		      "gmtModified" : {
		        "type" : "long",
		        "index": false
		      },
		      "modifiedBy" : {
		        "type" : "long",
		        "index": false
		      },
		      "suggestion" : {
		        "type" : "completion",
		        "analyzer" : "completion_analyzer",
		        "search_analyzer" : "ik_smart"
		      }
		    }
		  }
		}
	 */
	private String index_sys_account = "";
	
	
	
	
	
	
	
	
	
	/**
	 *     角色
	  PUT /sys_role
		{
		 "settings" : {
		    "index" : {
		      "number_of_shards" : "1",
		      "number_of_replicas" : "0",
		      "blocks.read_only_allow_delete": false,
		      "analysis" : {
		        "analyzer" : {
		          "text_analyzer" : {
		            "filter" : "py",
		            "tokenizer" : "ik_max_word"
		          },
		          "completion_analyzer" : {
		            "filter" : "py",
		            "tokenizer" : "keyword"
		          }
		        },
		        "filter" : {
		          "py" : {
		            "none_chinese_pinyin_tokenizer" : "false",
		            "keep_joined_full_pinyin" : "true",
		            "keep_original" : "true",
		            "remove_duplicated_term" : "true",
		            "type" : "pinyin",
		            "limit_first_letter_length" : "16",
		            "keep_full_pinyin" : "false"
		          }
		        }
		      }
		    }
		  },
		  "mappings" : {
		    "properties" : {
		      "id" : {
		        "type" : "long"
		      },
		      "all" : {
		        "type" : "text",
		        "analyzer" : "text_analyzer",
		        "search_analyzer" : "ik_smart"
		      },
		      "name" : {
		        "type" : "text",
		        "analyzer" : "text_analyzer",
		        "search_analyzer" : "ik_smart",
		        "copy_to" : "all"
		      },
		      "remark" : {
		        "type": "text",
		        "analyzer" : "text_analyzer",
		        "search_analyzer" : "ik_smart",
		        "copy_to" : "all"
		      },
		      "createBy" : {
		        "type": "long",
		        "index": false
		      },
		      "gmtCreate" : {
		        "type": "long",
		        "index": false
		      },
		      "modifiedBy" : {
		        "type": "long",
		        "index": false
		      },
		      "gmtModified" : {
		        "type": "long",
		        "index": false
		      },
		      "suggestion" : {
		        "type" : "completion",
		        "analyzer" : "completion_analyzer",
		        "search_analyzer" : "ik_smart"
		      }
		    }
		  }
		}
	 */
	private String index_sys_role = "";
	
	
	
	
	
	
	
	
	/**
	 * 人物
	 	PUT /personage
		{
		 "settings" : {
		    "index" : {
		      "number_of_shards" : "1",
		      "number_of_replicas" : "0",
		      "blocks.read_only_allow_delete": false,
		      "analysis" : {
		        "analyzer" : {
		          "text_analyzer" : {
		            "filter" : "py",
		            "tokenizer" : "ik_max_word"
		          },
		          "completion_analyzer" : {
		            "filter" : "py",
		            "tokenizer" : "keyword"
		          }
		        },
		        "filter" : {
		          "py" : {
		            "none_chinese_pinyin_tokenizer" : "false",
		            "keep_joined_full_pinyin" : "true",
		            "keep_original" : "true",
		            "remove_duplicated_term" : "true",
		            "type" : "pinyin",
		            "limit_first_letter_length" : "16",
		            "keep_full_pinyin" : "false"
		          }
		        }
		      }
		    }
		  },
		  "mappings" : {
		    "properties" : {
		      "id" : {
		        "type" : "long"
		      },
		      "all" : {
		        "type" : "text",
		        "analyzer" : "text_analyzer",
		        "search_analyzer" : "ik_smart"
		      },
		      "name" : {
		        "type" : "text",
		        "analyzer" : "text_analyzer",
		        "search_analyzer" : "ik_smart",
		        "copy_to" : "all"
		      },
		      "code" : {
		        "type" : "text",
		        "analyzer" : "text_analyzer",
		        "search_analyzer" : "ik_smart",
		        "copy_to" : "all"
		      }, 
			  "gender" : {
		        "type" : "byte"
		      },
		      "masterpiece": {
		        "type": "keyword",
		        "copy_to":"all"
		      },
		      "provinceId" : {
		         "type" : "long"
		      },
		      "cityId" : {
		        "type" : "long"
		      },
		      "districtId" : {
		        "type" : "long"
		      },
		      "location" : {
		        "type" : "geo_point"
		      },
		      "remark" : {
		        "type": "text",
		        "analyzer" : "text_analyzer",
		        "search_analyzer" : "ik_smart",
		        "copy_to" :"all"
		      },
		      "createBy" : {
		        "type": "long",
		        "index": false
		      },
		      "gmtCreate" : {
		        "type": "long",
		        "index": false
		      },
		      "modifiedBy" : {
		        "type": "long",
		        "index": false
		      },
		      "gmtModified" : {
		        "type": "long",
		        "index": false
		      },
		      "suggestion" : {
		        "type" : "completion",
		        "analyzer" : "completion_analyzer",
		        "search_analyzer" : "ik_smart"
		      }
		    }
		  }
		}
	 */
	private String index_personal = "";
}

