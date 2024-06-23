package com.reyco.dasbx.user.core.model.dto.sys;

import java.util.List;

import com.reyco.dasbx.user.core.model.domain.SysRole;

/**
 * @author reyco
 * @version v1.0.1
 * @date 2021.09.16
 */
public class SysRoleDto extends SysRole {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4428116983231027243L;
    private List<Long> menuIdList;
    private List<Long> noChecked;

    private String createDate;

    private String updateDate;

    public List<Long> getMenuIdList() {
        return menuIdList;
    }

    public void setMenuIdList(List<Long> menuIdList) {
        this.menuIdList = menuIdList;
    }

    public List<Long> getNoChecked() {
        return noChecked;
    }

    public void setNoChecked(List<Long> noChecked) {
        this.noChecked = noChecked;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
