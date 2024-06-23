package com.reyco.dasbx.user.core.model.dto.sys;

import java.util.List;

import com.reyco.dasbx.user.core.model.domain.SysMenu;

/**
 * 
 * @author reyco
 *
 */
public class SysMenuDto extends SysMenu{
    /**
	 * 
	 */
	private static final long serialVersionUID = -9142314992572843811L;
	/**
     * 父菜单名称
     */
    private String parentName;
    /**
     * ztree属性
     */
    private Boolean open;

    private List<SysMenuDto> children;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

	public List<SysMenuDto> getChildren() {
		return children;
	}

	public void setChildren(List<SysMenuDto> children) {
		this.children = children;
	}
}
