package com.deepscience.rpa.util;

import com.deepscience.rpa.common.container.VariableContainer;
import com.deepscience.rpa.common.context.ActionContext;
import com.deepscience.rpa.common.enums.ImageEnum;
import org.sikuli.basics.Settings;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import java.util.Objects;
import java.util.Optional;

/**
 * 屏幕操作工具类
 * @author yangzhuo
 * @date 2025/1/24 14:46
 */
public class ScreenUtils {

    /**
     * 查询指定图片所在屏幕
     * @param imageEnum 图片枚举
     * @return Integer
     */
    public static Integer findScreenId(ImageEnum imageEnum) {
        int numberScreens = Screen.getNumberScreens();
        Pattern pattern = new Pattern(imageEnum.getImage());
        Integer id = ConfigUtils.getLastScreenId();
        // 优化屏幕查找
        if (Objects.nonNull(id) && id >= 0 && id < numberScreens) {
            Integer screenId = findScreenId(id, pattern, 20.0);
            if (Objects.nonNull(screenId)) {
                return screenId;
            }
        }
        for (int i = 0; i < numberScreens; i++) {
            if (i == id) {
                continue;
            }
            Integer screenId = findScreenId(i, pattern, 20.0);
            if (Objects.nonNull(screenId)) {
                return screenId;
            }
        }

        return null;
    }

    public static Match matchImg(ImageEnum imageEnum) {
        return Optional.ofNullable(VariableContainer.getActionContext())
                .map(ActionContext::getScreenId)
                .map(id -> matchImg(id, imageEnum))
                .orElse(null);
    }

    public static Match matchImg(Match match, ImageEnum imageEnum) {
        return matchImg(match, imageEnum, null);
    }

    public static Match matchImg(Match match, ImageEnum imageEnum, Double sim) {
        Pattern pattern = new Pattern(imageEnum.getImage());
        if (Objects.nonNull(sim)) {
            pattern.similar(sim);
        }
        return Optional.ofNullable(match)
                .map(m -> m.get(0))
                .map(r -> exits(r, pattern, null))
                .orElse(null);
    }

    public static Match matchImg(ImageEnum imageEnum, Double timeout) {
        return matchImg(imageEnum, timeout, null);
    }

    public static Match matchImg(ImageEnum imageEnum, Double timeout, Double sim) {
        return Optional.ofNullable(VariableContainer.getActionContext())
                .map(ActionContext::getScreenId)
                .map(id -> matchImg(id, imageEnum, timeout, sim))
                .orElse(null);
    }

    public static Match matchImg(int id, ImageEnum imageEnum) {
        return matchImg(id, imageEnum, null, null);
    }

    public static Match matchImg(int id, ImageEnum imageEnum, Double timeout, Double sim) {
        Pattern pattern = new Pattern(imageEnum.getImage());
        if (Objects.nonNull(imageEnum.getSim())) {
            pattern.similar(imageEnum.getSim());
        }
        if (Objects.nonNull(sim)) {
            pattern.similar(sim);
        }
        return exits(new Screen(id), pattern, timeout);
    }

    public static Match exits(Region region, Pattern pattern, Double timeout) {
        if (Objects.isNull(region) || Objects.isNull(pattern)) {
            return null;
        }
        return region.exists(pattern, Objects.isNull(timeout) ? 5 : timeout);
    }

    private static Integer findScreenId(int id, Pattern pattern) {
        return findScreenId(id, pattern, null);
    }

    private static Integer findScreenId(int id, Pattern pattern, Double timeout) {
        Screen screen = new Screen(id);
        Match exists = screen.exists(pattern, Objects.isNull(timeout) ? Settings.AutoWaitTimeout : timeout);
        if (Objects.nonNull(exists) && exists.click() == 1) {
            ConfigUtils.setLastScreenId(id);
            return id;
        }
        return null;
    }
}
