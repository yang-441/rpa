package com.deepscience.rpa.model.platform.taobao.service.wrorkbench.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.deepscience.rpa.common.constants.AppFileConstants;
import com.deepscience.rpa.common.container.VariableContainer;
import com.deepscience.rpa.common.context.ActionContext;
import com.deepscience.rpa.common.enums.ImageEnum;
import com.deepscience.rpa.model.platform.taobao.enums.TaobaoImageEnum;
import com.deepscience.rpa.model.platform.taobao.service.wrorkbench.TaobaoLiveWorkbenchService;
import com.deepscience.rpa.util.ConfigUtils;
import com.deepscience.rpa.util.ImageUtils;
import com.deepscience.rpa.util.MsgUtils;
import com.deepscience.rpa.util.OCRUtils;
import com.deepscience.rpa.util.ScreenUtils;
import com.deepscience.rpa.util.pcap4j.NetWorkUtils;
import com.deepscience.rpa.util.process.CmdUtils;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.Button;
import org.sikuli.script.Match;
import org.sikuli.script.Mouse;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;
import org.sikuli.script.ScreenImage;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 淘宝直播工作台筛选器
 * @author yangzhuo
 * @date 2025/1/24 13:36
 */
@Slf4j
@Service
public class TaobaoLiveWorkbenchServiceImpl implements TaobaoLiveWorkbenchService {

    @Override
    public ActionContext startWorkbench() {
        ActionContext actionContext = VariableContainer.getActionContext();
        String workbenchLocation = ConfigUtils.getWorkbenchLocation();
        try {
            File file = FileUtil.file(workbenchLocation);
            // 指定exe文件路径
            ProcessBuilder processBuilder = new ProcessBuilder(workbenchLocation);
            // 设置工作目录
            processBuilder.directory(FileUtil.getParent(file, 1));
            Process process = processBuilder.start();
            // 获取日志打印
            // 处理标准输出流
            try (BufferedReader ignored = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                log.warn("process inputStream ignored");
            } catch (Exception exception) {
                log.error("读取标准输出流异常", exception);
            }

            // 处理错误输出流
            try (BufferedReader ignored = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                log.warn("process errorStream ignored");
            } catch (IOException exception) {
                log.error("读取错误输出流异常", exception);
            }
            log.info("应用启动成功, 应用路径: {}", workbenchLocation);
        } catch (Exception exception) {
            log.error("无法启动应用程序", exception);
            JOptionPane.showMessageDialog(null, "无法启动应用程序", "错误", JOptionPane.ERROR_MESSAGE);
        }
        return actionContext;
    }

    @Override
    public ActionContext findWorkbench() {
        ActionContext actionContext = VariableContainer.getActionContext();
        if (!actionContext.isHasNext()) {
            return actionContext;
        }
        Integer screenId = ScreenUtils.findScreenId(TaobaoImageEnum.TITLE);
        if (Objects.nonNull(screenId)) {
            log.info("寻找直播工作台...");
            MsgUtils.writeSuccessMsg("寻找直播工作台...");
            actionContext.setScreenId(screenId);
            quickEntry();
            closePopBox();
            return actionContext;
        }
        actionContext.setHasNext(false);
        return actionContext;
    }

    @Override
    public ActionContext createSession() {
        log.info("执行创建场次...");
        ActionContext actionContext = VariableContainer.getActionContext();
        boolean res;
        // 点击创建场次
        if (actionContext.isHasNext()) {
            actionContext.setHasNext(clickCreateSession());
        }
        // 等待编辑页加载完毕
        if (actionContext.isHasNext()) {
            waitCreateSessionPage();
            if (!isCreateSessionPage()) {
                res = getLiveIdBySessionListPage();
                // 默认弹出场次列表时, 执行关播动作
                if (res) {
                    log.info("当前弹出场次列表, 执行关播动作...");
                    endLiving();
                }
                actionContext.setHasNext(false);
            } else {
                log.error("创建场次页面判定失败");
            }
        }

        // 编辑场次并创建
        if (actionContext.isHasNext()) {
            actionContext.setHasNext(editAndCreateSession());
        }

        return actionContext;
    }

    @Override
    public ActionContext handleVerificationCode() {
        ActionContext actionContext = VariableContainer.getActionContext();
        if (actionContext.isHasNext()) {
            actionContext.setHasNext(slideVerification());
        }
        return actionContext;
    }

