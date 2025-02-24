package com.deepscience.rpa.view.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.deepscience.rpa.common.container.VariableContainer;
import com.deepscience.rpa.common.enums.RunningStateEnum;
import com.deepscience.rpa.model.login.service.LoginBindService;
import com.deepscience.rpa.util.ConfigUtils;
import com.deepscience.rpa.util.MsgUtils;
import com.deepscience.rpa.view.LoginDialog;
import com.deepscience.rpa.view.MainFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.Optional;

/**
 * @author yangzhuo
 * @Description 校验服务
 * @date 2025/2/24 13:22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckService {

    /**
     * 登录绑定服务
     */
    private final LoginBindService loginBindService;

    /**
     * 程序启动校验
     */
    public boolean startCheck() {
        // 凭证有效校验
        if (!loginBindService.isValidBind()) {
            VariableContainer.setRunningState(RunningStateEnum.STOP);
            ConfigUtils.setClientCredentials(null);
            Optional.ofNullable(SpringUtil.getBean(MainFrame.class))
                    .ifPresent(mainFrame -> {
                        mainFrame.showLoginDialog();
                        LoginDialog loginDialog = mainFrame.getLoginDialog();
                        JLabel msgLabel = loginDialog.getMsgLabel();
                        MsgUtils.writeErrorMsg(msgLabel, "登录凭证已失效, 请重新登录!");
                    });
            return false;
        }
        // 目录不能为空
        String workbenchLocation = ConfigUtils.getWorkbenchLocation();
        if (StrUtil.isBlank(workbenchLocation) || !FileUtil.exist(workbenchLocation)) {
            MsgUtils.writeErrorMsg("工作目录不能为空!");
            return false;
        }
        return true;
    }
}
