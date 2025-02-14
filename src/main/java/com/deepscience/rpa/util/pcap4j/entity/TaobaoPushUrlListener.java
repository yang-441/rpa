package com.deepscience.rpa.util.pcap4j.entity;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * 网络监听器
 * @author yangzhuo
 * @date 2025/2/7 12:23
 */
@Slf4j
@Data
public class TaobaoPushUrlListener implements Runnable {

    /**
     * 默认超时时间
     */
    private final static long DEFAULT_TIMEOUT = 60_000;

    /**
     * 默认监听过滤器
     */
    private final static String DEFAULT_FILTER = "udp";

    /**
     * 淘宝直播推流码前缀
     */
    private final static String BASE_HEX = "617274633a2f2f";

    /**
     * 淘宝直播推流码后缀
     */
    private final static String END_HEX = "04008";

    /**
     * 设备列表
     */
    private final List<PcapNetworkInterface> devices;

    /**
     * 完成回调
     */
    private final BiConsumer<String, ? super Throwable> complete;

    /**
     * 数据包
     */
    private String filter;

    /**
     * 捕获超时时间
     */
    private Long timeout;

    public TaobaoPushUrlListener(List<PcapNetworkInterface> devices, BiConsumer<String, ? super Throwable> complete) {
        this.devices = devices;
        this.complete = complete;
    }

    @Override
    public void run() {
        Assert.notEmpty(devices, "网络监听器创建失败, 网卡设备列表为空");
        int listenCount = devices.size();
        ThreadFactory threadFactory = ThreadFactoryBuilder.create().setDaemon(true)
                .setNamePrefix("network-listen-")
                .build();
        // 创建虚拟线程池
        ExecutorService executorService = new ThreadPoolExecutor(listenCount, listenCount, 0L,
                TimeUnit.MILLISECONDS, new SynchronousQueue<>(), threadFactory);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        List<PcapHandle> handles = new ArrayList<>(listenCount);
        log.info("网卡数据包监听[开始], 数量: {}", listenCount);

        try {
            // 遍历每个网络接口
            for (PcapNetworkInterface nif : devices) {
                // 捕获的最大字节数
                int snapLen = 65536;
                // 超时时间，单位毫秒
                int timeout = 10;
                // 混杂模式
                PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;

                // 打开网络接口进行捕获
                PcapHandle handle;
                try {
                    handle = nif.openLive(snapLen, mode, timeout);
                    handles.add(handle);
                } catch (Exception e) {
                    log.error(StrUtil.format("打开网络接口时出错, nif: {}", nif), e);
                    continue;
                }

                String currentFilter = StrUtil.isEmpty(filter) ? DEFAULT_FILTER : filter;
                // 设置过滤器，只捕获特定的流量
                try {
                    handle.setFilter(currentFilter, BpfProgram.BpfCompileMode.OPTIMIZE);
                } catch (Exception e) {
                    log.error(StrUtil.format("设置过滤器时出错, filter: {}", currentFilter), e);
                }

                // 异步执行网络抓包
                CompletableFuture.runAsync(() -> {
                    // 捕获数据包
                    try {
                        handle.loop(-1, this.createPacketFilterListener(countDownLatch));
                    } catch (InterruptedException ignored) {
                    } catch (Exception e) {
                        log.error("捕获数据包时出错", e);
                    }
                }, executorService);
            }

            Long currentTimeout = timeout;
            if (Objects.isNull(currentTimeout) || currentTimeout <= 0) {
                currentTimeout = DEFAULT_TIMEOUT;
            }
            try {
                boolean await = countDownLatch.await(currentTimeout, TimeUnit.MILLISECONDS);
                if (await) {
                    log.info("捕获成功, 任务执行退出...");
                } else {
                    log.error("捕获超时, 等待时长: {}ms", currentTimeout);
                }
            } catch (InterruptedException e) {
                log.error("捕获等待异常", e);
            }
        } finally {
            // 关闭网络监听
            handles.forEach(handle -> {
                try(handle) {
                    handle.breakLoop();
                } catch (Exception ignored) {
                }
            });

            // 关闭执行线程池
            executorService.shutdownNow();
        }
    }

    private PacketFilterListener createPacketFilterListener(CountDownLatch countDownLatch) {
        return new PacketFilterListener(countDownLatch, this::doFilter, complete);
    }

    private String doFilter(String hexStr) {
        if (StrUtil.isEmpty(hexStr)) {
            return null;
        }
        log.info("数据包捕获: {}", hexStr);
        int start = hexStr.indexOf(BASE_HEX);
        if (start < 0) {
            return null;
        }
        int end = hexStr.indexOf(END_HEX, start);
        if (end < 0) {
            return null;
        }
        String pushUrlHex = hexStr.substring(start, end);
        return HexUtil.decodeHexStr(pushUrlHex, StandardCharsets.UTF_8);
    }
}
