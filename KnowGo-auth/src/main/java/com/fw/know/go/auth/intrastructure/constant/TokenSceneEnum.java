package com.fw.know.go.auth.intrastructure.constant;

/**
 * @Description Token获取的场景枚举
 * @Date 26/3/2026 下午4:52
 * @Author Leo
 */
public enum TokenSceneEnum {

    /**
     * 下单-藏品
     */
    BUY_COLLECTION("buy"),

    /**
     * 下单-盲盒
     */
    BUY_BLIND_BOX("buyBb"),
    ;

    /**
     * 场景值
     */
    private final String scene;

    TokenSceneEnum(String scene) {
        this.scene = scene;
    }

    public String getScene() {
        return scene;
    }
}
