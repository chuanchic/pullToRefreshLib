package com.github.chuanchic.helper;

import java.io.Serializable;

/**
 * 实体类  通用
 */
public class CommonEntity implements Serializable {
    private int ViewType;//视图类型

    public int getViewType() {
        return ViewType;
    }

    public void setViewType(int viewType) {
        ViewType = viewType;
    }
}
