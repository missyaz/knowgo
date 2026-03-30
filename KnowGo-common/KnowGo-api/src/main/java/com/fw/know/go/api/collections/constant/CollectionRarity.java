package com.fw.know.go.api.collections.constant;

/**
 * @Description 藏品稀有度
 * @Date 30/3/2026 上午10:57
 * @Author Leo
 */
public enum CollectionRarity {

    /**
     * 普通
     */
    COMMON("普通"),
    /**
     * 稀有
     */
    RARE("稀有"),
    /**
     * 史诗
     */
    EPIC("史诗"),
    /**
     * 传说
     */
    LEGENDARY("传说"),
    /**
     * 独特
     */
    UNIQUE("独特"),
    /**
     * 神话
     */
    MYTHICAL("神话");

    private final String value;

    CollectionRarity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
