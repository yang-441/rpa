package com.deepscience.rpa.model.event.service.impl;

import cn.hutool.core.util.StrUtil;
import com.deepscience.rpa.common.container.VariableContainer;
import com.deepscience.rpa.common.context.ActionContext;
import com.deepscience.rpa.common.pojo.CommonResult;
import com.deepscience.rpa.model.event.service.ActionEventReportService;
import com.deepscience.rpa.model.live.service.LivePlanService;
import com.deepscience.rpa.rpc.api.event.ActionEventReportApi;
import com.deepscience.rpa.rpc.api.event.dto.ActionEventReportDTO;
import com.deepscience.rpa.rpc.api.event.dto.ErrorReportDTO;
import com.deepscience.rpa.rpc.api.event.enums.ActionEventEnum;
import com.deepscience.rpa.rpc.api.live.dto.LivePlanDTO;
import com.deepscience.rpa.util.ImageUtils;
import com.deepscience.rpa.util.ScreenUtils;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sikuli.script.ScreenImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 执行事件上报服务
 * @author yangzhuo
 * @date 2025/2/11 17:18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActionEventReportServiceImpl implements ActionEventReportService {

    /**
     * 重试队列
     */
    private static final LinkedBlockingDeque<LivePlanDTO> RETRY_QUEUE = new LinkedBlockingDeque<>();

    /**
     * 事件上报服务
     */
    private final ActionEventReportApi actionEventReportApi;

    /**
     * 直播计划服务
     */
    private final LivePlanService livePlanService;

    @Override
    public boolean reportInfo(LivePlanDTO livePlan) {
        if (Objects.isNull(livePlan)) {
            return false;
        }
        ActionEventEnum actionEvent = livePlan.getActionEvent();
        if (Objects.isNull(actionEvent)) {
            return false;
        }
        return switch (actionEvent) {
            case START_LIVING -> reportStartLivingInfo(livePlan);
            case END_LIVING -> reportEndLivingInfo(livePlan);
        };
    }

    /**
     * 定时将重试队列中的任务消费
     */
    @Override
    public void retryReport() {
        LivePlanDTO livePlanDTO = RETRY_QUEUE.poll();
        if (Objects.nonNull(livePlanDTO)) {
            reportInfo(livePlanDTO);
        }
    }

    @Override
    public void errorReport(LivePlanDTO livePlan) {
        MultipartFile imageFile = captureScreen();
        ErrorReportDTO errorReportDTO = convertErrorReport(livePlan);
        CommonResult<Boolean> result = actionEventReportApi.errorReport(imageFile, errorReportDTO);
        log.info("errorReport result: {}, errorReportDTO: {}", result, errorReportDTO);
    }


    @Override
    public void errorReportAsync(LivePlanDTO livePlan) {
        MultipartFile imageFile = captureScreen();
        CompletableFuture.runAsync(() -> {
            ErrorReportDTO errorReportDTO = convertErrorReport(livePlan);
            CommonResult<Boolean> result = actionEventReportApi.errorReport(imageFile, errorReportDTO);
            log.info("errorReportAsync result: {}, errorReportDTO: {}", result, errorReportDTO);
        }).whenComplete((aVoid, throwable) -> {
            if (throwable != null) {
                log.error("error report failed", throwable);
            } else {
                log.info("error report success");
            }
        });
    }

    /**
     * 开播上报
     * @param livePlan 直播计划信息
     */
    private boolean reportStartLivingInfo(LivePlanDTO livePlan) {
        ActionContext actionContext = VariableContainer.getActionContext();
        String liveId = actionContext.getLiveId();
        String pushUrl = actionContext.getPushUrl();
        if (Objects.isNull(liveId) || Objects.isNull(pushUrl)) {
            log.error("开播上报失败, liveId: {}, pushUrl: {}", liveId, pushUrl);
            return false;
        }
        ActionEventReportDTO dto = convert(livePlan);
        dto.setLiveAccount(liveId);
        dto.setLiveUrl(pushUrl);
        dto.setActionEvent(ActionEventEnum.START_LIVING);
        CommonResult<Boolean> resp;
        try {
            resp = actionEventReportApi.actionEventReport(dto);
        } catch (RetryableException retryableException) {
            // 记录异常请求
            log.error("开播上报远程接口调用失败: {}", livePlan);
            // 存入缓存中等待重试
            RETRY_QUEUE.add(livePlan);
            throw retryableException;
        }
        boolean result = resp.isSuccess() && Boolean.TRUE.equals(resp.getData());
        if (result) {
            log.info("开播上报成功, id: {}, liveAccount: {}", dto.getPlanId(), dto.getLiveAccount());
            livePlanService.removeLivePlan(livePlan);
        } else {
            log.error("开播上报失败, id: {}, liveAccount: {}", dto.getPlanId(), dto.getLiveAccount());
        }

        return result;
    }

    /**
     * 关播上报
     * @param livePlan 直播计划信息
     */
    private boolean reportEndLivingInfo(LivePlanDTO livePlan) {
        ActionContext actionContext = VariableContainer.getActionContext();
        String liveId = actionContext.getLiveId();
        if (StrUtil.isEmpty(liveId)) {
            log.error("关播上报失败, liveId: {}", livePlan.getLiveAccount());
            return false;
        }
        if (actionContext.isHasNext()) {
            log.error("关播上报失败, 关播执行异常, livePlan: {}", livePlan);
            return false;
        }
        if (!Objects.equals(liveId, livePlan.getLiveAccount())) {
            log.error("关播上报异常, 关闭的直播间id和执行任务直播间id不一致, liveId: {}, playPlanCode: {}",
                    liveId, livePlan.getLiveAccount());
        }
        ActionEventReportDTO dto = convert(livePlan);
        dto.setLiveAccount(liveId);
        dto.setActionEvent(ActionEventEnum.END_LIVING);
        CommonResult<Boolean> resp;
        try {
            resp = actionEventReportApi.actionEventReport(dto);
        } catch (RetryableException retryableException) {
            // 记录异常请求
            log.error("开播上报远程接口调用失败: {}", livePlan);
            // 存入缓存中等待重试
            RETRY_QUEUE.add(livePlan);
            throw retryableException;
        }
        boolean result = resp.isSuccess() && Boolean.TRUE.equals(resp.getData());
        if (result) {
            log.info("关播上报成功, id: {}, liveAccount: {}", dto.getPlanId(), dto.getLiveAccount());
            livePlanService.removeLivePlan(livePlan);
        } else {
            log.error("关播上报失败, id: {}, liveAccount: {}", dto.getPlanId(), dto.getLiveAccount());
        }
        return result;
    }

    /**
     * 转换直播计划信息
     * @param livePlan 直播计划
     * @return ActionEventReportDTO
     */
    private ActionEventReportDTO convert(LivePlanDTO livePlan) {
        ActionEventReportDTO dto = new ActionEventReportDTO();
        dto.setPlanId(livePlan.getId());
        dto.setPlayPlatform(livePlan.getPlayPlatform());
        dto.setPlayPlanCode(livePlan.getPlayPlanCode());
        return dto;
    }

    /**
     * 转换错误上报信息
     * @param livePlan 直播计划
     * @return ErrorReportDTO 错误上报信息
     */
    private ErrorReportDTO convertErrorReport(LivePlanDTO livePlan) {
        ErrorReportDTO dto = new ErrorReportDTO();
        dto.setPlanId(livePlan.getId());
        dto.setPlayPlatform(livePlan.getPlayPlatform());
        dto.setPlayPlanCode(livePlan.getPlayPlanCode());
        dto.setActionEvent(livePlan.getActionEvent());
        return dto;
    }

    private MultipartFile captureScreen() {
        ScreenImage capture = ScreenUtils.capture();
        BufferedImage image = capture.getImage();
        // BufferedImage 转 MultipartFile
        return ImageUtils.convertBufferedImageToMultipartFile(image, System.currentTimeMillis() + ".png");
    }
}
