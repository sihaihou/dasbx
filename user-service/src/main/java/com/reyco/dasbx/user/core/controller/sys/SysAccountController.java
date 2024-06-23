package com.reyco.dasbx.user.core.controller.sys;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.commons.utils.AESUtils;
import com.reyco.dasbx.commons.utils.JsonUtils;
import com.reyco.dasbx.commons.utils.SecretKeyUtils;
import com.reyco.dasbx.config.exception.core.BusinessException;
import com.reyco.dasbx.es.core.search.ElasticsearchVO;
import com.reyco.dasbx.model.domain.SysAccount;
import com.reyco.dasbx.user.core.model.dto.AccountBindDeveloperDto;
import com.reyco.dasbx.user.core.model.dto.SysAccountInsertDto;
import com.reyco.dasbx.user.core.model.dto.SysAccountRegisterDto;
import com.reyco.dasbx.user.core.model.dto.SysAccountUpdateDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysAccountDeleteDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysAccountDisableOrEnableDto;
import com.reyco.dasbx.user.core.model.dto.sys.SysAccountSearchDto;
import com.reyco.dasbx.user.core.model.vo.SysAccountInfoVO;
import com.reyco.dasbx.user.core.service.sys.SysAccountService;

@RestController
@RequestMapping("/sys/account")
public class SysAccountController {

	@Autowired
	private SysAccountService sysAccountService;
	
	@PostMapping("initElasticsearchSysAccount")
	public Object initElasticsearchSysAccount() throws IOException {
		int count = sysAccountService.initElasticsearchSysAccount();
		return R.success(count);
	}
	
	@GetMapping("{id}")
	public Object getById(@PathVariable("id")Long id) throws InterruptedException {
		SysAccountInfoVO accountInfoVO = sysAccountService.get(id);
		return R.success(accountInfoVO);
	}
	@GetMapping("getByEmail")
	public Object getByEmail(String email) throws InterruptedException {
		SysAccountInfoVO accountInfoVO = sysAccountService.getByEmail(email);
		return R.success(accountInfoVO);
	}
	
	@GetMapping("getByUid")
	public Object getByUid(String uid) {
		SysAccountInfoVO accountInfoVO = sysAccountService.getByUid(uid);
		return R.success(accountInfoVO);
	}
	
	@GetMapping("getByUsername")
	public Object getByUsername(String username) {
		SysAccount account = sysAccountService.getByUsername(username);
		return R.success(account);
	}
	
	@GetMapping("currentUser")
	public Object currentUser() throws Exception {
		SysAccountInfoVO accountInfoVO = sysAccountService.currentUser();
		return R.success(accountInfoVO);
	}
	@GetMapping("suggestion")
	public Object getSuggestion(String keyword) throws Exception {
		List<String> suggestions = sysAccountService.getSuggestion(keyword);
		return R.success(suggestions);
	}
	@GetMapping("search")
    public Object search(SysAccountSearchDto sysAccountSearchDto) throws IOException {
		ElasticsearchVO<SysAccountInfoVO> search = sysAccountService.search(sysAccountSearchDto);
        return R.success(search);
    }
	@PostMapping("register")
	public Object register(String r) {
		String secretKey = SecretKeyUtils.getSecretKey();
		String registerInfoJson = AESUtils.decrypt(r,secretKey);
		SysAccountRegisterDto sysAccountRegisterDto = JsonUtils.jsonToObj(registerInfoJson, SysAccountRegisterDto.class);
		if (StringUtils.isNotBlank(sysAccountRegisterDto.getUsername())) {
            int updateAccountCount = sysAccountService.getCountByTypeAndNameOrById(sysAccountRegisterDto.getUsername(), null);
            if (updateAccountCount > 0) {
                return R.fail("注册失败，该用户已经存在。。。。!");
            }
        }
		sysAccountService.register(sysAccountRegisterDto);
		return R.success("注册成功！");
	}
	/*@PostMapping("register")
	public Object register(@RequestBody SysAccountRegisterDto sysAccountRegisterDto) {
		if (StringUtils.isNotBlank(sysAccountRegisterDto.getUsername())) {
            int updateAccountCount = sysAccountService.getCountByTypeAndNameOrById(sysAccountRegisterDto.getUsername(), null);
            if (updateAccountCount > 0) {
                return R.fail("修改失败，该用户已经存在。。。。!");
            }
        }
		sysAccountService.register(sysAccountRegisterDto);
		return R.success("注册成功！");
	}*/
	@PostMapping
	public Object save(@RequestBody SysAccountInsertDto sysAccountInsertDto) throws Exception {
		if (StringUtils.isNotBlank(sysAccountInsertDto.getUsername())) {
            int updateAccountCount = sysAccountService.getCountByTypeAndNameOrById(sysAccountInsertDto.getUsername(), null);
            if (updateAccountCount > 0) {
                return R.fail("修改失败，该用户已经存在。。。。!");
            }
        }
		sysAccountService.insert(sysAccountInsertDto);
		return R.success();
	}
	
	@PutMapping
	public Object update(@RequestBody SysAccountUpdateDto sysAccountUpdateDto) {
		if (StringUtils.isNotBlank(sysAccountUpdateDto.getUsername())) {
            int updateAccountCount = sysAccountService.getCountByTypeAndNameOrById(sysAccountUpdateDto.getUsername(), sysAccountUpdateDto.getId());
            if (updateAccountCount > 0) {
                return R.fail("修改失败，该用户已经存在。。。。!");
            }
        }
		sysAccountService.update(sysAccountUpdateDto);
		return R.success();
	}
	/**
	 * 禁用启用
	 * @param sysAccountDisableOrEnableDto
	 * @return
	 * @throws IOException 
	 */
	@PatchMapping("disableOrEnable")
	public Object disableOrEnable(@RequestBody SysAccountDisableOrEnableDto sysAccountDisableOrEnableDto){
		sysAccountService.updateState(sysAccountDisableOrEnableDto);
		return R.success();
	}
	@DeleteMapping
	public Object delete(@RequestBody SysAccountDeleteDto sysAccountDeleteDto){
		sysAccountService.delete(sysAccountDeleteDto);
		return R.success();
	}
	/**
	 * 绑定开发者
	 * @param accountBindDeveloperDto
	 * @return
	 * @throws BusinessException
	 */
	@PatchMapping("bindDeveloper")
	public Object bindDeveloper(@RequestBody AccountBindDeveloperDto accountBindDeveloperDto) throws BusinessException {
		sysAccountService.bindDeveloper(accountBindDeveloperDto);
		return R.success();
	}
}
