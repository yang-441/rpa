package com.deepscience.rpa.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * 图片工具类
 * @author yangzhuo
 * @date 2025/1/24 09:50
 */
@Slf4j
public class ImageUtils {

    /**
     * 获取图片全路径
     * @param imagePath 图片资源路径
     * @return String
     */
    public static String getImagePath(String imagePath) {
        if (StrUtil.isEmpty(imagePath)) {
            return null;
        }
        return FileUtil.getAbsolutePath(imagePath);
    }

    /**
     * 获取图片全路径
     * @param imagePath 图片资源路径
     * @return String
     */
    public static BufferedImage getImage(String imagePath) {
        try {
            @Cleanup InputStream stream = ResourceUtil.getStream(imagePath);
            return ImgUtil.read(stream);
        } catch (Exception e) {
            log.error("get image error", e);
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    /**
     * 对BufferedImage进行等比缩放，返回缩放后的新BufferedImage
     * @param originalImage 原始图片
     * @param aspectRatio   缩放比例
     * @return 缩放后的BufferedImage
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, double aspectRatio) {
        // 获取原始图像的宽高
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // 计算等比缩放后的宽高
        int targetWidth = (int) (originalWidth * aspectRatio);
        int targetHeight = (int) (originalHeight * aspectRatio);

        // 创建目标尺寸的BufferedImage
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());

        // 使用Graphics2D进行绘制
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH), 0, 0, null);
        // 释放资源
        g2d.dispose();
        return resizedImage;
    }
}
