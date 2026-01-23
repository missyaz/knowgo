package com.fw.know.go.notice.domain.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.fw.know.go.datasource.domain.entity.BaseEntity;
import com.fw.know.go.notice.domain.constant.NoticeState;
import com.fw.know.go.notice.domain.constant.NoticeType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

/**
 * @Description
 * @Date 22/1/2026 下午5:56
 * @Author Leo
 */
@Getter
@Setter
@Builder
public class Notice extends BaseEntity {

    /**
     * 通知标题
     */
    private String noticeTitle;

    /**
     * 通知内容
     */
    private String noticeContent;

    /**
     * 通知类型
     */
    private NoticeType noticeType;

    /**
     * 发送成功时间
     */
    private Date sendSuccessTime;

    /**
     * 接收地址
     */
    private String targetAddress;

    /**
     * 状态
     */
    private NoticeState state;

    /**
     * 重试次数
     */
    private int retryTimes;

    /**
     * 扩展信息
     */
    private String extendInfo;

    /**
     * 添加扩展信息
     * @param key 扩展信息键
     * @param value 扩展信息值
     */
    public void addExtendInfo(String key, String value){
        Map<String, String> extendInfoMap;
        if (extendInfo == null){
            extendInfoMap = Maps.newHashMapWithExpectedSize(1);
        }
        else {
            extendInfoMap = JSON.parseObject(extendInfo, new TypeReference<Map<String, String>>() {});
        }
        extendInfoMap.put(key, value);
        this.extendInfo = JSON.toJSONString(extendInfoMap);
    }
}
