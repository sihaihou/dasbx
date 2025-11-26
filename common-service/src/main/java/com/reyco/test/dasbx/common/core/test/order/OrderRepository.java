package com.reyco.test.dasbx.common.core.test.order;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderRepository {
	
	void save(Order order );
	
    Optional<Order> findByOrderId(String orderId);
    
    List<Order> findByUserIdAndStatus(Long userId, Integer status);
    
    @Select("SELECT o FROM Order o WHERE o.createTime < #{expireTime} AND o.status = 1")
    List<Order> findExpiredUnpaidOrders(@Param("expireTime") Date expireTime);
    
    @Update("UPDATE Order o SET o.status = #{status} WHERE o.orderId = #{orderId}")
    int updateOrderStatus(@Param("orderId") String orderId, @Param("status") Integer status);
    
    @Update("UPDATE Order o SET o.status = #{status}, o.payTime = #{payTime} WHERE o.orderId = #{orderId}")
    int updateOrderToPaid(@Param("orderId") String orderId, @Param("status") Integer status, @Param("payTime") Date payTime);
}