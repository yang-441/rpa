package com.deepscience.rpa.common.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页结果
 * @param <T> 数据泛型
 * @author yangzhuo
 * @date 2025/2/10 12:08
 */
@Data
public final class PageResult<T> implements Serializable {

    private List<T> list;

    private Long total;

    public PageResult() {
    }

    public PageResult(List<T> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public PageResult(Long total) {
        this.list = new ArrayList<>();
        this.total = total;
    }

    public static <T> PageResult<T> empty() {
        return new PageResult<>(0L);
    }

    public static <T> PageResult<T> empty(Long total) {
        return new PageResult<>(total);
    }

}
