package com.reyco.dasbx.model.dao;

import com.reyco.dasbx.model.Base;
import com.reyco.dasbx.model.po.DeletePO;
import com.reyco.dasbx.model.po.InsertPO;
import com.reyco.dasbx.model.po.SelectPO;
import com.reyco.dasbx.model.po.UpdatePO;

/**
 * 基础Dao
 * @author reyco
 * @param <V extends Base>
 * @param <T extends BasePO>
 */
public interface BaseDao<V extends Base,T1 extends InsertPO,T2 extends DeletePO,T3 extends UpdatePO,T4 extends SelectPO> extends InsertDao<T1>,DeleteDao<T2>,UpdateDao<T3>,SelectDao<V, T4>{
	
}
