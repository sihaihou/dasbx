package com.reyco.dasbx.common.core.model.dto.sys;

import com.reyco.dasbx.model.dto.SimpleListDto;
/**
 * 下单请求
 * @author reyco
 *
 */
public class CreateOrderDto extends SimpleListDto{
	/**
	 * 用户ID不能为空
	 * 
	 */
	private Long userId;
	/**
	 * 商品ID不能为空
	 */
	private Long productId;
	/**
	 * 数量必须大于0
	 */
	private Integer quantity;

	private String remark;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
