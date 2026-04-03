package com.fw.know.go.order.sharding.strategy;

/**
 * @Description 分表策略
 * @Date 2/4/2026 下午1:22
 * @Author Leo
 */
public interface ShardingTableStrategy {

    /**
     * 获取分表结果
     * @param externalId 外部ID
     * @param tableCount 表数量
     * @return 分表结果
     */
    int getTable(String externalId, int tableCount);
}
