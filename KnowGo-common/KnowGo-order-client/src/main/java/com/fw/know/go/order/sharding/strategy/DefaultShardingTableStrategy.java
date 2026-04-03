package com.fw.know.go.order.sharding.strategy;

/**
 * @Description
 * @Date 2/4/2026 下午1:24
 * @Author Leo
 */
public class DefaultShardingTableStrategy implements ShardingTableStrategy{

    public DefaultShardingTableStrategy(){}

    @Override
    public int getTable(String externalId, int tableCount) {
        int hashCode = externalId.hashCode();
        //  为了性能更好，可以优化成：return (int) Math.abs((long) hashCode) & (tableCount - 1); 具体原理参考 hashmap 的 hash 方法
        // hashCode % tableCount	需要除法运算，CPU 执行较慢
        //hashCode & (tableCount - 1)	位运算，只需要 1 个 CPU 时钟周期
        //关键条件：这个优化必须要求 tableCount 是 2 的幂次方（如 2, 4, 8, 16, 32...）
        //
        //原理：
        //
        //当 N 是 2 的幂次方时：X % N 等价于 X & (N - 1)
        //例如 tableCount = 8（2³），tableCount - 1 = 7（二进制 0111）
        //任何数字 & 7 都能得到 0-7 的结果，相当于 mod 8
        return (int) Math.abs((long) hashCode & (tableCount - 1));
    }
}
