package com.fw.know.go.order.sharding.id;

import cn.hutool.core.util.IdUtil;
import com.fw.know.go.api.common.constant.BusinessCode;
import com.fw.know.go.order.sharding.strategy.DefaultShardingTableStrategy;
import org.apache.commons.lang.StringUtils;


/**
 * @Description 分布式ID
 * @Date 2/4/2026 下午1:20
 * @Author Leo
 */
public class DistributeID {

    /**
     * 系统标识码
     */
    private String businessCode;

    /**
     * 表下标
     */
    private String table;

    /**
     * 序列号
     */
    private String seq;

    /**
     * 分表策略
     */
    private static DefaultShardingTableStrategy shardingTableStrategy = new DefaultShardingTableStrategy();

    public DistributeID(){

    }

    /**
     * 利用雪花算法生成唯一ID
     * @param businessCode 业务码
     * @param workerId 工作ID
     * @param externalId 外部ID
     * @return 分布式唯一Id
     */
    public static String generateWithSnowflake(BusinessCode businessCode, long workerId,
                                               String externalId){
        long id = IdUtil.getSnowflake(workerId).nextId();
        return generate(businessCode, externalId, id);
    }

    /**
     * 生成一个唯一ID：10（业务码） 1769649671860822016（sequence) 1023(分表）
     * @param businessCode 业务码
     * @param externalId 外部ID
     * @param sequenceNumber 序列号
     * @return 唯一ID
     */
    public static String generate(BusinessCode businessCode, String externalId, Long sequenceNumber){
        DistributeID distributeId = create(businessCode, externalId, sequenceNumber);
        return distributeId.businessCode + distributeId.seq + distributeId.table;
    }

    public static DistributeID create(BusinessCode businessCode, String externalId, Long sequenceNumber){
        DistributeID distributeId = new DistributeID();
        distributeId.businessCode = businessCode.getCodeString();
        String table = String.valueOf(shardingTableStrategy.getTable(externalId, businessCode.tableCount()));
        distributeId.table = StringUtils.leftPad(table, 4, "0");
        distributeId.seq = String.valueOf(sequenceNumber);
        return distributeId;
    }

    @Override
    public String toString() {
        return this.businessCode + this.seq + this.table;
    }
}
