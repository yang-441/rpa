package com.deepscience.rpa.util.pcap4j.entity;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.pcap4j.core.PacketListener;
import org.pcap4j.packet.Packet;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 数据包过滤监听器, 监听到特定数据包后中断监听
 * @author yangzhuo
 * @date 2025/2/7 11:44
 */
@Data
public class PacketFilterListener implements PacketListener {
    /**
     * 监听器计数器
     */
    private final CountDownLatch countDownLatch;

    /**
     * 数据包过滤器
     */
    private final Function<String, String> filter;

    /**
     * 完成后执行
     */
    private final BiConsumer<String, ? super Throwable> complete;

    @Override
    public void gotPacket(Packet packet) {
        try {
            byte[] data = packet.getRawData();
            if (ArrayUtil.isEmpty(data)) {
                return;
            }
            String hexStr = HexUtil.encodeHexStr(data);
            String res = filter.apply(hexStr);
            if (StrUtil.isNotEmpty(res)) {
                countDownLatch.countDown();
                if (Objects.nonNull(complete)) {
                    complete.accept(res, null);
                }
            }
        } catch (Exception e) {
            if (Objects.nonNull(complete)) {
                complete.accept(null, e);
            }
        }
    }
}