    @Override
    public ActionContext startLiving() {
        ActionContext actionContext = VariableContainer.getActionContext();
        // 立即开播
        if (actionContext.isHasNext()) {
            actionContext.setHasNext(liveStartNow());
        }
        return actionContext;
    }

    @Override
    public ActionContext pauseLiving() {
        ActionContext actionContext = VariableContainer.getActionContext();
        // 暂停直播
        if (actionContext.isHasNext()) {
            actionContext.setHasNext(livePause());
        }
        return actionContext;
    }

    @Override
    public ActionContext endLiving() {
        // 结束直播
        log.info("结束正在直播的直播间...");
        ActionContext actionContext = VariableContainer.getActionContext();

        // 关闭直播
        if (actionContext.isHasNext()) {
            actionContext.setHasNext(liveEnd());
        }

        return actionContext;
    }

    @Override
    public ActionContext doGetLiveId() {
        ActionContext actionContext = VariableContainer.getActionContext();
        // 获取直播间id
        if (actionContext.isHasNext()) {
            if (StrUtil.isNotEmpty(actionContext.getLiveId())) {
                log.info("已获取直播间id: {}, 不再重复识别", actionContext.getLiveId());
            } else {
                actionContext.setHasNext(getLiveId());
            }
        }
        return actionContext;
    }

    @Override
    public ActionContext SearchLiveById() {
        ActionContext actionContext = VariableContainer.getActionContext();
        // 通过直播间id检索直播
        if (actionContext.isHasNext()) {
            actionContext.setHasNext(doSearchLiveById());
        }

        // 选择场次
        if (actionContext.isHasNext()) {
            actionContext.setHasNext(chooseSession());
        }
        return actionContext;
    }


    @Override
    public ActionContext closeAllWindows() {
        ActionContext actionContext = VariableContainer.getActionContext();
        // 处理验证码
        slideVerification();
        // 关闭弹出层
        closePopBox();
        return actionContext;
    }

    @Override
    public ActionContext closeWorkbench() {
        ActionContext actionContext = VariableContainer.getActionContext();
        // 关闭直播工作台
        if (actionContext.isHasNext()) {
            actionContext.setHasNext(closeLiveWorkbench());
        }
        killWorkbench();
        return actionContext;
    }

    @Override
    public ActionContext closeAndExit() {
        ActionContext actionContext = VariableContainer.getActionContext();
        // 关闭弹窗
        closeAllWindows();
        actionContext.setHasNext(closeLiveWorkbench());
        killWorkbench();
        return actionContext;
    }

    /**
     * 结束直播
     * @return boolean
     */
    public boolean liveEnd() {
        log.info("结束直播...");
        Match match = ScreenUtils.matchImg(TaobaoImageEnum.LIVE_END);
        if (Objects.nonNull(match) && match.click() == 1) {
            log.info("结束直播执行成功...");
            ThreadUtil.sleep(5, TimeUnit.SECONDS);
            match = ScreenUtils.matchImg(TaobaoImageEnum.LIVE_END_1);
            if (Objects.nonNull(match) && match.click() == 1) {
                log.info("确定结束直播执行成功...");
                return true;
            }
        }
        return false;
    }

    /**
     * 点击创建场次
     * @return boolean
     */
    public boolean clickCreateSession() {
        log.info("点击创建场次...");
        // 点击创建场次按钮
        Match createMatch = ScreenUtils.matchImg(TaobaoImageEnum.CREATE_SESSION);
        if (Objects.nonNull(createMatch) && createMatch.click() == 1) {
            MsgUtils.writeSuccessMsg("执行创建场次...");
            ThreadUtil.safeSleep(5000);
            return true;
        }
        return false;
    }

    private boolean waitCreateSessionPage() {
        // 等待场次编辑加载完毕
        Match editPageMatch = ScreenUtils.matchImg(TaobaoImageEnum.SESSION_EDIT_CREATE, 30.0);
        if (Objects.nonNull(editPageMatch) && editPageMatch.mouseMove() == 1) {
            log.info("创建场次编辑页加载完毕...");
            MsgUtils.writeSuccessMsg("创建场次编辑页加载完毕...");
            editPageMatch.mouseMove(0, -50);
            editPageMatch.wheel(10, 10);
            return true;
        }
        return false;
    }

