package com.cxc.firstproject.view.mysnackbar;

import com.cxc.firstproject.R;

public enum Prompt {
    /**
     * 红色,错误
     */
    ERROR(R.drawable.common_bounced_icon_error, R.color.main_color),

    /**
     * 红色,警告
     */
    WARNING(R.drawable.common_bounced_icon_warning, R.color.main_color),

    /**
     * 绿色,成功
     */
    SUCCESS(R.drawable.common_bounced_icon_successful, R.color.main_color);

    private int resIcon;
    private int backgroundColor;

    Prompt(int resIcon, int backgroundColor) {
        this.resIcon = resIcon;
        this.backgroundColor = backgroundColor;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
