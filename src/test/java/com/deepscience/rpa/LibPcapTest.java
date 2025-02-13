package com.deepscience.rpa;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import com.deepscience.rpa.util.pcap4j.NetWorkUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class LibPcapTest {

    @Test
    public void netWorkUtilTest() {
        NetWorkUtils.taobaoPushUrlListenAsync((r, t) -> {
            if (Objects.isNull(t)) {
                log.error("捕获数据包时出错", t);
            }
            if (StrUtil.isNotEmpty(r)) {
                log.info("捕获到数据包, res: {}", r);
            }
        }, null).join();
    }

    @Test
    public void capTest() throws PcapNativeException, NotOpenException, InterruptedException {
        // 获取系统所有网络接口
        List<PcapNetworkInterface> devices = Pcaps.findAllDevs();
        if (CollUtil.isEmpty(devices)) {
            log.error("未找到网络接口");
            return;
        }
        int listenCount = devices.size();

        // 创建虚拟线程池
        ExecutorService executorService = Executors.newFixedThreadPool(listenCount);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        List<PcapHandle> handles = new ArrayList<>(listenCount);
        log.info("网卡数据包监听[开始], 数量: {}", listenCount);

        // 遍历每个网络接口
        for (PcapNetworkInterface nif : devices) {
            // 打开网络接口进行捕获
            int snapLen = 65536; // 捕获的最大字节数
            int timeout = 10; // 超时时间，单位毫秒
            PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS; // 混杂模式
            // 设置过滤器，只捕获特定主机的流量
            String filter = "udp";
            PcapHandle handle = nif.openLive(snapLen, mode, timeout);
            handles.add(handle);
            handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
            CompletableFuture.runAsync(() -> {
                // 捕获数据包
                try {
                    handle.loop(
                            -1, // 捕获无限次数
                            (PacketListener) packet -> {
                                try {
                                    byte[] data = packet.getRawData();
                                    if (ArrayUtil.isEmpty(data)) {
                                        return;
                                    }
                                    String hexStr = HexUtil.encodeHexStr(data);
                                    String baseHex = "617274633a2f2f6c6976656e672d727463707573682e74616f62616f2e636f6d";
                                    String endHex = "04008";
                                    int start = hexStr.indexOf(baseHex);
                                    if (start < 0) {
                                        return;
                                    }
                                    int end = hexStr.indexOf(endHex, start);
                                    if (end < 0) {
                                        return;
                                    }
                                    String pushUrlHex = hexStr.substring(start, end);
                                    String pushUrl = HexUtil.decodeHexStr(pushUrlHex, StandardCharsets.UTF_8);
                                    if (StrUtil.isNotEmpty(pushUrl)) {
                                        System.out.println("推流地址: " + pushUrl);
                                        countDownLatch.countDown();
                                        System.out.println("countDown ...");
                                    }
                                } catch (Exception e) {
                                    log.error("解析数据包时出错", e);
                                }
                            }
                    );
                } catch (InterruptedException ignored) {
                } catch (Exception e) {
                    log.error("捕获数据包时出错", e);
                }
            }, executorService);
        }

        countDownLatch.await();
        System.out.println("结束");

        handles.forEach(handle -> {
            try(handle) {
                handle.breakLoop();
            } catch (Exception ignored) {
            }
        });
        System.out.println("关闭");

        executorService.shutdownNow();
        System.out.println("shutdownNow");

        ThreadUtil.safeSleep(30000);
    }


    @Test
    public void convertTest() {
        // Application specific data (Hex string)
        String hexData = "617274633a2f2f6c6976656e672d727463707573682e74616f62616f2e636f6d";
        System.out.println("解析后的字符串: " + HexUtil.decodeHexStr(hexData, StandardCharsets.UTF_8));
    }

    /**
     * 将十六进制字符串转换为字节数组
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    private static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] data = new byte[length / 2];

        for (int i = 0; i < length; i += 2) {
            // 每两个字符解析为一个字节
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}