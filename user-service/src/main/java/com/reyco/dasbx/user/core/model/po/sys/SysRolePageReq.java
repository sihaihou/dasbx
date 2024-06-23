package com.reyco.dasbx.user.core.model.po.sys;

import com.reyco.dasbx.model.dto.SimplePageDto;

/**
 * @author reyco
 * @version v1.0.1
 * @date 2021.09.16
 */
public class SysRolePageReq extends SimplePageDto {
    /**
     * 角色名称
     */
    private String name;
    /**
     * 开始时间
     */
    private String startDate;
    /**
     * 结束时间
     */
    private String endDate;

    private Long startTime;

    private Long endTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
