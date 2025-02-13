package com.deepscience.rpa;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import com.deepscience.rpa.common.container.VariableContainer;
import com.deepscience.rpa.common.context.ActionContext;
import com.deepscience.rpa.model.platform.taobao.enums.TaobaoImageEnum;
import com.deepscience.rpa.model.platform.taobao.service.wrorkbench.impl.TaobaoLiveWorkbenchServiceImpl;
import com.deepscience.rpa.util.ScreenUtils;
import org.junit.Test;
import org.sikuli.script.Button;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Mouse;
import org.sikuli.script.OCR;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;
import org.sikuli.script.ScreenImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class SikuliTest {
    @Test
    public void openFileTest() throws FindFailed, InterruptedException {
        int numberScreens = Screen.getNumberScreens();
        for (int i = 0; i < numberScreens; i++) {
            Screen screen = new Screen(i);
            String imagePath = TaobaoImageEnum.LIVING_TAG.getImagePath();
            Match exists = ScreenUtils.matchImg(i, TaobaoImageEnum.CREATE_SESSION);
            if (Objects.nonNull(exists)) {
                exists.mouseMove();
                BufferedImage liveIdImage = getLiveIdImage(exists);
                System.out.println(OCR.readLine(liveIdImage));
                System.out.println("匹配成功");
            }
        }
    }

    @Test
    public void closeButtonTest() throws FindFailed {
        int numberScreens = Screen.getNumberScreens();
        for (int i = 0; i < numberScreens; i++) {
            Match match = ScreenUtils.matchImg(i, TaobaoImageEnum.CLOSE_BUTTON_03);
            if (Objects.nonNull(match)) {
                match.mouseMove();
            }
        }
    }

    private BufferedImage getLiveIdImage(Match match) {
        if (Objects.isNull(match)) {
            return null;
        }
        Screen screen = new Screen();
        Region region = new Region(match.getX() + 156, match.getY() + 6, 105, 20);

        // 截取该区域的屏幕图像
        ScreenImage capture = screen.capture(region);
        ImgUtil.write(capture.getImage(), FileUtil.file("D:\\zksz\\autoLive\\logs\\test.png"));
        return capture.getImage();
    }

    @Test
    public void ocrTest() throws FindFailed {
        OCR.Options options = OCR.globalOptions();
        options.language("chi_sim");
        options.dataPath("D:\\zksz\\autoLive\\src\\main\\resources\\tessdata");
        System.out.println(options);
        // Match match = null;
        // for (int i = 0; i < Screen.getNumberScreens() ; i++) {
        //     match = ScreenUtils.matchImg(i, TaobaoImageEnum.LIVE_PAUSE_STOP);
        //     if (Objects.nonNull(match)) {
        //         break;
        //     }
        // }
        // Image image = new Region(match).getImage();
        // // BufferedImage resize = image.resize(10);
        // // ImgUtil.write(resize, FileUtil.file("D:\\zksz\\autoLive\\logs\\test.png"));
        //
        // ImgUtil.write(image.get(), FileUtil.file("D:\\zksz\\autoLive\\logs\\test.png"));
        // System.out.println(match.mouseMove());
        // System.out.println("ocr识别结果: " + OCR.readText("D:\\zksz\\autoLive\\logs\\test.png"));
        // match.mouseMove();
    }

    @Test
    public void pass() {
        Match match = null;
        for (int i = 0; i < Screen.getNumberScreens() ; i++) {
            match = ScreenUtils.matchImg(i, TaobaoImageEnum.VERIFICATION_01);
            if (Objects.nonNull(match)) {
                break;
            }
        }
        match.mouseMove();
        Mouse.down(Button.LEFT);
        int baseY = 0;
        for (int i = 0; i < 260;) {
            // 随机数生成器
            int x = RandomUtil.randomInt(0, 60);
            int y = RandomUtil.randomInt(-baseY, 10);
            System.out.println("x: " + x + ", y: " + y);
            Mouse.move(x, y);
            i += x;
            baseY += y;
        }
        Mouse.up();
    }

    /**
     * 替换图片中的指定颜色
     *
     * @param image 原图像
     * @param targetColor 要替换的颜色
     * @param replacementColor 替换后的颜色
     * @return 处理后的图片
     */
    public static BufferedImage replaceColor(BufferedImage image, Color targetColor, Color replacementColor) {
        int width = image.getWidth();
        int height = image.getHeight();

        // 遍历每一个像素，进行颜色替换
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color currentColor = new Color(image.getRGB(x, y));

                // 判断当前像素颜色是否与目标颜色匹配（使用 RGB 值进行比较）
                if (isColorMatch(currentColor, targetColor)) {
                    // 替换颜色
                    image.setRGB(x, y, replacementColor.getRGB());
                }
            }
        }
        return image;
    }

    /**
     * 判断当前颜色是否与目标颜色匹配
     *
     * @param color 当前像素的颜色
     * @param targetColor 目标颜色
     * @return 是否匹配
     */
    private static boolean isColorMatch(Color color, Color targetColor) {
        // 这里可以调整匹配的精度，避免颜色差异过大导致无法匹配
        int tolerance = 10;  // 颜色容忍度，可以根据需求调整
        return Math.abs(color.getRed() - targetColor.getRed()) < tolerance
                && Math.abs(color.getGreen() - targetColor.getGreen()) < tolerance
                && Math.abs(color.getBlue() - targetColor.getBlue()) < tolerance;
    }

    @Test
    public void taobaoLiveWorkbenchTest() {
        TaobaoLiveWorkbenchServiceImpl service = new TaobaoLiveWorkbenchServiceImpl();
        ActionContext actionContext = VariableContainer.getActionContext();
        actionContext.setScreenId(1);
        service.handleVerificationCode();
        // int numberScreens = Screen.getNumberScreens();
        // for (int i = 0; i < numberScreens; i++) {
        //     boolean flag = true;
        //     TaobaoImageEnum liveType = flag ? TaobaoImageEnum.PUBLIC_LIVE : TaobaoImageEnum.PRIVATE_LIVE;
        //     Match match = ScreenUtils.matchImg(i, liveType);
        //     if (Objects.nonNull(match) && match.mouseMove() == 1) {
        //     }
        // }

    }
}
