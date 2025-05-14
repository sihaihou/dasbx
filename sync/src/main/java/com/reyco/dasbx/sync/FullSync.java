package com.reyco.dasbx.sync;

import java.io.IOException;

import com.reyco.dasbx.sync.exception.SyncException;

/**
 * 全量同步
 * @author reyco
 *
 */
public interface FullSync<R> extends Sync {

	/**
	 * 全量同步
	 * @return
	 * @throws IOException
	 */
	R fullSync() throws SyncException;

}
