package com.github.chuanchic.helper;

import java.io.Serializable;

/**
 * 加载更多实体类
 */
public class CommonLoadMoreEntity extends CommonEntity implements Serializable {
    private String text;//显示内容

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
