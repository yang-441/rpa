package com.deepscience.rpa.model.platform.taobao.enums;

import com.deepscience.rpa.common.enums.ImageEnum;
import com.deepscience.rpa.util.ImageUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;

/**
 * 淘宝图片枚举类
 * @author yangzhuo
 * @date 2025/1/24 14:41
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public enum TaobaoImageEnum implements ImageEnum {
    /**
     * 主标题
     */
    TITLE(0, "images/taobao/title.png",
            null, "淘宝直播工作台主标题"),
    /**
     * 按钮 - 创建场次
     */
    CREATE_SESSION(1, "images/taobao/button/createSession.png",
            0.9, "创建场次"),
    /**
     * 复选框 - 仅投放至私域
     */
    PRIVATE_LIVE(2, "images/taobao/checkbox/privateLive.png",
            null,"仅投放至私域"),
    /**
     * 复选框 - 数字人直播
     */
    DIGITAL_LIVE(3, "images/taobao/checkbox/digitalLive.png",
            null,"数字人直播"),
    /**
     * 按钮 - 创建直播
     */
    SESSION_EDIT_CREATE(4, "images/taobao/button/sessionEditCreate.png",
            0.9,"创建直播"),
    /**
     * 按钮 - 立即开播
     */
    LIVE_START_NOW(5, "images/taobao/button/liveStartNow.png",
            0.7,"立即开播"),
    /**
     * 按钮 - 暂停/结束直播
     */
    LIVE_PAUSE_STOP(6, "images/taobao/button/livePauseStop.png",
            null,"暂停/结束直播"),
    /**
     * 按钮 - 暂停直播
     */
    LIVE_PAUSE(7, "images/taobao/button/livePause.png",
            null,"暂停/结束直播"),
    /**
     * 关闭界面
     */
    CLOSE(8, "images/taobao/close.png",
            null,"关闭界面"),
    /**
     * 点击 - 关闭
     */
    CLOSE_BUTTON_01(9, "images/taobao/closeButton_01.png",
            null,"关闭按钮"),
    /**
     * 选择场次
     */
    CHOICE_SESSION(10, "images/taobao/button/choiceSession.png",
            0.9,"选择场次"),
    /**
     * 列表模式
     */
    SESSION_LIST(11, "images/taobao/sessionList.png",
            null,"列表模式"),
    /**
     * 按钮 - 创建场次
     */
    CREATE_SESSION_01(12, "images/taobao/button/createSession_01.png",
            null, "创建场次"),
    /**
     * tag - 直播中
     */
    LIVING_TAG(13, "images/taobao/livingTag.png",
            0.9, "直播中"),
    /**
     * 点击 - 关闭
     */
    CLOSE_BUTTON_02(14, "images/taobao/closeButton_02.png",
            null,"关闭按钮"),
    /**
     * 按钮 - 继续直播
     */
    LIVE_GO_ON(15, "images/taobao/button/liveGoOn.png",
            null, "继续直播"),
    /**
     * 按钮 - 继续直播 -> 立即开播
     */
    LIVE_START_NOW_1(16, "images/taobao/button/liveStartNow_1.png",
            null,"立即开播"),
    /**
     * 按钮 - 选择本场
     */
    CHOOSE_SESSION(17, "images/taobao/button/chooseSession.png",
            null,"选择本场"),
    /**
     * 按钮 - 场次管理
     */
    MANAGE_SESSION(18, "images/taobao/button/manageSession.png",
            null,"场次管理"),
    /**
     * 按钮 - 结束直播
     */
    LIVE_END(19, "images/taobao/button/liveEnd.png",
            null,"结束直播"),
    /**
     * 按钮 - 结束直播
     */
    LIVE_END_1(20, "images/taobao/button/liveEnd_1.png",
            null,"结束直播"),
    /**
     * 按钮 - 场次列表
     */
    SESSION_LIST_BUTTON(20, "images/taobao/button/sessionListButton.png",
            null,"结束直播"),
    /**
     * 标题 - 创建场次标题
     */
    CREATE_SESSION_PAGE_TITLE(21, "images/taobao/createSessionPageTitle.png",
            null,"场次列表 / 创建场次"),
    /**
     * 按钮 - 快速进入
     */
    QUICK_ENTRY(22, "images/taobao/button/quickEntry.png",
            null,"快速进入"),
    /**
     * 复选框 - 公域直播
     */
    PUBLIC_LIVE(23, "images/taobao/checkbox/publicLive.png",
            null, "所有人可见"),
    /**
     * 点击 - 关闭
     */
    CLOSE_BUTTON_03(24, "images/taobao/closeButton_03.png",
            null,"关闭按钮"),
    /**
     * 点击 - 关闭
     */
    CLOSE_BUTTON_03_SUB(25, "images/taobao/closeButton_03_sub.png",
            null,"关闭按钮"),

    /**
     * 验证码标题
     */
    VERIFICATION_TITLE(1000, "images/taobao/verificationTitle.png",
            null,"验证码标题"),
    /**
     * 验证码滑块
     */
    VERIFICATION_01(1001, "images/taobao/verification_01.png",
            null,"验证码滑块"),
    ;

    /**
     * id
     */
    private final int id;

    /**
     * 图片地址
     */
    private final String imagePath;

    /**
     * 相似度
     */
    private final Double sim;

    /**
     * 描述
     */
    private final String desc;

    @Override
    public BufferedImage getImage() {
        try {
            return ImageUtils.getImage(imagePath);
        } catch (Exception e) {
            log.error("get image error", e);
            return null;
        }
    }
}
