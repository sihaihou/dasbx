package com.reyco.test.dasbx.common.core.test.order;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProductRepository{
    
	Product findById(Long productId);
	
    @Update("UPDATE Product p SET p.stock = p.stock - #{quantity} WHERE p.id = #{productId} AND p.stock >= #{quantity}")
    int deductStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
    
    @Update("UPDATE Product p SET p.stock = p.stock + #{quantity} WHERE p.id = #{productId}")
    int increaseStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
    
    @Update("UPDATE Product p SET p.reservedStock = p.reservedStock + #{quantity} WHERE p.id = #{productId} AND p.stock - p.reservedStock >= #{quantity}")
    int reserveStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
    
    @Update("UPDATE Product p SET p.reservedStock = p.reservedStock - #{quantity} WHERE p.id = #{productId} AND p.reservedStock >= #{quantity}")
    int releaseReservedStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
    
    @Update("UPDATE Product p SET p.stock = p.stock - #{quantity}, p.reservedStock = p.reservedStock - #{quantity} WHERE p.id = #{productId} AND p.reservedStock >= #{quantity}")
    int confirmReservedStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
    
}