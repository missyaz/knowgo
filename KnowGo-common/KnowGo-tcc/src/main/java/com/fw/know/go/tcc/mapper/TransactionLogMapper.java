package com.fw.know.go.tcc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fw.know.go.tcc.entity.TransactionLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 事务日志Mapper
 * @Date 16/4/2026 下午3:34
 * @Author Leo
 */
@Mapper
public interface TransactionLogMapper extends BaseMapper<TransactionLog> {
}
