package com.fad.androidlib.coreservice;

import com.fad.androidlib.scene.BaseScene;

/**
 * 消息处理完毕，向UI层传递消息的回调
 * @author 张帆 创建日期：2016/1/25
 * 修改者，修改日期，修改内容。
 */
public interface OnMessageSolvedListener {
    public void onMessageSolved(int messageType, Object ret);
}
