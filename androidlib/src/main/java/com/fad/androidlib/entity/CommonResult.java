package com.fad.androidlib.entity;

/**
 * 实现的主要功能:
 * 自定义结果
 *
 * @author zf 创建日期：2016/6/6
 * @modify 修改者:,修改日期:,修改内容:.
 */
public class CommonResult {
    /**
     * 是否成功
     */
    protected boolean success;

    /**
     * 提示信息
     */
    protected String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "MyResult{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                '}';
    }
}
