package com.deepscience.rpa.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.OCR;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * OCR识别工具类
 * @author yangzhuo
 * @date 2025/1/27 09:51
 */
@Slf4j
public class OCRUtils {

    static {
        String absolutePath = FileUtil.getAbsolutePath(Paths.get(System.getProperty("user.dir")) + "/runtime/tessdata");
        log.info("OCR初始化路径：{}", absolutePath);
        OCR.Options options = OCR.globalOptions();
        // 获取 defaultDataPath 字段
        try {
            Field defaultDataPathField;
            defaultDataPathField = options.getClass().getDeclaredField("defaultDataPath");
            // 设置可访问私有字段
            defaultDataPathField.setAccessible(true);
            defaultDataPathField.set(options, absolutePath);
        } catch (Exception e) {
            log.error("OCR初始化路径失败", e);
        }
    }

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
