package com.github.chuanchic.helper;

import java.io.Serializable;

/**
 * 没有更多实体类
 */
public class CommonNoMoreEntity extends CommonEntity implements Serializable {
    private String text;//显示内容

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
