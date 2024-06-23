package com.reyco.dasbx.user.core.config;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;

public class CustomBloomFilterHelper<T> {
	private int numHashFunctions;

    private long bitSize;
    
    private Funnel<T> funnel;

    public CustomBloomFilterHelper(Funnel<T> funnel, int expectedInsertions, double fpp) {
        Preconditions.checkArgument(funnel != null, "funnel不能为空");
        this.funnel = funnel;
        bitSize = optimalNumOfBits(expectedInsertions, fpp);
        numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, bitSize);
    }

    /**
     * 计算bit数组的长度
     * m = -n * lnp / Math.pow(ln2,2)
     * @param n 插入数据条数
     * @param p 误判率
     * @return
     */
    private long optimalNumOfBits(long n, double p) {
        if (p == 0.0D) {
            p = 4.9E-324D;
        }
        return (long)((double)(-n) * Math.log(p) / (Math.log(2.0D) * Math.log(2.0D)));
    }

    /**
     * 计算hash方法执行次数
     * k = m/n * ln2
     * @param n 插入数据条数
     * @param m 数据位数
     * @return
     */
    private int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int)Math.round((double)m / (double)n * Math.log(2.0D)));
    }

    /**
     * 计算经过多个函数处理之后数据的偏移数组
     * @param value
     * @return
     */
    public List<Long> murmurHashOffset(T value) {
        List<Long> offset = new ArrayList<>();
        byte[] bytes = Hashing.murmur3_128().hashObject(value, funnel).asBytes();
        long hash1 = lowerEight(bytes);
        long hash2 = upperEight(bytes);
        long combinedHash = hash1;
        for (int i = 0; i < numHashFunctions; i++) {
            long hash = (combinedHash & 9223372036854775807L) % bitSize;
            offset.add(hash);
            combinedHash += hash2;
        }
        return offset;
    }

    private long lowerEight(byte[] bytes) {
        return Longs.fromBytes(bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
    }

    private long upperEight(byte[] bytes) {
        return Longs.fromBytes(bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
    }
}
