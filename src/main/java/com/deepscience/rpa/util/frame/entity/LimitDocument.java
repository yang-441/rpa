package com.deepscience.rpa.util.frame.entity;

import lombok.extern.slf4j.Slf4j;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * 限制输入框输入长度
 * @author yangzhuo
 * @date 2025/2/8 15:29
 */
@Slf4j
public class LimitDocument extends PlainDocument {
    /**
     * 最大长度
     */
    private final int maxLength;

    /**
     * 满足条件执行
     */
    private final Runnable validCallback;

    /**
     * 不满足条件回调
     */
    private final Runnable invalidCallback;


    public LimitDocument(int maxLength) {
        this.maxLength = maxLength;
        this.validCallback = null;
        this.invalidCallback = null;
    }

    public LimitDocument(int maxLength, Runnable validCallback, Runnable invalidCallback) {
        this.maxLength = maxLength;
        this.validCallback = validCallback;
        this.invalidCallback = invalidCallback;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str != null && (getLength() + str.length()) <= maxLength) {
            if (validCallback != null) {
                try {
                    validCallback.run();
                } catch (Exception e) {
                    log.error("limitDocument insertString validCallback error", e);
                }
            }
        } else {
            if (invalidCallback != null) {
                try {
                    invalidCallback.run();
                } catch (Exception e) {
                    log.error("limitDocument insertString invalidCallback error", e);
                }
            }
        }
        super.insertString(offset, str, attr);
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        if (validCallback != null) {
            try {
                validCallback.run();
            } catch (Exception e) {
                log.error("limitDocument remove validCallback error", e);
            }
        }
        super.remove(offs, len);
    }
}
