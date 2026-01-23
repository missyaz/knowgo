package com.fw.know.go.notice.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fw.know.go.notice.domain.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 通知Mapper
 * @Date 23/1/2026 下午1:19
 * @Author Leo
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
}
