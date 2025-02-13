package com.deepscience.rpa.util;

import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import org.sikuli.script.OCR;

import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * OCR识别工具类
 * @author yangzhuo
 * @date 2025/1/27 09:51
 */
public class OCRUtils {
    /**
     * 识别数字
     * @param imagePath 图片地址
     * @return String
     */
    public static String readNumberText(String imagePath){
        if (StrUtil.isEmpty(imagePath)) {
            return null;
        }
        return OCR.readLine(ImageUtils.getImagePath(imagePath));
    }

    /**
     * 识别数字
     * @param image 图片
     * @return String
     */
    public static String readNumberText(BufferedImage image){
        if (Objects.isNull(image)) {
            return null;
        }
        String read = OCR.readLine(image);
        return ReUtil.get(PatternPool.NUMBERS, read, 0);
    }
}