    /**
     * 编辑创建场次
     * @return boolean
     */
    public boolean editAndCreateSession() {
        Boolean publicLive = Optional.ofNullable(VariableContainer.getActionContext().getLivePlan())
                .map(plan -> Objects.equals(1, plan.getVisibleRange()))
                .orElse(null);
        // 任务未配置时, 获取客户端配置
        publicLive = Objects.isNull(publicLive) ? ConfigUtils.getPublicLive() : publicLive;
        ImageEnum liveType = publicLive ? TaobaoImageEnum.PUBLIC_LIVE : TaobaoImageEnum.PRIVATE_LIVE;

        Match match = ScreenUtils.matchImg(liveType);
        if (Objects.nonNull(match) && match.click() == 1) {
            log.info("勾选可见范围: {}", liveType.getDesc());
            MsgUtils.writeSuccessMsg(StrUtil.format("勾选可见范围: {}", liveType.getDesc()));
        } else {
            return false;
        }

        if (ConfigUtils.getDigitalLive()) {
            match = ScreenUtils.matchImg(TaobaoImageEnum.DIGITAL_LIVE);
            if (Objects.nonNull(match) && match.click() == 1) {
                log.info("数字人直播...");
                MsgUtils.writeSuccessMsg("数字人直播...");
            } else {
                return false;
            }
        } else {
            log.info("不勾选数字人直播...");
        }


        match = ScreenUtils.matchImg(TaobaoImageEnum.SESSION_EDIT_CREATE);
        if (Objects.nonNull(match) && match.click() == 1) {
            log.info("创建直播...");
            MsgUtils.writeSuccessMsg("创建直播...");
        } else {
            return false;
        }
        return true;
    }

