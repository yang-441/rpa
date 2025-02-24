package com.deepscience.rpa.util.frame.entity;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.deepscience.rpa.util.ConfigUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * @author yangzhuo
 * @Description 文件筛选输入框
 * @date 2025/2/18 16:17
 */
@Slf4j
public class FileFilterDocument extends PlainDocument {

    /**
     * 工作台文件名
     */
    private static final List<String> WORK_BENCH_LIST = Lists.newArrayList("淘宝直播主播工作台.exe");

    /**
     * 满足条件执行
     */
    private final Runnable validCallback;

    /**
     * 不满足条件回调
     */
    private final Runnable invalidCallback;

    public FileFilterDocument(Runnable validCallback, Runnable invalidCallback) {
        this.validCallback = validCallback;
        this.invalidCallback = invalidCallback;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (StrUtil.isBlank(str)) {
            return;
        }
        File file = FileUtil.file(str);
        if (file.exists()) {
            for (String workBench : WORK_BENCH_LIST) {
                if (file.isFile() && str.endsWith(workBench)) {
                    doValidCallback(str);
                    super.insertString(offset, workBench, attr);
                    return;
                } else if (file.isDirectory()) {
                    String workBenchPath = file.getAbsolutePath() + FileUtil.FILE_SEPARATOR + workBench;
                    File workBenchFile = FileUtil.file(workBenchPath);
                    if (workBenchFile.exists() && workBenchFile.isFile()) {
                        doValidCallback(workBenchPath);
                        super.insertString(offset, workBench, attr);
                        return;
                    }
                }
            }
        }
        if (Objects.nonNull(invalidCallback)) {
            invalidCallback.run();
        }
    }

    private void doValidCallback(String workBench) {
        String workbenchLocation = ConfigUtils.getWorkbenchLocation();
        if (!Objects.equals(workbenchLocation, workBench)) {
            ConfigUtils.setWorkbenchLocation(workBench);
        }
        if (Objects.nonNull(validCallback)) {
            validCallback.run();
        }
    }
}
