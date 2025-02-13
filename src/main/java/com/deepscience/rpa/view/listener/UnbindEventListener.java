package com.deepscience.rpa.view.listener;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.deepscience.rpa.common.container.VariableContainer;
import com.deepscience.rpa.common.enums.RunningStateEnum;
import com.deepscience.rpa.model.login.service.LoginBindService;
import com.deepscience.rpa.util.ConfigUtils;
import com.deepscience.rpa.util.MsgUtils;
import com.deepscience.rpa.view.MainFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

/**
 * 解绑事件监听器
 * @author yangzhuo
 * @date 2025/2/10 13:50
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UnbindEventListener implements ActionListener {
    /**
     * 登录绑定服务
     */
    private final LoginBindService loginBindService;

    @Override
    public void actionPerformed(ActionEvent e) {
        log.info("unbind...");
        String username = ConfigUtils.getUsername();
        String clientCredentials = ConfigUtils.getClientCredentials();
        try {
            if (StrUtil.isNotEmpty(clientCredentials)) {
                Optional.ofNullable(SpringUtil.getBean(MainFrame.class))
                        .ifPresent(mainFrame -> {
                            if (loginBindService.unbind()) {
                                log.info("unbind success username: {} clientCredentials:{}", username, clientCredentials);
                                VariableContainer.setRunningState(RunningStateEnum.STOP);
                                ConfigUtils.setClientCredentials(null);
                                mainFrame.showLoginDialog();
                            } else {
                                log.error("unbind fail username: {} clientCredentials:{}", username, clientCredentials);
                                MsgUtils.writeErrorMsg("解绑失败, 请联系客服!");
                            }
                        });
            }
        } catch (Exception exception) {
            log.error("unbind error", exception);
            MsgUtils.writeErrorMsg(ExceptionUtil.getMessage(exception));
        }
    }
}
