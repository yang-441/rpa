package com.deepscience.rpa.task.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延迟任务模型
 * @author yangzhuo
 * @date 2025/2/11 12:13
 */
@Data
@RequiredArgsConstructor
public class DelayTaskModel<P> implements Delayed {
    /**
     * 参数
     */
    private final P param;
    /**
     * 触发时间
     */
    private final long triggerTime;
    /**
     * 任务执行回调
     */
    private final CompletableFuture<P> callback = new CompletableFuture<>();

    public DelayTaskModel(P param, LocalDateTime triggerTime) {
        this.param = param;
        // 将目标时间转为毫秒
        this.triggerTime = triggerTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @Override
    public long getDelay(@Nonnull TimeUnit unit) {
        long diff = this.triggerTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(@Nonnull Delayed o) {
        return Long.compare(this.triggerTime, ((DelayTaskModel<?>) o).triggerTime);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DelayTaskModel<?> that = (DelayTaskModel<?>) o;
        return triggerTime == that.triggerTime && Objects.equals(param, that.param);
    }

    @Override
    public int hashCode() {
        return Objects.hash(param, triggerTime);
    }
}
