package com.deepscience.rpa.task.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * 任务模型类
 * @author yangzhuo
 * @date 2025/1/24 17:47
 */
@Getter
@RequiredArgsConstructor
public class TaskModel<R> {
    /**
     * 需要调度执行的任务
     */
    private final Callable<R> callable;

    /**
     * 任务执行回调
     */
    private final CompletableFuture<R> callback = new CompletableFuture<>();
}
