package com.deepscience.rpa.view.filter;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Set;

/**
 * 文件选择器, 过滤直播工作台
 * @author yangzhuo
 * @date 2025/1/23 16:34
 */
@Component
public class LiveWorkbenchFilter extends FileFilter {

    private static final Set<String> WORK_BENCH_SET = Sets.newHashSet("淘宝直播主播工作台");

    private static final Set<String> WORK_BENCH_SUFFIX_SET = Sets.newHashSet("", "exe");

    @Override
    public boolean accept(File f) {
        return isMatch(f) || f.isDirectory();
    }

    @Override
    public String getDescription() {
        return "exe";
    }

    public boolean isMatch(File f) {
        String name = FileUtil.getName(f);
        if (StrUtil.isBlank(name)) {
            return false;
        }
        String suffix = FileUtil.getSuffix(name);
        // 去掉文件名的后缀
        if (StrUtil.isNotEmpty(suffix)) {
            name = name.substring(0, name.length() - suffix.length() - 1);
        }
        return WORK_BENCH_SUFFIX_SET.contains(suffix) && WORK_BENCH_SET.contains(name);
    }
}