    /**
     * 开始直播
     * @return boolean
     */
    public boolean liveStartNow() {
        // 抓取推流码
        taobaoPushUrlListen();
        // 等待立即开播
        Match match = ScreenUtils.matchImg(TaobaoImageEnum.LIVE_START_NOW);
        if (Objects.nonNull(match) && match.click() == 1) {
            log.info("立即开播...");
            MsgUtils.writeSuccessMsg("立即开播...");
            ThreadUtil.sleep(15, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    /**
     * 选择本场
     * @return boolean
     */
    public boolean chooseSession() {
        log.info("选择本场...");
        Match match = ScreenUtils.matchImg(TaobaoImageEnum.CHOOSE_SESSION);
        if (Objects.nonNull(match) && match.click() == 1) {
            log.info("选择本场执行成功...");
            MsgUtils.writeSuccessMsg("选择本场执行成功...");
            ThreadUtil.sleep(5, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    /**
     * 继续直播
     * @return boolean
     */
    public boolean liveGoOn() {
        // 抓取推流码
        taobaoPushUrlListen();
        // 继续直播
        Match match = ScreenUtils.matchImg(TaobaoImageEnum.LIVE_GO_ON);
        if (Objects.nonNull(match) && match.click() == 1) {
            log.info("继续直播...");
            MsgUtils.writeSuccessMsg("继续直播...");
            ThreadUtil.sleep(5, TimeUnit.SECONDS);
        } else {
            return false;
        }
        // 立即开播
        match = ScreenUtils.matchImg(TaobaoImageEnum.LIVE_START_NOW_1);
        if (Objects.nonNull(match) && match.click() == 1) {
            log.info("继续直播 -> 立即开播...");
            MsgUtils.writeSuccessMsg("立即开播...");
            ThreadUtil.sleep(10, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    /**
     * 暂停直播
     * @return boolean
     */
    public boolean livePause() {
        // 我知道了
        processIKnow();
        // 暂停/结束直播
        Match match = ScreenUtils.matchImg(TaobaoImageEnum.LIVE_PAUSE_STOP, 20.0);
        if (Objects.nonNull(match) && match.click() == 1) {
            log.info("暂停/结束直播...");
            MsgUtils.writeSuccessMsg("暂停/结束直播...");
        } else {
            return false;
        }
        // 我知道了
        processIKnow();
        // 暂停直播
        match = ScreenUtils.matchImg(TaobaoImageEnum.LIVE_PAUSE);
        if (Objects.nonNull(match) && match.click() == 1) {
            log.info("暂停直播...");
            MsgUtils.writeSuccessMsg("暂停直播...");
            return true;
        }
        return false;
    }

    /**
     * 滑块验证
     * @return boolean
     */
    public boolean slideVerification() {
        Match match = ScreenUtils.matchImg(TaobaoImageEnum.VERIFICATION_TITLE);
        if (Objects.nonNull(match)) {
            log.info("检测到滑块验证, 开始处理...");
        } else {
            log.info("滑块验证未处理, 未检测到滑块");
            return false;
        }
        match = ScreenUtils.matchImg(TaobaoImageEnum.VERIFICATION_01);
        if (Objects.isNull(match)) {
            log.error("滑块检索失败...");
            return false;
        }
        match.mouseMove();
        Mouse.down(Button.LEFT);
        int baseY = 0;
        for (int i = 0; i < 260; ) {
            // 随机数生成器
            int x = RandomUtil.randomInt(0, 60);
            int y = RandomUtil.randomInt(-baseY, 10);
            Mouse.move(x, y);
            i += x;
            baseY += y;
        }
        Mouse.up();
        return true;
    }

    /**
     * 关闭工作台
     * @return boolean
     */
    private boolean closeLiveWorkbench() {
        log.info("关闭直播工作台...");
        closePopBox();
        Match match = ScreenUtils.matchImg(TaobaoImageEnum.CLOSE);
        if (Objects.nonNull(match)) {
            log.info("准备关闭直播工作台执行成功...");
            MsgUtils.writeSuccessMsg("准备关闭直播工作台执行成功...");
            match = ScreenUtils.matchImg(match, TaobaoImageEnum.CLOSE_BUTTON_01);
            if (Objects.nonNull(match) && match.click() == 1) {
                log.info("关闭直播工作台执行成功...");
                MsgUtils.writeSuccessMsg("关闭直播工作台执行成功...");
                return true;
            }
        }
        return false;
    }

    /**
     * 关闭弹窗
     * @return boolean
     */
    private boolean closePopBox() {
        log.info("关闭弹窗...");
        boolean result = false;
        Match match = ScreenUtils.matchImg(TaobaoImageEnum.CLOSE_BUTTON_03);
        for (int i = 0; i < 5; i++) {
            if (Objects.nonNull(match)) {
                match = ScreenUtils.matchImg(match, TaobaoImageEnum.CLOSE_BUTTON_03_SUB);
                if (Objects.nonNull(match) && match.click() == 1) {
                    log.info("关闭问卷弹窗执行成功...");
                    result = true;
                    ThreadUtil.safeSleep(1000);
                } else {
                    break;
                }
            }
        }
        log.info("关闭弹出层弹窗...");
        match = ScreenUtils.matchImg(TaobaoImageEnum.CLOSE_BUTTON_02);
        if (Objects.nonNull(match) && match.click() == 1) {
            log.info("关闭弹出层弹窗执行成功...");
            result = true;
            ThreadUtil.safeSleep(1000);
        }
        return result;
    }

    /**
     * 快速进入, 启动工作台时有几率弹出
     * @return boolean
     */
    private boolean quickEntry() {
        Match match = ScreenUtils.matchImg(TaobaoImageEnum.QUICK_ENTRY);
        if (Objects.nonNull(match) && match.click() == 1) {
            log.info("快速进入...");
            ThreadUtil.sleep(3, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    private boolean isSessionListPage() {
        Match match = ScreenUtils.matchImg(TaobaoImageEnum.SESSION_LIST, 10.0);
        return Objects.nonNull(match);
    }

    private boolean isCreateSessionPage() {
        Match match = ScreenUtils.matchImg(TaobaoImageEnum.CREATE_SESSION_PAGE_TITLE);
        return Objects.nonNull(match);
    }

    /**
     * 切换到场次列表模式
     * @return boolean
     */
    private boolean changeSessionList() {
        Match match = ScreenUtils.matchImg(TaobaoImageEnum.SESSION_LIST_BUTTON);
        if (Objects.nonNull(match) && match.click() == 1) {
            log.info("切换到场次列表模式...");
            ThreadUtil.sleep(3, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    /**
     * 获取直播间id
     * @return boolean
     */
    public boolean getLiveId() {
        log.info("获取直播间id...");
        if (!sessionManageList()) {
            return false;
        }
        return getLiveIdBySessionListPage();
    }

    /**
     * 通过直播间id检索直播
     * @return boolean
     */
    public boolean doSearchLiveById() {
        log.info("通过id检索直播间...");
        String liveId = VariableContainer.getActionContext().getLiveId();
        if (StrUtil.isBlank(liveId)) {
            return false;
        }
        Match match = ScreenUtils.matchImg(TaobaoImageEnum.MANAGE_SESSION);
        if (Objects.nonNull(match) && match.click() == 1) {
            ThreadUtil.sleep(5, TimeUnit.SECONDS);
        }
        changeSessionList();
        match = ScreenUtils.matchImg(TaobaoImageEnum.LIVE_ID_SEARCH);
        if (Objects.nonNull(match) && match.click() == 1) {
            match.paste(liveId);
            match = ScreenUtils.matchImg(TaobaoImageEnum.LIVE_ID_SEARCH);
            return Objects.isNull(match);
        }
        return false;
    }

    /**
     * 打开场次管理列表
     * @return boolean
     */
    private boolean sessionManageList() {
        Match match = ScreenUtils.matchImg(TaobaoImageEnum.MANAGE_SESSION);
        if (Objects.isNull(match) || match.click() != 1) {
            return false;
        }
        ThreadUtil.sleep(5, TimeUnit.SECONDS);
        log.info("打开场次管理-列表模式成功...");
        if (changeSessionList()) {
            log.info("切换到列表模式...");
            return true;
        }
        return false;
    }

    /**
     * 处理场次列表页, ocr提取直播间id
     * @return boolean
     */
    private boolean getLiveIdBySessionListPage() {
        ActionContext actionContext = VariableContainer.getActionContext();
        BufferedImage image = getLiveIdImage();
        if (Objects.nonNull(image)) {
            for (int i = 0; i < 5; i++) {
                if (i > 0) {
                    image = ImageUtils.resizeImage(image, 1.5 * i);
                }
                String liveId = OCRUtils.readNumberText(image);
                if (StrUtil.isNotEmpty(liveId)) {
                    log.info("识别成功, 直播间id: {}", liveId);
                    MsgUtils.writeSuccessMsg(StrUtil.format("直播间id: {}", liveId));
                    actionContext.setLiveId(liveId);
                    return true;
                } else {
                    log.error("识别失败, 直播间id为空, 重试次数: {}...", i);
                }
            }
        }
        return false;
    }

    private BufferedImage getLiveIdImage() {
        Match match = ScreenUtils.matchImg(TaobaoImageEnum.LIVING_TAG);
        if (Objects.isNull(match)) {
            return null;
        }
        Screen screen = new Screen();
        Region region = new Region(match.getX() + 156, match.getY() + 6, 105, 20);

        // 截取该区域的屏幕图像
        ScreenImage capture = screen.capture(region);
        BufferedImage image = capture.getImage();
        String base = AppFileConstants.BATE_PATH + FileUtil.FILE_SEPARATOR + DateUtil.today() + FileUtil.FILE_SEPARATOR + System.currentTimeMillis() + ".png";
        log.debug("保存直播间id截屏, 路径: {}", base);
        File file = FileUtil.touch(base);
        ImgUtil.write(image, file);
        return image;
    }

    /**
     * 监听淘宝推流码
     */
    private void taobaoPushUrlListen() {
        NetWorkUtils.taobaoPushUrlListenAsync((r, t) -> {
            ActionContext actionContext = VariableContainer.getActionContext();
            if (StrUtil.isNotEmpty(r) && StrUtil.isEmpty(actionContext.getPushUrl())) {
                actionContext.setPushUrl(r);
                log.info("抓取到淘宝直播推流码: {}", r);
                MsgUtils.writeSuccessMsg(StrUtil.format("抓取到淘宝直播推流码: {}", r));
            }
        });
    }

    /**
     * 处理我知道了弹窗
     * @return boolean
     */
    private boolean processIKnow() {
        // 我知道了
        Match match = ScreenUtils.matchImg(TaobaoImageEnum.I_KNOW);
        if (Objects.nonNull(match) && match.click() == 1) {
            log.info("处理我知道了弹窗...");
            return true;
        }
        return false;
    }

    private void killWorkbench() {
        // 杀死工作台进程
        try {
            String workbenchLocation = ConfigUtils.getWorkbenchLocation();
            boolean kill = CmdUtils.kill(FileUtil.getName(workbenchLocation));
            if (kill) {
                log.info("工作台未正常关闭, 杀死工作台进程成功");
            }
        } catch (Exception e) {
            log.error("关闭工作台进程失败", e);
        }
    }

}
