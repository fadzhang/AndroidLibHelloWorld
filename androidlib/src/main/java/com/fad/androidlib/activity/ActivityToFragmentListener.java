package com.fad.androidlib.activity;

/**
 * 实现的主要功能:
 * activity给fragment发送信息的回调
 * @author zf 创建日期：2016/2/1
 * @modify 修改者:,修改日期:,修改内容:.
 */
public interface ActivityToFragmentListener {
    public void onActivityBaseMessageSolved(int messageType, Object ret);
}
