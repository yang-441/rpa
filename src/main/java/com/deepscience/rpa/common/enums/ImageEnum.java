package com.deepscience.rpa.common.enums;

/**
 * 图片枚举接口
 * @author yangzhuo
 * @date 2025/1/24 14:55
 */
public interface ImageEnum {
    /**
     * 获取图片路径
     * @return String
     */
    String getImagePath();

    /**
     * 获取图片匹配相似度
     * @return Double
     */
    Double getSim();

    /**
     * 获取图片描述
     * @return String
     */
    String getDesc();
}
