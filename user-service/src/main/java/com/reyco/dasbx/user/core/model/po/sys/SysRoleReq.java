package com.reyco.dasbx.user.core.model.po.sys;

import java.util.List;

import com.reyco.dasbx.user.core.model.domain.SysRole;

/**
 * 
 * @author reyco
 *
 */
public class SysRoleReq extends SysRole {
    /**
     *
     */
    private static final long serialVersionUID = -2010675491182696905L;

    private List<Long> menuIdList;
    private List<Long> noChecked;
    private Long startTime;
    private Long endTime;
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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
