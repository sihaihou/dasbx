package com.reyco.dasbx.sync;

import java.io.IOException;
import java.util.function.Supplier;

import com.reyco.dasbx.sync.exception.SyncException;

/**
 * 增量更新
 * @author reyco
 *
 */
public interface IncrementUpdateSync<T,R> extends Sync {
	/**
	 * 增量更新
	 * @param supplier
	 * @return
	 * @throws IOException
	 */
	R incrementUpdateSync(Supplier<T> supplier) throws SyncException;
	
	/**
	 * 增量删除
	 * @param supplier
	 * @return
	 * @throws IOException
	 */
	R incrementDeleteSync(Supplier<T> supplier) throws SyncException;
}
