package com.deepscience.rpa.util.pcap4j;

import com.deepscience.rpa.util.pcap4j.entity.TaobaoPushUrlListener;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

/**
 * 网络抓包工具类
 * @author yangzhuo
 * @date 2025/2/6 18:23
 */
@Slf4j
public class NetWorkUtils {
    /**
     * 获取所有网卡
     * @return List<PcapNetworkInterface>
     */
    public static List<PcapNetworkInterface> findAllDevs() {
        try {
            return Pcaps.findAllDevs();
        } catch (PcapNativeException e) {
            log.error("获取网卡失败", e);
            return Collections.emptyList();
        }
    }

    /**
     * 监听淘宝直播推流url, 默认使用ForkJoinPool.commonPool(), 默认超时时间30s
     * @param complete 完成回调
     * @return CompletableFuture
     */
    public static CompletableFuture<Void> taobaoPushUrlListenAsync(BiConsumer<String, ? super Throwable> complete) {
        return taobaoPushUrlListenAsync(complete, null, null);
    }

    /**
     * 监听淘宝直播推流url, 默认使用ForkJoinPool.commonPool()
     * @param complete 完成回调
     * @param timeout  超时时间
     * @return CompletableFuture
     */
    public static CompletableFuture<Void> taobaoPushUrlListenAsync(BiConsumer<String, ? super Throwable> complete,
                                                                   Long timeout) {
        return taobaoPushUrlListenAsync(complete, null, timeout);
    }

    /**
     * 监听淘宝直播推流url
     * @param complete 完成回调
     * @param executor 线程池
     * @param timeout  超时时间
     * @return CompletableFuture
     */
    public static CompletableFuture<Void> taobaoPushUrlListenAsync(BiConsumer<String, ? super Throwable> complete,
                                                                   Executor executor,
                                                                   Long timeout) {
        List<PcapNetworkInterface> allDevs = findAllDevs();
        TaobaoPushUrlListener listener = new TaobaoPushUrlListener(allDevs, complete);
        listener.setTimeout(timeout);
        // 不配置线程池时, 默认使用ForkJoinPool.commonPool()
        if (Objects.isNull(executor)) {
            return CompletableFuture.runAsync(listener);
        }
        return CompletableFuture.runAsync(listener, executor);
    }
}
